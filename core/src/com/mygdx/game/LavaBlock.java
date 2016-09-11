package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
public class LavaBlock implements Block{
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;

    LavaBlock(World scene) {
        sprite = new Sprite(new Texture("core/textures/lavafull.bmp"));
        batch = new SpriteBatch();
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
        p1 = p1Flip ? p1 * -1: p1;
        p2 = p2Flip ? p2 * -1: p2;
        System.out.println(p1 + " , " + p2);

        bodyDef4.position.set(p1, p2);

        body = world.createBody(bodyDef4);

        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.filter.categoryBits = WORLD_ENTITY;
        fixtureDef5.filter.maskBits = PHYSICS_ENTITY;
        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        body.createFixture(fixtureDef5);

        blocker.dispose();
    }
    @Override
    public void draw(Matrix4 camera) {
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
}
