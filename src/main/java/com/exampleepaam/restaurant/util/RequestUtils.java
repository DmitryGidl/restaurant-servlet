package com.exampleepaam.restaurant.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

/*
 * Helper class to manipulate with HttpServletRequest
 */
public class RequestUtils {
    private RequestUtils() {
    }

    /**
     * Retrieves an object from a session
     *
     * @param request       Request with an object
     * @param attributeName Name of an attribute
     * @param clazz         Class a retrieved object should be cast to
     * @return Object in a session or null if there is no object or the object is not an instance of the given class
     */
    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName, Class<T> clazz) {
        HttpSession session = request.getSession(false);
        if (session != null && attributeName != null) {
            Object attribute = session.getAttribute(attributeName);
            if (clazz.isInstance(attribute)) {
                return clazz.cast(attribute);
            }
        }
        return null;
    }


}

