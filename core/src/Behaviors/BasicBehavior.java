package Behaviors;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Entity;
import com.mygdx.game.EntityStrategy;

/**
 * Created by Bradley on 9/14/2016.
 */
public class BasicBehavior implements EntityStrategy {
    //basic behavior will return null aka no~op
    @Override
    public Entity effect(World scene, float x, float y) {
        System.out.println("No op");
        return null;
    }
}
