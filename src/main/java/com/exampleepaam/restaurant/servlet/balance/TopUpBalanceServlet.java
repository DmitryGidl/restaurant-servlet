package com.exampleepaam.restaurant.servlet.balance;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.USER_BALANCE_PARAM;

/**
 * Top up balance servlet
 * Replenishes the user's balance
 */
@WebServlet(PathConstants.USER_TOP_UP_PATH)
public class TopUpBalanceServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(TopUpBalanceServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BigDecimal topUp = new BigDecimal(req.getParameter(USER_BALANCE_PARAM));
        User loggedUser = RequestUtils.getSessionAttribute(req, "user", User.class);
        if (loggedUser == null) {
            req.getRequestDispatcher(PathConstants.INDEX_PAGE_PATH).forward(req, resp);
            return;
        }
        userService.addUserBalance(loggedUser, topUp);
        logger.info("{} toped up te balance for {} UAH", loggedUser, topUp);

        resp.sendRedirect(PathConstants.USER_MENU_PATH);
    }
}
