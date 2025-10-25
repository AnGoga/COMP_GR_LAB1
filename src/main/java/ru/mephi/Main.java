package ru.mephi;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Task1.doTask("src/main/resources/images1/1.png", "src/main/resources/result/task1/1.png");
        Task1.doTask("src/main/resources/images1/2.png", "src/main/resources/result/task1/2.png");
        Task1.doTask("src/main/resources/images1/3.png", "src/main/resources/result/task1/3.png");
        Task1.doTask("src/main/resources/images1/4.png", "src/main/resources/result/task1/4.png");


        Task2.doWork(
                "src/main/resources/images2/",
                "src/main/resources/masks/",
                "src/main/resources/result/task2/"
        );

        new ImageScreen().displayImage(ImageIO.read(new File("src/main/resources/result/task1/4.png")));
    }
}