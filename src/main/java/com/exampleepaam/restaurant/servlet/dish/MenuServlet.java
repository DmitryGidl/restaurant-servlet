package com.exampleepaam.restaurant.servlet.dish;


import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.SharedServices;
import com.exampleepaam.restaurant.model.dto.DishResponseDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;
import static com.exampleepaam.restaurant.mapper.DishMapper.toDishResponseDtoList;

/**
 * Menu servlet
 * Returns a page with a list of dishes as an attribute
 */
@WebServlet(PathConstants.USER_MENU_PATH)
public class MenuServlet extends HttpServlet {

    private DishService dishService;

    private static final String DEFAULT_CATEGRY = "all";
    private static final String DEFAULT_SORT_FIELD = "category";
    private static final String DEFAULT_SORT_DIR = "asc";

    @Override
    public void init() throws ServletException {
        dishService = SharedServices.getInstance().getDishService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String sortField = req.getParameter(SORT_FIELD_PARAM);
        String sortDir = req.getParameter(SORT_DIR_PARAM);
        String filterCategory = req.getParameter(FILTER_CATEGORY_PARAM);

        if (sortField == null) sortField = DEFAULT_SORT_FIELD;
        if (sortDir == null) sortDir = DEFAULT_SORT_DIR;
        if (filterCategory == null) filterCategory = DEFAULT_CATEGRY;

        String reverSortDir = sortDir.equals("asc") ? "desc" : "asc";

        req.setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverSortDir);


        List<Dish> dishes;

        if (Objects.equals(filterCategory, DEFAULT_CATEGRY)) {
            dishes = dishService.findAllDishesSorted(sortField, sortDir);
        } else {
            dishes = dishService.findAllDishesByCategorySorted(sortField, sortDir, filterCategory);
        }


        List<DishResponseDto> dishResponseDtos = toDishResponseDtoList(dishes);

        req.setAttribute(DISHES_ATTRIBUTE, dishResponseDtos);
        req.setAttribute(SORT_FIELD_PARAM, sortField);
        req.setAttribute(SORT_DIR_PARAM, sortDir);
        req.setAttribute(FILTER_CATEGORY_PARAM, filterCategory);

        try {
            req.getRequestDispatcher(MENU_VIEW).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
