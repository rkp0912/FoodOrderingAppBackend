package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext
    EntityManager entityManager;

    public CustomerAuthEntity login(CustomerAuthEntity customerAuth){
        entityManager.persist(customerAuth);
        return customerAuth;
    }


    public CustomerAuthEntity getCustomerAuthByCustomer(CustomerEntity customer){
        try {
            return entityManager.createNamedQuery("customerAuthByCustomerId", CustomerAuthEntity.class)
                    .setParameter("customer", customer)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthEntity getCustomerAuthByAccessToken(String accessToken){
        try {
            return entityManager.createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public CustomerAuthEntity updatedCustomerAuth(CustomerAuthEntity customerAuthEntity){
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }

}
