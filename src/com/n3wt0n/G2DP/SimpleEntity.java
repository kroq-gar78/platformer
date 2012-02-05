package com.n3wt0n.G2DP;

import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.shapes.Box;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class SimpleEntity {

	private World world;
	private Body body;

	private float velocity;

	private float rotation; // the rotation of the image. setRotation(0) for upright.

	private SoundWrapper soundWrapper;

	private int width, height;
	private float mass;
	
	private Image image;

	// For the player position on the screen
	private int vx, vy;

	private boolean falling = false, moving = false, onGround = true;

	private String name;

	/**
	 * Create an Entity. Extend it for use as a Lamp, NPC, or Player, etc.
	 * 
	 * @param x
	 *            Initial location in x-plane.
	 * @param y
	 *            Initial location in y-plane.
	 * @param width
	 *            Width to assign the physical body.
	 * @param height
	 *            Height to assign the physical body.
	 * @param mass
	 *            Mass to assign the physical body.
	 * @param name
	 *            Name of the Entity.
	 */
	public SimpleEntity(World world, float x, float y, int width, int height, float mass, String name, SoundWrapper soundWrapper) {
		
		setWorld(world);
		
		this.soundWrapper = soundWrapper;
		
		this.width = width;
		this.height = height;
		this.mass = mass;

		this.name = name;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2, height/2);
		body = new Body(shape,x,y,false);
		//body.setUserData(this);
		body.setRestitution(0f);
		body.setFriction(0f);
		//body.setMaxVelocity(200, 500);
		//body.setRotatable(false);
		body.setWorld(world);
		setXY(x, y);
	}

	/**
	 * Update the variables of the Entity each step.
	 * 
	 * @param delta
	 *            Time passed since last update.
	 * @throws SlickException
	 */
	public abstract void update(int delta) throws SlickException;

	/**
	 * Render the Entity.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param g
	 *            Java Graphics.
	 * @throws SlickException
	 */
	public abstract void render(GameContainer gc, Graphics g) throws SlickException;

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}

	/**
	 * Not yet used. Will eventually check for intersection with the body of
	 * another Entity.
	 * 
	 * @param entity
	 *            The Entity to test the collision with.
	 * @return False.
	 */
	public boolean collidedWith(SimpleEntity entity) {
		return false;
	}

	/**
	 * Set the Phys2D World.
	 * 
	 * @param world
	 *            The Phys2D World.
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * Set the SoundWrapper.
	 * 
	 * @param swrap
	 *            The SoundWrapper.
	 */
	public void setSoundWrapper(SoundWrapper swrap) {
		this.soundWrapper = swrap;
	}

	/**
	 * Apply a force vector to a Phys2D Body. Adjusts Entity's facingRight and
	 * Jumping booleans.
	 * 
	 * @param x
	 *            Force in the x-plane.
	 * @param y
	 *            Force in the y-plane.
	 */
	public void applyForce(float x, float y) {
		body.applyForce(x, y);
	}

	/**
	 * Absolutely set the velocity of the Entity (rather than adjusting the
	 * force).
	 * 
	 * @param x
	 *            The new force in the x-plane.
	 * @param y
	 *            The new force in the y-plane.
	 */
	public void setVelocity(float x, float y) {
		Vec2 vel = new Vec2(body.getVelocity());
		vel = vel.mul(-1);
		//body.setVelocity((vec));
		body.setVelocity(vel.x, vel.y);
		//body.adjustVelocity(new Vector2f(x, y));
	}

	/**
	 * Get the Entity's velocity along the x-plane.
	 * 
	 * @return The X velocity of the Entity.
	 */
	public float getVelX() {
		return body.getVelocity().x;
	}

	/**
	 * Get the Entity's velocity along the y-plane.
	 * 
	 * @return The Y velocity of the Entity.
	 */
	public float getVelY() {
		return body.getVelocity().y;
	}

	/**
	 * Get the Entity's location (in pixels) along the x-plane.
	 * 
	 * @return The X location.
	 */
	public float getX() {
		// return x;
		return body.getX();
	}

	/**
	 * Set the Entity's location along the x-plane.
	 * 
	 * @param x
	 *            Desired location of Entity along x-plane.
	 */
	public void setX(float x) {
		body.setPosition(x, this.getY());
	}

	/**
	 * Get the Entity's location (in pixels) along the y-plane.
	 * 
	 * @return The Y location.
	 */
	public float getY() {
		// return y;
		return body.getY();
	}

	/**
	 * Set the Entity's location along the y-plane.
	 * 
	 * @param y
	 *            Desired location of Entity along y-plane.
	 */
	public void setY(float y) {
		body.setPosition(this.getX(), y);
	}

	/**
	 * Set the location (x,y) of the Entity.
	 * 
	 * @param x
	 *            Location along the x-plane.
	 * @param y
	 *            Location along the y-plane.
	 */
	public void setXY(float x, float y) {
		body.setPosition(x, y);
	}
	
	/**
	 * Set the location (x,y) of the Entity.
	 * 
	 * @param position
	 *            Vector2f containing the location along the x- and y-planes.
	 */
	public void setXY(Vec2 position) {
		body.setPosition(position.x, position.y);
	}
	
	/**
	 * Get the Entity's location (in pixels) along the y-plane.
	 * 
	 * @return The Y location.
	 */
	public Vec2 getPosition() {
		// return y;
		return (Vec2)body.getPosition();
	}
	
	/**
	 * Set the location (x,y) of the Entity.
	 * 
	 * @param position
	 *            Vector2f containing the location along the x- and y-planes.
	 */
	public void setPosition(Vec2 position) {
		this.setXY(position);
	}

	/**
	 * Get the Phys2D Body of the Entity.
	 * 
	 * @return Phys2D Body.
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Get the name of the Entity.
	 * 
	 * @return The Entity name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the Entity.
	 * 
	 * @param name
	 *            Entity name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the moving flag to True or False.
	 * 
	 * @param b
	 *            True or False if the Entity is moving or not.
	 */
	public void setMoving(boolean b) {
		moving = b;
	}

	/**
	 * Check to see if the Entity is moving.
	 * 
	 * @return True if moving. False if resting.
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * Set the onGround flag to True or False.
	 * 
	 * @param b
	 *            True if Entity is on the ground. False if jumping or falling.
	 */
	public void setOnGround(boolean b) {
		onGround = b;
	}

	/**
	 * Check to see if Entity is on the ground.
	 * 
	 * @return True if touching the ground.
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Set the falling flag to True or False.
	 * 
	 * @param b
	 *            True if falling.
	 */
	public void setFalling(boolean b) {
		falling = b;
	}

	/**
	 * Check to see if the Entity is falling.
	 * 
	 * @return True if falling. False if on the ground.
	 */
	public boolean isFalling() {
		return falling;
	}

	/**
	 * Set the visual position of the Entity within the GameContainer.
	 * 
	 * @param x
	 *            Location along x-plane.
	 * @param y
	 *            Location along y-plane.
	 */
	public void setVisualLocation(float x, float y) {
		this.vx = (int) x;
		this.vy = (int) y;
	}

	/**
	 * Set the visual position of the Entity along the x-plane.
	 * 
	 * @param x
	 *            Location along x-plane.
	 */
	public void setVisualX(float x) {
		this.vx = (int) x;
	}

	/**
	 * Set the visual position of the Entity along the y-plane.
	 * 
	 * @param y
	 *            Location along y-plane.
	 */
	public void setVisualY(float y) {
		this.vy = (int) y;
	}

	/**
	 * Get the visual position of the Entity in the x-plane.
	 * 
	 * @return X location of Entity on the screen.
	 */
	public float getVisualX() {
		return vx;
	}

	/**
	 * Get the visual position of the Entity in the y-plane.
	 * 
	 * @return Y location of Entity on the screen.
	 */
	public float getVisualY() {
		return vy;
	}

	/**
	 * Get the Entity Image.
	 * 
	 * @return Entity Image.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Set the Entity Image.
	 * 
	 * @param image
	 *            Image to set.
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Get the Entity width.
	 * 
	 * @return Entity width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the Entity width.
	 * 
	 * @param width
	 *            Width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the Entity height.
	 * 
	 * @return Entity height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the Entity height.
	 * 
	 * @param height
	 *            Heigh to set.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the Entity mass.
	 * 
	 * @return Entity mass.
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Set the Entity mass.
	 * 
	 * @param mass
	 *            Mass to set.
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * Get the velocity of the Entity.
	 * 
	 * @return Entity velocity.
	 */
	public float getVelocity() {
		return this.velocity;
	}

	/**
	 * Set the Entity velocity.
	 * 
	 * @param velocity Velocity to set.
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * Helper method. Determines if the Entity is touching the ground.
	 * 
	 * @param body Phys2D Body of Entity.
	 * @return True if body is touching ground.
	 */
	protected boolean onGroundImpl(Body body) {
		if (world == null) {
			return false;
		}
		// System.out.println ("Testing if onGroundImpl");

		// loop through the collision events that have occurred in the
		// world
		Contact contacts = world.getJBoxWorld().getContactList();
		while( contacts != null )
		{
			contacts = contacts.getNext();
			// if the point of collision was below the centre of the actor
			// i.e. near the feet
			if (contacts.getManifold().localPoint.y > getY() + (height / 4)) {
				// check the normal to work out which body we care about
				// if the right body is involved and a collision has happened
				// below it then we're on the ground
				if (contacts.getManifold().localNormal.y < -0.5) {
					if (contacts.getFixtureB() == body.getFixture()) {
						// System.out.println(events[i].getPoint()+","+events[i].getNormal());
						return true;
					}
				}
				if (contacts.getManifold().localNormal.y > 0.5) {
					if (contacts.getFixtureB() == body.getFixture()) {
						// System.out.println(events[i].getPoint()+","+events[i].getNormal());
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public World getWorld() { return this.world; }

	/**
	 * Have the SoundWrapper play a sound.
	 * 
	 * @param sound The location of the sound to play.
	 * @throws SlickException
	 */
	public void playSound(String sound) throws SlickException {
		// System.out.println ("Parent playSound");
		soundWrapper.playSound(sound);
	}
	
	public SoundWrapper getSoundWrapper() { return this.soundWrapper; }

	/**
	 * Flip the Entity Image horizontally.
	 */
	public void flipBaseImage() {
		image = image.getFlippedCopy(true, false);
	}

}
