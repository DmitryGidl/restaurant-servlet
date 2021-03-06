package com.exampleepaam.restaurant.servlet.balance;

import com.exampleepaam.restaurant.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

import static com.exampleepaam.restaurant.constant.PathConstants.USER_MENU_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopUpBalanceServletTest {
    private static final String BALANCE_PARAM = "balance";
    private static final String USER_PARAM = "user";


    @Spy
    private TopUpBalanceServlet topUpBalanceServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserService userService;
    @Mock
    private SharedServices serviceManager;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private static MockedStatic<SharedServices> serviceManagerDummy;
    private static MockedStatic<RequestUtils> requestUtilsDummy;


    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(SharedServices.class);
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
        serviceManagerDummy.when(SharedServices::getInstance).thenReturn(serviceManager);


    }

    @AfterEach
    void close() {
        requestUtilsDummy.close();
        serviceManagerDummy.close();

    }

    @Test
    void whenPostTopUpBalanceAndRedirect() throws IOException, ServletException {
        when(serviceManager.getUserService()).thenReturn(userService);

        User user = TestData.getUser1();
        String balanceToAdd = "5";
        BigDecimal balanceToAddBigDecimal = new BigDecimal(balanceToAdd);
        when(request.getParameter(BALANCE_PARAM)).thenReturn(balanceToAdd);

        requestUtilsDummy.when(() -> RequestUtils.getSessionAttribute(request, USER_PARAM, User.class))
                .thenReturn(user);

        topUpBalanceServlet.init();
        topUpBalanceServlet.doPost(request, response);

        Mockito.verify(userService, Mockito.times(1))
                .addUserBalance(userCaptor.capture(), eq(balanceToAddBigDecimal));
        assertEquals(user, userCaptor.getValue());
        Mockito.verify(response, Mockito.times(1)).sendRedirect(USER_MENU_PATH);
    }

}

