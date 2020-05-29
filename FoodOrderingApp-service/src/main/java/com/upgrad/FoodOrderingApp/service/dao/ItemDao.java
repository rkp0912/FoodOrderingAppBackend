package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the item based on UUID
     * @param UUID
     * @return ItemEntity
     */
    public ItemEntity getItemByUUID(final String UUID){
        try {
            return entityManager.createNamedQuery("itemByUUID", ItemEntity.class).
                    setParameter("uuid", UUID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches the item based on id
     * @param id
     * @return ItemEntity
     */
    public ItemEntity getItemById(final int id){
        try {
            return entityManager.createNamedQuery("itemById", ItemEntity.class).
                    setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
