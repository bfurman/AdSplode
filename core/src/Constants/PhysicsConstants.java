package Constants;

/**
 * Created by Bradley on 9/12/2016.
 */
public class PhysicsConstants {
    public static final float PIXELS_TO_METERS = 100f;

    public static final short PHYSICS_ENTITY = 0x1;    // 0001
    public static final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
    public static final short BLOCK_ENTITY = 0x4;
    public static final short WALL_ENTITY = 0x8;
}
