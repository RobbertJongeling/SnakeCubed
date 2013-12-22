package org.alt0179.Snake.model;

import java.util.ArrayList;
import org.alt0179.Snake.Snake3D.Game;

/**
 *
 * @author Robbert
 */
public class World {

    private int dimension;
    private Snake snake;
    private Position apple;
    private int nrApples;
    private boolean dead;

    private Game game;
    
    public World(Game game, int dimension) {
        this.game = game;
        this.dimension = dimension;

        initSnake();
        initApple();
    }

    public final void initSnake() {
        snake = new Snake(new Position(dimension / 2, dimension / 2, dimension / 2), new Position(0, 1, 0), new Position(0, 0, 1));
    }

    public final void initApple() {
        boolean canPlace = false;

        while (!canPlace) {
            int x = (int) (Math.random() * dimension);
            int y = (int) (Math.random() * dimension);
            int z = (int) (Math.random() * dimension);

            canPlace = canPlaceCube(x, y, z);
            if (canPlace) {
                apple = new Position(x, y, z);
            }
        }
    }

    /**
     * @pre initSnake()
     * @param x
     * @param y
     * @param z
     * @return 
     */
    private boolean canPlaceCube(int x, int y, int z) {
        if(x >= 0 && x < dimension && y >= 0 && y < dimension && z >= 0 && z < dimension) {
            Position toTest = new Position(x, y, z);
            ArrayList<Position> cubes = snake.getCubes();
            for (Position p : cubes) {
                if (p.equals(toTest)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean canPlaceCube(Position toPlace) {
        return canPlaceCube(toPlace.X, toPlace.Y, toPlace.Z);
    }

    /**
     * 
     * @param key key pressed
     * @post snake in new orientation
     */
    public void keyPressed(Key key) {        
        snake.setDirection(key);
    }

    /**
     * updates snake to new position given current orientation
     */
    public void moveSnake() {
        if(snake.getCubes().size() == dimension*dimension*dimension) {
            //TODO implement, give over 9000 extra points
        } else {
            Position toMove = snake.getCurrentPosition().add(snake.getCurrentDirection());
            if (canPlaceCube(toMove)) {
                if (toMove.equals(apple)) {                    
                    initApple();
                    snake.moveStep(toMove, true);
                    nrApples++;
                    game.appleEaten();
                } else {
                    snake.moveStep(toMove, false);
                }
            } else {
                dead = true;
            }
        }
    }
  
    /**
     * position in coordinates of snake head
     * @return 
     */
    public Position getCurrentPosition() {
        //throw new UnsupportedOperationException("Not yet implemented");
        //return new Position(0, 0, 0);
        return snake.getCurrentPosition();
    }

    public int getDimension() {
        return dimension;
    }

    public Position getApple() {
        return apple;
    }

    public Snake getSnake() {
        return snake;
    }
    
    public int getNrApples() {
        return nrApples;
    }
    
    public boolean isDead() {
        return dead;
    }
}
