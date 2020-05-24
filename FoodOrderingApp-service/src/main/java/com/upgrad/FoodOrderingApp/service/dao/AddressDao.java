package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves new address entity to the address table
     * @param addressEntity
     * @return
     */
    public AddressEntity saveAddress(final AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Fetches the address based on the address Id
     * @param id
     * @return
     */
    public AddressEntity getAddressById(final int id){
        try{
            return entityManager.createNamedQuery("addressById", AddressEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches the address by UUID
     * @param UUID
     * @return
     */
    public AddressEntity getAddressByUUID(final String UUID){
        try{
            return entityManager.createNamedQuery("addressByUUID", AddressEntity.class)
                    .setParameter("uuid", UUID)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }


    /**
     * Update the address of the existing row
     * @param addressEntity
     * @return
     */
    public AddressEntity updateAddress(final AddressEntity addressEntity){
        entityManager.merge(addressEntity);
        return addressEntity;
    }

    /**
     * Deletes the address from the address table.
     * @param deleteAddress
     */
    public void deleteAddress(final AddressEntity deleteAddress){
        entityManager.remove(deleteAddress);
    }

}
