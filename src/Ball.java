import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Ball {
    private static int yDirection;
    private static int xDirection;
    private int[] pixels;
    private Rectangle boundingBox;
    private int height = 10;
    private int width = 10;

    public Ball(int x, int y){
        pixels = new int[width*height];

        for (int i = 0 ; i < pixels.length ; i++)
            pixels[i] = 0xFFFFFFFF;

        boundingBox = new Rectangle(x, y, width, height);
    }

    public static void keyPressed(KeyEvent e){
        if(e.getKeyCode() == e.VK_LEFT){
            setXDirection(-1);
        }
        if(e.getKeyCode() == e.VK_RIGHT){
            setXDirection(1);
        }
        if(e.getKeyCode() == e.VK_UP){
            setYDirection(-1);
        }
        if(e.getKeyCode() == e.VK_DOWN){
            setYDirection(1);
        }
    }

    public static void keyReleased(KeyEvent e){
        if(e.getKeyCode() == e.VK_LEFT){
            setXDirection(0);
        }
        if(e.getKeyCode() == e.VK_RIGHT){
            setXDirection(0);
        }
        if(e.getKeyCode() == e.VK_UP){
            setYDirection(0);
        }
        if(e.getKeyCode() == e.VK_DOWN){
            setYDirection(0);
        }

    }
    public static void setXDirection(int xdir){
        xDirection = xdir;
    }

    public static void setYDirection(int ydir){
        yDirection = ydir;
    }

    public int getXDirection() {
        return xDirection;
    }

    public int getYDirection() {
        return yDirection;
    }

    public void draw(int[] Screen, int screenWidth){
        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                Screen[(boundingBox.y+i)*screenWidth + boundingBox.x+j] = pixels[i*width+j];
            }
        }
    }

    public void collision(Rectangle r){
        if(boundingBox.intersects(r)) {
            if (getXDirection() > 0 && Math.abs(r.x - (boundingBox.x + boundingBox.width)) <= getXDirection()) {
                setXDirection(0);
                setYDirection(0);
            } else if (getXDirection() < 0 && Math.abs(r.x + r.width - boundingBox.x) <= -getXDirection()) {
                setXDirection(-0);
                setYDirection(-0);
            } else if (getYDirection() > 0 && Math.abs(r.y - (boundingBox.y + boundingBox.height)) <= getYDirection()) {
                setYDirection(0);
                setXDirection(0);
            } else if (getYDirection() < 0 && Math.abs(r.y + r.height - boundingBox.y) <= -getYDirection()) {
                setYDirection(-0);
                setXDirection(-0);
            }
        }
    }

    public void move() {
        boundingBox.x += xDirection;
        boundingBox.y += yDirection;
        //Bounce the ball when edge is detected
        if (boundingBox.x <= 0) {
            setXDirection(0);
        }
        if (boundingBox.x >= 390) {
            setXDirection(0);
        }
        if (boundingBox.y <= 0) setYDirection(0);
        if (boundingBox.y >= 290) setYDirection(0);
    }

    public void update(Rectangle r) {
        collision(r);
        move();
        collision(r);
    }
}