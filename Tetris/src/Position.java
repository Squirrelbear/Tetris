/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * Position class:
 * Defines a pair of x and y to be used for representing
 * a coordinate.
 */
public class Position {
    /**
     * Down moving unit vector.
     */
    public static final Position DOWN = new Position(0,1);
    /**
     * Up moving unit vector.
     */
    public static final Position UP = new Position(0,-1);
    /**
     * Left moving unit vector.
     */
    public static final Position LEFT = new Position(-1,0);
    /**
     * Right moving unit vector.
     */
    public static final Position RIGHT = new Position(1,0);

    /**
     * X coordinate for horizontal component of the Position.
     */
    public int x;
    /**
     * Y coordinate for vertical component of the Position
     */
    public int y;

    /**
     * Creates a new position at x,y.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor sets the new object to a copy of the other.
     *
     * @param otherPos Other Position to be copied.
     */
    public Position(Position otherPos) {
        this.x = otherPos.x;
        this.y = otherPos.y;
    }

    /**
     * Adds the movement vector to this position.
     *
     * @param movementVector The unit vector to define direction of movement.
     */
    public void move(Position movementVector) {
        this.x = this.x + movementVector.x;
        this.y = this.y + movementVector.y;
    }
}
