package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Bradley on 9/10/2016.
 */
public interface Block {
    final float PIXELS_TO_METERS = 100f;

    public void draw(Body body, Matrix4 camera);
    public void trigger();
    public float getWidth();
    public float getHeight();

}
