package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private CategoryService categoryService;

    /**
     * Gets the restaurant by UUID
     * @param restaurantId
     * @return
     * @throws RestaurantNotFoundException
     */
    public RestaurantEntity restaurantByUUID(String restaurantId) throws RestaurantNotFoundException{
        if(restaurantId == "" || restaurantId == null){
            throw  new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUUID(restaurantId);
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        return restaurantEntity;
    }

    /**
     * Gets the restaurants based on rating.
     * @return List<RestaurantEntity>
     */
    public List<RestaurantEntity> restaurantsByRating(){
        List<RestaurantEntity> restaurantEntityList = restaurantDao.getAllRestaurantByRating();
        return restaurantEntityList;
    }

    /**
     * gets the restaurants by name.
     * @param restaurantName
     * @return List<RestaurantEntity>
     * @throws RestaurantNotFoundException
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException{
        if(restaurantName == "" || restaurantName == null){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }
        List<RestaurantEntity> restaurantEntityList = restaurantDao.getRestaurantsByName(restaurantName);
        return restaurantEntityList;
    }

    /**
     * Gets the RestaurantByCategory by Category
     * @param categoryId
     * @return
     * @throws CategoryNotFoundException
     */
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryId);
        List<RestaurantCategoryEntity> restaurantCategoryEntityList =
                restaurantCategoryDao.getRestaurantCategoryByCategory(categoryEntity.getId());
        List<RestaurantEntity> restaurantEntityList = new ArrayList<>();
        for (RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryEntityList) {
            RestaurantEntity restaurantEntity = new RestaurantEntity();
            RestaurantEntity restaurant = restaurantDao.getRestaurantById(restaurantCategoryEntity.getRestaurant().getId());
            restaurantEntity.setId(restaurant.getId());
            restaurantEntity.setUuid(restaurant.getUuid());
            restaurantEntity.setRestaurantName(restaurant.getRestaurantName());
            restaurantEntity.setPhotoUrl(restaurant.getPhotoUrl());
            restaurantEntity.setCustomerRating(restaurant.getCustomerRating());
            restaurantEntity.setAvgPrice(restaurant.getAvgPrice());
            restaurantEntity.setNumberCustomersRated(restaurant.getNumberCustomersRated());
            restaurantEntity.setAddress(restaurant.getAddress());
            restaurantEntityList.add(restaurantEntity);
        }
        return restaurantEntityList;
    }

    /**
     * Update the number of customer rated and average rating of a restaurant.
     * @param restaurantEntity
     * @param rating
     * @return
     * @throws InvalidRatingException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double rating)
            throws InvalidRatingException {

        if(rating < 1 || rating >5){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }

        Double sumOfCurrentRating = restaurantEntity.getCustomerRating() * restaurantEntity.getNumberCustomersRated();
        Double sumOfUpdatedRating = sumOfCurrentRating + rating;
        //Increment of no of customer rated by 1;
        restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated() + 1);
        //New Average rating
        Double newAvgRating = (sumOfUpdatedRating / restaurantEntity.getNumberCustomersRated());
        restaurantEntity.setCustomerRating(Math.round(newAvgRating * 100.0)/100.0);
        RestaurantEntity updatedRestaurantEntity = restaurantDao.updateRestaurantInfo(restaurantEntity);
        return updatedRestaurantEntity;
    }
}
