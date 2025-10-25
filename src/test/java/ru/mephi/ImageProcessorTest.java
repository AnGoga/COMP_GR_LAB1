package ru.mephi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса ImageProcessor
 */
public class ImageProcessorTest {

    @TempDir
    Path tempDir;

    /**
     * Тестирование создания и сохранения круглого градиентного изображения
     */
    @Test
    public void testCreateCircleGradient() throws IOException {
        // Создание круглого градиентного изображения
        int width = 100;
        int height = 100;
        BufferedImage image = ImageProcessor.createCircleGradient(width, height);

        // Проверка, что изображение создано правильно
        assertNotNull(image);
        assertEquals(width, image.getWidth());
        assertEquals(height, image.getHeight());

        // Проверка градиента - центр должен быть светлее краев
        int centerColor = image.getRGB(width / 2, height / 2);
        int edgeColor = image.getRGB(0, 0);

        int centerBrightness = (centerColor >> 16) & 0xFF;
        int edgeBrightness = (edgeColor >> 16) & 0xFF;

        assertTrue(centerBrightness > edgeBrightness,
                   "Центр должен быть ярче края");

        // Сохранение в файл
        File outputFile = tempDir.resolve("circle.png").toFile();
        ImageProcessor.writeImage(image, outputFile.getAbsolutePath());

        // Проверка, что файл существует и не пустой
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    /**
     * Тестирование инверсии изображения
     */
    @Test
    public void testInvertImage() throws IOException {
        // Создаем тестовое изображение - просто белый квадрат
        int width = 50;
        int height = 50;
        BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int whiteRGB = (255 << 16) | (255 << 8) | 255; // белый цвет

        // Заполняем изображение белым цветом
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                original.setRGB(x, y, whiteRGB);
            }
        }

        // Инвертируем изображение
        BufferedImage inverted = ImageProcessor.invertImage(original);

        // Проверяем, что инвертированное изображение черное
        int blackRGB = 0; // черный цвет
        assertEquals(blackRGB, inverted.getRGB(0, 0));
    }

    /**
     * Тестирование зеркального отражения
     */
    @Test
    public void testMirrorImage() {
        // Создаем простое тестовое изображение
        int width = 2;
        int height = 2;
        BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Задаем разные цвета для каждого пикселя
        original.setRGB(0, 0, 0xFF0000); // красный
        original.setRGB(1, 0, 0x00FF00); // зеленый
        original.setRGB(0, 1, 0x0000FF); // синий
        original.setRGB(1, 1, 0xFFFF00); // желтый

        // Горизонтальное зеркалирование
        BufferedImage horizontalMirror = ImageProcessor.mirrorImage(original, 0);

        // Проверяем, что пиксели поменялись местами по горизонтали
        assertEquals(0x00FF00, horizontalMirror.getRGB(0, 0) & 0xFFFFFF); // зеленый
        assertEquals(0xFF0000, horizontalMirror.getRGB(1, 0) & 0xFFFFFF); // красный
        assertEquals(0xFFFF00, horizontalMirror.getRGB(0, 1) & 0xFFFFFF); // желтый
        assertEquals(0x0000FF, horizontalMirror.getRGB(1, 1) & 0xFFFFFF); // синий

        // Вертикальное зеркалирование
        BufferedImage verticalMirror = ImageProcessor.mirrorImage(original, 1);

        // Проверяем, что пиксели поменялись местами по вертикали
        assertEquals(0x0000FF, verticalMirror.getRGB(0, 0) & 0xFFFFFF); // синий
        assertEquals(0xFFFF00, verticalMirror.getRGB(1, 0) & 0xFFFFFF); // желтый
        assertEquals(0xFF0000, verticalMirror.getRGB(0, 1) & 0xFFFFFF); // красный
        assertEquals(0x00FF00, verticalMirror.getRGB(1, 1) & 0xFFFFFF); // зеленый
    }

    /**
     * Тестирование транспонирования изображения
     */
    @Test
    public void testTransposeImage() {
        // Создаем простое тестовое изображение
        int width = 2;
        int height = 2;
        BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Задаем разные цвета для каждого пикселя
        original.setRGB(0, 0, 0xFF0000); // красный
        original.setRGB(1, 0, 0x00FF00); // зеленый
        original.setRGB(0, 1, 0x0000FF); // синий
        original.setRGB(1, 1, 0xFFFF00); // желтый

        // Транспонирование
        BufferedImage transposed = ImageProcessor.transposeImage(original);

        // Проверяем, что размеры поменялись местами
        assertEquals(height, transposed.getWidth());
        assertEquals(width, transposed.getHeight());

        // Проверяем, что пиксели транспонированы правильно
        assertEquals(0xFF0000, transposed.getRGB(0, 0) & 0xFFFFFF); // красный
        assertEquals(0x0000FF, transposed.getRGB(0, 1) & 0xFFFFFF); // синий
        assertEquals(0x00FF00, transposed.getRGB(1, 0) & 0xFFFFFF); // зеленый
        assertEquals(0xFFFF00, transposed.getRGB(1, 1) & 0xFFFFFF); // желтый
    }

    /**
     * Тестирование смешивания изображений
     */
    @Test
    public void testBlendImages() {
        // Создаем простые тестовые изображения
        int width = 1;
        int height = 1;

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage alpha = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Устанавливаем цвета: img1 - белый, img2 - черный, alpha - серый (50%)
        img1.setRGB(0, 0, 0xFFFFFF);
        img2.setRGB(0, 0, 0x000000);
        alpha.setRGB(0, 0, 0x7F7F7F); // примерно 50% серый

        // Смешивание в RGB
        BufferedImage blendedRGB = ImageProcessor.blendImages(img1, img2, alpha, false);

        // Проверяем, что результат примерно серый (с погрешностью)
        int rgbResult = blendedRGB.getRGB(0, 0) & 0xFFFFFF;
        int expectedGray = 0x7F7F7F; // серый (50%)

        assertTrue(Math.abs(((rgbResult >> 16) & 0xFF) - 0x7F) <= 1);
        assertTrue(Math.abs(((rgbResult >> 8) & 0xFF) - 0x7F) <= 1);
        assertTrue(Math.abs((rgbResult & 0xFF) - 0x7F) <= 1);

        // Смешивание в HSV (в данном случае результат должен быть таким же)
        BufferedImage blendedHSV = ImageProcessor.blendImages(img1, img2, alpha, true);

        // Проверяем, что результат примерно серый (с погрешностью)
        int hsvResult = blendedHSV.getRGB(0, 0) & 0xFFFFFF;

        assertTrue(Math.abs(((hsvResult >> 16) & 0xFF) - 0x7F) <= 1);
        assertTrue(Math.abs(((hsvResult >> 8) & 0xFF) - 0x7F) <= 1);
        assertTrue(Math.abs((hsvResult & 0xFF) - 0x7F) <= 1);
    }
}