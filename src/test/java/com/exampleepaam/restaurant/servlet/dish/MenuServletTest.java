package com.exampleepaam.restaurant.servlet.dish;

import com.exampleepaam.restaurant.mapper.DishMapper;
import com.exampleepaam.restaurant.model.dto.DishResponseDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.testdata.TestData;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.MENU_VIEW;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServletTest {
    @Spy
    private MenuServlet menuServlet;
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
    private ArgumentCaptor<Dish> dishCaptor;

    private static MockedStatic<DishMapper> dishMapperDummy;
    private static MockedStatic<SharedServices> serviceManagerDummy;


    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(SharedServices.class);
        dishMapperDummy = Mockito.mockStatic(DishMapper.class);
        serviceManagerDummy.when(SharedServices::getInstance).thenReturn(serviceManager);


    }

    @AfterEach
    void close() {
        dishMapperDummy.close();
        serviceManagerDummy.close();
    }

    @Test
    void whenGetReturnDishes() throws ServletException, IOException {
        String sortDir = "asc";
        String sortField = "category";
        String filterCategory = "all";
        String reverseSortDir = "desc";

        when(serviceManager.getDishService()).thenReturn(dishService);
        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(sortField);
        when(request.getParameter(SORT_DIR_PARAM)).thenReturn(sortDir);
        when(request.getParameter(FILTER_CATEGORY_PARAM)).thenReturn(filterCategory);
        List<Dish> dishes = TestData.getDishList();
        when(dishService.findAllDishesSorted(sortField, sortDir)).thenReturn(dishes);

        List<DishResponseDto> dishesResponse = TestData.getDishResponseList();
        dishMapperDummy.when(() -> DishMapper.toDishResponseDtoList(dishes)).thenReturn(dishesResponse);

        when(request.getRequestDispatcher(MENU_VIEW)).thenReturn(dispatcher);

        menuServlet.init();
        menuServlet.doGet(request, response);

        verify(request, times(1)).setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverseSortDir);
        verify(request, times(1)).setAttribute(eq(DISHES_ATTRIBUTE), dishCaptor.capture());
        assertTrue(EqualsBuilder.reflectionEquals(dishesResponse, dishCaptor.getValue()));
        verify(request, times(1)).setAttribute(SORT_DIR_PARAM, sortDir);
        verify(request, times(1)).setAttribute(FILTER_CATEGORY_PARAM, filterCategory);
        verify(dispatcher, times(1)).forward(request, response);
    }
}


