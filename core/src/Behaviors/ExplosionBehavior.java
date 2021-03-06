package Behaviors;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Entity;
import com.mygdx.game.EntityStrategy;

import ParticleEntities.Explosion;

/**
 * Created by Bradley on 9/14/2016.
 */
public class ExplosionBehavior implements EntityStrategy{
    @Override
    public Entity effect(World scene, float x, float y) {
        // should expand this to make it do more than just this, but for testing this is fine
        Entity toRet = new Explosion(scene, x, y);
        System.out.println("Explosion");
        return toRet;
    }
}
