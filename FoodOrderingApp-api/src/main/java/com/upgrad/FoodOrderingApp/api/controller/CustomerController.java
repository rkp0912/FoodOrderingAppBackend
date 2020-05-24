package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.security.ssl.Debug;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * Handles the incoming POST request to sign up a new customer.
     * @param signupCustomerRequest
     * @return SignupCustomerResponse JSON
     * @throws SignUpRestrictedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path="/customer/signup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)throws SignUpRestrictedException
    {

        if( signupCustomerRequest.getFirstName() == null || signupCustomerRequest.getFirstName().equals("") ||
                signupCustomerRequest.getContactNumber() == null || signupCustomerRequest.getContactNumber().equals("") ||
                signupCustomerRequest.getEmailAddress() == null || signupCustomerRequest.getEmailAddress().equals("") ||
                signupCustomerRequest.getPassword() == null || signupCustomerRequest.getPassword().equals(""))
        {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled.");
        }

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getFirstName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setSalt("1234abc");

        final CustomerEntity signedUpCustomerEntity = customerService.saveCustomer(customerEntity);

        SignupCustomerResponse signupResponse = new SignupCustomerResponse()
                .id(signedUpCustomerEntity.getUuid())
                .status("CUSTOMER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(signupResponse, HttpStatus.CREATED);
    }

    /**
     * Handles incoming POST request for Login.
     * Accepts "Basic Base64c(Username:Password)" format in header. Here username is Contact Number.
     * On successful validation, returns Login response and access token
     * @param authorization
     * @return LoginResponse JSON
     * @throws AuthenticationFailedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path="/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization")  final String authorization )throws AuthenticationFailedException
    {
        byte[] decode = {};
        String[] decodedArray = {};
        try{
            decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);
            decodedArray = decodedText.split(":");
        } catch (Exception ex){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }

        if(decodedArray.length == 0)
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");

        CustomerAuthEntity customerAuthEntity = customerService.authenticate(decodedArray[0], decodedArray[1]);
        CustomerEntity customer =  customerAuthEntity.getCustomer();


        LoginResponse loginResponse =  new LoginResponse().id(customer.getUuid())
                .firstName(customer.getFirstName())
                .lastName(customer.getFirstName())
                .emailAddress(customer.getEmail())
                .contactNumber(customer.getContactNumber())
                .message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccessToken());
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);

        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }

    /**
     * Logout the user if already loggedin
     * @param accessToken
     * @return LogoutResponse JSON
     * @throws AuthorizationFailedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path="/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization")  final String accessToken )throws AuthorizationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerService.logout(accessToken.split("Bearer ")[1]);

        LogoutResponse logoutResponse = new LogoutResponse().id(customerAuthEntity.getCustomer().getUuid())
                .message("LOGGED OUT SUCCESSFULLY");

        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }

    /**
     * Updates the customer firstname and lastname
     * @param accessToken
     * @param updateCustomerRequest
     * @return UpdateCustomerResponse JSON
     * @throws UpdateCustomerException
     * @throws AuthorizationFailedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path="/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update (@RequestHeader("authorization")  final String accessToken,
                                      @RequestBody final UpdateCustomerRequest updateCustomerRequest)throws UpdateCustomerException, AuthorizationFailedException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        if(updateCustomerRequest.getFirstName() == null || updateCustomerRequest.getFirstName() == "")
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");

        CustomerEntity customerEntity = customerService.getCustomer(token);
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());
        CustomerEntity updatedCustomer = customerService.updateCustomer(customerEntity);
        UpdateCustomerResponse updatedCustomerResponse = new UpdateCustomerResponse().id(updatedCustomer.getUuid())
                .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY")
                .firstName(updatedCustomer.getFirstName())
                .lastName(updatedCustomer.getLastName());

        return new ResponseEntity<UpdateCustomerResponse>(updatedCustomerResponse, HttpStatus.OK);
    }

    /**
     * Updates the customer password.
     * @param accessToken
     * @param updatePasswordRequest
     * @return UpdatePasswordResponse JSON
     * @throws UpdateCustomerException
     * @throws AuthorizationFailedException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path="/customer/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestHeader("authorization") final String accessToken,
                                                         @RequestBody final UpdatePasswordRequest updatePasswordRequest)throws UpdateCustomerException, AuthorizationFailedException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        if(updatePasswordRequest.getNewPassword() == null || updatePasswordRequest.getOldPassword() == null ||
            updatePasswordRequest.getNewPassword() == "" || updatePasswordRequest.getOldPassword() == "")
            throw new UpdateCustomerException("UCR-003","No field should be empty");

        CustomerEntity customerEntity = customerService.getCustomer(token);
        CustomerEntity updatedCustomerPassword = customerService.updateCustomerPassword(updatePasswordRequest.getOldPassword(),
                updatePasswordRequest.getNewPassword(), customerEntity);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(updatedCustomerPassword.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }

}
