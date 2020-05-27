package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemService {
    @Autowired
    private RestaurantItemDao restaurantItemDao;

    @Autowired
    private CategoryItemDao categoryItemDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private OrderDao ordersDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ItemDao itemDao;

    /**
     * Gets the items by category and restaurant.
     * @param restaurantUUID
     * @param categoryUUID
     * @return
     */
     public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUUID, String categoryUUID){
        List<RestaurantItemEntity> restaurantItemEntityList = restaurantItemDao.getRestaurantItemByUUID(restaurantUUID);
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (RestaurantItemEntity restaurantItem : restaurantItemEntityList) {
            CategoryItemEntity categoryItemEntity = categoryItemDao.getItemByItemIdAndCategoryUUID(restaurantItem.getItem().getId(), categoryUUID);

            if (categoryItemEntity != null) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setId(categoryItemEntity.getItem().getId());
                itemEntity.setUuid(categoryItemEntity.getItem().getUuid());
                itemEntity.setItemName(categoryItemEntity.getItem().getItemName());
                itemEntity.setPrice(categoryItemEntity.getItem().getPrice());
                itemEntity.setType(categoryItemEntity.getItem().getType());
                itemEntities.add(itemEntity);
             }
        }

        return itemEntities;
     }


    /**
     * Gets to 5 items ordered the most from a restaurant
     * @param restaurantEntity
     * @return
     */
     public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){

        List<OrderEntity> orderEntityList = ordersDao.getOrdersByRestaurant(restaurantEntity.getId());
        List<Integer> orderIdList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntityList ) {
            int id = orderEntity.getId();
            orderIdList.add(id);
        }
        List<Integer> items =  orderItemDao.getItemsCountByOrders(orderIdList);
        List<ItemEntity> itemEntityList = new ArrayList<>();
        int counter = 0;
        for (Integer itemId : items ) {
            if(counter == 5)
                break;
            counter++;
            ItemEntity itemEntity = new ItemEntity();
            ItemEntity item = itemDao.getItemById(itemId);
            itemEntity.setId(item.getId());
            itemEntity.setUuid(item.getUuid());
            itemEntity.setItemName(item.getItemName());
            itemEntity.setPrice(item.getPrice());
            itemEntity.setType(item.getType());
            itemEntityList.add(itemEntity);
        }
        return itemEntityList;
     }

    /**
     * Gets the CategoryItemLists based on category ID
     * @param categoryId
     * @return
     */
     public List<CategoryItemEntity> getItemsOfCategory(int categoryId){
         return categoryItemDao.getCategoryItemsCategoryId(categoryId);
     }
}
