package Constants;

/**
 * Created by Bradley on 9/12/2016.
 */
public class PhysicsConstants {
    public static final float PIXELS_TO_METERS = 100f;

    public static final short PHYSICS_ENTITY = 0x1;    // 0001 // probably refactor into ball entity
    public static final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
    public static final short BLOCK_ENTITY = 0x4;
    public static final short WALL_ENTITY = 0x8;
    public static final short PADEL_ENTITY = 0x10; // 0001 0000
    public static final short EFFECT_ENTITY =0x20;
}
