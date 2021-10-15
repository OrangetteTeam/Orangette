package club.eridani.cursa.utils.math;

public class Vec2i {
    public static final Vec2i ZERO = new Vec2i(0, 0);
    public final int x;
    public final int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
