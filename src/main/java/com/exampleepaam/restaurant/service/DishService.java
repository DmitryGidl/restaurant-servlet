package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.DishDao;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.util.FileUtil;

import javax.servlet.http.Part;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for the Dish entity
 */
public class DishService {


    /**
     * Saves an order with file if Id equals 0 and updates if not
     *
     * @param newDish Dish entity for saving or update
     * @param imagePart image file to be stored
     * @param imagesFolder relative folder where image file will be saved
     */
    public void saveOrUpdateWithFile(Dish newDish, Part imagePart, String imagesFolder) {

        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {

            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            newDish.setImageFileName(fileName);
            long newDishId = newDish.getId();
            long persistedDishId;

            if (newDishId == 0) {
                persistedDishId = dishDao.save(newDish);
            } else {
                persistedDishId = newDishId;
                dishDao.    update(newDish);
            }

            saveImage(imagePart, persistedDishId, fileName, imagesFolder);
        }
    }

    /**
     * Saves an order if id equals 0 and updates if not
     *
     * @param newDish Dish entity for saving or update
     */
    public void saveOrUpdate(Dish newDish) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            long newDishId = newDish.getId();
            if (newDishId == 0) {
                dishDao.save(newDish);
            } else {
                dishDao.update(newDish);
            }
        }
    }

    /**
     *  Saves an image
     *
     * @param imagePart image file to be saved
     * @param persistedDishId id is a part of a folder name
     * @param fileName name of the image file
     * @param imagesFolder  relative folder where image file will be saved
     */
    private void saveImage(Part imagePart, long persistedDishId, String fileName, String imagesFolder) {

        String uploadDir = imagesFolder + persistedDishId;
        FileUtil.deleteFolder(uploadDir);
        FileUtil.saveFile(uploadDir, fileName, imagePart);
    }

    /**
     *  Deletes a dish with an image
     *
     * @param id Dish id to be deleted
     * @param deleteDir Dir of the folder where dish image is saved
     */
    public void deleteWithImage(long id, String deleteDir) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            dishDao.delete(id);
        }
        FileUtil.deleteFolder(deleteDir);
    }

    /**
     * Returns a Paged object with a list of sorted dishes filtered by category
     *
     * @param currentPage current page
     * @param pageSize number of rows per page
     * @param sortField sort column for rows
     * @param sortDir sort direction for rows
     * @param filterCategory filter category, 'all' value makes it ignored
     * @return a Paged object with a sorted and filtered by category list of dishes or an empty list if nothing is found
     */
    public Paged<Dish> findPaginatedByCategory(int currentPage, int pageSize,
                                               String sortField, String sortDir, String filterCategory) {
        Paged<Dish> dishPaged;
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            if (filterCategory.equals("all")) {
                dishPaged = dishDao.findPaginated(currentPage, pageSize, sortField, sortDir);
            } else {
                dishPaged = dishDao.findPaginatedByCategory(currentPage, pageSize, sortField, sortDir, filterCategory);
            }
            return dishPaged;
        }
    }

    /**
     *  Returns a dish by id
     *
     * @param id Dish id
     * @return a Dish or null if dish is not found by id
     */
    public Dish findById(long id) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            Dish dish;
            dish = dishDao.findById(id);
            return dish;
        }
    }

    /**
     *  Maps a DishID-Quantity map to a Dish-Quantity map by fetching dishes
     *
     * @param  dishIdQuantityMap Long-Integer map that contains dishID and quantity of times dish was ordered
     * @return a map of Dishes-Quantity
     */
    public Map<Dish, Integer> fetchDishesToMap(Map<Long, Integer> dishIdQuantityMap) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            return dishIdQuantityMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> dishDao.findById(entry.getKey()),
                            Map.Entry::getValue));
        }

    }

    /**
     *  Returns a list of sorted dishes
     *
     * @param  sortField sort column for rows
     * @param  sortDir sort direction for rows
     * @return a list of dishes or an empty list if no dishes were found
     */
    public List<Dish> findAllDishesSorted(String sortField, String sortDir) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            return dishDao.findAllDishesSorted(sortField, sortDir);
        }

    }

    /**
     *  Returns a list of sorted and filtered by category dishes
     *
     * @param  sortField sort column for rows
     * @param  sortDir sort direction for rows
     * @param  filterCategory filter value for category
     * @return a list of Dishes or empty list if no dishes were found
     */
    public List<Dish> findAllDishesByCategorySorted(String sortField, String sortDir, String filterCategory) {
        try (DishDao dishDao = DaoFactory.getInstance().createDishDao()) {
            return dishDao.findAllDishesByCategorySorted(sortField, sortDir, filterCategory);
        }
    }
}