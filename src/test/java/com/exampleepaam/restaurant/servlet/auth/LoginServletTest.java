package com.exampleepaam.restaurant.servlet.auth;

import com.exampleepaam.restaurant.model.dto.UserCreationDto;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.RequestUtils;
import com.exampleepaam.restaurant.validator.UserValidator;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.PathConstants.USER_MENU_PATH;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Spy
    private LoginServlet loginServlet;

    @Mock
    private UserService userService;

    @Mock
    private ServiceFactory serviceManager;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private HttpSession session;


    public static final String USER_ATTRIBUTE = "user";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String EMAIL_PARAMETER = "email";

    private static MockedStatic<ServiceFactory> serviceManagerDummy;
    private static MockedStatic<UserValidator> userValidatorDummy;
    private static MockedStatic<RequestUtils> requestUtilsDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        userValidatorDummy = Mockito.mockStatic(UserValidator.class);
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        userValidatorDummy.close();
        requestUtilsDummy.close();
    }


    @Test
    void whenGetThenReturnLoginPage() throws ServletException, IOException {

        when(request.getRequestDispatcher(LOGIN_VIEW)).thenReturn(dispatcher);
        loginServlet.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(LOGIN_VIEW);
        verify(request, never()).getSession(true);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void whenPostThenLoginAndRedirect() throws ServletException, IOException {
        UserCreationDto userCreation = TestData.getUserCreation1();
        User user = TestData.getUser1();

        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(serviceManager.getUserService()).thenReturn(userService);
        when(request.getParameter(EMAIL_PARAMETER)).thenReturn(userCreation.getEmail());
        when(request.getParameter(PASSWORD_PARAMETER)).thenReturn(userCreation.getPassword());
        when(request.getSession(true)).thenReturn(session);


        when(userService.findByEmail(userCreation.getEmail())).thenReturn(user);

        userValidatorDummy.when(() -> UserValidator.isPasswordCorrect
                (userCreation.getPassword(), user.getPassword())).thenReturn(true);
        requestUtilsDummy.when(() -> RequestUtils.getSessionAttribute(any(), any(), any())).thenReturn(null);

        when(userService.findByEmail(userCreation.getEmail())).thenReturn(user);
        loginServlet.init();
        loginServlet.doPost(request, response);


        verify(session, times(1)).setAttribute(eq(USER_ATTRIBUTE), any(User.class));
        verify(session, times(1)).setMaxInactiveInterval(604800);
        verify(response, times(1)).sendRedirect(USER_MENU_PATH);

    }


}



