package com.exampleepaam.restaurant.servlet.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServletTest {

    @Spy
    private LogoutServlet logoutServlet;

    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    void whenGetInvalidateSessionAndRedirect() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(httpSession);
        logoutServlet.doGet(request, response);
        verify(httpSession, times(1)).invalidate();
    }
}

