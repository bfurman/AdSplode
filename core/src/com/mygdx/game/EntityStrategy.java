package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Bradley on 9/14/2016.
 */
public interface EntityStrategy {
    public Entity effect(World world, float x, float y);
}
