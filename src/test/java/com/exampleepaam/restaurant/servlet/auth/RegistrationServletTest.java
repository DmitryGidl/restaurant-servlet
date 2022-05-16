package com.exampleepaam.restaurant.servlet.auth;

import com.exampleepaam.restaurant.mapper.UserMapper;
import com.exampleepaam.restaurant.model.dto.UserCreationDto;
import com.exampleepaam.restaurant.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.RequestUtils;
import com.exampleepaam.restaurant.validator.UserValidator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

import static com.exampleepaam.restaurant.constant.PathConstants.USER_MENU_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.REGISTRATION_VIEW;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {


    @Spy
    private RegistrationServlet registrationServlet;
    @Mock
    private UserService userService;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private SharedServices serviceManager;

    private static MockedStatic<RequestUtils> requestUtilsDummy;
    private static MockedStatic<SharedServices> serviceManagerDummy;
    private static MockedStatic<UserValidator> userValidatorDummy;
    private static MockedStatic<UserMapper> userMapperDummy;


    @BeforeEach
    void setUp() {
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
        serviceManagerDummy = Mockito.mockStatic(SharedServices.class);
        userValidatorDummy = Mockito.mockStatic(UserValidator.class);
        userMapperDummy = Mockito.mockStatic(UserMapper.class);

    }

    @AfterEach
    void close() {
        requestUtilsDummy.close();
        serviceManagerDummy.close();
        userValidatorDummy.close();
        userMapperDummy.close();
    }


    @Test
    void whenGetThenReturnRegistrationPage() throws ServletException, IOException {

        requestUtilsDummy.when(() -> RequestUtils.getSessionAttribute(any(), any(), any())).thenReturn(null);
        when(request.getRequestDispatcher(REGISTRATION_VIEW)).thenReturn(dispatcher);

        registrationServlet.doGet(request, response);

        verify(dispatcher, times(1)).forward(request, response);

    }

    @Test
    void whenPostThenSaveUser() throws IOException, ServletException {
        serviceManagerDummy.when(SharedServices::getInstance).thenReturn(serviceManager);
        when(serviceManager.getUserService()).thenReturn(userService);

        UserCreationDto userCreationDto = TestData.getUserCreation1();
        User user = TestData.getUser1();

        when(request.getParameter(USER_NAME_PARAM)).thenReturn(userCreationDto.getName());
        when(request.getParameter(USER_EMAIL_PARAM)).thenReturn(userCreationDto.getEmail());
        when(request.getParameter(USER_PASSWORD_PARAM)).thenReturn(userCreationDto.getPassword());
        when(request.getParameter(AUTH_MATCHING_PASSWORD_PARAM)).thenReturn(userCreationDto.getMatchingPassword());
        userValidatorDummy.when(() -> UserValidator.isUserValid(userCreationDto)).thenReturn(new HashMap<>());
        when(userService.save(user)).thenReturn(1L);
        when(request.getSession(true)).thenReturn(session);
        userMapperDummy.when(() -> UserMapper.toUser(userCreationDto)).thenReturn(user);

        registrationServlet.init();
        registrationServlet.doPost(request, response);

        verify(request, times(1)).setAttribute(USER_EMAIL_PARAM, userCreationDto.getEmail());
        verify(request, times(1)).setAttribute(USER_NAME_PARAM, userCreationDto.getName());
        verify(request, times(1)).setAttribute(USER_PASSWORD_PARAM, userCreationDto.getPassword());
        verify(request, times(1)).getSession(true);
        verify(session, times(1)).setAttribute(USER_PARAM, user);
        verify(session, times(1)).setMaxInactiveInterval(86400);
        verify(response, times(1)).sendRedirect(USER_MENU_PATH);
    }

}

