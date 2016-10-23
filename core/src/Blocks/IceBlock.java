package Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Block;
import com.mygdx.game.Entity;

import Constants.EntityType;
import Constants.PhysicsConstants;
import Constants.Utilities;

/**
 * IceBlock
 * On Collision it will freeze surrounding blocks adding a layer of "Ice" which makes them immune to
 * 1 or more hits for a set amount of time, if the block below this one breaks it will fall
 * and can collide with a padel freezing the padel preventing it from being redrawn
 * for a set amount of time.
 */
public class IceBlock implements Block{
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;
    boolean destroy = false;
    public IceBlock(World scene, float xPos, float yPos) {
        sprite = new Sprite(new Texture("core/textures/iceBlock.bmp"));
        batch = new SpriteBatch();
        world = scene;
        BodyDef iceBody = new BodyDef();
        iceBody.type = BodyDef.BodyType.DynamicBody;

        iceBody.position.set(xPos, yPos);
        iceBody.linearVelocity.set(0f, -1.0f);

        body = world.createBody(iceBody);

        FixtureDef iceFixture = new FixtureDef();
        iceFixture.filter.categoryBits = BLOCK_ENTITY;
        iceFixture.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | PhysicsConstants.WALL_ENTITY;
        iceFixture.density = .3f;
        iceFixture.friction = 0f;
        iceFixture.restitution = .2f;

        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);
        iceFixture.shape = blocker;

        body.createFixture(iceFixture);
        body.setUserData(this);
        blocker.dispose();
    }
    @Override
    public void draw(Matrix4 camera) {
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        Vector2 position = Utilities.spritePositionCalc(body, sprite);
        sprite.setPosition(position.x, position.y);

        batch.setProjectionMatrix(camera);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
                        getScaleY(),sprite.getRotation());
        batch.end();
    }

    //trigger will be for functionality on ball colliding with block
    @Override
    public void trigger() {

    }

    @Override
    public float getWidth() {
        return sprite.getWidth();
    }

    @Override
    public float getHeight() {
        return sprite.getHeight();
    }

    @Override
    public EntityType type() {
        return EntityType.ICEBLOCK;
    }

    @Override
    public Entity onContact() {
        //behavior
        destroy = true;
        return null;
    }

    @Override
    public String contactDebug() {
        return "IceBlock";
    }

    @Override
    public void finishCreation() {

    }

    @Override
    public boolean destroy() {
        if(destroy) {
            world.destroyBody(body);
            return true;
        }
        return false;
    }
}
