package com.digitinary.customercare.model.entities.customer;

import com.digitinary.customercare.common.id.SnowflakeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ORDER_ITEMS")
public class ItemEntity {

    @Id
    @SnowflakeId
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntity order;

    @NotBlank
    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Column(name = "QUANTITY", nullable = false)
    @Digits(integer = 10, fraction = 0)
    private Integer quantity = 1;


    /**
     *  precision = 19
     *  يضمن تخزين رقم مئوي لديه 19 خانة كاملة قبل وبعد الفاصلة
     *  scale = 2
     *  يضمن انه يوجد رقمين فقط بعد الفاصلة لاننا نتعامل مع عملة
     *            17 . 2
     * ----------------------------------------------------------
     *  BigDecimal
     *شيء جديد تعلمته هو وجود نوع بيانات من ضمن جافا وهو النوع المعياري والامن في نفس اللغة للعمليات المالية والحسابية
     * يمكن استعمال كل من double أوfloat لكن حسب ما قرات فانه يوجد اخطاء تقريب لانها قيم عائمة في نفي الذاكرة
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "UNIT_PRICE", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    protected ItemEntity() {
    }

    public ItemEntity(OrderEntity order, String productName, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}