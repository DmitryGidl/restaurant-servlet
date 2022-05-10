package com.exampleepaam.restaurant.servlet.auth;

import com.exampleepaam.restaurant.constant.PathConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logout servlet
 * Invalidates a user's session
 */
@WebServlet(PathConstants.USER_LOGOUT_PATH)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null)
            session.invalidate();
        resp.sendRedirect(PathConstants.INDEX_PAGE_PATH);
    }
}
