package ru.mephi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Task1 {

    public static void doTask(String in, String out) throws IOException {
        BufferedImage img = getImage(ImageIO.read(new File(in)));
        ImageIO.write(img, "png", new File(out));
    }

    public static BufferedImage getImage(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int x0 = img.getWidth() / 2;
        int y0 = img.getHeight() / 2;
        int r = Math.min(x0, y0);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color rgb = new Color(img.getRGB(x, y));

                int gray = (int) (0.2126 * rgb.getRed() + 0.7152 * rgb.getGreen() + 0.0722 * rgb.getBlue());
                double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                int alpha = d <= r ? 255 : 0;
                res.setRGB(x, y, new Color(gray, gray, gray, alpha).getRGB());
            }
        }
        return res;
    }
}