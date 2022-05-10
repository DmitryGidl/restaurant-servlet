package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.dto.DishCreationDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceManager;
import com.exampleepaam.restaurant.mapper.DishMapper;
import com.exampleepaam.restaurant.model.dto.CategoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exampleepaam.restaurant.validator.DishValidator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;
import static com.exampleepaam.restaurant.util.ControllerUtil.passErrorsToView;
import static com.exampleepaam.restaurant.web.listener.ContextListener.UPLOAD_DIR_ATTRIBUTE;

/**
 * Update dish servlet
 * Returns dish update form and handles its update with an image
 */
@WebServlet(PathConstants.ADMIN_UPDATE_DISH_PATH)
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class UpdateDishServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(UpdateDishServlet.class);

    private String uploadDir;
    private DishService dishService;

    @Override
    public void init() throws ServletException {
        dishService = ServiceManager.getInstance().getDishService();
        ServletContext sc = getServletContext();
        uploadDir = (String) sc.getAttribute(UPLOAD_DIR_ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(ID_PARAM);
        Dish dish = dishService.findById(Long.parseLong(id));

        req.setAttribute(DISH_ATTRIBUTE, dish);
        req.getRequestDispatcher(DISH_UPDATE_VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part imagePart = req.getPart(DISH_IMAGE_PARAM);

        String name = req.getParameter(DISH_NAME_PARAM).trim();
        String description = req.getParameter(DISH_DESCRIPTION_PARAM).trim();
        String category = req.getParameter(DISH_CATEGORY_PARAM).toUpperCase(Locale.ROOT);
        BigDecimal price = new BigDecimal(req.getParameter(DISH_PRICE_PARAM));
        long id = Long.parseLong(req.getParameter(ID_PARAM));
        DishCreationDto dishDto = new DishCreationDto(name, description,
                CategoryDto.valueOf(category), price);

        Map<String, String> viewAttributes = DishValidator.validateDish(dishDto);

        if (!viewAttributes.isEmpty()) {
            req.setAttribute(DISH_ATTRIBUTE, dishDto);
            passErrorsToView(DISH_UPDATE_VIEW, req, resp, viewAttributes);
            return;
        }

        Dish dish = DishMapper.toDish(dishDto);
        dish.setId(id);
        if (imagePart.getSize() <= 0) {
            var oldDish = dishService.findById(id);
            dish.setImageFileName(oldDish.getImageFileName());
            dishService.saveOrUpdate(dish);
            logger.info("{} is saved or updated without an image", dish);
        } else {
            dishService.saveOrUpdateWithFile(dish, imagePart, uploadDir);
            logger.info("{} is saved or updated with an image", dish);
        }
        resp.sendRedirect( PathConstants.ADMIN_DISH_PATH);
    }
}
