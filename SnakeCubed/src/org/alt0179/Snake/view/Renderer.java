package org.alt0179.Snake.view;

import com.sun.opengl.util.GLUT;
import org.alt0179.Snake.Snake3D.Controller;
import java.awt.Color;
import java.util.ArrayList;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import org.alt0179.Snake.model.CamType;
import org.alt0179.Snake.model.ColorScheme;
import org.alt0179.Snake.model.Position;
import org.alt0179.Snake.model.Snake;
import org.alt0179.Snake.model.World;

/**
 * @author Robbert
 */
public class Renderer extends AbstractRenderer {

    private World world;
    private Controller controller;
    
    private final GLUT glut = new GLUT();
    private final GLU glu = new GLU();
    
    private CamType camType = CamType.DYNAMIC;
    private ColorScheme colors = ColorScheme.NORMAL;

    private boolean rasterDensity = true;
    
    public Renderer(GLAutoDrawable drawable, Controller controller, World world) {
        super(drawable, controller);
        this.world = world;
        this.controller = controller;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0, 0, 0, 0);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_CULL_FACE);
        gl.glShadeModel(GL_SMOOTH);

        gl.glEnable(GL_COLOR_MATERIAL);

        gl.glEnable(GL.GL_NORMALIZE);

        initLights(gl);

    }

    private void initLights(GL gl) {
        gl.glLightfv(GL_LIGHT2, GL_AMBIENT, new float[]{0.5f, 0.5f, 0.5f, 1.0f}, 0);
        gl.glLightfv(GL_LIGHT2, GL_DIFFUSE, new float[]{1f, 1f, 1f, 1.0f}, 0);

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT2);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        init(drawable);
        GL gl = drawable.getGL();

        //clear image- and depth buffer to get a clean canvas
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        updateCamera();

        drawLights(gl);
        drawSideCubes(gl);
        drawObjects(gl);

        gl.glPopMatrix();
    }

    private void updateCamera() {
        Snake snake = world.getSnake();
        Position cur = snake.getCurrentPosition();
        Position up = snake.getCurrentOrientation();
        Position dir = snake.getCurrentDirection();
        Position lookAt = cur.add(dir);

        if (this.camType == CamType.DYNAMIC) {
            //dynamic camera
            glu.gluLookAt(cur.X, cur.Y, cur.Z, lookAt.X, lookAt.Y, lookAt.Z, up.X, up.Y, up.Z);
        }
        if (this.camType == CamType.STATIC) {
            //test camera, static
            glu.gluLookAt(-13, 10, -15, 0, 10, 0, 0, 1, 0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        // Set projection matrix
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set up a perspective camera at point (0,0,0), looking in negative Z-direction
        // Angle of view is 90 degrees. Near and far clipping planes are 10 and 1000
        glu.gluPerspective(90.0, (float) width / height, 0.5f, 1000);

        // set up geometric transformations
        gl.glMatrixMode(GL_MODELVIEW);
        // Set up viewport
        gl.glViewport(0, 0, width, height);
    }

    private void drawLights(GL gl) {
        gl.glLightfv(GL_LIGHT2, GL_POSITION, new float[]{100f, 100f, 100f, 1f}, 0);
    }

    private void drawSideCubes(GL gl) {
        gl.glPushMatrix();
        gl.glColor3f(255, 255, 255);

        //world
        //normal
        if (this.colors == ColorScheme.NORMAL) {
            drawCubes(0, gl, -1, .275f, 0.51f, 0.71f);
            drawCubes(0, gl, world.getDimension(), .275f, 0.51f, 0.71f);
            drawCubes(1, gl, -1, 1, 0.65f, 0);
            drawCubes(1, gl, world.getDimension(), 1, 0.65f, 0);
            drawCubes(2, gl, -1, 0.63f, 0.125f, 0.94f);
            drawCubes(2, gl, world.getDimension(), 0.63f, 0.125f, 0.94f);
        }
        if (this.colors == ColorScheme.RUBIKS) {
            //rubiks
            drawCubes(0, gl, -1, 1, 0, 0);
            drawCubes(0, gl, world.getDimension(), 1, 0.27f, 0f);
            drawCubes(1, gl, -1, 1, 1, 1);
            drawCubes(1, gl, world.getDimension(), 1, 1, 0);
            drawCubes(2, gl, -1, 0f, 0f, 1f);
            drawCubes(2, gl, world.getDimension(), 0f, 1f, 0f);
        }

        gl.glPopMatrix();
    }

    private void drawCubes(int dim, GL gl, int q, float r, float g, float b) {
        int raster = this.rasterDensity ? 2 : 1;
        for (int i = 0; i < world.getDimension(); i+=raster) {
            for (int j = 0; j < world.getDimension(); j+=raster) {
                gl.glPushMatrix();
                switch (dim) {
                    case 0:
                        gl.glTranslatef(q, i, j);
                        break;
                    case 1:
                        gl.glTranslatef(i, q, j);
                        break;
                    case 2:
                        gl.glTranslatef(i, j, q);
                }
                if (this.camType == CamType.DYNAMIC) {
                    gl.glColor3f(r, g, b);
                    glut.glutSolidCube(0.7f);
                }
                if (this.camType == CamType.STATIC) {
                    gl.glColor3f(255, 255, 255);
                    glut.glutWireCube(1f);
                }
                gl.glPopMatrix();
            }
        }
    }

    private void drawObjects(GL gl) {
        Position apple = world.getApple();
        drawSphere(gl, Color.RED, apple);
        ArrayList<Position> snake = world.getSnake().getCubes();
        if (this.camType == CamType.STATIC) {
            drawSphere(gl, Color.BLUE, snake.get(0));
        }
        for (int i = 1; i < snake.size(); i++) {
            Position c = snake.get(i);
            drawSphere(gl, Color.GREEN, c);
        }
        //drawCube(gl, Color.BLUE, world.getSnake().getCurrentPosition());        
    }

    private void drawSphere(GL gl, Color c, Position toDraw) {
        gl.glPushMatrix();
        gl.glTranslatef(toDraw.X, toDraw.Y, toDraw.Z);
        if (c == Color.RED) {
            gl.glColor3f(1, 0, 0);
        }
        if (c == Color.GREEN) {
            gl.glColor3f(0, 1, 0);
        }
        if (c == Color.BLUE) {
            gl.glColor3f(0, 0, 1);
        }
        glut.glutSolidSphere(0.5, 24, 24);

        gl.glPopMatrix();
    }

    @Override
    public void keyboard(char key) {
        switch (key) {
            case 'v':
                CamType[] camTypes = CamType.values();
                for (int i = 0; i < camTypes.length; i++) {
                    if (camTypes[i] == camType) {
                        camType = camTypes[(i + 1) % camTypes.length];
                        break;
                    }
                }
                break;
            case 'c':
                ColorScheme[] schemes = ColorScheme.values();
                for (int i = 0; i < schemes.length; i++) {
                    if (schemes[i].equals(colors)) {
                        colors = schemes[(i + 1) % schemes.length];
                        break;
                    }
                }               
                break;
            case 'r':
                this.rasterDensity = !this.rasterDensity;
                break;
            case 'q':
                controller.die();
                break;
        }
    }
}
