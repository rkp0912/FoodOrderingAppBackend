package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    /**
     * Fetches all restaurants in the descending order of the customer rating.
     * @return List<RestaurantEntity>
     */
    public List<RestaurantEntity> getAllRestaurantByRating(){
        try {
            return entityManager.createNamedQuery("getAllRestaurants", RestaurantEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches the restaurant by matching substring
     * @param restaurantName
     * @return RestaurantEntity
     */
    public List<RestaurantEntity> getRestaurantsByName(final String restaurantName){
        try {
            return entityManager.createNamedQuery("getRestaurantByName", RestaurantEntity.class)
                    .setParameter("restaurantName", "%"+restaurantName+"%")
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches the restaurant using Id
     * @param id
     * @return RestaurantEntity
     */
    public RestaurantEntity getRestaurantById(final int id){
        try {
            return entityManager.createNamedQuery("getRestaurantById", RestaurantEntity.class).
                    setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Updates an existing restaurant entity to restaurant table
     * @param restaurantEntity
     * @return
     */
    public RestaurantEntity updateRestaurantInfo(final RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }

}
