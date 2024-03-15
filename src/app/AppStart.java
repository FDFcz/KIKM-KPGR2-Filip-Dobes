package app;

import control.Controller3D;
import view.Window;

import javax.swing.*;
import java.util.TimerTask;

public class AppStart {
    static Controller3D controller3D;
    static float deltaTime =0f;
    static float oldTime = 0f;
    public static void main(String[] args) throws InterruptedException {
        oldTime = System.nanoTime();
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            controller3D = new Controller3D(window.getPanel());
            window.setVisible(true);
        });
        /*
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                deltaTime = System.nanoTime() - oldTime;
                controller3D.update(deltaTime/2000000000);
                oldTime = System.nanoTime();
            }
        }, 300, 200);
        // https://www.google.com/search?q=SwingUtilities.invokeLater
        // https://www.javamex.com/tutorials/threads/invokelater.shtml
         */
        Thread.sleep(300);
        while (true)
        {
            deltaTime = System.nanoTime() - oldTime;
            controller3D.update(deltaTime/1000000000);
            oldTime = System.nanoTime();
            Thread.sleep(20);
        }
    }
}
