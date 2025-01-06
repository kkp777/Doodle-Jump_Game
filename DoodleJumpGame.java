import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class DoodleJumpGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int score;
    private int playerX, playerY, playerWidth, playerHeight;
    private int yVelocity;
    private boolean isJumping;

    private ArrayList<Platform> platforms;

    public DoodleJumpGame() {
        setFocusable(true);
        addKeyListener(this);

        // Game settings
        timer = new Timer(20, this);
        timer.start();

        score = 0;
        playerX = 200;
        playerY = 400;
        playerWidth = 30;
        playerHeight = 30;
        yVelocity = 0;
        isJumping = false;

        // Generate platforms
        platforms = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(300);
            int y = 500 - i * 100;
            platforms.add(new Platform(x, y, 60, 10));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Player
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        // Platforms
        g.setColor(Color.GREEN);
        for (Platform platform : platforms) {
            g.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        // Score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        yVelocity += 1; // Gravity
        playerY += yVelocity;

        // Check for jumping on platforms
        for (Platform platform : platforms) {
            if (playerY + playerHeight >= platform.y && playerY + playerHeight <= platform.y + 10
                    && playerX + playerWidth > platform.x && playerX < platform.x + platform.width) {
                yVelocity = -10; // Jumping effect
                score++;
            }
        }

        // Player reaches the top, move platforms down
        if (playerY < 200) {
            playerY = 200;
            for (Platform platform : platforms) {
                platform.y += 10;
                if (platform.y > 600) {
                    platform.y = 0;
                    platform.x = new Random().nextInt(300);
                }
            }
        }

        // Game over condition
        if (playerY > 600) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Your score is: " + score);
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            playerX -= 10;
        } else if (key == KeyEvent.VK_RIGHT) {
            playerX += 10;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Doodle Jump Game");
        DoodleJumpGame game = new DoodleJumpGame();
        frame.add(game);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Platform {
    int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

