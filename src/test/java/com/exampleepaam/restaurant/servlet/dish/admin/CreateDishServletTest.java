package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.model.dto.CategoryDto;
import com.exampleepaam.restaurant.model.dto.DishCreationDto;
import com.exampleepaam.restaurant.model.entity.Category;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.validator.DishValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;

import static com.exampleepaam.restaurant.constant.PathConstants.ADMIN_DISH_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.DISH_NEW_VIEW;
import static com.exampleepaam.restaurant.testdata.TestData.UPLOAD_DIR;
import static com.exampleepaam.restaurant.web.listener.ContextListener.UPLOAD_DIR_ATTRIBUTE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDishServletTest {
    @Spy
    CreateDishServlet createDishServlet;
    @Mock
    ServletContext servletContext;
    @Mock
    ServiceFactory serviceManager;
    @Mock
    DishService dishService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    RequestDispatcher dispatcher;
    @Spy
    ServletConfig servletConfig;
    @Mock
    Part imagePart;

    private static MockedStatic<ServiceFactory> serviceManagerDummy;
    private static MockedStatic<DishValidator> dishValidatorDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        dishValidatorDummy = Mockito.mockStatic(DishValidator.class);
        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(UPLOAD_DIR_ATTRIBUTE)).thenReturn(UPLOAD_DIR);

        when(serviceManager.getDishService()).thenReturn(dishService);
    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        dishValidatorDummy.close();
    }

    @Test
    void whenGetThenReturnNewDishForm() throws ServletException, IOException {
        when(request.getRequestDispatcher(DISH_NEW_VIEW)).thenReturn(dispatcher);


        createDishServlet.init(servletConfig);
        createDishServlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void whenPostThenSaveDish() throws ServletException, IOException {


        String name = "NameTest";
        String description = "DescriptionTest";
        String category = "DRINKS";
        String price = "243";
        BigDecimal priceBigDecimal = new BigDecimal(price);

        DishCreationDto dishDto = new DishCreationDto(name, description, CategoryDto.valueOf(category), priceBigDecimal);
        Dish dish = new Dish(name, description, Category.valueOf(category), priceBigDecimal);

        when(request.getParameter(DISH_NAME_PARAM)).thenReturn(name);
        when(request.getParameter(DISH_DESCRIPTION_PARAM)).thenReturn(description);
        when(request.getParameter(DISH_CATEGORY_PARAM)).thenReturn(category);
        when(request.getParameter(DISH_PRICE_PARAM)).thenReturn(price);
        when(request.getPart(DISH_IMAGE_PARAM)).thenReturn(imagePart);
        when(imagePart.getSize()).thenReturn(0L);

        createDishServlet.init(servletConfig);
        createDishServlet.doPost(request, response);

        verify(request).setAttribute(eq(DISH_ATTRIBUTE), refEq(dishDto));
        verify(dishService, times(1)).saveOrUpdate(refEq(dish));

        verify(response).sendRedirect(ADMIN_DISH_PATH);
    }
}

