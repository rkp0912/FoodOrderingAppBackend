package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches all the categories by UUID
     * @param UUID
     * @return CategoryEntity
     */
    public CategoryEntity getCategoryByUUID(final String UUID){
        try{
            return entityManager.createNamedQuery("categoryByUUID", CategoryEntity.class)
                    .setParameter("uuid", UUID)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches all the categories in alphabetical order
     * @return  List<CategoryEntity>
     */
    public List<CategoryEntity> getAllCategories(){
        try{
            return entityManager.createNamedQuery("allCategories", CategoryEntity.class)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}
