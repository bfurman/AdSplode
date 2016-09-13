package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

import Constants.PhysicsConstants;

public class AdSplode extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Sprite sprite,sprite2;
	Texture img;
	World world;
	Block tester, test2;
	Body body,body2;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Ball orb;
	Matrix4 debugMatrix;
	OrthographicCamera camera;


	final float PIXELS_TO_METERS = PhysicsConstants.PIXELS_TO_METERS;

	final short PHYSICS_ENTITY = PhysicsConstants.PHYSICS_ENTITY;    // 0001
	final short WORLD_ENTITY = PhysicsConstants.WORLD_ENTITY; // 0010 or 0x2 in hex
	float torque = 0.0f;
	boolean drawSprite = true;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("core/textures/badlogic.png");
		System.out.println("creating app");
		// Create two identical sprites slightly offset from each other vertically
		sprite = new Sprite(img);
		sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2 +200);
		sprite2 = new Sprite(img);
		sprite2.setPosition(-sprite.getWidth()/2 + 20,-sprite.getHeight()/2 + 400);

		world = new World(new Vector2(0, -1f),true);

		// Sprite1's Physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
						PIXELS_TO_METERS,
				(sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

		body = world.createBody(bodyDef);


		// Sprite2's physics body
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.DynamicBody;
		bodyDef2.position.set((sprite2.getX() + sprite2.getWidth()/2) /
						PIXELS_TO_METERS,
				(sprite2.getY() + sprite2.getHeight()/2) / PIXELS_TO_METERS);

		body2 = world.createBody(bodyDef2);

		// Both bodies have identical shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
				/2 / PIXELS_TO_METERS);

		// Sprite1
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = .9f;
		fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
		//fixtureDef.filter.maskBits = WORLD_ENTITY;


		// Sprite2
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = shape;
		fixtureDef2.density = 0.1f;
		fixtureDef2.restitution = 1.0f;
		fixtureDef2.filter.categoryBits = PHYSICS_ENTITY;
		//fixtureDef2.filter.maskBits = WORLD_ENTITY;

		body.createFixture(fixtureDef);
		body2.createFixture(fixtureDef2);

		shape.dispose();

		// test factory
		BlockFactory factory = new BlockFactory(world);

		tester = factory.getBlock(2, randomCoordinate());
		test2 = factory.getBlock(1, randomCoordinate());
		orb = new Ball(world, -.1f, .1f);

		createWalls();

		createCollisionListener();
		Gdx.input.setInputProcessor(this);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}

	@Override
	public void render() {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);

		body.applyTorque(torque, true);
		sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
						getWidth()/2 ,
				(body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );


		sprite.setRotation((float)Math.toDegrees(body.getAngle()));
		sprite2.setPosition((body2.getPosition().x * PIXELS_TO_METERS) - sprite2.
						getWidth()/2 ,
				(body2.getPosition().y * PIXELS_TO_METERS) -sprite2.getHeight()/2 );
		sprite2.setRotation((float)Math.toDegrees(body2.getAngle()));

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
				PIXELS_TO_METERS, 0);

		tester.draw(camera.combined);
		test2.draw(camera.combined);
		orb.draw(camera.combined);

		batch.begin();
		if(drawSprite)
			batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
				sprite.getOriginY(),
				sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
						getScaleY(),sprite.getRotation());
		batch.draw(sprite2, sprite2.getX(), sprite2.getY(),sprite2.getOriginX(),
				sprite2.getOriginY(),
				sprite2.getWidth(),sprite2.getHeight(),sprite2.getScaleX(),sprite2.
						getScaleY(),sprite2.getRotation());
		batch.end();

		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose() {
		img.dispose();
		world.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {


		if(keycode == Input.Keys.RIGHT)
			body.setLinearVelocity(1f, 0f);
		if(keycode == Input.Keys.LEFT)
			body.setLinearVelocity(-1f,0f);

		if(keycode == Input.Keys.UP)
			body.applyForceToCenter(0f,10f,true);
		if(keycode == Input.Keys.DOWN)
			body.applyForceToCenter(0f, -10f, true);

		// On brackets ( [ ] ) apply torque, either clock or counterclockwise
		if(keycode == Input.Keys.RIGHT_BRACKET)
			torque += 0.1f;
		if(keycode == Input.Keys.LEFT_BRACKET)
			torque -= 0.1f;

		// Remove the torque using backslash /
		if(keycode == Input.Keys.BACKSLASH)
			torque = 0.0f;

		// If user hits spacebar, reset everything back to normal
		if(keycode == Input.Keys.SPACE|| keycode == Input.Keys.NUM_2) {
			body.setLinearVelocity(0f, 0f);
			body.setAngularVelocity(0f);
			torque = 0f;
			sprite.setPosition(0f,0f);
			body.setTransform(0f,0f,0f);
		}

		if(keycode == Input.Keys.COMMA) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()-0.1f);
		}
		if(keycode == Input.Keys.PERIOD) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()+0.1f);
		}
		if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1)
			drawSprite = !drawSprite;

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	// On touch we apply force from the direction of the users touch.
	// This could result in the object "spinning"
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		body.applyForce(1f,1f,screenX,screenY,true);
		//body.applyTorque(0.4f,true);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void createWalls() {
		// Now the physics body of the bottom edge of the screen
		BodyDef bodyDef3 = new BodyDef();
		bodyDef3.type = BodyDef.BodyType.StaticBody;

		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - 50/PIXELS_TO_METERS;

		bodyDef3.position.set(0,0);

		//bottom edge
		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.filter.categoryBits = WORLD_ENTITY;
		fixtureDef3.filter.maskBits = PHYSICS_ENTITY;

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2,-h/2,w/2,-h/2);
		fixtureDef3.shape = edgeShape;

		//bodyDef3.position.set(0,0);
		//left wall
		FixtureDef fixtureDef4 = new FixtureDef();
		fixtureDef4.filter.categoryBits = WORLD_ENTITY;
		fixtureDef4.filter.maskBits = PHYSICS_ENTITY;


		EdgeShape edgeShape2 = new EdgeShape();
		edgeShape2.set(-w/2,-h/2,-w/2, 10);
		fixtureDef4.shape = edgeShape2;

		bodyEdgeScreen = world.createBody(bodyDef3);
		bodyEdgeScreen.createFixture(fixtureDef3);
		bodyEdgeScreen.createFixture(fixtureDef4);

		edgeShape2 = new EdgeShape();
		edgeShape2.set(w/2,-h/2,w/2, 10);
		fixtureDef4.shape = edgeShape2;
		bodyEdgeScreen.createFixture(fixtureDef4);

		edgeShape2 = new EdgeShape();
		edgeShape2.set(w/2, 10, -w/2, 10);
		fixtureDef4.shape = edgeShape2;
		bodyEdgeScreen.createFixture(fixtureDef4);
		debugRenderer = new Box2DDebugRenderer();


		edgeShape.dispose();
		edgeShape2.dispose();
	}

	public Vector2 randomCoordinate() {
		Random generator = new Random();
		float p1 = generator.nextFloat() * Gdx.graphics.getWidth()/PIXELS_TO_METERS/2;
		float p2 = generator.nextFloat() * Gdx.graphics.getHeight()/PIXELS_TO_METERS/2;
		generator = new Random();
		boolean p1Flip = generator.nextBoolean();
		generator = new Random();
		boolean p2Flip = generator.nextBoolean();
		p1 = p1Flip ? p1 * -1: p1;
		p2 = p2Flip ? p2 * -1: p2;
		return new Vector2(p1, p2);
	}

	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				Body bodyA = fixtureA.getBody();
				Body bodyB = fixtureB.getBody();
				Entity A = (Entity)bodyA.getUserData();
				Entity B = (Entity)bodyB.getUserData();
				if (A != null && B != null) {
					System.out.println("beginContact" + "between " + A.contactDebug() + " and " + B.contactDebug());
				}
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				Body bodyA = fixtureA.getBody();
				Body bodyB = fixtureB.getBody();
				Entity A = (Entity)bodyA.getUserData();
				Entity B = (Entity)bodyB.getUserData();
				if (A != null && B != null) {
					System.out.println("endContact" + "between " + A.contactDebug() + " and " + B.contactDebug());
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

		});
	}
}
