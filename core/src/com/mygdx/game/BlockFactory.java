package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BlockFactory {
    World world;
    BlockFactory(World screen) {
        world = screen;
    }
    //alter from int to enum
    public Block getBlock(int blockId) {
        return new LavaBlock(world);
    }
}
