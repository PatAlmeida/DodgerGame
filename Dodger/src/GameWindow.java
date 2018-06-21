import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class GameWindow {

    private static int WIDTH = 512;
    private static int HEIGHT = 512;
    private static Color BGCOLOR = Color.rgb(50, 50, 50);
    private static Color PLAYERCOLOR = Color.rgb(220, 220, 220);
    private static Color BLOCKCOLOR = Color.rgb(220, 0, 0);

    private int frameCount;
    private int playerX, playerY;
    private int playerSize;

    private int blockX, blockY;
    private int blockSize;

    public GameWindow(Stage myStage) {

        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("RIGHT")) {
                if (playerX < WIDTH - playerSize) playerX += 5;
            }
            if (e.getCode().toString().equals("LEFT")) {
                if (playerX > 0) playerX -= 5;
            }
        });

        playerSize = 40;
        playerX = (WIDTH / 2) - (playerSize / 2);
        playerY = HEIGHT - playerSize - 20;

        blockSize = 40;
        blockX = 150;
        blockY = 20;

        frameCount = 0;
        new AnimationTimer() {

            // Game loop - runs at 60fps
            public void handle(long nano) {

                // Draw background
                gc.setFill(BGCOLOR);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                // Draw player
                gc.setFill(PLAYERCOLOR);
                gc.fillRect(playerX, playerY, playerSize, playerSize);

                // Draw block
                gc.setFill(BLOCKCOLOR);
                gc.fillRect(blockX, blockY, blockSize, blockSize);

                checkCollisions();

                updateBlock();

            }

        }.start();

        myStage.setTitle("Dodger");
        myStage.setScene(scene);
        myStage.show();

    }

    private void checkCollisions() {
        int dx = (blockX + blockSize) - playerX;
        int dy = (blockY + blockSize) - playerY;
        if (dx < playerSize && dx > 0 && dy < playerSize && dy > 0) {
            System.out.println("COLLISION");
        }
    }

    private void updateBlock() {
        blockY++;
    }

}
