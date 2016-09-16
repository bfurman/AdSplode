package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import Blocks.*;
import Constants.EntityType;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BlockFactory {
    World world;
    BlockFactory(World screen) {
        world = screen;
    }
    //alter from int to enum
    public Block getBlock(EntityType blockId, Vector2 vec2) {
        if(blockId == EntityType.BLOCK) {
            return new BasicBlock(world, vec2.x, vec2.y);
        }
        if (blockId == EntityType.ICEBLOCK) {
            return new IceBlock(world, vec2.x, vec2.y);
        }
        return new LavaBlock(world, vec2.x, vec2.y);
    }
}
