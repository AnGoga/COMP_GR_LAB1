package ru.mephi;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScreen {
    public void displayImage(BufferedImage img) {
        Runnable show = () -> {
            JComponent imageView = new JComponent() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(img.getWidth(), img.getHeight());
                }
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, null);
                }
            };

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(imageView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            show.run();
        } else {
            SwingUtilities.invokeLater(show);
        }
    }
}
