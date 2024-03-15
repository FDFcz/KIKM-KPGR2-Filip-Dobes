package view;

import raster.ImageBuffer;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel {
    private static final int FPS = 1000 / 20;
    public static final int WIDTH = 800, HEIGHT = 600;

    private ImageBuffer raster;

    public Panel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        raster = new ImageBuffer(WIDTH, HEIGHT);
        //setLoop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public void resize() {
        if (this.getWidth() < 1 || this.getHeight() < 1)
            return;

        ImageBuffer newRaster = new ImageBuffer(this.getWidth(), this.getHeight());
        newRaster.draw(raster);
        raster = newRaster;
    }
    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public ImageBuffer getRaster() {
        return raster;
    }

    public void clear() {
        raster.clear();
    }
}
