package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves order to the order table
     * @param orderEntity
     * @return OrderEntity
     */
    public OrderEntity saveOrder(final OrderEntity orderEntity){
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    /**
     * Fetches the all the orders in the orders table.
     * @return List<OrderEntity>
     */
    public List<OrderEntity> getOrdersByCustomer(final String  customerId){
        try{
            return entityManager.createNamedQuery("getAllOrders", OrderEntity.class)
                    .setParameter("uuid", customerId)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches the all the orders by restaurant id
     * @return List<OrderEntity>
     */
    public List<OrderEntity> getOrdersByRestaurant(final int  restaurantId){
        try{
            return entityManager.createNamedQuery("getOrdersByRestaurant", OrderEntity.class)
                    .setParameter("id", restaurantId)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}
