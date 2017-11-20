package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import Constants.EntityType;
import Constants.PhysicsConstants;

public class Padel implements Entity {
    World world;
    Bezier padel;
    ShapeRenderer batch;

    ArrayList<Vector2> padelPoints = new ArrayList<>();
    boolean needsToFinish = false;
    Body body;
    BodyDef initBodyDef;

    Padel(World world, float xPos, float yPos) {
        padelPoints.add(new Vector2(xPos, yPos));
        this.world = world;
        batch = new ShapeRenderer();

        initBodyDef = new BodyDef();
        initBodyDef.type = BodyDef.BodyType.DynamicBody;
        initBodyDef.linearVelocity.set(0f, 0.0f);
        initBodyDef.gravityScale = 0f;

        initBodyDef.position.set(xPos, yPos);
        needsToFinish = true;
    }

    public void addVector(float xPos, float yPos) {
        if (padelPoints.size() < 6) {
            padelPoints.add(new Vector2(xPos, yPos));
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
//            batch.setProjectionMatrix(camera);
//            batch.setTransformMatrix(camera);
            for (int i = 0; i + 1 < padelPoints.size(); i++) {
                batch.begin(ShapeRenderer.ShapeType.Line);
//                System.out.println("line between points: " + padelPoints.get(i).toString() + " & " +  padelPoints.get(i + 1).toString());
                batch.rectLine(padelPoints.get(i), padelPoints.get(i+1), 5);
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
        return null;
    }

    @Override
    public String contactDebug() {
        return null;
    }

    @Override
    public void finishCreation() {
        if (needsToFinish && padelPoints.size() > 2) {
            body = world.createBody(initBodyDef);
            System.out.println("body made");

            for (int i = 0; i+1 < padelPoints.size(); i++) {
                EdgeShape shape = new EdgeShape();
                shape.set(padelPoints.get(i), padelPoints.get(i+1));
                FixtureDef padelPhysics = new FixtureDef();
                padelPhysics.filter.categoryBits = PhysicsConstants.PADEL_ENTITY;
                padelPhysics.shape = shape;
                body.createFixture(padelPhysics);
            }
            body.setUserData(this);

            needsToFinish = false;
        }
    }

    @Override
    public boolean destroy() {
        return false;
    }
}
