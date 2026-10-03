package com.digitinary.customercare.common.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.IdentifierGenerator;

import java.lang.reflect.Member;


/**
   Entity
     │
     │ @Id
     │ @SnowflakeId
     ▼
@IdGeneratorType
     │
     ▼
SnowflakeIdGenerator.class
     │
     │ generate(...)
     ▼
Generated ID: 351446910423041 مثال واقعي ومجرب
     │
     ▼
  Entity.id
     │
     ▼
   INSERT

   فكرة Snowflake هي بختصار انه بنعمل id جديد على حسب توقيت السيرفر او السستم على utc مثلا وبنوخد تاريخ معين للسستم ونستعمله
   وفي عنا اشي اسمه Workers هم السستم نفسه يعني شيء فريد بكل نظام او اعتبرها node
   Server A -> Worker 1
   Server B -> Worker 2
   Server B -> Worker 3
   اما نثبته فلكود بكل سستم عنا هاي بتساعد انه يكون المعرف اشي فريد
    يعني بنعرف الid من اي سستم جاي او المكان بالاصح هي في الحالة الطبيعة بكونو مقسمين 5 مع 5 بس في حالتي ماعندي سيرفر
   وفيه اكثر من سستم برضه ما عندي اكثر من سيرفر
   اخر قسم sequence هو المسؤل عن التفريق بين كل مجموعة انتتي شو مكانت انضافت بنفس الملي ثانية

   في اله استعمالات كثيرة انا كنت استعمله من باب التعلم ومن باب بناء SnowflakeId annotation
 */

public class SnowflakeIdGenerator implements IdentifierGenerator {

    // تاريخ استلام الاسايمنت 2026/09/29
    // فكرة انه نستغل توقيت معين وتوقيت الحالي بعطينا قيمة صغيرة جدا وهي بتعطينامثل لمحة من وقت انشاء المشروع
    private static final long CUSTOM_EPOCH = 1790640000000L;

    // 10 bits -> 0 to 1023
    private static final long MAX_WORKER_ID = (1L << 10) - 1;

    // 12 bits -> 0 to 4095
    private static final long MAX_SEQUENCE = (1L << 12) - 1;

    // One worker for this application
    private final long workerId = 13L;

    private long lastTimestamp = -1L;
    private Long sequence = 0L;

    public SnowflakeIdGenerator() {

    }

    public SnowflakeIdGenerator(
            SnowflakeId config,
            Member member,
            GeneratorCreationContext context
    ) {
    }

    private long getTimestamp() {
        // هون بنطرح القيم من بعض عشان نوخد قيمة جديدة نعتبرها الوقت الحالي نسند عليه
        return System.currentTimeMillis() - CUSTOM_EPOCH;
    }

    private long waitNextMillis(long lastTimestamp) {

        long timestamp = getTimestamp();

        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp();
        }

        return timestamp;
    }

    @Override
    public synchronized Object generate(
            SharedSessionContractImplementor session,
            Object object
    ) {

        long timestamp = getTimestamp();


        //هاي في حال السيرفر رجع تاريخه قبل التاريخ المحطوط فوق
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate ID.");
        }

        if (timestamp == lastTimestamp) {

            // Sequence is full
            if (sequence >= MAX_SEQUENCE) {
                timestamp = waitNextMillis(lastTimestamp);
                sequence = 0L;
            }

            sequence++;

        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        /**
         * 64-bit Snowflake layout:
         *
         * | timestamp | worker | sequence |
         *
         * timestamp -> shifted 22 bits
         * worker    -> shifted 12 bits
         * sequence  -> stays in the last 12 bits
         شيء جديد تعلمته وانا بكتب هو bit shifting *
         *
         *  5bit + 3bit + 2bit
         *
         *  T = 13 = 1101
         *  W = 5 = 101
         *  S = 2 = 10
         *
         *  110100000
         *     101000  OR
         *         10
         * ----------
         * 110110110 = 447
         *
         */
        return (timestamp << 22) | (workerId << 12) | sequence;
    }
}
