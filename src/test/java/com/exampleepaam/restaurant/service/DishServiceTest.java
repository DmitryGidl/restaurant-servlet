package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.DishDao;
import com.exampleepaam.restaurant.model.entity.Category;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.FileUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    static MockedStatic<DaoFactory> daoFactoryDummy;
    static MockedStatic<FileUtil> fileUtilDummy;

    @Mock
    static DaoFactory daoFactory;
    @Mock
    static DishDao dishDao;
    @Spy
    DishService dishService;

    @BeforeEach
    void setUp() {
        daoFactoryDummy = Mockito.mockStatic(DaoFactory.class);
        daoFactoryDummy.when(DaoFactory::getInstance).thenReturn(daoFactory);
        when(daoFactory.createDishDao()).thenReturn(dishDao);
        fileUtilDummy = Mockito.mockStatic(FileUtil.class);
    }

    @AfterEach
    void close() {
        daoFactoryDummy.close();
        fileUtilDummy.close();
    }

    @Test
    void deleteWithImage_id0_success() {

        long id = 2;
        String deleteDir = "/dish-image/2";

        dishService.deleteWithImage(id, deleteDir);
        verify(dishDao, times(1)).delete(id);
        fileUtilDummy.verify(() -> FileUtil.deleteFolder(deleteDir));
    }

    @Test
    void update_idSet_success() {
        Dish dish = new Dish(2, "testName",
                "testDescription", Category.DRINKS, BigDecimal.TEN, "/image21");
        dishService.saveOrUpdate(dish);
        verify(dishDao, times(1)).update(dish);
        verify(dishDao, times(1)).close();
    }

    @Test
    void save_id0_success() {
        Dish dish = new Dish(0, "testName", "testDescription", Category.DRINKS, BigDecimal.TEN, "/image21");
        dishService.saveOrUpdate(dish);
        verify(dishDao, times(1)).save(dish);
        verify(dishDao, times(1)).close();
    }

    @Test
    void findPaginatedByCategory_filterCategoryAll_success() {

        int currentPage = 1;
        int pageSize = 10;
        String sortField = "Category";
        String sortDir = "asc";
        String filterCategory = "all";
        Paged<Dish> dishPagedExpected = TestData.getDishPaged();
        when(dishDao.findPaginated(currentPage, pageSize, sortField, sortDir)).thenReturn(dishPagedExpected);

        Paged<Dish> dishPagedActual = dishService.findPaginatedByCategory(currentPage, pageSize,
                sortField, sortDir, filterCategory);
        Assertions.assertEquals(dishPagedExpected, dishPagedActual);
        verify(dishDao).close();
    }

    @Test
    void getById_success() {
        Dish dishExpected = TestData.getDish1();
        long id = 132;
        when(dishDao.findById(id)).thenReturn(dishExpected);
        Dish dishActual = dishService.findById(id);
        Assertions.assertEquals(dishExpected, dishActual);
        verify(dishDao, times(1)).findById(id);
        verify(dishDao).close();

    }

    @Test
    void findAllDishesSorted_success() {
        String sortField = "price";
        String sortDir = "desc";
        List<Dish> dishListExptected = TestData.getDishList();
        when(dishDao.findAllDishesSorted(sortField, sortDir)).thenReturn(dishListExptected);
        List<Dish> dishListActual = dishService.findAllDishesSorted(sortField, sortDir);
        Assertions.assertEquals(dishListExptected, dishListActual);
        verify(dishDao).close();
    }

    @Test
    void findAllDishesByCategorySorted_success() {
        String sortField = "price";
        String sortDir = "desc";
        String filterCategory = "BURGERS";
        List<Dish> dishListExptected = TestData.getDishList();
        when(dishDao.findAllDishesByCategorySorted(sortField, sortDir, filterCategory)).thenReturn(dishListExptected);
        List<Dish> dishListActual = dishService.findAllDishesByCategorySorted(sortField, sortDir, filterCategory);
        Assertions.assertEquals(dishListExptected, dishListActual);
        verify(dishDao).close();
    }

    @Test
    void fetchDishesToMap_success() {
        Map<Long, Integer> dishIdQuantityMap = new HashMap<>();
        dishIdQuantityMap.put(1L, 2);
        dishIdQuantityMap.put(2L, 4);
        Dish dish1 = TestData.getDish1();
        Dish dish2 = TestData.getDish2();
        when(dishDao.findById(1L)).thenReturn(dish1);
        when(dishDao.findById(2L)).thenReturn(dish2);

        Map<Dish, Integer> dishQuantityMapExpected = new HashMap<>();
        dishQuantityMapExpected.put(dish1, 2);
        dishQuantityMapExpected.put(dish2, 4);

        Map<Dish, Integer> dishQuantityMapActual = dishService.fetchDishesToMap(dishIdQuantityMap);
        Assertions.assertEquals(dishQuantityMapExpected, dishQuantityMapActual);
        verify(dishDao).close();
    }

}
