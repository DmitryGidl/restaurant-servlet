package com.exampleepaam.restaurant.servlet.order.admin;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.mapper.OrderMapper;
import com.exampleepaam.restaurant.model.dto.OrderResponseDto;
import com.exampleepaam.restaurant.model.entity.Order;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exampleepaam.restaurant.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.exampleepaam.restaurant.constant.PathConstants.ADMIN_ORDER_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;

/**
 * Admin order servlet
 * Returns a page with all orders and handles changing of their status
 */
@WebServlet(ADMIN_ORDER_PATH)
public class AdminOrderServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(AdminOrderServlet.class);

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

    private static final String ACTION_PARAMETER = "action";
    private static final String STATUS_ACTION_NEXT = "next";
    private static final String STATUS_ACTION_DECLINE = "decline";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        String reverSortDir = sortDir.equals("asc") ? "desc" : "asc";
        Paged<Order> orderPaged = orderService.findPaginated(
                Integer.parseInt(currentPage), Integer.parseInt(pageSize), sortField, sortDir, filterStatus);
        Paged<OrderResponseDto> orderResponseDtoPaged = new Paged<>(
                OrderMapper.toOrderResponseDtoList(orderPaged.getPageContent()), orderPaged.getPaging());
        
        req.setAttribute(ORDER_PAGED_ATTRIBUTE, orderResponseDtoPaged);
        req.setAttribute(SORT_FIELD_PARAM, sortField);
        req.setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverSortDir);
        req.setAttribute(SORT_DIR_PARAM, sortDir);
        req.setAttribute(FILTER_STATUS_PARAM, filterStatus);
        req.setAttribute(PAGE_SIZE_PARAM, pageSize);
        req.setAttribute(CURRENT_PAGE_PARAM, currentPage);

        req.getRequestDispatcher(ORDERS_VIEW_PATH).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String actionParam = req.getParameter(ACTION_PARAMETER);
        String idParam = req.getParameter(ID_PARAM);

        long id = Long.parseLong(idParam);

        Order order = orderService.findById(id);

        if(order == null) {
            resp.sendRedirect(ADMIN_ORDER_PATH);
            logger.warn("Admin tried to change status of non-existent order with id {}", id);
            return;
        }

        // Changes status of an order if action equals 'next' or 'declined'
        if (actionParam.equals(STATUS_ACTION_NEXT)) {
            orderService.setStatusNext(order);
            logger.info("Status of {} was set to next", order);
        } else if (actionParam.equals(STATUS_ACTION_DECLINE)) {
            orderService.setStatusDeclinedAndRefund(order);
            logger.info("Status of {} was set to declined", order);
        }

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

// Redirecting to same page with the same pagination, sorting and filtering
        URIBuilder builder;
        try {
            builder = new URIBuilder(ADMIN_ORDER_PATH)
                    .addParameter(SORT_FIELD_PARAM, sortField)
                    .addParameter(SORT_DIR_PARAM, sortDir)
                    .addParameter(FILTER_STATUS_PARAM, filterStatus)
                    .addParameter(PAGE_SIZE_PARAM, pageSize)
                    .addParameter(CURRENT_PAGE_PARAM, currentPage);
        } catch (URISyntaxException e) {
            resp.sendRedirect(PathConstants.INDEX_PAGE_PATH);
            return;
        }

        resp.sendRedirect(builder.toString());
    }

}
