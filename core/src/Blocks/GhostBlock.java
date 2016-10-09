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
import com.mygdx.game.AdSplode;
import com.mygdx.game.Block;
import com.mygdx.game.Entity;

import Constants.EntityType;
import Constants.PhysicsConstants;
import Constants.Utilities;

/**
 * GhostBlock
 * Troll block that will fall through all blocks at the top without causing a collision and
 * can only collide with the ball or padel, if it hits the padel it will cause same effect as
 * ice block collision, if the ball hits it, it will slow the ball down considerably.
 * This would be a block for later levels in the game to add more challenge.
 */
public class GhostBlock implements Block {
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;


    public GhostBlock(float xPos, float yPos) {
        //TODO get ghostly block image for this
        sprite = new Sprite(new Texture("core/textures/iceBlock.bmp"));
        batch = new SpriteBatch();
        world = AdSplode.world;
        BodyDef spectralBody = new BodyDef();
        spectralBody.type = BodyDef.BodyType.DynamicBody;

        spectralBody.position.set(xPos, yPos);
        spectralBody.linearVelocity.set(0f, -1.0f);

        body = world.createBody(spectralBody);

        FixtureDef specterFixture = new FixtureDef();
        specterFixture.filter.categoryBits = BLOCK_ENTITY;
        specterFixture.filter.maskBits = PHYSICS_ENTITY |
                       PhysicsConstants.WALL_ENTITY |
                       PhysicsConstants.PADEL_ENTITY;
        specterFixture.density = .3f;
        specterFixture.friction = 0f;

        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);
        specterFixture.shape = blocker;

        body.createFixture(specterFixture);
        body.setUserData(this);
        blocker.dispose();
    }

    //really need to figure out why i put this in, may be make it a cause and effect so if it is hit
    //by a behavior we can chain different behaviors together to cause different results?
    //like maybe lava block explosion hitting an iceblock will cause the explosion to be canceled at
    //that radius
    @Override
    public void trigger() {

    }

    //maybe make a default on since it is starting to look like all blocks that use sprites will be
    //identical, if a block wants to do something different it can override the method,
    //can interfaces have default implementation?
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
        return EntityType.GHOSTBLOCK;
    }

    @Override
    public Entity onContact() {
        return null;
    }

    @Override
    public String contactDebug() {
        return "GhostBlock";
    }

    @Override
    public void finishCreation() {

    }
}
