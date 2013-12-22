package org.alt0179.Snake.model;

import java.awt.geom.Point2D;

/**
 * @author Robbert 
 */
public class Position {

    public final int X, Y, Z;
    transient private boolean isUpdated;
    
    public Position(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public Position() {
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
    }

    public Position copy() {
        return new Position(X, Y, Z);
    }

    @Override
    public String toString() {
        return X + " - " + Y + " - " + Z;
    }

    public boolean isUpdated() {
        boolean wasUpdated = isUpdated;
        isUpdated = false;
        return wasUpdated;
    }

    public boolean equals(Position p) {
        return X == p.X && Y == p.Y && Z == p.Z;
    }

    public Point2D toPoint2D() {
        return new Point2D.Float(X, Z);
    }

    public Position add(Position toAdd) {
        return new Position(toAdd.X + X, toAdd.Y + Y, toAdd.Z + Z);
    }

    public Position mul(int a) {
        return new Position(X * a, Y * a, Z * a);
    }

    public Position crossProduct(Position toProd) {
        int u = toProd.X;
        int v = toProd.Y;
        int w = toProd.Z;

        return new Position(Y * w - Z * v, Z * u - X * w, X * v - Y * u);
    }
}
