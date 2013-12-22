package org.alt0179.Snake.Snake3D;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Collections;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.swing.JFrame;
import org.alt0179.Snake.model.World;
import org.alt0179.Snake.view.Renderer;

/**
 *
 * @author Robbert
 */
public class Game {

    private Controller controller;
    private World world;
    private JFrame frame;
    private int dimension;
    public static Animator animator = new FPSAnimator(120);
    private boolean fullscreen;

    public Game(boolean fullscreen, int dimension) {
        this.fullscreen = fullscreen;
        this.dimension = dimension;
    }

    public void start() {
        if (!(animator != null && animator.isAnimating())) {
            world = new World(this, dimension);
            controller = new Controller(this, world);

            Thread gameLoop = new Thread() {

                @Override
                public void run() {
                    controller.startGameLoop(0, controller.getSpeed());
                }
            };

            initGraphics();

            gameLoop.start();
        }
    }

    private void initGraphics() {

        GLJPanel panel = new GLJPanel();

        // add a renderer to the panel
        GLEventListener renderer = new Renderer(panel, controller, world);
        panel.addGLEventListener(renderer);


        panel.setFocusable(true);
        panel.requestFocusInWindow();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (fullscreen) {
            panel.setPreferredSize(screen);
        } else {
            panel.setPreferredSize(new Dimension(screen.width / 2, screen.height / 2));
            Point centerScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            Point n = new Point((int) (centerScreen.x - panel.getWidth() / 2), (int) (centerScreen.y - panel.getHeight() / 2));
            panel.setLocation(n);
        }

        // construct a window frame around the drawing panel
        // and makes sure that the program quits if window is closed
        frame = new JFrame("Snake³");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        frame.setUndecorated(fullscreen);
        frame.pack();
        frame.setVisible(true);

        setBlankCursor();

        controller.asignFrame(frame);

        // add panel to animator and start calling its display
        // at the specified number of frames per second (see above).
        animator.add(panel);
        animator.setIgnoreExceptions(false);
        animator.setPrintExceptions(true);
        animator.start();
    }

    private void setBlankCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        frame.getContentPane().setCursor(blankCursor);
    }

    public void appleEaten() {
        controller.appleEaten();
    }

    public void die() {
        animator.stop();
    }
}
