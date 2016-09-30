package ParticleEntities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AdSplode;
import com.mygdx.game.Entity;

import Constants.EntityType;

/**
 * Created by Bradley on 9/14/2016.
 */
public class Explosion implements Entity {
    World world;
    Body body;
    ShapeRenderer batch;
    BodyDef initBodyDef;
    boolean needsToFinish;
    final float startRadius = .7f;
    final float endRadius = 3.5f;
    float currentRadius;
    boolean toDispose = false;
    //small change to push
    //Particle stuff needs to be done

    //TODO ok so the current way of doing this is wrong, looked up some box2d and libgdx stuff,
    // the correct way to do this would be having a small ball shoot out at each angle,
    // the current way you wouldnt be able to have say bunch of industructible blocks being able
    // to block an explosion the ball in every angle method you would be able to accomplish this,
    // but once it hits something at each angle it would stop at that point
    // instead of destroying everything in a radius. so there are pros and cons to both, we could
    // have 2 different type of explosion blocks i suppose which would be cool
    public Explosion(float x, float y) {
        batch = new ShapeRenderer();
        world = AdSplode.world;

        initBodyDef = new BodyDef();
        initBodyDef.type = BodyDef.BodyType.StaticBody;

        currentRadius = startRadius;
        initBodyDef.position.set(x, y);
        needsToFinish = true;
    }
    @Override
    public void draw(Matrix4 camera) {
        if (!needsToFinish) {
            batch.setProjectionMatrix(camera);

            batch.setColor(Color.GREEN);
            batch.begin(ShapeRenderer.ShapeType.Line);

            batch.circle(body.getPosition().x * PIXELS_TO_METERS,
                    body.getPosition().y * PIXELS_TO_METERS,
                    currentRadius);
            batch.end();
            currentRadius += .2f;
            //TODO update fixture with new size of the radius so physics shape grows as well
            if (currentRadius > endRadius) {
                toDispose = true;
                //currentRadius = 3.5f;
            }
        }
    }

    //change type to enum eventually
    @Override
    public EntityType type() {
        return EntityType.EXPLOSION;
    }

    @Override
    public Entity onContact() {
        // I say no~op for this for now and let the other body default onContact
        // Will probably need to alter onContact in future to get the enum of the body type
        // all entities should have a type getter,
        return null;
    }

    @Override
    public String contactDebug() {
        return "Explosion";
    }

    public void finishCreation() {
        if (needsToFinish) {
            body = world.createBody(initBodyDef);
            System.out.println("body made");
            FixtureDef explosionPhysics = new FixtureDef();
            explosionPhysics.filter.categoryBits = PHYSICS_ENTITY;

            CircleShape blocker = new CircleShape();
            blocker.setRadius(currentRadius);
            explosionPhysics.shape = blocker;

            body.createFixture(explosionPhysics);
            body.setUserData(this);

            blocker.dispose();
            needsToFinish = false;
        }
    }
}
