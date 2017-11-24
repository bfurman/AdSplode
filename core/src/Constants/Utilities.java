package Constants;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Static class to hold utility functions
 */
public class Utilities {

    public static final float BLOCK_WIDTH = 50f;
    public static final float BLOCK_HEIGHT = 40f;
    private Utilities(){}

    public static Vector2 spritePositionCalc(Body body, Sprite sprite) {
        return positionCalc(body, sprite.getWidth(), sprite.getHeight());
    }

    public static Vector2 positionCalc(Body body, float width, float height) {
        float x = body.getPosition().x * PhysicsConstants.PIXELS_TO_METERS - width/2;
        float y = body.getPosition().y * PhysicsConstants.PIXELS_TO_METERS - height/2;
        return new Vector2(x, y);
    }
}
