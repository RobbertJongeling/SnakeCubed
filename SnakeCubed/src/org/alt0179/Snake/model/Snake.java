package org.alt0179.Snake.model;

import java.util.ArrayList;

/**
 *
 * @author Robbert
 */
public class Snake {

    //snake
    private ArrayList<Position> snake = new ArrayList<Position>();
    private ArrayList<Position> eatenApples = new ArrayList<Position>();
    private Position up;
    private Position direction;

    public Snake(Position startHead, Position up, Position direction) {
        this.snake.add(startHead);
        this.up = up;
        this.direction = direction;
    }

    //---getters
    public ArrayList<Position> getCubes() {
        return snake;
    }

    public Position getCurrentPosition() {
        return snake.get(0);
    }

    public Position getCurrentDirection() {
        return direction;
    }

    public Position getCurrentOrientation() {
        return up;
    }

    //---setters
    public void moveStep(Position to, boolean apple) {
        this.snake.add(0, to);
        if (apple) {
            eatenApples.add(0, to);
        }

        Position toRemove = snake.get(snake.size() - 1);
        if (eatenApples.size() > 0) {
            Position longestAgoEatenApple = eatenApples.get(eatenApples.size() - 1);
            if (longestAgoEatenApple.equals(toRemove)) {
                //Do not remove, thus lengthen snake
                eatenApples.remove(longestAgoEatenApple);
            } else {
                snake.remove(toRemove);
            }
        } else {
            snake.remove(toRemove);
        }

    }

    void setDirection(Key key) {
        switch (key) {
            case UP:
                goUp();
                break;
            case LEFT:
                goLeft();
                break;
            case RIGHT:
                goRight();
                break;
            case DOWN:
                goDown();
                break;

        }
    }

    public void setDirection(Position direction) {
        this.direction = direction;
    }

    public void setUp(Position up) {
        this.up = up;
    }

    private void goUp() {
        Position oldDir = direction;
        setDirection(up);
        setUp(oldDir.mul(-1));
    }

    private void goLeft() {
        //up stays the same
        setDirection(up.crossProduct(direction));
    }

    private void goRight() {
        //up stays the same
        setDirection(direction.crossProduct(up));
    }

    private void goDown() {
        Position oldDir = direction;
        setDirection(up.mul(-1));
        setUp(oldDir);
    }
}
