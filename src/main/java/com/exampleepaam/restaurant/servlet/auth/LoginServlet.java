package com.exampleepaam.restaurant.servlet.auth;

import com.exampleepaam.restaurant.constant.ErrorAttributeConstants;
import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.util.ControllerUtil;
import com.exampleepaam.restaurant.util.RequestUtils;
import com.exampleepaam.restaurant.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.exampleepaam.restaurant.constant.ErrorAttributeConstants.ERROR_ATTRIBUTE_GLOBAL;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.LOGIN_VIEW;

/**
 * Login servlet
 * Returns login page and handles login
 */

@WebServlet(PathConstants.LOGIN_PATH)
public class LoginServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    private UserService userService;

    @Override
    public void init() {
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (RequestUtils.getSessionAttribute(req, USER_PARAM, User.class) != null) {
            resp.sendRedirect(PathConstants.USER_MENU_PATH);
            logger.warn("Logged sent a login request");
            return;
        }

        String email = req.getParameter(USER_EMAIL_PARAM);
        String password = req.getParameter(USER_PASSWORD_PARAM);
        String remember = req.getParameter(AUTH_REMEMBER_PARAM);

        Map<String, String> viewAttributes = new HashMap<>();

        User user = userService.findByEmail(email);

// If validation fails - immediately return the login page with error attribute in it

        if (user == null) {
            viewAttributes.put(ERROR_ATTRIBUTE_GLOBAL, ErrorAttributeConstants.USER_NOT_FOUND);
            ControllerUtil.passErrorsToView(LOGIN_VIEW, req, resp, viewAttributes);
            return;
        }

        if (!user.isEnabled()) {
            viewAttributes.put(ERROR_ATTRIBUTE_GLOBAL, ErrorAttributeConstants.USER_BLOCKED);
            ControllerUtil.passErrorsToView(LOGIN_VIEW, req, resp, viewAttributes);
            return;
        }

        if (!UserValidator.isPasswordCorrect(password, user.getPassword())) {
            viewAttributes.put(ERROR_ATTRIBUTE_GLOBAL, ErrorAttributeConstants.INVALID_CREDENTIALS);
            ControllerUtil.passErrorsToView(LOGIN_VIEW, req, resp, viewAttributes);
            return;
        }

// Validation successful - save the user in a session
        HttpSession session = req.getSession(true);
        session.setAttribute(USER_PARAM, user);
        logger.debug("{} was put in a session", user);
        if ("".equals(remember))
            session.setMaxInactiveInterval(1800); // 30 minutes
        else
            session.setMaxInactiveInterval(604800); // 7 days
        resp.sendRedirect(PathConstants.USER_MENU_PATH);

    }


}
