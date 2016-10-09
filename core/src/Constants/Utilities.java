package Constants;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Static class to hold utility functions
 */
public class Utilities {

    private Utilities(){}
    public static Vector2 spritePositionCalc(Body body, Sprite sprite) {
        float x = (body.getPosition().x * PhysicsConstants.PIXELS_TO_METERS) - sprite.
                getWidth()/2;
        float y = (body.getPosition().y * PhysicsConstants.PIXELS_TO_METERS) -sprite.getHeight()/2;
        return new Vector2(x, y);
    }
}
