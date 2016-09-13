package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;

import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/10/2016.
 */
public interface Block extends Entity{
    final float PIXELS_TO_METERS = PhysicsConstants.PIXELS_TO_METERS;
    final short PHYSICS_ENTITY = PhysicsConstants.PHYSICS_ENTITY;    // 0001
    final short WORLD_ENTITY = PhysicsConstants.WORLD_ENTITY; // 0010 or 0x2 in hex

    public void draw(Matrix4 camera);
    public void trigger();
    public float getWidth();
    public float getHeight();

}
