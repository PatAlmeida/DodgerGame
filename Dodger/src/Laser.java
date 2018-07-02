import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Laser {

    private static int ammoSpacing = 3;
    private static Color COL = Color.rgb(0, 220, 0);
    private static Color BORDER_COL = Color.BLACK;

    private int x, y, w, h, speed;
    private boolean forAmmoDisplay;

    public Laser(int playerX, int playerY, int playerSize) {
        w = 12;
        h = 30;
        x = playerX + (playerSize / 2) - (w / 2);
        y = playerY - h;
        speed = 6;
        forAmmoDisplay = false;
    }

    // Could pass HEIGHT and WIDTH as paramaters...
    public Laser(int ammoIndex) {
        w = 12;
        h = 30;
        x = 512 - (ammoIndex + 1) * (w + ammoSpacing);
        y = 512 - h - 5;
        speed = 0;
        forAmmoDisplay = true;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void show(GraphicsContext gc) {
        gc.setFill(BORDER_COL);
        gc.fillRect(x, y, w, h);
        gc.setFill(COL);
        gc.fillRect(x+2, y+2, w-4, h-4);
    }

    public void update() {
        y -= speed;
    }

    // https://silentmatt.com/rectangle-intersection/
    public boolean collidesWith(Block block) {
        boolean b1 = x < block.getX() + block.getSize();
        boolean b2 = x + w > block.getX();
        boolean b3 = y < block.getY() + block.getSize();
        boolean b4 = y + h > block.getY();
        return (b1 && b2 && b3 && b4);
    }

}
