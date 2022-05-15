package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.PathConstants.ADMIN_DISH_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.ID_PARAM;
import static com.exampleepaam.restaurant.testdata.TestData.UPLOAD_DIR;
import static com.exampleepaam.restaurant.web.listener.ContextListener.UPLOAD_DIR_ATTRIBUTE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDishServletTest {
    @Spy
    private DeleteDishServlet deleteDishServlet;
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
    @Spy
    private ServletConfig servletConfig;

    private static MockedStatic<ServiceFactory> serviceManagerDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(serviceManager.getDishService()).thenReturn(dishService);
        when(servletConfig.getServletContext()).thenReturn(servletContext);
    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
    }

    @Test
    void whenPostThenDelete() throws ServletException, IOException {

        when(servletContext.getAttribute(UPLOAD_DIR_ATTRIBUTE)).thenReturn(UPLOAD_DIR);
        String id = "10";
        when(request.getParameter(ID_PARAM)).thenReturn(id);

        deleteDishServlet.init(servletConfig);
        deleteDishServlet.doPost(request, response);
        verify(dishService, times(1)).deleteWithImage(Long.parseLong(id), UPLOAD_DIR);
        verify(response).sendRedirect(ADMIN_DISH_PATH);
    }
}
