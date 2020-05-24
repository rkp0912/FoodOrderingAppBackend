package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="address")
@NamedQueries({
        @NamedQuery(name = "addressById" , query = "select a from AddressEntity a where a.id =:id"),
        @NamedQuery(name = "addressByUUID" , query = "select a from AddressEntity a where a.uuid =:uuid order by a.id desc")
})
public class AddressEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name="flat_buil_number")
    @NotNull
    @Size(max = 255)
    private String FlatBuilNo;

    @Column(name="locality")
    @NotNull
    @Size(max = 255)
    private String locality;


    @Column(name="city")
    @NotNull
    @Size(max = 30)
    private String city;


    @Column(name="pincode")
    @NotNull
    @Size(max = 30)
    private String pincode;

    @ManyToOne
    @JoinColumn(name="state_id")
    private StateEntity state;

    @Column(name="active")
    private Integer active;

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

    public String getFlatBuilNo() {
        return FlatBuilNo;
    }

    public void setFlatBuilNo(String flatBuilNo) {
        FlatBuilNo = flatBuilNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
