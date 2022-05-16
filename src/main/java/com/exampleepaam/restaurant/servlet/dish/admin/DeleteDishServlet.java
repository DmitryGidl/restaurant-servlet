package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.web.listener.ContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.ID_PARAM;

/**
 * Delete dish servlet
 * Deletes a dish with its image folder
 */
@WebServlet(PathConstants.ADMIN_DELETE_DISH_PATH)
public class DeleteDishServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(DeleteDishServlet.class);

    private String uploadDir;
    private DishService dishService;

    @Override
    public void init() throws ServletException {
        dishService = SharedServices.getInstance().getDishService();
        ServletContext sc = getServletContext();
        uploadDir = (String) sc.getAttribute(ContextListener.UPLOAD_DIR_ATTRIBUTE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter(ID_PARAM));
        if (id > 0) {
            dishService.deleteWithImage(id, uploadDir);
            logger.info("Dish with id {} is deleted with an image", id);
        }
        resp.sendRedirect(PathConstants.ADMIN_DISH_PATH);
    }
}
