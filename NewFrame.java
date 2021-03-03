import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewFrame extends JFrame {

    private JFrame f;
    private JPanel p;
    private JButton b1;
    private JLabel lab;
    private Ship fly;
    private int currentX = 300;
    private int currentY = 350;

    public NewFrame() {

        setTitle("Galaga Recoded");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b1 = new JButton("test");
        lab = new JLabel("This is a test label");

        fly = new Ship();

        add(fly);
        setVisible(true);

    }

    class Ship extends JPanel {

        public Ship() {

            setBackground(Color.WHITE);

            setPreferredSize(new Dimension(600, 400));
            addKeyListener(new Controls());
        }

        public void paintComponent(Graphics g) {

            super.paintComponents(g);

            g.setColor(Color.BLACK);
            g.fillRect(currentX, currentY, 10, 10);
        }

    }

    class Controls extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {

                currentX = 5;  
                

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                currentX = currentX - 5; 

            }

            repaint();

        }
    }
}