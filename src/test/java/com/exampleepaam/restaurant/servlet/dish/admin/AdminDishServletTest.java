package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.model.dto.DishResponseDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.testdata.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.DISH_MANAGEMENT_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminDishServletTest {
    @Spy
    private AdminDishServlet adminDishServlet;
    @Mock
    private SharedServices serviceManager;
    @Mock
    private DishService dishService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Captor
    private ArgumentCaptor<Paged<DishResponseDto>> dishResponseCaptor;

    private static MockedStatic<SharedServices> serviceManagerDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(SharedServices.class);
        serviceManagerDummy.when(SharedServices::getInstance).thenReturn(serviceManager);
        when(serviceManager.getDishService()).thenReturn(dishService);
    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
    }

    @Test
    void whenGetReturnDishes() throws ServletException, IOException {

        String uploadDir = "/testDir";
        String sortField = "category";
        String sortDir = "asc";
        String filterCategory = "all";
        String pageSize = "10";
        String currentPage = "1";
        String reverseSortDir = "desc";

        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(sortField);
        when(request.getParameter(SORT_DIR_PARAM)).thenReturn(sortDir);
        when(request.getParameter(FILTER_CATEGORY_PARAM)).thenReturn(filterCategory);
        when(request.getParameter(PAGE_SIZE_PARAM)).thenReturn(pageSize);
        when(request.getParameter(CURRENT_PAGE_PARAM)).thenReturn(currentPage);
        when(request.getRequestDispatcher(DISH_MANAGEMENT_VIEW)).thenReturn(dispatcher);


        Paged<Dish> pageDish = TestData.getDishPaged();
        when(dishService.findPaginatedByCategory(Integer.parseInt(currentPage), Integer.parseInt(pageSize),
                sortField, sortDir, filterCategory)).thenReturn(pageDish);
        adminDishServlet.init();
        adminDishServlet.doGet(request, response);
        Paged<DishResponseDto> pageDishDto = TestData.getDishDtoPaged();
        verify(request, times(1)).setAttribute(eq(DISH_PAGED_ATTRIBUTE), dishResponseCaptor.capture());
        assertEquals(pageDishDto, dishResponseCaptor.getValue());
        verify(dispatcher, times(1)).forward(request, response);
    }
}

