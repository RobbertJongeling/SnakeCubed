package org.alt0179.Snake.view;

import org.alt0179.Snake.Snake3D.Controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public abstract class AbstractRenderer implements GLEventListener {

    private Controller controller;
    
    public AbstractRenderer(GLAutoDrawable drawable, Controller controller) {
        this.controller = controller;
        GameKeyListener gameKeyListener = new GameKeyListener();
        drawable.addKeyListener(gameKeyListener);
        drawable.addKeyListener(new viewKeyListener());
    }

    @Override
    public abstract void display(GLAutoDrawable drawable);

    @Override
    public abstract void init(final GLAutoDrawable drawable);

    @Override
    public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { };
      
    private class GameKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            controller.keyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
   
    private class viewKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            keyboard(e.getKeyChar());
        }
    }

    public abstract void keyboard(char key);
}
