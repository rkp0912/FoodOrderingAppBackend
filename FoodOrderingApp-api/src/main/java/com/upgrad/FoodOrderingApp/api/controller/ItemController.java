package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class ItemController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

    /**
     * Handles the Getting the items by popularity in a restaurant
     * @param restaurantId
     * @return
     * @throws RestaurantNotFoundException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path="/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getItemsByPopularity(@PathVariable("restaurant_id") final String restaurantId) throws RestaurantNotFoundException
    {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        List<ItemEntity> itemEntities = itemService.getItemsByPopularity(restaurantEntity);
        ItemListResponse itemListResponse = new ItemListResponse();
        for (ItemEntity itemEntity : itemEntities) {
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(itemEntity.getUuid()));
            itemList.setItemName(itemEntity.getItemName());
            itemList.setPrice(itemEntity.getPrice());
            itemList.setItemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().toString()));
            itemListResponse.add(itemList);
        }
        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }


}
