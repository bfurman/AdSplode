package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;

import Constants.PhysicsConstants;

/**
 * Created by Bradley on 9/10/2016.
 */
public interface Block extends Entity{
    public void draw(Matrix4 camera);
    public void trigger();
    public float getWidth();
    public float getHeight();

}
