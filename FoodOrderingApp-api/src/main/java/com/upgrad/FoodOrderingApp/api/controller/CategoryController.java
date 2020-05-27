package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
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
        CategoryEntity categoryEntity  = categoryService.getCategoryById(categoryId);
        List<CategoryItemEntity> categoryItemEntities =itemService.getItemsOfCategory(categoryEntity.getId());
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        for (CategoryItemEntity categoryItemEntity : categoryItemEntities) {
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(categoryItemEntity.getItem().getUuid().toString()));
            itemList.setItemName(categoryItemEntity.getItem().getItemName());
            itemList.setPrice(categoryItemEntity.getItem().getPrice());
            itemList.setItemType(ItemList.ItemTypeEnum.valueOf(categoryItemEntity.getItem().getType().toString()));
            categoryDetailsResponse.addItemListItem(itemList);
        }

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
