package com.exampleepaam.restaurant.servlet.order;

import com.exampleepaam.restaurant.constant.PathConstants;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;
import com.exampleepaam.restaurant.mapper.OrderMapper;
import com.exampleepaam.restaurant.model.dto.OrderResponseDto;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.service.OrderService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.util.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Order history servlet
 * Returns page with orders of the currently logged user
 */
@WebServlet(PathConstants.USER_ORDER_HISTORY_PATH)
public class OrderHistoryServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() {
        orderService = ServiceFactory.getInstance().getOrderService();
    }

    private static final String DEFAULT_STATUS = "active";
    private static final String DEFAULT_SORT_FIELD = "creation_date_time";
    private static final String DEFAULT_SORT_DIR = "desc";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NO = 1;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String sortField = req.getParameter(SORT_FIELD_PARAM);
        String sortDir = req.getParameter(SORT_DIR_PARAM);
        String filterStatus = req.getParameter(FILTER_STATUS_PARAM);
        String pageSize = req.getParameter(PAGE_SIZE_PARAM);
        String currentPage = req.getParameter(CURRENT_PAGE_PARAM);

        if (sortField == null) sortField = DEFAULT_SORT_FIELD;
        if (sortDir == null) sortDir = DEFAULT_SORT_DIR;
        if (filterStatus == null) filterStatus = DEFAULT_STATUS;
        if (pageSize == null) pageSize = String.valueOf(DEFAULT_PAGE_SIZE);
        if (currentPage == null) currentPage = String.valueOf(DEFAULT_PAGE_NO);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        User loggedUser = RequestUtils.getSessionAttribute(req, USER_PARAM, User.class);

        if (loggedUser == null) {
            resp.sendRedirect(PathConstants.INDEX_PAGE_PATH);
            return;
        }
        Paged<Order> orders = orderService.findPaginatedByUser(
                Integer.parseInt(currentPage), Integer.parseInt(pageSize),
                sortField, sortDir, filterStatus, loggedUser);

        Paged<OrderResponseDto> orderResponseDtosPaged = new Paged<>(
                OrderMapper.toOrderResponseDtoList(orders.getPageContent()), orders.getPaging());

        req.setAttribute(ORDER_PAGED_ATTRIBUTE, orderResponseDtosPaged);
        req.setAttribute(SORT_FIELD_PARAM, sortField);
        req.setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverseSortDir);
        req.setAttribute(SORT_DIR_PARAM, sortDir);
        req.setAttribute(FILTER_STATUS_PARAM, filterStatus);
        req.setAttribute(PAGE_SIZE_PARAM, pageSize);
        req.setAttribute(CURRENT_PAGE_PARAM, currentPage);

        req.getRequestDispatcher(ORDER_HISTORY_VIEW).forward(req, resp);

    }
}
