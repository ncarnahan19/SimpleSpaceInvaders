import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

class NewFrame extends JFrame {

    final int FR_PAN_X = 650;
    final int FR_PAN_Y = 400;
    final int WIN_LOSS_MESSAGE_X = 285;
    final int WIN_LOSS_MESSAGE_Y = 50;
    final int RESTART_MESSAGE_X = 240;
    final int RESTART_MESSAGE_Y = 100;
    private int enemyCount = 0;
    private Ship fly;
    private final int SQUARE_SIZE = 30;
    private int currentX = 300;
    int movmentX = currentX;
    private int currentY = 350;

    // enemy variables
    private int[] enemyX = { 30, 90, 150, 210, 270, 330, 390, 450, 510, 570 };
    private int[] enemyY = { 20, 60, 100, 140 };
    private int bulX = currentX;
    private int bulY = 350;
    private boolean spacePressed = false;
    private Timer bulletMovementTimer;
    private Timer enemyMovementTimer;
    private final int X_RIGHT_LIMIT = 680;
    private boolean enemyHit[] = new boolean[40];
    boolean gameLost = false;
    boolean gameWon = false;
    File laserCanonFile = new File("Laser_Cannon.png");
    BufferedImage laserCanonIcon;
    File alienFile = new File("Alien.png");
    BufferedImage alienIcon;
    JLabel laserCanonLabel; 

    public NewFrame() throws Exception {

        setTitle("Galaga Recoded");
        setSize(FR_PAN_X, FR_PAN_Y);
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fly = new Ship();
        addKeyListener(new KeyControls());
        addKeyListener(new GunControl());

        add(fly);
        setVisible(true);

    }

    /**
     * Panel that holds the ships and game in general
     */

    class Ship extends JPanel {

        public Ship() throws IOException {

            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(FR_PAN_X, FR_PAN_Y));
            setLayout(new GridLayout(10, 10));
            addKeyListener(new KeyControls());
            addMouseListener(new enemyMouseListener());
            bulletMovementTimer = new Timer(10, new GalagaActionListener());
            bulletMovementTimer.start();
            enemyMovementTimer = new Timer(5000, new EnemyActionListener());
            enemyMovementTimer.start();
            laserCanonIcon = ImageIO.read(laserCanonFile);
            alienIcon = ImageIO.read(alienFile);
            for (int enemyHitIndex = 0; enemyHitIndex < enemyHit.length; enemyHitIndex++) {

                enemyHit[enemyHitIndex] = false;

            }

        }

