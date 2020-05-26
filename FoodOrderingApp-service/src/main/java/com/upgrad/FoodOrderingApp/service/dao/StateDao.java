package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates new state in the state table
     * @param stateEntity
     * @return StateEntity
     */
    public StateEntity saveState(StateEntity stateEntity){
        entityManager.persist(stateEntity);
        return stateEntity;
    }

    /**
     * Fetches the state by UUID
     * @param uuid
     * @return StateEntity
     */
    public StateEntity getStateByUUID(String uuid){
        try{
            return entityManager.createNamedQuery("stateByUUID", StateEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches all the states in the states table
     * @return List<StateEntity>
     */
    public List<StateEntity> getAllStates(){
        try{
            return entityManager.createNamedQuery("getAllStates", StateEntity.class)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Updates an existing entity in the table
     * @param stateEntity
     * @return StateEntity
     */
    public StateEntity updateState(StateEntity stateEntity){
        entityManager.merge(stateEntity);
        return stateEntity;
    }

}
