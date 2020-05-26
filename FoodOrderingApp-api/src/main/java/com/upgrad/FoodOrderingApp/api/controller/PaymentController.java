package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * Handles the /payment and gets all the available payment methods
     * @return PaymentListResponse JSON
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getCoupon()
    {
        List<PaymentEntity> paymentEntityList = paymentService.getAllPaymentMethods();


        PaymentListResponse paymentListResponse = new PaymentListResponse();
        for (PaymentEntity paymentEntity : paymentEntityList) {
            PaymentResponse response = new PaymentResponse();
            response.setId(UUID.fromString(paymentEntity.getUuid().toString()));
            response.setPaymentName(paymentEntity.getPaymentName());
            paymentListResponse.addPaymentMethodsItem(response);
        }

        return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);
    }
}
