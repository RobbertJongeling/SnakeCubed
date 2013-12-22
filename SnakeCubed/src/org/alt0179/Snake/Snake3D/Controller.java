package org.alt0179.Snake.Snake3D;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import org.alt0179.Snake.model.Key;
import org.alt0179.Snake.model.World;

/**
 *
 * @author Robbert
 */
public class Controller {

    private World world;
    private ArrayList<Key> keys = new ArrayList<Key>();
    private JFrame frame = null;
    private int initialSpeed = 500;
    private int score;
    private Timer t;
    private Game game;
    
    public Controller(Game game, World world) {
        this.game = game;
        this.world = world;

        world.initSnake();
        world.initApple();
    }

    public final void startGameLoop(int delay, int interval) {
        if (t != null) {
            t.cancel();
        }
        t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!world.isDead()) {
                    if (keys.size() > 0) {
                        Key pressed = keys.remove(0);
                        world.keyPressed(pressed);
                    }
                    world.moveSnake();
                    setScore();
                } else {
                    die();
                }
            }
        }, delay, interval);
    }

    public void appleEaten() {
        int newspeed = getSpeed();
        startGameLoop(newspeed, newspeed);
    }
    
    public int getSpeed() {
        int newspeed = initialSpeed - world.getNrApples() * 25;
        if (newspeed < 50) {
            newspeed = 50;
        }
        return newspeed;
    }

    private void setScore() {
        score = world.getNrApples();
    }

    /**
     * 
     * @param keyCode keyCode of pressed key 
     */
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            keys.add(Key.DOWN);
        }
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            keys.add(Key.UP);
        }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            keys.add(Key.LEFT);
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            keys.add(Key.RIGHT);
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            die();
        }
    }

    public void asignFrame(JFrame frame) {
        this.frame = frame;
    }

    public int getScore() {
        return score;
    }

    /**
     * called when snake dies or escape pressed //TODO display score
     */
    public void die() {
        t.cancel();
        if (frame != null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
        Highscores scores = new Highscores(score);
        scores.setVisible(true);
        game.die();
    }
}
