package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the coupon details based on coupon name
     * @param couponName
     * @return
     */
    public CouponEntity getCouponByName(final String couponName){
        try{
            return entityManager.createNamedQuery("couponByName", CouponEntity.class)
                    .setParameter("couponName", couponName)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches the coupon details based on coupon UUID
     * @param UUID
     * @return
     */
    public CouponEntity getCouponByUUID(final String UUID){
        try{
            return entityManager.createNamedQuery("couponByUUID", CouponEntity.class)
                    .setParameter("uuid", UUID)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

}
