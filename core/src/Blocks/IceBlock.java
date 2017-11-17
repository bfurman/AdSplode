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
public class IceBlock extends  Block{
    public IceBlock(World scene, float xPos, float yPos) {
        super(scene, xPos, yPos, "core/textures/iceBlock.bmp");
        //setBehavior
    }

    @Override
    protected BodyDef createBodyDef()  {
        BodyDef iceBody = new BodyDef();
        iceBody.type = BodyDef.BodyType.DynamicBody;
        iceBody.linearVelocity.set(0f, -1.0f);
        return iceBody;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef iceFixture = new FixtureDef();
        iceFixture.filter.categoryBits = BLOCK_ENTITY;
        iceFixture.density = .3f;
        iceFixture.friction = 0f;
        iceFixture.restitution = .2f;
        return iceFixture;
    }

    @Override
    protected void setMaskBits(FixtureDef fixture) {
        fixture.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | PhysicsConstants.WALL_ENTITY;
    }

    //trigger will be for functionality on ball colliding with block
    @Override
    public void trigger() {

    }

    @Override
    public EntityType type() {
        return EntityType.ICEBLOCK;
    }

    @Override
    public String contactDebug() {
        return "IceBlock";
    }

    @Override
    public void finishCreation() {

    }
}
