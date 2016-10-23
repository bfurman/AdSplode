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

import java.util.ArrayList;
import java.util.Random;

import Constants.EntityType;
import Constants.PhysicsConstants;

public class AdSplode extends ApplicationAdapter implements InputProcessor {
	Texture img;
	public static World world; //refactor for world becoming a global variable
	Block tester, test2, testIce;
	Body body, body2;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Ball orb;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	ArrayList<Entity> particleEntities;
	ArrayList<Block> blocks;
	int particleListSize = 0;

	final float PIXELS_TO_METERS = PhysicsConstants.PIXELS_TO_METERS;

	final short PHYSICS_ENTITY = PhysicsConstants.PHYSICS_ENTITY;    // 0001
	final short WORLD_ENTITY = PhysicsConstants.WORLD_ENTITY; // 0010 or 0x2 in hex
	float torque = 0.0f;
	boolean drawSprite = true;

	@Override
	public void create() {
		img = new Texture("core/textures/badlogic.png");
		System.out.println("creating app");

		world = new World(new Vector2(0, -1f),true);


		// test factory
		BlockFactory factory = new BlockFactory(world);
		Vector2 testIceFall = randomCoordinate();
		tester = factory.getBlock(EntityType.LAVABALL, testIceFall);
		test2 = factory.getBlock(EntityType.BLOCK, randomCoordinate());
		testIceFall.y+=.2f;
		testIce = factory.getBlock(EntityType.ICEBLOCK, testIceFall);
		orb = new Ball(world, -.1f, .1f);
		particleEntities = new ArrayList<Entity>();
		blocks = new ArrayList<Block>();
		blocks.add(tester);
		blocks.add(test2);
		blocks.add(testIce);
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

		//after world step check on all entities need to finish creation, and then mark as destroy
		//and then update particle list size, world gets locked in middle of step which causes issues
		//not using java 8 cause i need to update ide
		if (particleListSize < particleEntities.size()) {
			for (Entity item: particleEntities) {
				item.finishCreation();
			}
		}
		//check particle entities also
		for (Entity toDestory: particleEntities) {
			//check for destruction, expand to all entities not just the particle entities
		}

		//check blocks for destory
		for (int index = 0; index < blocks.size(); index++) {
			boolean wasDestroyed = blocks.get(index).destroy();
			if (wasDestroyed) {
				blocks.remove(index);
			}
		}

		particleListSize = particleEntities.size();
		orb.applyTorque(torque, true);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (Entity particle: particleEntities) {
			particle.draw(camera.combined);
		}
//		tester.draw(camera.combined);
//		test2.draw(camera.combined);
//		testIce.draw(camera.combined);
		for (Block block: blocks) {
			block.draw(camera.combined);
		}

		orb.draw(camera.combined);
		debugMatrix = camera.combined.scale(PIXELS_TO_METERS,
				PIXELS_TO_METERS, 0);
		//particleEntities.forEach(i -> i.draw(camera.combined));

		debugRenderer.render(world, debugMatrix); //draws boundaries around physic bodies

		particleListSize = particleEntities.size();
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
	// the key press is only for debugging it controls to ball to force collisions for now
	@Override
	public boolean keyUp(int keycode) {


		if(keycode == Input.Keys.RIGHT)
			orb.setLinearVelocity(1f, 0f);
		if(keycode == Input.Keys.LEFT)
			orb.setLinearVelocity(-1f,0f);

		if(keycode == Input.Keys.UP)
			orb.applyForceToCenter(0f,10f,true);
		if(keycode == Input.Keys.DOWN)
			orb.applyForceToCenter(0f, -10f, true);

		// On brackets ( [ ] ) apply torque, either clock or counterclockwise
		if(keycode == Input.Keys.RIGHT_BRACKET)
			torque += 0.1f;
		if(keycode == Input.Keys.LEFT_BRACKET)
			torque -= 0.1f;

		// Remove the torque using backslash /
		if(keycode == Input.Keys.BACKSLASH)
			torque = 0.0f;

		// If user hits spacebar, reset everything back to normal
		/*if(keycode == Input.Keys.SPACE|| keycode == Input.Keys.NUM_2) {
			body.setLinearVelocity(0f, 0f);
			body.setAngularVelocity(0f);
			torque = 0f;
			sprite.setPosition(0f,0f);
			body.setTransform(0f,0f,0f);
		}*/

		//code below will be useful for a different instance later on(change elasticity of ball powerup)
		/*if(keycode == Input.Keys.COMMA) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()-0.1f);
		}
		if(keycode == Input.Keys.PERIOD) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()+0.1f);
		}*/
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
		//body.applyForce(1f,1f,screenX,screenY,true);
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
	//create the walls
	public void createWalls() {
		// Now the physics body of the bottom edge of the screen
		BodyDef bodyDef3 = new BodyDef();
		bodyDef3.type = BodyDef.BodyType.StaticBody;

		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - 50/PIXELS_TO_METERS;

		bodyDef3.position.set(0,0);

		//bottom edge
		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.filter.categoryBits = PhysicsConstants.WALL_ENTITY;
		//fixtureDef3.filter.maskBits = PHYSICS_ENTITY;

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2,-h/2,w/2,-h/2);
		fixtureDef3.shape = edgeShape;

		//bodyDef3.position.set(0,0);
		//left wall
		FixtureDef fixtureDef4 = new FixtureDef();
		fixtureDef4.filter.categoryBits = PhysicsConstants.WALL_ENTITY;
		//fixtureDef4.filter.maskBits = PHYSICS_ENTITY;


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
	// collisionlistener is used by the world to get details about 2 entities contacting
	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				//find more elegant way so that if they are in the same category allow collision but ignore contact
				if (fixtureA.getFilterData().categoryBits != fixtureB.getFilterData().categoryBits) {
					Body bodyA = fixtureA.getBody();
					Body bodyB = fixtureB.getBody();
					Entity A = (Entity) bodyA.getUserData();
					Entity B = (Entity) bodyB.getUserData();
					if (A != null && B != null) {
						System.out.println("beginContact" + "between " + A.contactDebug() + " and " + B.contactDebug());
						Entity effectA = A.onContact();
						Entity effectB = B.onContact();
						if (effectA != null) {
							particleEntities.add(effectA);
						}
						if (effectB != null) {
							particleEntities.add(effectB);
						}
					}
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
