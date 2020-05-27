package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the list of Order Items based on order id
     * @param id
     * @return List<OrderItemEntity>
     */
    public List<OrderItemEntity> getOrderItemsByOrderId(final int id){
        try {
            return entityManager.createNamedQuery("orderItemById", OrderItemEntity.class).
                    setParameter("id", id)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Persists the order item in database.
     * @param orderItem
     * @return OrderItemEntity
     */
    public OrderItemEntity saveOrderItem(final OrderItemEntity orderItem){
        entityManager.persist(orderItem);
        return orderItem;
    }

    /**
     * Fetches the item id's in the order of max times ordered.
     * @param orderList
     * @return
     */
    public List<Integer> getItemsCountByOrders(final List<Integer> orderList ){
        try {
            return entityManager.createNamedQuery("itemsCountByOrder", Integer.class)
                    .setParameter("orders", orderList)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
