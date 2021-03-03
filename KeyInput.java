import java.awt.event.*;

public class KeyInput extends KeyAdapter {

    GalagaApp galagaApp;

    public KeyInput(GalagaApp galagaApp) {

        this.galagaApp = galagaApp;
    }

    public void keyPressed(KeyEvent e) {

        galagaApp.keyPressed(e);

    }

    public void keyReleased(KeyEvent e) {

        galagaApp.keyReleased(e);

    }

}