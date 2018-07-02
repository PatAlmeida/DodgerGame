import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameWindow {

    private static int WIDTH = 512;
    private static int HEIGHT = 512;
    private static int SCORE_X = 6;
    private static int SCORE_Y = 20;
    private static int PAUSE_X = 224;
    private static int PAUSE_Y = 265;
    private static int SPAWN_RATE = 25;
    private static int DEATH_ANIM_TIME = 215;
    private static int BLINK_RATE = 20;
    private static Color BG_COLOR = Color.rgb(50, 50, 50);
    private static Color PLAYER_COLOR = Color.rgb(220, 220, 220);
    private static Color PLAYER_BORDER_COLOR = Color.BLACK;
    private static Color TEXT_COLOR = Color.WHITE;
    private static Font SCORE_FONT = Font.font("Times New Roman", FontWeight.BOLD, 20);

    private int frameCount;
    private int score, hiScore;
    private int totalBlocks, deaths, destroyed;
    private int playerSize, playerSpeed;
    private int playerX, playerY;
    private int blinkAnimCount;
    private int ammo;
    private boolean paused;
    private boolean blinking;
    private ArrayList<Block> blocks;
    private ArrayList<Laser> lasers, ammoLasers;
    private ArrayList<String> keys;

    public GameWindow(Stage myStage) {

        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // ** Do this only for movement? -> Not lasers? **
        keys = new ArrayList<String>();
        scene.setOnKeyPressed(e -> {
            String keyStr = e.getCode().toString();
            if (!paused) {
                if (keyStr.equals("RIGHT") || keyStr.equals("LEFT")) {
                    if (!keys.contains(keyStr)) {
                        keys.add(keyStr);
                    }
                } else if (keyStr.equals("SPACE")) {
                    if (ammo != 0) {
                        lasers.add(new Laser(playerX, playerY, playerSize));
                        ammoLasers.remove(ammoLasers.size() - 1);
                        ammo--;
                    }
                }
            }
            if (keyStr.equals("ESCAPE")) {
                paused = !paused;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (keys.contains(e.getCode().toString())) {
                keys.remove(e.getCode().toString());
            }
        });

        frameCount = 0;
        score = 0;
        hiScore = 0;
        totalBlocks = 0;
        deaths = 0;
        destroyed = 0;

        blinkAnimCount = 0;
        blinking = false;

        playerSize = 40;
        playerSpeed = 4;
        playerX = (WIDTH / 2) - (playerSize / 2);
        playerY = HEIGHT - playerSize - 20;

        ammo = 20;
        ammoLasers = new ArrayList<Laser>();
        for (int i=0; i < ammo; i++) {
            ammoLasers.add(new Laser(i));
        }

        paused = false;

        blocks = new ArrayList<Block>();
        lasers = new ArrayList<Laser>();

        // Game loop - runs at (about) 60fps
        new AnimationTimer() {

            public void handle(long nano) {

                // Draw background
                gc.setFill(BG_COLOR);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                // Draw blocks
                for (Block block : blocks) {
                    block.show(gc);
                }

                // Draw player
                if ((blinkAnimCount / BLINK_RATE) % 2 != 0 || !blinking) {
                    gc.setFill(PLAYER_BORDER_COLOR);
                    gc.fillRect(playerX, playerY, playerSize, playerSize);
                    gc.setFill(PLAYER_COLOR);
                    gc.fillRect(playerX+2, playerY+2, playerSize-4, playerSize-4);
                }

                // Draw lasers
                for (Laser laser : lasers) {
                    laser.show(gc);
                }

                // Draw ammo
                for (Laser laser : ammoLasers) {
                    laser.show(gc);
                }

                // Draw score and other game info
                gc.setFill(TEXT_COLOR);
                gc.setFont(SCORE_FONT);
                gc.fillText("Score: " + score + " (High: " + hiScore + ")", SCORE_X, SCORE_Y);
                gc.fillText("Passed: " + totalBlocks, SCORE_X, SCORE_Y + 25);
                gc.fillText("Deaths: " + deaths, SCORE_X, SCORE_Y + 50);
                gc.fillText("Destroyed: " + destroyed, SCORE_X, SCORE_Y + 75);

                // Update if not paused
                if (!paused) {
                    frameCount++;
                    if (keys.size() > 0) {
                        String moveStr = keys.get(keys.size() - 1);
                        if (moveStr.equals("RIGHT")) {
                            if (playerX < WIDTH - playerSize) playerX += playerSpeed;
                        } else if (moveStr.equals("LEFT")) {
                            if (playerX > 0) playerX -= playerSpeed;
                        }
                    }
                    if (frameCount % SPAWN_RATE == 0) {
                        blocks.add(new Block());
                    }
                    if (blinking) {
                        blinkAnimCount++;
                        if (blinkAnimCount > DEATH_ANIM_TIME) {
                            blinking = false;
                            blinkAnimCount = 0;
                        }
                    }
                    checkCollisions();
                    updateBlocks();
                    updateLasers();
                } else {
                    // Draw 'Paused'
                    gc.setFill(TEXT_COLOR);
                    gc.setFont(SCORE_FONT);
                    gc.fillText("PAUSE", PAUSE_X, PAUSE_Y);
                }

            }

        }.start();

        myStage.setTitle("Dodger");
        myStage.setScene(scene);
        myStage.show();

    }

    private void checkCollisions() {

        for (int i = blocks.size() - 1; i >= 0; i--) {
            if (blocks.get(i).collidesWith(playerX, playerY, playerSize)) {
                if (!blinking) {
                    score = 0;
                    deaths++;
                    totalBlocks--;
                    blinking = true;
                    blocks.remove(i);
                }
            }
        }

        for (int i = blocks.size() - 1; i >= 0; i--) {
            for (int j = lasers.size() - 1; j >= 0; j--) {
                if (lasers.get(j).collidesWith(blocks.get(i))) {
                    blocks.remove(i);
                    lasers.remove(j);
                    score++;
                    totalBlocks++;
                    destroyed++;
                }
            }
        }

    }

    private void updateBlocks() {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            blocks.get(i).update();
            if (blocks.get(i).getY() > HEIGHT) {
                blocks.remove(i);
                if (!blinking) score++;
                if (score > hiScore) hiScore = score;
                totalBlocks++;
            }
        }
    }

    private void updateLasers() {
        for (int i = lasers.size() - 1; i >= 0; i--) {
            lasers.get(i).update();
            // -30 is laser size, don't remove until fully off screen
            if (lasers.get(i).getY() < -30) {
                lasers.remove(i);
            }
        }
    }

}
