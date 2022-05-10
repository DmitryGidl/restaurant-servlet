package com.exampleepaam.restaurant.servlet.auth;

import com.exampleepaam.restaurant.constant.ErrorAttributeConstants;
import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.exception.UserAlreadyExistsException;
import com.exampleepaam.restaurant.mapper.UserMapper;
import com.exampleepaam.restaurant.model.dto.UserCreationDto;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.ServiceManager;
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
import java.util.Map;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.REGISTRATION_VIEW;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.SIGN_UP_VIEW;

/**
 * Registration servlet
 * Returns a registration page and handles registration
 */
@WebServlet(PathConstants.SIGNUP_PATH)
public class RegistrationServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(RegistrationServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ServiceManager.getInstance().getUserService();
    }


    public static final String ERROR_ATTRIBUTE_GLOBAL = "errorGlobal";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (RequestUtils.getSessionAttribute(req, USER_PARAM, User.class) != null) {
            resp.sendRedirect(PathConstants.INDEX_PAGE_PATH);
        } else {
            req.getRequestDispatcher(REGISTRATION_VIEW).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(USER_NAME_PARAM);
        String email = req.getParameter(USER_EMAIL_PARAM);
        String password = req.getParameter(USER_PASSWORD_PARAM);
        String matchingPassword = req.getParameter(AUTH_MATCHING_PASSWORD_PARAM);

        UserCreationDto userCreationDto = new UserCreationDto(name, email, password, matchingPassword);
        Map<String, String> viewAttributes = UserValidator.isUserValid(userCreationDto);

        req.setAttribute(USER_EMAIL_PARAM, email);
        req.setAttribute(USER_NAME_PARAM, name);
        req.setAttribute(USER_PASSWORD_PARAM, password);

        User user = UserMapper.toUser(userCreationDto);

// If map contains attributes - validation is failed
        if (!viewAttributes.isEmpty()) {
            ControllerUtil.passErrorsToView(SIGN_UP_VIEW, req, resp, viewAttributes);
            return;
        }

        try {
            long id = userService.save(user);
            user.setId(id);
            logger.info("{} was registered", user);
            HttpSession session = req.getSession(true);
            session.setAttribute(USER_PARAM, user);
            session.setMaxInactiveInterval(86400); // 1 day
        } catch (UserAlreadyExistsException e) {
            logger.debug("{} tried to register, but account exists", user);
            req.setAttribute(ERROR_ATTRIBUTE_GLOBAL, ErrorAttributeConstants.ACCOUNT_EXISTS);
            req.getRequestDispatcher(REGISTRATION_VIEW).forward(req, resp);
            return;
        }
        resp.sendRedirect(PathConstants.USER_MENU_PATH);
    }
}

