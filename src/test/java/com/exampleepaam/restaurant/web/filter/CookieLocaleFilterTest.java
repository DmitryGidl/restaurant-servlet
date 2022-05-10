package com.exampleepaam.restaurant.web.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.exampleepaam.restaurant.web.filter.CookieLocaleFilter.LANG_LOCALE_NAME;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class CookieLocaleFilterTest {
    @Spy
    CookieLocaleFilter cookieLocaleFilter;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;



    @Test
     void testDoFilter() throws Exception {
        String languageAttribute = "en";
        when(request.getParameter(LANG_LOCALE_NAME)).thenReturn(languageAttribute);

        cookieLocaleFilter.doFilter(request, response, filterChain);
        Mockito.verify(response, times(1)).addCookie(argThat(
                cookie -> LANG_LOCALE_NAME.equals(cookie.getName()) && languageAttribute.equals(cookie.getValue())));
    }
}