package com.exampleepaam.restaurant.servlet.dish.admin;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.dto.DishResponseDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.DISH_MANAGEMENT_VIEW;
import static com.exampleepaam.restaurant.mapper.DishMapper.toDishResponseDtoList;

/**
 * Admin dish servlet
 * Returns a page, sets paged with List of dishes as attribute
 */
@WebServlet(PathConstants.ADMIN_DISH_PATH)
public class AdminDishServlet extends HttpServlet {

    private static final String DEFAULT_CATEGORY = "all";
    private static final String DEFAULT_SORT_FIELD = "category";
    private static final String DEFAULT_SORT_DIR = "asc";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NO = 1;

    private DishService dishService;

    @Override
    public void init() throws ServletException {
        dishService = ServiceManager.getInstance().getDishService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sortField = req.getParameter(SORT_FIELD_PARAM);
        String sortDir = req.getParameter(SORT_DIR_PARAM);
        String filterCategory = req.getParameter(FILTER_CATEGORY_PARAM);
        String pageSize = req.getParameter(PAGE_SIZE_PARAM);
        String currentPage = req.getParameter(CURRENT_PAGE_PARAM);

        if (sortField == null) sortField = DEFAULT_SORT_FIELD;
        if (sortDir == null) sortDir = DEFAULT_SORT_DIR;
        if (filterCategory == null) filterCategory = DEFAULT_CATEGORY;
        if (pageSize == null) pageSize = String.valueOf(DEFAULT_PAGE_SIZE);
        if (currentPage == null) currentPage = String.valueOf(DEFAULT_PAGE_NO);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Paged<Dish> dishPaged = dishService
                .findPaginatedByCategory(Integer.parseInt(currentPage), Integer.parseInt(pageSize),
                        sortField, sortDir, filterCategory);

        Paged<DishResponseDto> dishResponseDtosPaged = new Paged<>(
                toDishResponseDtoList(dishPaged.getPageContent()), dishPaged.getPaging());


        req.setAttribute(DISH_PAGED_ATTRIBUTE, dishResponseDtosPaged);
        req.setAttribute(SORT_FIELD_PARAM, sortField);
        req.setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverseSortDir);
        req.setAttribute(SORT_DIR_PARAM, sortDir);
        req.setAttribute(FILTER_CATEGORY_PARAM, filterCategory);
        req.setAttribute(PAGE_SIZE_PARAM, pageSize);
        req.setAttribute(CURRENT_PAGE_PARAM, currentPage);

        req.getRequestDispatcher(DISH_MANAGEMENT_VIEW).forward(req, resp);
    }

}
