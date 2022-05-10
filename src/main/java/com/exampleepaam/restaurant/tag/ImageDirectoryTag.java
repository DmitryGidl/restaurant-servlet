package com.exampleepaam.restaurant.tag;

import com.exampleepaam.restaurant.web.listener.ContextListener;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Tag class that is used in a tld file
 * Returns a relative dir where dish image is stored
 */
public class ImageDirectoryTag extends TagSupport {
    private int dishId;
    private String imageFileName;

    @Override
    public int doStartTag() {
        String dir = String.format("/%s/%s/%s", ContextListener.IMAGES_DIR, dishId, imageFileName);
        JspWriter out = pageContext.getOut();

        try {
            out.print(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }


}
