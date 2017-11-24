package Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

import Behaviors.BasicBehavior;
import Constants.EntityType;
import Constants.PhysicsConstants;
import Constants.Utilities;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BasicBlock extends Block {
    World world;
    Body body;
    ShapeRenderer batch;
    Color color;
    EntityStrategy behavior;
    boolean destroy = false;
    int frozen = 0;
    float x,y, width, height;

    public BasicBlock(World scene, float xPos, float yPos) {
        batch = new ShapeRenderer();
        world = scene;

        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;

        width = Utilities.BLOCK_WIDTH;
        height = Utilities.BLOCK_HEIGHT;
        color = Color.NAVY;

        bodyDef4.position.set(xPos, yPos);

        body = world.createBody(bodyDef4);

        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.filter.categoryBits = BLOCK_ENTITY;
        fixtureDef5.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | EFFECT_ENTITY;
        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(width/2 / PIXELS_TO_METERS, height/2/PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        body.createFixture(fixtureDef5);
        body.setUserData(this);
        behavior = new BasicBehavior();
        setBehavior(behavior);
        setBody(body);
        blocker.dispose();
    }
    @Override
    public void draw(Matrix4 camera) {
        //NOTE SHAPERENDERER USES ACTUAL PIXELS NOT PIXELS TO METERS LIEK THE REST OF LIBGDX

        batch.setProjectionMatrix(camera);
        Vector2 position = Utilities.positionCalc(body, width, height);
        batch.setColor(color);
        batch.begin(ShapeRenderer.ShapeType.Filled);

        batch.rect(position.x,
                position.y,
                width, height);
        batch.end();
    }

    @Override
    public void trigger() {

    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public EntityType type() {
        return EntityType.BLOCK;
    }

    @Override
    public Entity onContact() {
        behavior.effect(world, body.getPosition().x, body.getPosition().y);
        if (frozen <= 0) {
            destroy = true;
        } else {
            frozen-= 1;
        }
        return null;
    }

    @Override
    public String contactDebug() {
        return "BasicBlock";
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
