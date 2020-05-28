package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "categoryByUUID" , query = "select c from CategoryEntity c where c.uuid =:uuid"),
        @NamedQuery(name = "allCategories" , query = "select c from CategoryEntity c order by c.categoryName")
})
public class CategoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name = "category_name")
    @NotNull
    @Size(max=255)
    private String categoryName;

    //This field is not to persisted in the DB Table
    @Transient
    private List<ItemEntity> items;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
