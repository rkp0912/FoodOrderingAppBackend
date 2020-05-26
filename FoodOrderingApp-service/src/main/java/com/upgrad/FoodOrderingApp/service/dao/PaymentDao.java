package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the payment details based on UUID
     * @param UUID
     * @return
     */
    public PaymentEntity getPaymentEntityByUUID(final String UUID){
        try {
            return entityManager.createNamedQuery("paymentByUUID", PaymentEntity.class).
                    setParameter("uuid", UUID)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches all the payment methods available
     * @return
     */
    public List<PaymentEntity> getAllPaymentEntities(){
        try {
            return entityManager.createNamedQuery("getAllPaymentEntities", PaymentEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }


}
