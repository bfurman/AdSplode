package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/12/2016.
 */
public class Ball implements Entity{
    World world;
    Body body;
    ShapeRenderer batch;
    Color color;
    float x,y, radius;

    final float PIXELS_TO_METERS = PhysicsConstants.PIXELS_TO_METERS;
    final short PHYSICS_ENTITY = PhysicsConstants.PHYSICS_ENTITY;    // 0001
    final short WORLD_ENTITY = PhysicsConstants.WORLD_ENTITY; // 0010 or 0x2 in hex

    Ball(World scene, float xPos, float yPos) {
        batch = new ShapeRenderer();
        world = scene;

        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.DynamicBody;

        radius = 15;
        color = Color.FOREST;

        bodyDef4.position.set(xPos, yPos);

        body = world.createBody(bodyDef4);

        FixtureDef ballPhysics = new FixtureDef();
        ballPhysics.filter.categoryBits = PHYSICS_ENTITY;

        CircleShape blocker = new CircleShape();
        blocker.setRadius(radius/PIXELS_TO_METERS);
        ballPhysics.shape = blocker;
        ballPhysics.density = 0.1f;
        ballPhysics.friction = 0.1f;
        ballPhysics.restitution = 1f;

        body.createFixture(ballPhysics);
        body.setUserData(this);
        blocker.dispose();
    }

    public void draw(Matrix4 camera) {
        //NOTE SHAPERENDERER USES ACTUAL PIXELS NOT PIXELS TO METERS LIEK THE REST OF LIBGDX

        batch.setProjectionMatrix(camera);

        batch.setColor(color);
        batch.begin(ShapeRenderer.ShapeType.Filled);

        batch.circle(body.getPosition().x * PIXELS_TO_METERS,
                body.getPosition().y * PIXELS_TO_METERS,
                radius);
        batch.end();
    }


    public void trigger() {

    }


    public float getRadius() {
        return radius;
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void onContact() {

    }

    @Override
    public String contactDebug() {
        return "Ball";
    }
}
