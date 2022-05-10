package com.exampleepaam.restaurant.web.filter;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.entity.Role;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.util.RequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.exampleepaam.restaurant.constant.PathConstants.*;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.USER_PARAM;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.ERROR_VIEW;

/*
 * Filter that restricts access to admin pages
 */
@WebFilter("/*")
public class SecurityFilter extends HttpFilter {

    protected static final List<String> ADMIN_PATHS = Arrays.asList(ADMIN_DISH_PATH,
            ADMIN_ORDER_PATH, ADMIN_NEW_DISH_PATH, ADMIN_UPDATE_DISH_PATH);

    protected static final List<String> USER_PATHS = Arrays.asList(USER_LOGOUT_PATH, USER_MENU_PATH,
            USER_ORDER_POST_PATH, USER_ORDER_HISTORY_PATH, USER_TOP_UP_PATH);

    public static final List<String> UNAUTHORIZED_USER_PATHS = List.of(INDEX_PAGE_PATH, LOGIN_PATH, SIGNUP_PATH);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String accessedPath = req.getServletPath();
        User user = RequestUtils.getSessionAttribute(req, USER_PARAM, User.class);

        if (user == null) {

            // If user is not logged in - only Auth pages are accessible
            if (ADMIN_PATHS.contains(accessedPath) || USER_PATHS.contains(accessedPath)) {
                request.getRequestDispatcher(SIGNUP_PATH).forward(request, response);
            }

            // If user is logged
        } else {
            // needs to have an admin role to access admin pages
            if (ADMIN_PATHS.contains(accessedPath) && user.getRole() != Role.ADMIN) {
                request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
            }
        }
        chain.doFilter(request, response);

    }
}
