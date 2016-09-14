package Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Block;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BasicBlock implements Block {
    World world;
    Body body;
    ShapeRenderer batch;
    Color color;
    float x,y, width, height;

    public BasicBlock(World scene, float xPos, float yPos) {
        batch = new ShapeRenderer();
        world = scene;

        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;

        width = 50;
        height = 40;
        color = Color.NAVY;

        bodyDef4.position.set(xPos, yPos);

        body = world.createBody(bodyDef4);

        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.filter.categoryBits = BLOCK_ENTITY;
        fixtureDef5.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY;
        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(width/2 / PIXELS_TO_METERS, height/2/PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        body.createFixture(fixtureDef5);
        body.setUserData(this);
        blocker.dispose();
    }
    @Override
    public void draw(Matrix4 camera) {
        //NOTE SHAPERENDERER USES ACTUAL PIXELS NOT PIXELS TO METERS LIEK THE REST OF LIBGDX

        batch.setProjectionMatrix(camera);

        batch.setColor(color);
        batch.begin(ShapeRenderer.ShapeType.Filled);

        batch.rect(body.getPosition().x * PIXELS_TO_METERS - width/2,
                body.getPosition().y * PIXELS_TO_METERS - height/2,
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
    public int type() {
        return 0;
    }

    @Override
    public void onContact() {

    }

    @Override
    public String contactDebug() {
        return "BasicBlock";
    }
}
