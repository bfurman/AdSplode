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
    /**
     * every entity will be drawn in some way, however many different entities will be drawn
     * differently so each one will have to implement it on its own to suit each entities need.
     */
    public void draw(Matrix4 camera);
    /**
     * quick way to tell what the type of the entity is without having to cast it, this will most
     * likely be used to determine which entity in a collision is the ball
     */
    public EntityType type();

    /**
     * called by contact listener, the entity returned for now will be for particle effect stuff,
     * this way you can make explosions, otherwise it will be null
     * @return
     */
    public Entity onContact();
    public String contactDebug();

    /**
     * because you can not change the state of the world while it is stepping adding newly created
     * entities such as explosion has to be done before or after the step/frame, similar event for
     * destruction
     */
    public void finishCreation();
}
