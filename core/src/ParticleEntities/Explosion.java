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
    //Particle stuff needs to be done
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
    public int type() {
        return 4;
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
