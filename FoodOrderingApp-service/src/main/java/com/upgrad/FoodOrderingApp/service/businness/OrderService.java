package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ItemDao itemDao;


    /**
     * Gets the coupon details if coupon name is not empty or null and details exists
     * @param couponName
     * @return CouponEntity
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponName(final String couponName) throws CouponNotFoundException {

        if(couponName == "" || couponName == null){
            throw  new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
        CouponEntity coupon = couponDao.getCouponByName(couponName);
          if(coupon == null){
            throw  new CouponNotFoundException("CPF-001","No coupon by this name");
          }
          return coupon;
    }

    /**
     * Fetches all the orders of a customer in the table
     * @return
     */
    public List<OrderEntity> getOrdersByCustomers(String customerid){
        return orderDao.getOrdersByCustomer(customerid);
    }

    /**
     * Returns the order items based on order.
     * @param order
     * @return
     */
    public List<OrderItemEntity> getOrderItemByOrder(OrderEntity order){
        return orderItemDao.getOrderItemsByOrderId(order.getId());
    }

    /**
     * Returns the order coupon based on UUID
     * @param couponId
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException{
        CouponEntity coupon = couponDao.getCouponByUUID(couponId);
        if(coupon == null){
            throw  new CouponNotFoundException("CPF-002","No coupon by this id");
        }
        return coupon;
    }

    /**
     * Gets the Item based on UUID
     * @param itemId
     * @return
     * @throws ItemNotFoundException
     */
    public ItemEntity getItemById(String itemId) throws ItemNotFoundException{
        ItemEntity item = itemDao.getItemByUUID(itemId);
        if(item == null){
            throw new  ItemNotFoundException("INF-003","No item by this id exist");
        }
        return item;
    }

    /**
     * Persists the order in order table
     * @param order
     * @return
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation =  Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity order){
        OrderEntity orderEntity = orderDao.saveOrder(order);
        return orderEntity;
    }

    /**
     * Saves orderitem to the orderitem table
     * @param orderItem
     * @return
     */
    @Transactional(propagation =  Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItem){
        OrderItemEntity orderItemEntity = orderItemDao.saveOrderItem(orderItem);
        return orderItemEntity;
    }

}
