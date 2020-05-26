package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Handlers SignupRestrictedException
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
            new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handles AuthenticationFailed Exception.
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Handles AuthorizationFailed Exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }

    /**
     * Handles Update Customer exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerException(UpdateCustomerException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }

    /**
     *Handles Save Address Exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }

    /**
     *Handles Address Not found exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundException(AddressNotFoundException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles Coupon not found exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles payment method not found exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> paymentMethodNotFoundException(PaymentMethodNotFoundException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles restaurant not found exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNotFoundException(RestaurantNotFoundException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles item not found exception
     * @param exc
     * @param request
     * @return
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> itemNotFoundException(ItemNotFoundException exc, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code((exc.getCode())).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

}
