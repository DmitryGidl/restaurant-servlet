package com.exampleepaam.restaurant.util;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * File helper class
 */
public class FileUtil {
    private FileUtil() {
    }

    private static final int MENU_IMAGE_HEIGHT = 400;
    private static final int MENU_IMAGE_WIDTH = 400;

    /**
     *  Saves file to a given folder
     *
     * @param uploadDir relative dir where a file should be saved
     * @param multipartFile file to be saved
     */
    public static void saveFile(String uploadDir, String fileName,
                                Part multipartFile)   {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);

            BufferedImage bufferedImageResized =
                    resizeImage(ImageIO.read(inputStream), MENU_IMAGE_WIDTH, MENU_IMAGE_HEIGHT);
            InputStream resizedInputStream = toInputStream(bufferedImageResized);

            Files.copy(resizedInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            try {
                throw new IOException("Could not save image file: " + fileName, ioe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Resizes images
     *
     * @param originalImage original image to be resized
     * @param targetWidth target width for resizing
     * @param targetHeight target height for resizing
     * @return Resized bufferedImage
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     *  Creates an InputStream from a BufferedImage
     *
     * @param image bufferedImage to be written to InputStream
     * @return InputStream with an image
     */
    public static InputStream toInputStream(BufferedImage image) {
        InputStream is;
        try (
                ByteArrayOutputStream os = new ByteArrayOutputStream();
        ) {
            ImageIO.write(image, "gif", os);
            is = new ByteArrayInputStream(os.toByteArray());
            return is;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Deletes a given folder
     *
     * @param deleteDir relative dir with a folder to be deleted
     */
    public static void deleteFolder(String deleteDir) {
        try {
            FileUtils.deleteDirectory(new File(deleteDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}