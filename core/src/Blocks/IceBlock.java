package Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Block;
import com.mygdx.game.Entity;

import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/13/2016.
 */
public class IceBlock implements Block{
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;

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
        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2);

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
    public int type() {
        return 0;
    }

    @Override
    public Entity onContact() {
        return null;
    }

    @Override
    public String contactDebug() {
        return "IceBlock";
    }

    @Override
    public void finishCreation() {

    }
}
