package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches RestaurantItems by restaurant UUID
     * @param restaurantUUID
     * @return List<RestaurantItemEntity>
     */
    public List<RestaurantItemEntity> getRestaurantItemByUUID(final String restaurantUUID){
        try {
            return entityManager.createNamedQuery("getRestaurantItemByUUID", RestaurantItemEntity.class)
                    .setParameter("uuid", restaurantUUID)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
