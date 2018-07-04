import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block {

    private static Color COL = Color.rgb(220, 0, 0);
    private static Color BORDER_COL = Color.BLACK;

    private int x, y, size, speed;

    public Block() {
        size = 40;
        x = (int) (Math.random() * (512 - size));
        y = -size;
        speed = GameWindow.BLOCK_SPEED;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }

    public void show(GraphicsContext gc) {
        gc.setFill(BORDER_COL);
        gc.fillRect(x, y, size, size);
        gc.setFill(COL);
        gc.fillRect(x+2, y+1, size-3, size-2);
    }

    public void update() {
        y += speed;
    }

    public void incSpeed() {
        speed++;
    }

    // https://silentmatt.com/rectangle-intersection/
    public boolean collidesWith(int px, int py, int ps) {
        boolean b1 = x < px + ps;
        boolean b2 = x + size > px;
        boolean b3 = y < py + ps;
        boolean b4 = y + size > py;
        return (b1 && b2 && b3 && b4);
    }

}
