import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Powerup {

    private static Color COL = Color.rgb(0, 0, 220);
    private static Color BORDER_COL = Color.BLACK;

    private int r, x, y, speed;

    public Powerup() {
        r = 15;
        x = (int) (Math.random() * (512 - 2*r));
        y = -2*r;
        speed = 4;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void show(GraphicsContext gc) {
        gc.setFill(BORDER_COL);
        gc.fillOval(x, y, 2*r, 2*r);
        gc.setFill(COL);
        gc.fillOval(x+1, y+1, 2*(r-1), 2*(r-1));
    }

    public void update() {
        y += speed;
    }

    /* https://yal.cc/rectangle-circle-intersection-test/
     * dx = CircleX - Max(RectX, Min(CircleX, RectX + RectWidth));
     * dy = CircleY - Max(RectY, Min(CircleY, RectY + RectHeight));
     * return (dx * dx + dy * dy) < (CircleRadius * CircleRadius);
     */
    public boolean collidesWith(int px, int py, int ps) {
        int dx = (x + r) - Math.max(px, Math.min(x + r, px + ps));
        int dy = (y + r) - Math.max(py, Math.min(y + r, py + ps));
        return (dx * dx + dy * dy) < (r * r);
    }

}
