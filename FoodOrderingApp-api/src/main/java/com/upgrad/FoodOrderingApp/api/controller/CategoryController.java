package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    /**
     * Handles the /category and gets all categories
     * @return PaymentListResponse JSON
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories()
    {
        //Get all available categories
        List<CategoryEntity>  categoryEntities = categoryService.getAllCategoriesOrderedByName();
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
        for (CategoryEntity category : categoryEntities ) {
            CategoryListResponse categoryList = new CategoryListResponse();
            categoryList.setId(UUID.fromString(category.getUuid()));
            categoryList.categoryName(category.getCategoryName());
            categoriesListResponse.addCategoriesItem(categoryList);
        }
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    /**
     * Handles the /category/{category_id} and gets items in the category
     * @return CategoryDetailsResponse JSON
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById (@PathVariable("category_id") final String categoryId)
    throws CategoryNotFoundException
    {
        //Get the category by UUID
        CategoryEntity categoryEntity  = categoryService.getCategoryById(categoryId);
        //Create response
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryDetailsResponse.setId(UUID.fromString(categoryEntity.getUuid().toString()));
        categoryDetailsResponse.setCategoryName(categoryEntity.getCategoryName());
        for (ItemEntity item : categoryEntity.getItems()) {
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(item.getUuid().toString()));
            itemList.setItemName(item.getItemName());
            itemList.setPrice(item.getPrice());
            itemList.setItemType(ItemList.ItemTypeEnum.valueOf(item.getType().toString()));
            categoryDetailsResponse.addItemListItem(itemList);
        }

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