        public void paintComponent(Graphics g) {

            super.paintComponents(g);

            g.setColor(Color.RED);
            //g.fillRect(currentX, currentY, SQUARE_SIZE, SQUARE_SIZE);
            g.drawImage(laserCanonIcon, currentX, currentY, null);

            // paints the enemies

            g.setColor(Color.GREEN);

            for (int iy = 0; iy < enemyY.length; iy++) {

                for (int i = 0; i < enemyX.length; i++) {

                    if (!enemyHit[(iy * 10) + i]) {

                        g.drawImage(alienIcon, enemyX[i], enemyY[iy], null);

                    } else if (gameLost) {

                        g.drawString("You Lose!", WIN_LOSS_MESSAGE_X, WIN_LOSS_MESSAGE_Y);
                        g.drawString("Press enter to play again.", RESTART_MESSAGE_X, RESTART_MESSAGE_Y);
                    } else if (gameWon) {

                        g.drawString("You Win!", WIN_LOSS_MESSAGE_X, WIN_LOSS_MESSAGE_Y);
                        g.drawString("Press enter to play again.", RESTART_MESSAGE_X, RESTART_MESSAGE_Y);
                    }
                }
            }

            // bullet

            if (spacePressed) {

                g.fillRect(bulX, bulY, 5, 5);
            }

            repaint();
        }

    }

    /**
     * Controls for the protaganist
     */

    class KeyControls extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            int deathCount = 0;
            int origX = 50;
            int origY = 20;

            if (key == KeyEvent.VK_RIGHT && currentX != X_RIGHT_LIMIT && !gameLost && !gameWon) {

                currentX += 5;

            } else if (key == KeyEvent.VK_LEFT && currentX != 0 && !gameLost && !gameWon) {

                currentX -= 5;

            } else if (key == KeyEvent.VK_SPACE) {

                bulX = currentX + 25;
                spacePressed = true;

                // Sets the parameters for a game win

                for (int i = 0; i < enemyHit.length; i++) {

                    if (enemyHit[i]) {

                        deathCount++;

                        if (deathCount >= enemyHit.length) {

                            enemyMovementTimer.stop();
                            gameWon = true;
                        }
                    }
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                // reset game won or lost
                gameLost = false;
                gameWon = false;

                // reset enemy hits

                for (int enemyHitIndex = 0; enemyHitIndex < enemyHit.length; enemyHitIndex++) {

                    enemyHit[enemyHitIndex] = false;

                }

                // reset death count

                deathCount = 0;

                // Reset position of enemies.

                for (int i = 0; i < enemyY.length; i++) {

                    for (int j = 0; j < enemyX.length; j++) {

                        enemyX[j] = origX;
                        enemyY[i] = origY;

                        origX = origX + 60;

                    }
                    origX = 50;
                    origY = origY + 40;
                }

                // Restarts the timer for the enemy movement

                enemyMovementTimer.start();

            }

            repaint();

        }

    }

    class GunControl extends KeyAdapter {

        public void keyPressed(KeyEvent e) {


        }        

    }

    /**
     * The Timer Listener for the bullet movement
     */

    class GalagaActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // bullet action on a timer
            boolean insideHitLoop = false;

            if (!spacePressed) {
                bulY = 351;
            }

            if (spacePressed && !gameLost && !gameWon) {

                if (bulY > 0) {

                    bulY -= 5;

                    repaint();

                } else {

                    spacePressed = false;
                }
            }

            // senses the enemy that got hit

            for (int i = 0; i < enemyX.length; i++) {
                for (int iy = 0; iy < enemyY.length; iy++) {

                    if ((enemyY[iy] > bulY && enemyY[iy] - SQUARE_SIZE < bulY)
                            && (enemyX[i] < bulX && enemyX[i] + SQUARE_SIZE > bulX)) {

                        if (!enemyHit[(iy * 10) + i]) {

                            enemyHit[(iy * 10) + i] = true;
                            spacePressed = false;
                            insideHitLoop = true;
                        }
                        insideHitLoop = false;

                    }

                }
            }

            // Sets the parameters for a game loss

            if ((enemyY[0] >= 340 && !enemyHit[0]) || (enemyY[1] >= 340 && !enemyHit[1])
                    || (enemyY[2] >= 340 && !enemyHit[2]) || (enemyY[3] >= 340 && !enemyHit[3])) {

                enemyMovementTimer.stop();
                gameLost = true;

            }

            repaint();

        }
    }

    /**
     * Timer listener for ememy movement.
     */

    class EnemyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (enemyCount == 0) {

                for (int i = 0; i < enemyX.length; i++) {

                    enemyX[i] += 10;
                }
                enemyCount++;
            } else if ((enemyCount % 2) != 0) {

                for (int i = 0; i < enemyX.length; i++) {

                    enemyX[i] -= 10;

                }
                enemyCount++;
            } else if ((enemyCount % 2) == 0) {

                for (int i = 0; i < enemyX.length; i++) {

                    enemyX[i] += 10;

                }
                enemyCount++;
            }

            for (int i = 0; i < enemyY.length; i++) {

                enemyY[i] += 20;
            }

            repaint();

        }

    }

    class enemyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {

            System.out.println(e.getX());
            System.out.println(e.getY());

        }
    }

}

public class GalagaApp {

    public static void main(String[] args) throws Exception {

        new NewFrame();
    }
}