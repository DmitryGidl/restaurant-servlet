package com.exampleepaam.restaurant.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
 * Filter that adds Cookie with locale
 */
@WebFilter("/*")
public class CookieLocaleFilter implements Filter {
    public static final String LANG_LOCALE_NAME = "locale";
    protected static final List<String> SUPPORTED_LOCALES = Arrays.asList("ua", "en");
    public static final String DEFAULT_LOCALE = "en";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String locale = req.getParameter(LANG_LOCALE_NAME);
        Cookie cookie;

        if (locale != null && SUPPORTED_LOCALES.contains(locale)) {
             cookie = new Cookie(LANG_LOCALE_NAME, locale);
        } else {
            cookie = new Cookie(DEFAULT_LOCALE, locale);
        }

        res.addCookie(cookie);
        chain.doFilter(request, response);
    }
}
