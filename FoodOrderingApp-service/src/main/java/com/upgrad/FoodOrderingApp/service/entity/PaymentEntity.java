package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="payment")
@NamedQueries({
        @NamedQuery(name = "paymentByUUID" , query = "select p from PaymentEntity p where p.uuid =:uuid"),
        @NamedQuery(name = "getAllPaymentEntities" , query = "select p from PaymentEntity p")
})
public class PaymentEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name = "payment_name")
    @NotNull
    @Size(max=255)
    private String paymentName;

    public PaymentEntity() {
    }

    public PaymentEntity(@NotNull @Size(max = 200) String uuid, @NotNull @Size(max = 255) String paymentName) {
        this.uuid = uuid;
        this.paymentName = paymentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
