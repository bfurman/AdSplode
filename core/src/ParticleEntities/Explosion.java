package ParticleEntities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.AdSplode;
import com.mygdx.game.Entity;

import Constants.EntityType;
import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/14/2016.
 */
public class Explosion implements Entity {
    World world;
    Body body;
    ShapeRenderer batch;
    SpriteBatch particleEffect;
    ParticleEffect pe;
    BodyDef initBodyDef;
    boolean needsToFinish;
    final float startRadius = 0.1f;
    final float endRadius = .5f;
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

    //currently the explosion radius physics body doesnt expand with the shape draw
    public Explosion(World scene, float x, float y) {
        batch = new ShapeRenderer();
        particleEffect = new SpriteBatch();
        world = scene;

        initBodyDef = new BodyDef();
        initBodyDef.type = BodyDef.BodyType.DynamicBody;
        initBodyDef.linearVelocity.set(0f, 0.0f);
        initBodyDef.gravityScale = 0f;

        currentRadius = startRadius;
        initBodyDef.position.set(x, y);
        needsToFinish = true;

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("core/textures/ExplosionParticle_v1.p"),Gdx.files.internal("core/textures/"));
        //emitter drawing does the same thing as shaperenderer
        pe.getEmitters().first().setPosition(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS);
        System.out.println("Explosion X: " + x + ", Y:" + y);
        pe.start();
    }
    @Override
    public void draw(Matrix4 camera) {
        if (!needsToFinish) {
            batch.setProjectionMatrix(camera);

            batch.setColor(Color.BLACK);
            batch.begin(ShapeRenderer.ShapeType.Line);

            batch.circle(body.getPosition().x * PIXELS_TO_METERS,
                    body.getPosition().y * PIXELS_TO_METERS,
                    currentRadius);
            batch.end();
            pe.update(Gdx.graphics.getDeltaTime());
            particleEffect.setProjectionMatrix(camera);
            particleEffect.begin();
            pe.draw(particleEffect);
            particleEffect.end();
            if (pe.isComplete())
                toDispose=true;
            currentRadius += .1f;
            //TODO update fixture with new size of the radius so physics shape grows as well
            if (currentRadius > endRadius) {
                //toDispose = true;
                currentRadius = endRadius;
            }
            Shape shape = body.getFixtureList().first().getShape();
            shape.setRadius(currentRadius);
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
        System.out.println("Explosion hit an object");
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
            explosionPhysics.filter.categoryBits = EFFECT_ENTITY;

            CircleShape blocker = new CircleShape();
            blocker.setRadius(endRadius);
            explosionPhysics.shape = blocker;

            body.createFixture(explosionPhysics);
            body.setUserData(this);

            needsToFinish = false;
        }
    }

    @Override
    public boolean destroy() {
        if (toDispose) {
            world.destroyBody(body);
        }
        return toDispose;
    }
}
