package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetches the CategoryItem using itemId and category UUID.
     * @param itemId
     * @param categoryUUID
     * @return CategoryItemEntity
     */
    public CategoryItemEntity getItemByItemIdAndCategoryUUID(final int itemId, final String categoryUUID){
        try{
            return entityManager.createNamedQuery("categoryItemByIdAndCategoryId", CategoryItemEntity.class)
                    .setParameter("id", itemId)
                    .setParameter("categoryUUID", categoryUUID)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetches the CategoryItems by category Id.
     * @param categoryId
     * @return List<CategoryItemEntity>
     */
    public List<CategoryItemEntity> getCategoryItemsCategoryId(final int categoryId){
        try{
            return entityManager.createNamedQuery("categoryItemByCategoryId", CategoryItemEntity.class)
                    .setParameter("id", categoryId)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}
