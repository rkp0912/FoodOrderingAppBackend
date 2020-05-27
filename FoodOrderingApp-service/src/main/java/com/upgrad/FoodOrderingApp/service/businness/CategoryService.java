package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * Gets the categories of the restaurant
     * @param restaurantUUID
     * @return List<CategoryEntity>
     * @throws RestaurantNotFoundException
     */
    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUUID) throws  RestaurantNotFoundException{
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUUID);
        List<RestaurantCategoryEntity> restaurantCategoryEntityList =
                restaurantCategoryDao.getRestaurantCategoryByRestaurant(restaurantEntity.getId());
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        for (RestaurantCategoryEntity restaurantCategory : restaurantCategoryEntityList) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(restaurantCategory.getCategory().getId());
            categoryEntity.setUuid(restaurantCategory.getCategory().getUuid());
            categoryEntity.setCategoryName(restaurantCategory.getCategory().getCategoryName());
            categoryEntityList.add(categoryEntity);
        }
        return categoryEntityList;
    }

    /**
     * Gets the category by UUID
     * @param categoryId
     * @return
     * @throws CategoryNotFoundException
     */
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException{
        if(categoryId == "" || categoryId == null){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryByUUID(categoryId);
        if(categoryEntity == null){
            throw  new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return categoryEntity;
    }

    /**
     * Gets all the categories in alphabetical order
     * @return
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName(){
        return categoryDao.getAllCategories();
    }
}
