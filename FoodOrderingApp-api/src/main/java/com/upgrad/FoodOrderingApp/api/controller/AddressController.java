package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class AddressController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    /**
     * Handles adding address to the customer.
     * @param accessToken
     * @param saveAddressRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws SaveAddressException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path="/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization")  final String accessToken,
                                   @RequestBody(required = false)  final SaveAddressRequest saveAddressRequest)throws
                            AuthorizationFailedException, AddressNotFoundException, SaveAddressException
    {

        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        CustomerEntity customerEntity = customerService.getCustomer(token);

        StateEntity customerState = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        final AddressEntity customerAddress = new AddressEntity();
        customerAddress.setUuid(UUID.randomUUID().toString());
        customerAddress.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        customerAddress.setLocality(saveAddressRequest.getLocality());
        customerAddress.setCity(saveAddressRequest.getCity());
        customerAddress.setPincode(saveAddressRequest.getPincode());
        customerAddress.setState(customerState);
        customerAddress.setActive(1);

        final AddressEntity savedAddress = addressService.saveAddress(customerEntity, customerAddress);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(savedAddress.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * Handles the /address/customer and gets list of all addresses
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws SaveAddressException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getSavedAddresses(@RequestHeader("authorization")  final String accessToken)throws
                                                    AuthorizationFailedException, AddressNotFoundException, SaveAddressException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        CustomerEntity customerEntity = customerService.getCustomer(token);
        List<AddressEntity> addressEntityList = addressService.getAllAddress(customerEntity);
        AddressListResponse addressListResponse = new AddressListResponse();
        for (AddressEntity address : addressEntityList ) {
            AddressList addressList = new AddressList();
            addressList.setId(UUID.fromString(address.getUuid()));
            addressList.setCity(address.getCity());
            addressList.setLocality(address.getLocality());
            addressList.setFlatBuildingName(address.getFlatBuilNo());
            addressList.setPincode(address.getPincode());

            AddressListState addressListState = new AddressListState();
            addressListState.setId(UUID.fromString(address.getState().getUuid()));
            addressListState.setStateName(address.getState().getStatesName());
            addressList.setState(addressListState);

            addressListResponse.addAddressesItem(addressList);
        }
        
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /**
     * Handles the deletion of address based on UUID
     * @param addressUUID
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, path="/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> getSavedAddresses(@PathVariable("address_id") final String addressUUID,
                                            @RequestHeader("authorization")  final String accessToken)throws
                                            AuthorizationFailedException, AddressNotFoundException
    {
        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        CustomerEntity customerEntity = customerService.getCustomer(token);
        AddressEntity  addressEntity = addressService.getAddressByUUID(addressUUID, customerEntity);
        AddressEntity deletedAddress = addressService.deleteAddress(addressEntity);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(deletedAddress.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * Handles the Get States request.
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getStates()
    {
         List<StateEntity> stateEntityList =  addressService.getAllStates();
         StatesListResponse statesListResponse = new StatesListResponse();
         for (StateEntity state : stateEntityList ) {
             StatesList statesList = new StatesList();
             statesList.setId(UUID.fromString(state.getUuid()));
             statesList.setStateName(state.getStatesName());
             statesListResponse.addStatesItem(statesList);
         }
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }

}
