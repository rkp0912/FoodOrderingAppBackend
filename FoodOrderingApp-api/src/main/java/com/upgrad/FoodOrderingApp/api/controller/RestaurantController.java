package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CustomerService customerService;

    /**
     * Handles the /restaurant and returns restaurant details ordered by highest rating.
     * @return RestaurantListResponse JSON
     * @throws RestaurantNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurants() throws RestaurantNotFoundException
    {
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByRating();
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurant : restaurantEntityList) {
            RestaurantList restaurantList = new RestaurantList();
            restaurantList.setId(UUID.fromString(restaurant.getUuid().toString()));
            restaurantList.setRestaurantName(restaurant.getRestaurantName());
            restaurantList.setPhotoURL(restaurant.getPhotoUrl());
            restaurantList.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restaurantList.setAveragePrice(restaurant.getAvgPrice());
            restaurantList.setNumberCustomersRated(restaurant.getNumberCustomersRated());
            //Restaurant Address
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.setId(UUID.fromString(restaurant.getAddress().getUuid().toString()));
            restaurantDetailsResponseAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuilNo());
            restaurantDetailsResponseAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantDetailsResponseAddress.setCity(restaurant.getAddress().getCity());
            restaurantDetailsResponseAddress.setPincode(restaurant.getAddress().getPincode());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
                    new RestaurantDetailsResponseAddressState();
            //Restaurant Address State
            restaurantDetailsResponseAddressState.setId(UUID.fromString(
                    restaurant.getAddress().getState().getUuid().toString()));
            restaurantDetailsResponseAddressState.setStateName(restaurant.getAddress().getState().getStatesName());
            restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

            restaurantList.setAddress(restaurantDetailsResponseAddress);

            //Get Restaurant Categories
            List<CategoryEntity> categoryEntityList =
                    categoryService.getCategoriesByRestaurant(restaurant.getUuid().toString());

            String categories = "";
            for (CategoryEntity category : categoryEntityList ) {
                if(categories == "")
                    categories = category.getCategoryName();
                else
                    categories = categories +", "+ category.getCategoryName();
            }
            restaurantList.setCategories(categories);
            //Add to responseList
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * Handles getting restaurant by name.
     * @param restaurantName
     * @return RestaurantListResponse JSON
     * @throws RestaurantNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable("restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException
    {
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByName(restaurantName);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurant : restaurantEntityList) {
            RestaurantList restaurantList = new RestaurantList();
            restaurantList.setId(UUID.fromString(restaurant.getUuid().toString()));
            restaurantList.setRestaurantName(restaurant.getRestaurantName());
            restaurantList.setPhotoURL(restaurant.getPhotoUrl());
            restaurantList.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restaurantList.setAveragePrice(restaurant.getAvgPrice());
            restaurantList.setNumberCustomersRated(restaurant.getNumberCustomersRated());
            //Restaurant Address
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.setId(UUID.fromString(restaurant.getAddress().getUuid().toString()));
            restaurantDetailsResponseAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuilNo());
            restaurantDetailsResponseAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantDetailsResponseAddress.setCity(restaurant.getAddress().getCity());
            restaurantDetailsResponseAddress.setPincode(restaurant.getAddress().getPincode());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
                    new RestaurantDetailsResponseAddressState();
            //Restaurant Address State
            restaurantDetailsResponseAddressState.setId(UUID.fromString(
                    restaurant.getAddress().getState().getUuid().toString()));
            restaurantDetailsResponseAddressState.setStateName(restaurant.getAddress().getState().getStatesName());
            restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

            restaurantList.setAddress(restaurantDetailsResponseAddress);

            //Get Restaurant Categories
            List<CategoryEntity> categoryEntityList =
                    categoryService.getCategoriesByRestaurant(restaurant.getUuid().toString());

            String categories = "";
            for (CategoryEntity category : categoryEntityList ) {
                if(categories == "")
                    categories = category.getCategoryName();
                else
                    categories = categories +", "+ category.getCategoryName();
            }
            restaurantList.setCategories(categories);
            //Add to responseList
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    /**
     * Handles getting restaurants by category
     * @param categoryId
     * @return RestaurantListResponse JSON
     * @throws RestaurantNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategory(@PathVariable("category_id") final String categoryId)
            throws RestaurantNotFoundException, CategoryNotFoundException
    {
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantByCategory(categoryId);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurant : restaurantEntityList) {
            RestaurantList restaurantList = new RestaurantList();
            restaurantList.setId(UUID.fromString(restaurant.getUuid().toString()));
            restaurantList.setRestaurantName(restaurant.getRestaurantName());
            restaurantList.setPhotoURL(restaurant.getPhotoUrl());
            restaurantList.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restaurantList.setAveragePrice(restaurant.getAvgPrice());
            restaurantList.setNumberCustomersRated(restaurant.getNumberCustomersRated());
            //Restaurant Address
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.setId(UUID.fromString(restaurant.getAddress().getUuid().toString()));
            restaurantDetailsResponseAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuilNo());
            restaurantDetailsResponseAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantDetailsResponseAddress.setCity(restaurant.getAddress().getCity());
            restaurantDetailsResponseAddress.setPincode(restaurant.getAddress().getPincode());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
                    new RestaurantDetailsResponseAddressState();
            //Restaurant Address State
            restaurantDetailsResponseAddressState.setId(UUID.fromString(
                    restaurant.getAddress().getState().getUuid().toString()));
            restaurantDetailsResponseAddressState.setStateName(restaurant.getAddress().getState().getStatesName());
            restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

            restaurantList.setAddress(restaurantDetailsResponseAddress);

            //Get Restaurant Categories
            List<CategoryEntity> categoryEntityList =
                    categoryService.getCategoriesByRestaurant(restaurant.getUuid().toString());

            String categories = "";
            for (CategoryEntity category : categoryEntityList ) {
                if(categories == "")
                    categories = category.getCategoryName();
                else
                    categories = categories +", "+ category.getCategoryName();
            }
            restaurantList.setCategories(categories);
            //Add to responseList
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * Handles the getting restaurant details
     * @param restaurantId
     * @return RestaurantDetailsResponse JSON
     * @throws RestaurantNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsById(@PathVariable("restaurant_id") final String restaurantId)
            throws RestaurantNotFoundException
    {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);

        RestaurantDetailsResponse restaurantListResponse = new RestaurantDetailsResponse();
        restaurantListResponse.setId(UUID.fromString(restaurantEntity.getUuid().toString()));
        restaurantListResponse.setRestaurantName(restaurantEntity.getRestaurantName());
        restaurantListResponse.setPhotoURL(restaurantEntity.getPhotoUrl());
        restaurantListResponse.setCustomerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()));
        restaurantListResponse.setAveragePrice(restaurantEntity.getAvgPrice());
        restaurantListResponse.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated());
        //Restaurant Address
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
        restaurantDetailsResponseAddress.setId(UUID.fromString(restaurantEntity.getAddress().getUuid().toString()));
        restaurantDetailsResponseAddress.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
        restaurantDetailsResponseAddress.setLocality(restaurantEntity.getAddress().getLocality());
        restaurantDetailsResponseAddress.setCity(restaurantEntity.getAddress().getCity());
        restaurantDetailsResponseAddress.setPincode(restaurantEntity.getAddress().getPincode());
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
                new RestaurantDetailsResponseAddressState();
        //Restaurant Address State
        restaurantDetailsResponseAddressState.setId(UUID.fromString(
                restaurantEntity.getAddress().getState().getUuid().toString()));
        restaurantDetailsResponseAddressState.setStateName(restaurantEntity.getAddress().getState().getStatesName());
        restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

        restaurantListResponse.setAddress(restaurantDetailsResponseAddress);
        //Get Restaurant Categories
        List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantId);

        List<CategoryList> categoryLists = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntityList ) {
            CategoryList categoryList = new CategoryList();
            categoryList.setId(UUID.fromString(categoryEntity.getUuid().toString()));
            categoryList.setCategoryName(categoryEntity.getCategoryName());

            List<ItemList> itemLists = new ArrayList<>();
            List<ItemEntity> itemEntityList =
                    itemService.getItemsByCategoryAndRestaurant(restaurantId, categoryEntity.getUuid());

            for (ItemEntity item : itemEntityList ) {
                ItemList itemList = new ItemList();
                itemList.setId(UUID.fromString(item.getUuid()));
                itemList.setItemName(item.getItemName());
                itemList.setPrice(item.getPrice());
                itemList.setItemType(ItemList.ItemTypeEnum.valueOf(item.getType().toString()));
                itemLists.add(itemList);
            }
            categoryList.setItemList(itemLists);
            categoryLists.add(categoryList);
        }
        restaurantListResponse.setCategories(categoryLists);

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * Handles the updating rating for a restaurant
     * @param restaurantId
     * @param customerRating
     * @param accessToken
     * @return RestaurantUpdatedResponse JSON
     * @throws RestaurantNotFoundException
     * @throws AuthorizationFailedException
     * @throws InvalidRatingException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, path="/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@PathVariable("restaurant_id") final String restaurantId,
                                                                        @RequestParam("customer_rating") final String customerRating,
                                                                        @RequestHeader("authorization") final String accessToken)
            throws RestaurantNotFoundException, AuthorizationFailedException, InvalidRatingException
    {

        String token = "";
        try{
            token = accessToken.split("Bearer ")[1];
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        CustomerEntity customerEntity = customerService.getCustomer(token);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity,
                                                                                     Double.parseDouble(customerRating));

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                                                            .id(UUID.fromString(restaurantId))
                                                             .status("RESTAURANT RATING UPDATED SUCCESSFULLY");


        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }


}
