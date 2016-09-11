package com.mygdx.game;

/**
 * Created by Bradley on 9/10/2016.
 */
public class BlockFactory {
    //alter from int to enum
    public Block getBlock(int blockId) {
        return new LavaBlock();
    }
}
