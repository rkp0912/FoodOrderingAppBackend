package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private StateDao stateDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    /**
     * Gets the states based on UUID of the state
     * @param stateUUID
     * @return
     * @throws AddressNotFoundException
     */
    public StateEntity getStateByUUID(String stateUUID) throws AddressNotFoundException {
        StateEntity stateEntity =  stateDao.getStateByUUID(stateUUID);
        if(stateEntity == null){
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return stateEntity;
    }

    /**
     * This method saves the address into address table also saves customer and address relationship in the customer_address table.
     * @param customer
     * @param address
     * @return
     * @throws SaveAddressException
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress (CustomerEntity customer, AddressEntity address) throws SaveAddressException{

        if( address.getFlatBuilNo() == null || address.getFlatBuilNo() == "" ||
            address.getLocality() == null || address.getLocality() == "" ||
            address.getCity() == null || address.getCity() == "" ||
            address.getPincode() == null || address.getPincode() == "" ||
            address.getState() == null)
        {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        if(address.getPincode().matches("^\\d{6}$") == false){
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        AddressEntity savedAddress = addressDao.saveAddress(address);
        final CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setAddressId(savedAddress);
        customerAddressEntity.setCustomerId(customer);
        customerAddressDao.saveCustomerAddress(customerAddressEntity);
        return savedAddress;
    }

    /**
     * Gets list of  all the address for the given customer
     * @param customerEntity
     * @return
     * @throws AuthorizationFailedException
     */
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) throws AuthorizationFailedException{

        List<CustomerAddressEntity> customerAddressEntityList  = customerAddressDao.getCustomerAddressByCustomerId(customerEntity.getId());
        List<AddressEntity> addressList = new ArrayList<AddressEntity>();
        for (CustomerAddressEntity customerAddressEntity : customerAddressEntityList ) {
            AddressEntity addressEntity = new AddressEntity();
            addressEntity =  addressDao.getAddressById(customerAddressEntity.getAddressId().getId());
            addressList.add(addressEntity);
        }

        return addressList;
    }


    /**
     * Gets the address of based on UUID if the address belongs to the user.
     * @param addressUUID
     * @param customerEntity
     * @return
     * @throws AddressNotFoundException
     * @throws AuthorizationFailedException
     */
    public AddressEntity getAddressByUUID(String addressUUID, CustomerEntity customerEntity) throws
            AddressNotFoundException, AuthorizationFailedException{

        if(addressUUID == "" || addressUUID == null){
            throw new AddressNotFoundException("ANF-005", "(Address id can not be empty");
        }

        AddressEntity addressEntity = addressDao.getAddressByUUID(addressUUID);
        if(addressEntity == null){
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        CustomerAddressEntity customerAddressEntity = customerAddressDao.getCustomerAddressByAddressId(addressEntity.getId());
        if(customerAddressEntity.getCustomerId().getId() != customerEntity.getId()){
            throw  new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        return addressEntity;
    }

    /**
     * Deletes the address
     * @param addressEntity
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity){
        addressDao.deleteAddress(addressDao.getAddressByUUID(addressEntity.getUuid()));
        return addressEntity;
    }

    /**
     * Gets the list of all states.
     * @return
     */
    public List<StateEntity> getAllStates(){
       return stateDao.getAllStates();
    }

}

