package Entities;

import java.awt.*;

/**
 * Abstract blueprint base class representing any positionable game object.
 * Defines shared coordinates and forces lifecycle methods for updating and rendering.
 * @author Ondřej Ptáček
 */
public abstract class Entity {

    protected float x;
    protected float y;

    /**
     * Initializes a new entity with starting spatial coordinates.
     * @param x global horizontal layout position coordinate
     * @param y global vertical layout position coordinate
     */
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Abstract method to update internal positioning or animation logic state per tick.
     */
    public abstract void update();

    /**
     * Abstract method to handle drawing the specific entity onto the screen layout.
     * @param g current Graphics context container instance
     */
    public abstract void render(Graphics g);

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}