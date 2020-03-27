import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * This is a class
 * Created 2020-03-25
 *
 * @author Magnus Silverdal
 */
public class Graphics extends Canvas implements Runnable {
    private String title = "Graphics";
    private int width;
    private int height;

    private JFrame frame;
    private BufferedImage image;
    private int[] pixels;

    private Thread thread;
    private boolean running = false;
    private int fps = 60;
    private int ups = 60;

    private int xSquare1 = 0;
    private int ySquare1 = 0;
    private int vxSquare1 = 0;
    private int vySquare1 = 0;

    private double t=0;
    private Sprite s;
    private Sprite2 s2;
    private Sprite square1;
    public Graphics(int w, int h) {
        this.width = w;
        this.height = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        frame = new JFrame();
        frame.setTitle(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        s = new Sprite(32,32, 0xFFFF00);
        square1 = new Sprite(48,48,0xFF00FF);
    }

    private void draw() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        java.awt.Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private void update() {
        t += Math.PI/180;

        int x = (int) (width / 2 + (width / 2 - s.getWidth()) * Math.sin(t));
        int y = (int) (height / 2 + (height / 2 - s.getHeight()) * Math.cos(t));

        for (int i = 0; i < s.getHeight(); i++) {
            for (int j = 0; j < s.getWidth(); j++) {
                pixels[(y + i) * width + x + j] = s.getPixels()[i * s.getWidth() + j];
            }
        }

        if (xSquare1 + vxSquare1 < 0 || xSquare1 + vxSquare1 > width - square1.getWidth())
            vxSquare1 = 0;
        if (ySquare1 + vySquare1 < 0 || ySquare1 + vySquare1 > height - square1.getHeight())
            vySquare1 = 0;

        xSquare1 += vxSquare1;
        ySquare1 += vySquare1;

        for (int i = 0 ; i < square1.getHeight() ; i++) {
            for (int j = 0 ; j < square1.getWidth() ; j++) {
                pixels[(ySquare1+i)*width + xSquare1+j] = square1.getPixels()[i*square1.getWidth()+j];
            }
        }
    }
    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        double frameUpdateinteval = 1000000000.0 / fps;
        double stateUpdateinteval = 1000000000.0 / ups;
        double deltaFrame = 0;
        double deltaUpdate = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            deltaFrame += (now - lastTime) / frameUpdateinteval;
            deltaUpdate += (now - lastTime) / stateUpdateinteval;
            lastTime = now;

            while (deltaUpdate >= 1) {
                update();
                deltaUpdate--;
            }

            while (deltaFrame >= 1) {
                draw();
                deltaFrame--;
            }
        }
        stop();
    }

    private class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a') {
                vxSquare1 = -5;
            } else if (keyEvent.getKeyChar()=='d') {
                vxSquare1 = 5;
            } else if (keyEvent.getKeyChar()=='w') {
                vySquare1 = -5;
            } else if (keyEvent.getKeyChar()=='s') {
                vySquare1 = 5;
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a' || keyEvent.getKeyChar()=='d') {
                vxSquare1 = 0;
            } else if (keyEvent.getKeyChar()=='w' || keyEvent.getKeyChar()=='s') {
                vySquare1 = 0;
            }
        }
    }
}