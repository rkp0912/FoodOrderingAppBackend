package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerAddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * creates the entity, contains relation with customer id and his/her address.
     * @param customerAddressEntity
     * @return CustomerAddressEntity
     */
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    /**
     * Fetches the list of addresses of a customer
     * @param customerId
     * @return List<CustomerAddressEntity>
     */
    public List<CustomerAddressEntity> getCustomerAddressByCustomerId(Integer customerId){
        try {
            return entityManager.createNamedQuery("customerAddressByCustomerId", CustomerAddressEntity.class).setParameter("customerId", customerId)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches the customer address entity by addressId
     * @param addressId
     * @return CustomerAddressEntity
     */
    public CustomerAddressEntity getCustomerAddressByAddressId(Integer addressId){
        try {
            return entityManager.createNamedQuery("customerAddressByAddressId", CustomerAddressEntity.class).setParameter("addressId", addressId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Updates the existing customer address entity in the table
     * @param customerAddressEntity
     * @return CustomerAddressEntity
     */
    public CustomerAddressEntity updateCustomerAddress(CustomerAddressEntity customerAddressEntity){
        entityManager.merge(customerAddressEntity);
        return customerAddressEntity;
    }
}
