package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Gets RestaurantCategories by restaurant ID.
     * @param id
     * @return List<RestaurantCategoryEntity>
     */
    public List<RestaurantCategoryEntity> getRestaurantCategoryByRestaurant(final int id){
        try {
            return entityManager.createNamedQuery("restaurantCategoryById", RestaurantCategoryEntity.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Gets RestaurantCategories by category ID.
     * @param id
     * @return List<RestaurantCategoryEntity>
     */
    public List<RestaurantCategoryEntity> getRestaurantCategoryByCategory(final int id){
        try {
            return entityManager.createNamedQuery("restaurantCategoryByCategoryId", RestaurantCategoryEntity.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
