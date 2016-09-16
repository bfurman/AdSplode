package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;

import Constants.EntityType;
import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/12/2016.
 */

//change type to return enum
public interface Entity {
    final float PIXELS_TO_METERS = PhysicsConstants.PIXELS_TO_METERS;
    final short PHYSICS_ENTITY = PhysicsConstants.PHYSICS_ENTITY;    // 0001
    final short WORLD_ENTITY = PhysicsConstants.WORLD_ENTITY; // 0010 or 0x2 in hex
    final short BLOCK_ENTITY = PhysicsConstants.BLOCK_ENTITY;
    public void draw(Matrix4 camera);
    public EntityType type();
    public Entity onContact();
    public String contactDebug();
    public void finishCreation();
}
