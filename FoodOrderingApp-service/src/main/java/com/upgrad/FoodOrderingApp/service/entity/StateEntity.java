package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="state")
@NamedQueries({
        @NamedQuery(name = "stateByUUID" , query = "select s from StateEntity s where s.uuid =:uuid"),
        @NamedQuery(name = "getAllStates" , query = "select s from StateEntity s order by s.statesName")
})
public class StateEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name="state_name")
    @NotNull
    @Size(max=30)
    private String statesName;

    public StateEntity() {
    }

    public StateEntity(@NotNull @Size(max = 200) String uuid, @NotNull @Size(max = 30) String statesName) {
        this.uuid = uuid;
        this.statesName = statesName;
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

    public String getStatesName() {
        return statesName;
    }

    public void setStatesName(String statesName) {
        this.statesName = statesName;
    }
}
