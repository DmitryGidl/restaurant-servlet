package com.exampleepaam.restaurant.util;


import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.exampleepaam.restaurant.constant.ErrorAttributeConstants.ERRORS_ATTRIBUTE_GLOBAL;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.ORDER_MAP_PARAM;

/**
 * Controller helper class
 */
public class ControllerUtil {
    private ControllerUtil() {
    }

    /**
     *  Retrieves a dishId-quantity Map and filters it
     *  leaving only pairs where quantity more than 0
     *
     * @param request user's request
     * @return a map dishId - Quantity of dishes ordered
     */
    public static Map<Long, Integer> getOrderMap(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .filter(stringEntry -> stringEntry.getKey().startsWith(ORDER_MAP_PARAM))
                .map(entry -> new Pair<>(Long.parseLong(entry.getKey().replaceAll("\\D+", "")),
                        Integer.parseInt(entry.getValue()[0])))
                .filter(kvp -> kvp.getValue() > 0)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

    }

    /**
     *  Loops over a map with error attributes adding them to a request.
     *  Then - forwards the request to the given page
     *
     * @param request user's request
     * @param response servlet's response
     * @param viewAttributes validation error map
     */
    public static void passErrorsToView(String viewPath, HttpServletRequest request,
                                        HttpServletResponse response,
                                        Map<String, String> viewAttributes) throws ServletException, IOException {
        for (Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    /**
     *  Adds a map with error attributes to a request.
     *  Then - forwards the request to the given page
     *
     * @param request user's request
     * @param response servlet's response
     * @param viewAttributes validation error map
     */
    public static void passErrorMapToView(String viewPath, HttpServletRequest request,
                                          HttpServletResponse response,
                                          Map<String, String> viewAttributes) throws ServletException, IOException {
        request.setAttribute(ERRORS_ATTRIBUTE_GLOBAL, viewAttributes);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }


}
