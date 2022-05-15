package com.exampleepaam.restaurant.web.filter;

import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.util.RequestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.USER_BALANCE_PARAM;

/*
 * Filter that set attribute with an updated balance when user is logged in
 */
@WebFilter("/*")
public class UserBalanceFilter extends HttpFilter {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;

        User user = RequestUtils.getSessionAttribute(req, "user", User.class);
        if (user != null) {
            BigDecimal balance = userService.getUserBalance(user.getId());
            req.setAttribute(USER_BALANCE_PARAM, balance);
        }
        chain.doFilter(request, response);

    }
}


