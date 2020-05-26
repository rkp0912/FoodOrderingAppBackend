package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Handles the coupon/{coupon_name} and returns the coupon details.
     * @param couponName
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     * @throws CouponNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@PathVariable("coupon_name") final String couponName,
                                                           @RequestHeader("authorization") final String accessToken)throws
            AuthorizationFailedException, CouponNotFoundException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        CustomerEntity customerEntity = customerService.getCustomer(token);
        CouponEntity coupon = orderService.getCouponByCouponName(couponName);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(coupon.getUuid()))
                .couponName(coupon.getCouponName())
                .percent(coupon.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    /**
     * Handles the orders, fetches list of orders placed by customer.
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getCoupon(@RequestHeader("authorization") final String accessToken)
                                                                throws AuthorizationFailedException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        CustomerEntity customerEntity = customerService.getCustomer(token);
        String customerId = customerEntity.getUuid();
        List<OrderEntity> ordersEntityList = orderService.getOrdersByCustomers(customerId);


        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

        for (OrderEntity order : ordersEntityList) {
            OrderList orderList = new OrderList();

            orderList.setId(UUID.fromString(order.getUuid()));
            orderList.setBill(BigDecimal.valueOf(order.getBill()));

            OrderListCoupon orderListCoupon = new OrderListCoupon();
            orderListCoupon.setId(UUID.fromString(order.getCoupon().getUuid()));
            orderListCoupon.setCouponName(order.getCoupon().getCouponName());
            orderListCoupon.setPercent(order.getCoupon().getPercent());
            orderList.setCoupon(orderListCoupon);

            orderList.setDiscount(BigDecimal.valueOf(order.getDiscount()));
            orderList.setDate(order.getDate().toString());

            OrderListPayment orderListPayment = new OrderListPayment();
            orderListPayment.setId(UUID.fromString(order.getPayment().getUuid()));
            orderListPayment.setPaymentName(order.getPayment().getPaymentName());
            orderList.setPayment(orderListPayment);

            OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.setId(UUID.fromString(order.getCustomer().getUuid()));
            orderListCustomer.setFirstName(order.getCustomer().getFirstName());
            orderListCustomer.setLastName(order.getCustomer().getLastName());
            orderListCustomer.setEmailAddress(order.getCustomer().getEmail());
            orderListCustomer.setContactNumber(order.getCustomer().getContactNumber());
            orderList.setCustomer(orderListCustomer);

            //OrderListAddress
            OrderListAddress orderListAddress = new OrderListAddress();
            orderListAddress.setId(UUID.fromString(order.getAddress().getUuid()));
            orderListAddress.setFlatBuildingName(order.getAddress().getFlatBuilNo());
            orderListAddress.setLocality(order.getAddress().getLocality());
            orderListAddress.setCity(order.getAddress().getCity());
            orderListAddress.setPincode(order.getAddress().getPincode());
            OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListAddressState.setId(UUID.fromString(order.getAddress().getState().getUuid()));
            orderListAddressState.setStateName(order.getAddress().getState().getStatesName());
            orderListAddress.setState(orderListAddressState);
            orderList.setAddress(orderListAddress);


            List<OrderItemEntity> orderItemEntityList = orderService.getOrderItemByOrder(order);
            List<ItemQuantityResponse> itemQuantityResponseList = new ArrayList<ItemQuantityResponse>();

            for (OrderItemEntity orderItem : orderItemEntityList) {
                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();

                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                itemQuantityResponseItem.setId(UUID.fromString(orderItem.getItem().getUuid()));
                itemQuantityResponseItem.setItemName(orderItem.getItem().getItemName());
                itemQuantityResponseItem.setItemPrice(orderItem.getItem().getPrice());
                itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.fromValue(orderItem.getItem().getType()));

                itemQuantityResponse.setItem(itemQuantityResponseItem);
                itemQuantityResponse.setPrice(orderItem.getPrice());
                itemQuantityResponse.setQuantity(orderItem.getQuantity());

                itemQuantityResponseList.add(itemQuantityResponse);
            }
            orderList.setItemQuantities(itemQuantityResponseList);
            customerOrderResponse.addOrdersItem(orderList);
        }

        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
    }

    /**
     * Handles saving the order.
     * @param accessToken
     * @param saveOrderRequest
     * @return SaveOrderResponse JSON
     * @throws AuthorizationFailedException
     * @throws PaymentMethodNotFoundException
     * @throws AddressNotFoundException
     * @throws RestaurantNotFoundException
     * @throws CouponNotFoundException
     * @throws ItemNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path="/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String accessToken,
                                        @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
                                        throws AuthorizationFailedException, PaymentMethodNotFoundException,
                                               AddressNotFoundException, RestaurantNotFoundException,
                                               CouponNotFoundException, ItemNotFoundException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        CustomerEntity customerEntity = customerService.getCustomer(token);
        CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        OrderEntity saveOrderEntity = new OrderEntity();
        final String orderId = UUID.randomUUID().toString();
        saveOrderEntity.setUuid(orderId);
        saveOrderEntity.setBill( Double.parseDouble(saveOrderRequest.getBill().toString()));
        saveOrderEntity.setCoupon(couponEntity);
        saveOrderEntity.setDiscount(Double.parseDouble(saveOrderRequest.getDiscount().toString()));
        Date today = new Date();
        saveOrderEntity.setDate(today);
        saveOrderEntity.setPayment(paymentEntity);
        saveOrderEntity.setCustomer(customerEntity);
        saveOrderEntity.setAddress(addressEntity);
        saveOrderEntity.setRestaurant(restaurantEntity);

        OrderEntity orderEntity = orderService.saveOrder(saveOrderEntity);

         for (ItemQuantity item : saveOrderRequest.getItemQuantities()) {
             OrderItemEntity orderItemEntity = new OrderItemEntity();
             orderItemEntity.setOrder(orderEntity);
             ItemEntity itemEntity = orderService.getItemById(item.getItemId().toString());
             orderItemEntity.setItem(itemEntity);
             orderItemEntity.setPrice(item.getPrice());
             orderItemEntity.setQuantity(item.getQuantity());
             orderService.saveOrderItem(orderItemEntity);
         }

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(orderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);

    }

}
