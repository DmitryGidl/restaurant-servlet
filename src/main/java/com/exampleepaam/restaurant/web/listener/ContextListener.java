package com.exampleepaam.restaurant.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

/*
 * Listener that sets relative upload dir to context
 */
@WebListener
public class ContextListener implements ServletContextListener {
    public static final String IMAGES_DIR = "dish-images";
    public static final String UPLOAD_DIR_ATTRIBUTE = "uploadDir";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        String uploadDir = context.getRealPath("") + File.separator + IMAGES_DIR + File.separator;
        context.setAttribute(UPLOAD_DIR_ATTRIBUTE, uploadDir);
    }
}
