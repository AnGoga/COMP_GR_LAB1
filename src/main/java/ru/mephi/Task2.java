package ru.mephi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task2 {
    public static void doWork(String imgDir, String masksDir, String outDir) throws IOException {
        List<File> masks = new ArrayList<>(Arrays.asList(new File(masksDir).listFiles()));

        File outFolder = new File(outDir);
        outFolder.mkdirs();

        for (File folder : new File(imgDir).listFiles(File::isDirectory)) {
            File[] imgs = folder.listFiles();

            BufferedImage img1 = ImageIO.read(imgs[0]);
            BufferedImage img2 = ImageIO.read(imgs[1]);

            for (File mfile : masks) {
                BufferedImage mask = ImageIO.read(mfile);

                String outFileName = imgs[0].getName().replace(".png", "") + "_" +
                        imgs[1].getName().replace(".png", "") + "_" +
                        mfile.getName();

                File out = new File(outDir + "/" + folder.getName());
                out.mkdirs();
                File outFile = new File(out, outFileName);
                ImageIO.write(getBlendedImage(img1, img2, mask), "png", outFile);
            }

        }
    }

    public static BufferedImage getBlendedImage(BufferedImage img1, BufferedImage img2, BufferedImage alphaImg) {
        BufferedImage res = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                Color c1 = new Color(img1.getRGB(x, y));
                Color c2 = new Color(img2.getRGB(x, y));
                Color c3 = new Color(alphaImg.getRGB(x, y));

                double w = c3.getRed() / 255.0;

                int r = (int) (c1.getRed() * w + c2.getRed() * (1.0 - w));
                int g = (int) (c1.getGreen() * w + c2.getGreen() * (1.0 - w));
                int b = (int) (c1.getBlue() * w + c2.getBlue() * (1.0 - w));

                res.setRGB(x, y, new Color(r, g, b, 255).getRGB());
            }
        }
        return res;
    }

}