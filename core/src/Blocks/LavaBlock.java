package Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Block;
import com.mygdx.game.Entity;
import com.mygdx.game.EntityStrategy;

import Behaviors.ExplosionBehavior;
import Constants.EntityType;
import Constants.Utilities;

/**
 * On ball collision will cause a n explosion destroying surrounding blocks in a set radius
 */
public class LavaBlock extends Block {
    public LavaBlock(World scene, float xPos, float yPos) {
        super(scene, xPos, yPos, "core/textures/lavafull.bmp");
        setBehavior(new ExplosionBehavior());
    }

    //trigger will be for functionality on ball colliding with block
    @Override
    public void trigger() {

    }


    @Override
    public EntityType type() {
        return EntityType.LAVABLOCK;
    }

    @Override
    public String contactDebug() {
        return "LavaBlock";
    }

    @Override
    public void finishCreation() {

    }
}
