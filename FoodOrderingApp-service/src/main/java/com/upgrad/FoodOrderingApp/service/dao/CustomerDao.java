package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a customer entity to the table.
     * @param customerEntity
     * @return CustomerEntity
     */
    public CustomerEntity signup(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    /**
     * Fetches a customer entity using phonenumber
     * @param contactNumber
     * @return CustomerEntity
     */
    public CustomerEntity getCustomerByPhoneNo(String contactNumber){
        try {
            return entityManager.createNamedQuery("customerByPhone", CustomerEntity.class).setParameter("contactNumber", contactNumber)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Updates an existing customer entity
     * @param customerEntity
     * @return CustomerEntity
     */
    public CustomerEntity updateCustomer(CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
        return customerEntity;
    }

}
