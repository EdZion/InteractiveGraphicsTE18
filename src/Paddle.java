import java.awt.*;
/**
 * This is a class
 * Created 2020-03-25
 * @author Magnus Silverdal
 * Edited by Alexander Holmström.
 */
public class Paddle {
    private int[] pixels;
    private Rectangle boundingBox;
    private int width = 250;
    private int height = 185;

    public Paddle(int x, int y, int col){
        boundingBox = new Rectangle(x, y, width, height);
        pixels = new int[width*height];
        for (int i = 0 ; i < pixels.length ; i++) {
            pixels[i] = col;
        }
    }


    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void draw(int[] Screen, int screenWidth){
        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                Screen[(boundingBox.y+i)*screenWidth + boundingBox.x+j] = pixels[i*width+j];
            }
        }
    }
}