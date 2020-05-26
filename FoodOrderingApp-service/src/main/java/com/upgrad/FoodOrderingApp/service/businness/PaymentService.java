package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    /**
     * Fetch the payment based on UUID
     * @param paymentId
     * @return PaymentEntity
     * @throws PaymentMethodNotFoundException
     */
    public PaymentEntity getPaymentByUUID(String paymentId) throws PaymentMethodNotFoundException{
        PaymentEntity payment = paymentDao.getPaymentEntityByUUID(paymentId);
        if(payment == null){
            throw new PaymentMethodNotFoundException("PNF-002","No payment method found by this id");
        }
        return payment;
    }

    /**
     * Gets all the available payment methods available
     * @return List<PaymentEntity>
     * @throws PaymentMethodNotFoundException
     */
    public List<PaymentEntity> getAllPaymentMethods(){
        List<PaymentEntity> paymentEntitiesList = paymentDao.getAllPaymentEntities();
        return paymentEntitiesList;
    }

}
