package com.mygdx.game;

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

import Behaviors.ExplosionBehavior;
import Constants.EntityType;
import Constants.PhysicsConstants;
import Constants.Utilities;

/**
 * Created by Bradley on 9/10/2016.
 */
//so instead of an interface make this an abstract calss that implements entity
    //this way we dont repeat code for stuff like draw getwidth ext. since we cant make default
    //implementation for interfaces which is a java 8 feature
public abstract class Block implements Entity {
    Sprite sprite;
    SpriteBatch batch;
    World world;
    Body body;
    EntityStrategy behavior;
    boolean destroy = false;

    public Block() {

    }

    public Block(World scene, float xPos, float yPos, String texture) {
        setSprite(new Sprite(new Texture(texture)));
        setBatch(new SpriteBatch());

        BodyDef bodyDef4 = createBodyDef();
        bodyDef4.position.set(xPos, yPos);

        setBody(scene.createBody(bodyDef4));

        FixtureDef fixtureDef5 = createFixtureDef();
        setMaskBits(fixtureDef5);

        PolygonShape blocker = new PolygonShape();
        blocker.setAsBox(getSprite().getWidth()/2 / PIXELS_TO_METERS, getSprite().getHeight()
                /2 / PIXELS_TO_METERS);
        fixtureDef5.shape = blocker;

        getBody().createFixture(fixtureDef5);
        getBody().setUserData(this);
        blocker.dispose();
    }


    public void draw(Matrix4 camera) {
        //set rotation will probably be removed since it is a static entity, but kept it in for now,
        // we may want to make a block in the future that rotates to make even more weird shots,
        //actually that sounds cool, an industructable block that is static but rotates so it can launch,
        // the ball at new angles but isnt always beneficial
        getSprite().setRotation((float)Math.toDegrees(getBody().getAngle()));
        Vector2 position = Utilities.spritePositionCalc(getBody(), getSprite());
        getSprite().setPosition(position.x, position.y);

        getBatch().setProjectionMatrix(camera);
        //when drawing anything either sprite or shaperenderer it has to have the begin and end
        //also you have to end a drawer before starting a different one
        getBatch().begin();
        getBatch().draw(getSprite(), getSprite().getX(), getSprite().getY(),getSprite().getOriginX(),
                getSprite().getOriginY(),
                getSprite().getWidth(),getSprite().getHeight(),getSprite().getScaleX(),getSprite().
                        getScaleY(),getSprite().getRotation());
        getBatch().end();
    }

    public EntityType type() {
        return EntityType.BLOCK;
    }

    public Entity onContact() {
        Entity toRet = getBehavior().effect(getBody().getPosition().x, getBody().getPosition().y);
        markAsDestroyed(true);
        return toRet;
    }

    public String contactDebug() {
        return "Block";
    }

    public boolean destroy() {
        if(destroy) {
            world.destroyBody(body);
            return true;
        }
        return false;
    }

    public void finishCreation() {

    }

    public void trigger() {

    }
    public float getWidth() {
        return getSprite().getWidth();
    }
    public float getHeight() {
        return getSprite().getHeight();
    }

    protected Sprite getSprite() {
        return sprite;
    }

    protected SpriteBatch getBatch() {
        return batch;
    }

    protected Body getBody() {
        return body;
    }

    protected EntityStrategy getBehavior() {
        return behavior;
    }

    protected void setBody(Body b) {
        body = b;
    }

    protected void setSprite(Sprite s) {
        sprite = s;
    }

    protected void setBatch(SpriteBatch sb) {
        batch = sb;
    }

    protected void setBehavior(EntityStrategy strategy) {
        behavior = strategy;
    }

    protected void markAsDestroyed(boolean hit) {
        destroy = hit;
    }

    /**
     * Override to mark what type of entity this block interacts with and how it responds
     * @param fixture
     */
    protected void setMaskBits(FixtureDef fixture) {
        fixture.filter.maskBits =  PHYSICS_ENTITY | BLOCK_ENTITY;
    }

    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef5=  new FixtureDef();
        fixtureDef5.filter.categoryBits = BLOCK_ENTITY;
        return fixtureDef5;
    }

    protected BodyDef createBodyDef() {
        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;
        return bodyDef4;
    }
}
