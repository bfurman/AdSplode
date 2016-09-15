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
    float radius;
    // need to refactor for world being global
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
        //NOTE SHAPERENDERER USES ACTUAL PIXELS NOT PIXELS TO METERS LIKE THE REST OF LIBGDX

        batch.setProjectionMatrix(camera);

        batch.setColor(color);
        batch.begin(ShapeRenderer.ShapeType.Filled);

        batch.circle(body.getPosition().x * PIXELS_TO_METERS,
                body.getPosition().y * PIXELS_TO_METERS,
                radius);
        batch.end();
    }

    // actions to be done specific to this entity, should be called in onContact and after behavior
    // may be removed, if not repurposed for something like at the end of a timer
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
    public Entity onContact() {
        return null;
    }

    @Override
    public String contactDebug() {
        return "Ball";
    }

    @Override
    public void finishCreation() {

    }

    /**
     * applies the torque to the physics body may be used for a power up
     *
     * @param force
     * @param wake
     */
    public void applyTorque(float force, boolean wake) {
        body.applyTorque(force, wake);
    }

    //everything below is for keyboard commands for now so they are debug methods

    /**
     * move left or right
     *
     * @param velocityX
     * @param velocityY
     * both params are the vector 2 of the force being applied
     */
    public void setLinearVelocity(float velocityX, float velocityY) {
        body.setLinearVelocity(velocityX, velocityY);
    }

    /**
     *
     * @param vX
     * @param vY
     * @param wake libgdx entities can be a "sleep" this is to wake it and let it move
     */
    public void applyForceToCenter(float vX, float vY, boolean wake) {
        body.applyForceToCenter(vX, vY, wake);
    }
}
