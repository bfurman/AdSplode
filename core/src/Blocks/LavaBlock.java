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
import com.mygdx.game.EntityStrategy;

import Behaviors.ExplosionBehavior;
import Constants.EntityType;
import Constants.Utilities;

/**
 * On ball collision will cause a n explosion destroying surrounding blocks in a set radius
 */
public class LavaBlock implements Block {
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;
    EntityStrategy behavior;

    public LavaBlock(World scene, float xPos, float yPos) {
        sprite = new Sprite(new Texture("core/textures/lavafull.bmp"));
        batch = new SpriteBatch();
        world = scene;
        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;

        bodyDef4.position.set(xPos, yPos);

        body = world.createBody(bodyDef4);

        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.filter.categoryBits = BLOCK_ENTITY;
        fixtureDef5.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY;
        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        body.createFixture(fixtureDef5);
        body.setUserData(this);
        behavior = new ExplosionBehavior();
        blocker.dispose();
    }
    @Override
    public void draw(Matrix4 camera) {
        //set rotation will probably be removed since it is a static entity, but kept it in for now,
        // we may want to make a block in the future that rotates to make even more weird shots,
        //actually that sounds cool, an industructable block that is static but rotates so it can launch,
        // the ball at new angles but isnt always beneficial
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        Vector2 position = Utilities.spritePositionCalc(body, sprite);
        sprite.setPosition(position.x, position.y);

        batch.setProjectionMatrix(camera);
        //when drawing anything either sprite or shaperenderer it has to have the begin and end
        //also you have to end a drawer before starting a different one
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
        return EntityType.LAVABLOCK;
    }

    @Override
    public Entity onContact() {
        Entity toRet = behavior.effect(body.getPosition().x, body.getPosition().y);
        // mark for destroy maybe
        return toRet;
    }

    @Override
    public String contactDebug() {
        return "LavaBlock";
    }

    @Override
    public void finishCreation() {

    }
}
