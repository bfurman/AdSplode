package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import Constants.EntityType;
import Constants.PhysicsConstants;

public class Padel implements Entity {
    World world;
    Bezier padel;
    ShapeRenderer batch;

    ArrayList<Vector2> padelPoints = new ArrayList<>();
    ArrayList<Vector2> fixturePoints = new ArrayList<>();
    boolean needsToFinish = false;
    Body body;
    BodyDef initBodyDef;
    boolean noMorePoints = false;
    Padel(World world, float xPos, float yPos) {
        padelPoints.add(new Vector2(xPos, yPos));
        fixturePoints.add(new Vector2(getFixtureXPos(xPos) , getFixtureYPos(yPos)));

        this.world = world;
        batch = new ShapeRenderer();

        initBodyDef = new BodyDef();
        initBodyDef.type = BodyDef.BodyType.StaticBody;
/*        initBodyDef.linearVelocity.set(0f, 0.0f);
        initBodyDef.gravityScale = 0f;*/

        //FOR WHAT EVER FUCKED UP REASON THE FUCKING FIXTURE BUILDS BASED ON WHERE THE
        //BODY IS NOT THE COORDINATES IN THE FUCKING WORLD SO SET THE FUCKING BODY AT THE
        //CENTER OF THE FUCKING WORLD SO IT BUILDS EASILY
        //THIS IS FUCKING RETARDED THE DRAW SHOULD BE THE SAME REGARDLESS OF WHETHER IT IS A
        //SPRITE OR A SHAPE!!!!
        initBodyDef.position.set(0f, 0f);
        needsToFinish = true;
    }

    public void addVector(float xPos, float yPos) {
        if (padelPoints.size() < 6) {
            padelPoints.add(new Vector2(xPos, yPos));
            fixturePoints.add(new Vector2( getFixtureXPos(xPos),getFixtureYPos(yPos)));
        }
    }

    public void addVector(Vector2 vec) {
        padelPoints.add(vec);
    }

    @Override
    public void draw(Matrix4 camera) {
        if (!needsToFinish) {
/*            padel = new Bezier();
            padel.set(padelPoints.toArray(new Vector2[]{}));*/
            batch.setColor(Color.RED);
            for (int i = 0; i + 1 < padelPoints.size(); i++) {
                batch.begin(ShapeRenderer.ShapeType.Line);
                batch.rectLine(padelPoints.get(i), padelPoints.get(i+1), 6);
                batch.end();
            }
        }
    }

    @Override
    public EntityType type() {
        return EntityType.PADEL;
    }

    @Override
    public Entity onContact() {
        System.out.println("hit");
        return null;
    }

    @Override
    public String contactDebug() {
        System.out.println("hit");
        return null;
    }

    public void finishBuild() {
        noMorePoints = true;
    }
    @Override
    public void finishCreation() {
        if (needsToFinish && padelPoints.size() > 2 && noMorePoints) {
            body = world.createBody(initBodyDef);
            System.out.println("body made");

            //for (int i = 0; i+1 < padelPoints.size(); i++) {
            ChainShape shape = new ChainShape();
            shape.createChain(fixturePoints.toArray(new Vector2[]{}));
/*            CircleShape shape = new CircleShape();
            shape.setRadius(2f);*/
            FixtureDef padelPhysics = new FixtureDef();
            padelPhysics.filter.categoryBits = PhysicsConstants.PADEL_ENTITY;
            padelPhysics.shape = shape;
            body.createFixture(padelPhysics);
            shape.dispose();
            //}
            body.setUserData(this);

            needsToFinish = false;
        }
    }

    @Override
    public boolean destroy() {
        if (body != null) {
            world.destroyBody(body);
        }
        return false;
    }

    private float getFixtureXPos(float xPos) {
        System.out.println("initial point:" + xPos);
        System.out.println("divide by pixel meters:" + xPos/PIXELS_TO_METERS);
        //if left of center needs to be negative if right positive
        float XPosition = xPos/PIXELS_TO_METERS;
        if (XPosition < Gdx.graphics.getWidth()/PIXELS_TO_METERS/2) {
            XPosition = -1 * Gdx.graphics.getWidth()/PIXELS_TO_METERS/2 + XPosition;
        } else if (XPosition > Gdx.graphics.getWidth()/PIXELS_TO_METERS/2) {
            XPosition = XPosition - Gdx.graphics.getWidth()/PIXELS_TO_METERS/2;
        } else {
            XPosition = 0f;
        }
        System.out.println("Calculated X Positon:" + XPosition);
        return XPosition;
    }

    private float getFixtureYPos(float yPos) {
        float YPosition = yPos/PIXELS_TO_METERS;
        if (YPosition < Gdx.graphics.getHeight()/PIXELS_TO_METERS/2) {
            YPosition = -1 * Gdx.graphics.getHeight()/PIXELS_TO_METERS/2 + YPosition;
        } else if (YPosition > Gdx.graphics.getHeight()/PIXELS_TO_METERS/2) {
            YPosition = YPosition - Gdx.graphics.getHeight()/PIXELS_TO_METERS/2;
        } else {
            YPosition = 0f;
        }
        System.out.println("Calculated Y Position:" + YPosition);
        return YPosition;
    }
}
