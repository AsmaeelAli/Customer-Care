package com.digitinary.customercare.common.id;


import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
فكرة اضافية مني انا اسماعيل هي صنع انوتيشن خاصة لتوليد id

@Target(ElementType.FIELD)
 معناها انه يمكن وضعها على fields

@Retention(RetentionPolicy.RUNTIME)
احقظ هذه الانوتيشن اثناء التشغيل لكي يراها Hibernate ويقراها

@IdGeneratorType(SnowflakeIdGenerator.class)
بدي افهم Hibernate من اي كلاس رح تستلم id


 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(SnowflakeIdGenerator.class)
public @interface SnowflakeId {
}
