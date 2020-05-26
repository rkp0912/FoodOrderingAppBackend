package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the restaurant using UUID
     * @param UUID
     * @return RestaurantEntity
     */
    public RestaurantEntity getRestaurantByUUID(final String UUID){
        try {
            return entityManager.createNamedQuery("getRestaurantByUUID", RestaurantEntity.class).
                    setParameter("uuid", UUID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
