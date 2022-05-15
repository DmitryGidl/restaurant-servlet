package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.model.dto.CategoryDto;
import com.exampleepaam.restaurant.model.dto.DishCreationDto;
import com.exampleepaam.restaurant.model.entity.Category;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.testdata.TestData;
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
import java.util.HashMap;

import static com.exampleepaam.restaurant.constant.PathConstants.ADMIN_DISH_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;

import static com.exampleepaam.restaurant.constant.ViewPathConstants.DISH_UPDATE_VIEW;
import static com.exampleepaam.restaurant.testdata.TestData.UPLOAD_DIR;
import static com.exampleepaam.restaurant.web.listener.ContextListener.UPLOAD_DIR_ATTRIBUTE;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateDishServletTest {
    @Spy
    private UpdateDishServlet updateDishServlet;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServiceFactory serviceManager;
    @Mock
    private DishService dishService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Spy
    private ServletConfig servletConfig;
    @Mock
    private Part imagePart;

    private static MockedStatic<ServiceFactory> serviceManagerDummy;
    private static MockedStatic<DishValidator> dishValidatorDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        dishValidatorDummy = Mockito.mockStatic(DishValidator.class);

        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(serviceManager.getDishService()).thenReturn(dishService);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(UPLOAD_DIR_ATTRIBUTE)).thenReturn(UPLOAD_DIR);

    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        dishValidatorDummy.close();
    }

    @Test
    void whenGetThenReturnUpdateDishForm() throws ServletException, IOException {
        when(request.getRequestDispatcher(DISH_UPDATE_VIEW)).thenReturn(dispatcher);

        String id = "2";
        when(request.getParameter(ID_PARAM)).thenReturn(id);
        Dish dish = TestData.getDish1();
        when(dishService.findById(Long.parseLong(id))).thenReturn(dish);

        updateDishServlet.init(servletConfig);
        updateDishServlet.doGet(request, response);

        verify(request).setAttribute(DISH_ATTRIBUTE, dish);

    }

    @Test
    void whenPostThenSaveDish() throws ServletException, IOException {


        String name = "NameTest";
        String description = "DescriptionTest";
        String id = "1";
        String category = "DRINKS";
        String price = "243";
        BigDecimal priceBigDecimal = new BigDecimal(price);

        DishCreationDto dishDto = new DishCreationDto(name, description, CategoryDto.valueOf(category), priceBigDecimal);
        Dish dish = new Dish(name, description, Category.valueOf(category), priceBigDecimal);
        dish.setId(Long.parseLong(id));

        dishValidatorDummy.when(() -> DishValidator.validateDish(dishDto)).thenReturn(new HashMap<>());
        when(request.getParameter(DISH_NAME_PARAM)).thenReturn(name);
        when(request.getParameter(DISH_DESCRIPTION_PARAM)).thenReturn(description);
        when(request.getParameter(DISH_CATEGORY_PARAM)).thenReturn(category);
        when(request.getParameter(DISH_PRICE_PARAM)).thenReturn(price);
        when(request.getParameter(ID_PARAM)).thenReturn(id);
        when(dishService.findById(Long.parseLong(id))).thenReturn(dish);
        when(request.getPart(DISH_IMAGE_PARAM)).thenReturn(imagePart);
        when(imagePart.getSize()).thenReturn(0L);

        updateDishServlet.init(servletConfig);
        updateDishServlet.doPost(request, response);

        verify(dishService, times(1)).saveOrUpdate(refEq(dish));

        verify(response).sendRedirect(ADMIN_DISH_PATH);
    }
}
