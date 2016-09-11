package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BasicBlock implements Block{
    World world;
    Body body;
    ShapeRenderer batch;
    Color color;
    float x,y, width, height;

    BasicBlock(World scene) {
        batch = new ShapeRenderer();
        world = scene;

        Random generator = new Random();
        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;
        float p1 = generator.nextFloat() * Gdx.graphics.getWidth()/PIXELS_TO_METERS/2;
        float p2 = generator.nextFloat() * Gdx.graphics.getHeight()/PIXELS_TO_METERS/2;
        generator = new Random();
        boolean p1Flip = generator.nextBoolean();
        generator = new Random();
        boolean p2Flip = generator.nextBoolean();
        x = p1Flip ? p1 * -1: p1;
        y = p2Flip ? p2 * -1: p2;
        width = 50;
        height = 40;
        color = Color.NAVY;

        System.out.println("Basic Block at " + x + " , " + y);

        bodyDef4.position.set(x, y);

        body = world.createBody(bodyDef4);

        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.filter.categoryBits = WORLD_ENTITY;
        fixtureDef5.filter.maskBits = PHYSICS_ENTITY;
        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(width/2 / PIXELS_TO_METERS, height/2/PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        body.createFixture(fixtureDef5);

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
}
