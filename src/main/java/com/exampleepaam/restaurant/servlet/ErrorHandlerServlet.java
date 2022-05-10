package com.exampleepaam.restaurant.servlet;

import com.exampleepaam.restaurant.constant.PathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.ViewPathConstants.ERROR_VIEW;

/**
 * Error handling servlet
 * Logs critical errors
 */
@WebServlet(PathConstants.ERROR_HANDLER_PATH)
public class ErrorHandlerServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(ErrorHandlerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logError(req, resp);
        req.getRequestDispatcher(ERROR_VIEW).forward(req, resp);

    }

    private void logError(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        //customize error message
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request
                .getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }
        logger.error("Throwable {}, Status code {}, servletName {}, requestUri {}",
                throwable, statusCode, servletName, requestUri);

    }
}
