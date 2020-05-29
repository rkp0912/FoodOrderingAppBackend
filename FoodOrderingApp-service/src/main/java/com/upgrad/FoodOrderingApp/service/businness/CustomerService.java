package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     *This method saves the new customer into the database.
     * Before saving the customer to DB, validate the all the mandatory fields are present.
     * @param customerEntity
     * @return CustomerEntity
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public  CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        //All the fields except lastname should be present
        if( customerEntity.getFirstName() == null || customerEntity.getFirstName().equals("") ||
            customerEntity.getContactNumber() == null || customerEntity.getContactNumber().equals("") ||
            customerEntity.getEmail() == null || customerEntity.getEmail().equals("") ||
            customerEntity.getPassword() == null || customerEntity.getPassword().equals(""))
        {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled.");
        }

        // check of email syntax is valid
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(customerEntity.getEmail());
        if(matcher.matches() == false){
            throw  new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        }

        //Check if contact number is valid
        if(customerEntity.getContactNumber().matches("\\d{10}") == false){
            throw  new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }

        //Validate password format
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(customerEntity.getPassword());
        if(passwordMatcher.matches() == false){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        //Validate if contact number already exists in the DB
        CustomerEntity existingCustomer = customerDao.getCustomerByPhoneNo(customerEntity.getContactNumber());
        if(existingCustomer != null){
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }

        //Create user in the database
        String password = customerEntity.getPassword();
        String[] encryptedText = passwordCryptographyProvider.encrypt(password);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDao.signup(customerEntity);
    }

    /**
     *Method validates the username and password
     *  if credentials are valid, an access-token is generated and the entry is persisted in the DB
     *  Otherwise, throws respective exception
     * @param contactNumber
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(final String contactNumber, final String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByPhoneNo(contactNumber);
        if(customerEntity == null){
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
        if(encryptedPassword.equals(customerEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setCustomer(customerEntity);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now,expiresAt));
            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthDao.login(customerAuthEntity);
            return customerAuthEntity;
        } else{
            throw  new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    /**
     * Method validates access token is exists in DB and not expired then user will be Logged out.
     * @param accessToken
     * @return CustomerEntity
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException{
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(accessToken);
        if(customerAuthEntity == null){
            throw  new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if(customerAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        if(customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        customerAuthEntity.setLogoutAt(now);
        customerAuthDao.updatedCustomerAuth(customerAuthEntity);
        return customerAuthEntity;
    }

    /**
     *GetCustomer
     * @param accessToken
     * @return CustomerEntity
     * @throws AuthorizationFailedException
     */
    public CustomerEntity getCustomer(final String accessToken)throws AuthorizationFailedException{
        CustomerAuthEntity customerAuthEntity =
                customerAuthDao.getCustomerAuthByAccessToken(accessToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        if (customerAuthEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        return customerAuthEntity.getCustomer();
    }

    public CustomerAuthEntity getCustomerAuth(final String accessToken)throws AuthorizationFailedException{
        CustomerAuthEntity customerAuthEntity =
                customerAuthDao.getCustomerAuthByAccessToken(accessToken);
        return customerAuthEntity;
    }

    /**
     * Update Customer
     * @param updatedCustomerEntity
     * @return CustomerEntity
     * @throws UpdateCustomerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity updatedCustomerEntity)throws UpdateCustomerException {

        if(updatedCustomerEntity.getFirstName() == "" || updatedCustomerEntity.getFirstName() == null){
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        }
        CustomerEntity customerEntity = customerDao.getCustomerByPhoneNo(updatedCustomerEntity.getContactNumber());
        customerEntity.setFirstName(updatedCustomerEntity.getFirstName());
        customerEntity.setLastName(updatedCustomerEntity.getLastName());
        customerDao.updateCustomer(customerEntity);
        return customerEntity;
    }

    /**
     *  Updates the password in the DB if the old password is correct and new password meets all the password requirements
     * @param oldPassword
     * @param newPassword
     * @param customerEntity
     * @return CustomerEntity
     * @throws UpdateCustomerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(final String oldPassword, final String newPassword,
                                                 final CustomerEntity customerEntity)throws UpdateCustomerException {
        if(oldPassword == null || oldPassword == "" ||
            newPassword == null || newPassword == ""){
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        }
        //Validate password format
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(newPassword);
        if(passwordMatcher.matches() == false){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        //Validate old password entered is correct
        String encryptedPassword = passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        if(customerEntity.getPassword().equals(encryptedPassword) == false){
            throw  new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        CustomerEntity updatedPasswordCustomer = customerDao.getCustomerByPhoneNo(customerEntity.getContactNumber());

        String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
        updatedPasswordCustomer.setSalt(encryptedText[0]);
        updatedPasswordCustomer.setPassword(encryptedText[1]);
        customerDao.updateCustomer(updatedPasswordCustomer);
        return updatedPasswordCustomer;
    }


}
