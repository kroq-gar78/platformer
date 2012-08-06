package org.newdawn.fizzy;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 * A single body in the world. A body holds a shape which collide with
 * the rest of the world. It also holds properties about the shapes once
 * they have been created
 * 
 * @author kglass | kroq-gar78
 */
public class Body {
	
	/** The default density applied to shapes if none is specified (25.0f) */
	public static final float DEFAULT_DENSITY = 25.0f;
	/** The default restitution applied to shapes if none is specified (0.9f) */
	public static final float DEFAULT_RESTITUTION = 0.9f;
	/** The default friction applied to shapes if none is specified (0.1f) */
	public static final float DEFAULT_FRICTION = 0.1f;
	
	/** The body held by JBox2D */
	private org.jbox2d.dynamics.Body jboxBody;
	/** The body definition held by JBox2D */
	private BodyDef bd;
	/** True if this should be a static body */
	private boolean staticBody;
	/** The list of bodies this body is touching */
	private ArrayList<Body> touching = new ArrayList<Body>();
	/** The Fixture defining all of the material properties of the body */
	//private Fixture fixture;
	/** The definition of the Fixture defining all of the material properties of the body */
	//private FixtureDef fd;
	/** The list of containers of Fixture(Def)s and Shapes */
	private ArrayList<FixtureShape> fixtureShapes = new ArrayList<FixtureShape>();
	/** The userdata assigned to this body if any */
	private Object userData;
	/** A World the object is in */
	private World world;
	
	/**
	 * Create a new body
	 * 
	 * @param shape The shape the body should have
	 * @param x The x axis location of the body
	 * @param y The y axis location of the body
	 */
	public Body(org.jbox2d.collision.shapes.Shape shape, float x, float y) {
		this(shape,x,y,false);
	}

	/**
	 * Create a new body
	 * last
	 * @param shape The shape the body should have
	 * @param x The x axis location of the body
	 * @param y The y axis location of the body
	 * @param staticBody True if this body should be static
	 */
	public Body(org.jbox2d.collision.shapes.Shape shape, float x, float y, boolean staticBody) {
		bd = new BodyDef();
		bd.position = new Vec2(x,y);
		bd.type = (staticBody?BodyType.STATIC:BodyType.DYNAMIC);
		this.staticBody = staticBody;
		FixtureShape fixShape = new FixtureShape();
		fixShape.fd = new FixtureDef();
		fixShape.fd.shape = shape;
		fixShape.fd.density = DEFAULT_DENSITY;
		fixShape.fd.friction = DEFAULT_FRICTION;
		fixShape.fd.restitution = DEFAULT_RESTITUTION;
	}
	
	/**
	 * Check if this body was declared as static
	 * 
	 * @return True if this body was declared as static
	 */
	public boolean isStatic() {
		return staticBody;
	}
	
	/**
	 * Get the user data assigned to this body if any
	 * 
	 * @return Get the user data assigned to this body (or null if none is defined);
	 */
	public Object getUserData() {
		return userData;
	}
	
	/**
	 * Set the user data assigned to this body
	 * 
	 * @param object The user data to be assigned to this body
	 */
	public void setUserData(Object object) {
		this.userData = object;
	}
	
	/**
	 * Check if this body is touching another
	 * 
	 * @param other The other body to check against 
	 * @return True if the bodies are touching
	 */
	public boolean isTouching(Body other) {
		return touching.contains(other);
	}
	
	/**
	 * Check how many contact points there are between two bodies
	 * 
	 * @param other The other body to check against
	 * @return The number of contact points
	 */
	public int touchCount(Body other) {
		int count = 0;
		
		for (int i=0;i<touching.size();i++) {
			if (touching.get(i) == other) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Indicate that this body touches another at a *new* contact 
	 * point.
	 * 
	 * @param other The other body that is touched
	 */
	void touch(Body other) {
		touching.add(other);
	}
	
	/**
	 * Indicate that one contact point between the bodies has been removed
	 * 
	 * @param other The other body that is no longer touched by a particular 
	 * contact point.
	 */
	void untouch(Body other) {
		touching.remove(other);
	}
	
	/**
	 * Apply force to the body
	 * 
	 * @param x The amount of force on the X axis
	 * @param y The amount of force on the Y axis
	 */
	public void applyForce(float x, float y) {
		checkBody();
		jboxBody.applyForce(new Vec2(x,y), new Vec2(0,0));
	}
	
	/**
	 * Get the X position of the body
	 * 
	 * @return The x position of the body
	 */
	public float getX() {
		checkBody();
		return jboxBody.getPosition().x;		
	}

	
	/**
	 * Get the Y position of the body
	 * 
	 * @return The y position of the body
	 */
	public float getY() {
		checkBody();
		return jboxBody.getPosition().y;		
	}
	
	public Vec2 getPosition()
	{
		checkBody();
		return jboxBody.getPosition();
	}
	
	/**
	 * Get the rotation of the body
	 * 
	 * @return The rotation of the body
	 */
	public float getRotation() {
		checkBody();
		return jboxBody.getAngle();
	}

	/**
	 * Get the X velocity of the body
	 * 
	 * @return The x velocity of the body
	 */
	public float getXVelocity() {
		checkBody();
		return jboxBody.getLinearVelocity().x;		
	}

	
	/**
	 * Get the Y velocity of the body
	 * 
	 * @return The y velocity of the body
	 */
	public float getYVelocity() {
		checkBody();
		return jboxBody.getLinearVelocity().y;		
	}
	
	public Vec2 getVelocity()
	{
		return jboxBody.getLinearVelocity();
	}
	
	/**
	 * Get the angular velocity of the body
	 * 
	 * @return The angular velocity of the body
	 */
	public float getAngularVelocity() {
		checkBody();
		return jboxBody.getAngularVelocity();
	}
	
	/*public void setMass(float mass)
	{
		if(fixture==null) fixture.
	}*/
	
	/**
	 * Set the restitution applied when this body collides
	 * 
	 * @param rest The restitution applied when this body collides
	 */
	public void setRestitution(float rest) {
		if(fixture == null) fd.restitution = rest;
		else fixture.m_restitution = rest;
	}

	/**
	 * Set the friction applied when this body collides
	 * 
	 * @param f The friction applied when this body collides
	 */
	public void setFriction(float f)
	{
		if(fixture == null) fd.restitution = f;
		else fixture.m_friction = f;
	}
	
	/**
	 * Set the density of this body
	 * 
	 * @param den The density of this body
	 */
	public void setDensity(float den)
	{
		if(fixture==null) fd.density = den;
		else fixture.m_density = den;
	}
	
	public void setShape(Shape newShape)
	{
		if(fixture==null) fd.shape=newShape;
		else fixture.m_shape=newShape;
	}
	
	/* // append a '*' to the previous '*' before this to restore this comment block to a Javadoc thing
	 * True if this body has reached the edge of the world bounds and hence
	 * is frozen in space.
	 * 
	 * @return True if this body has reached the edge of the world bounds.
	 */
	/*public boolean isOutOfBounds() {
		checkBody();
		return jboxBody.isFrozen();
	}*/
	
	/**
	 * Notification that this body is being added to the world
	 * 
	 * @param world The world this body is being added to
	 */
	void addToWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
				
		jboxBody = jboxWorld.createBody(bd);
		//shape.createInBody(this);
		fixture = jboxBody.createFixture(fd);
		
		if (!staticBody) {
			jboxBody.resetMassData(); // set Mass to the total of all of its fixtures
			//MassData md = new MassData();
			//jboxBody.getMass();
			System.out.println("JBox2D body mass: " + jboxBody.getMass());
			System.out.println("JBox2D fixture density: " + fixture.getDensity());
		} else {
			jboxBody.m_type = org.jbox2d.dynamics.BodyType.STATIC;
		}
	}

	/**
	 * Notification that this body is being removed from the world
	 * key
	 * @param world The world this body is being removed from
	 */
	void removeFromWorld(World world) {
		org.jbox2d.dynamics.World jboxWorld = world.getJBoxWorld();
		jboxWorld.destroyBody(jboxBody);
	}

	public void setWorld(World world)
	{
		if(this.world!=null)removeFromWorld(this.world); // remove from old world before adding to new world
		this.world = world;
		addToWorld(world);
	}
	
	public World getWorld() { return this.world; }
	
	/**
	 * Get the JBox2D body that is wrapped by this class
	 * 
	 * @return The body that is wrapped by this proxy class
	 */
	public org.jbox2d.dynamics.Body getJBoxBody() {
		return jboxBody;
	}
	
	/**
	 * Get the Fizzy shape representing this body
	 * 
	 * @return The fizzy shape representing this body
	 */
	public org.jbox2d.collision.shapes.Shape getShape() {
		return shape;
	}
	
	/**
	 * Get the JBox2D Fixture containing the physical properties of this body
	 * 
	 * @return The JBox2D Fixture containing the physical properties of this body
	 */
	public Fixture getFixture() { return this.fixture; }
	
	/**
	 * Set the position of the body. This can only be called after the body has been added
	 * to the world.
	 * 
	 * @param x The new x coordinate of the body
	 * @param y The new y coordinate of the body
	 */
	public void setPosition(float x, float y) {
		//checkBody();
		if(jboxBody == null) bd.position = new Vec2(x,y);
		else jboxBody.setTransform(new Vec2(x,y), jboxBody.getAngle());
	}
	
	/**
	 * Set the rotation of the body. This can only be called after the body has been added
	 * to the world.
	 * 
	 * @param rotation The new rotation of the body
	 */
	public void setRotation(float rotation) {
		//checkBody();
		if(jboxBody==null) bd.angle = rotation;
		else jboxBody.setTransform(jboxBody.getPosition(), rotation);
	}
	
	/**
	 * Check the body has been added to the world 
	 */
	private void checkBody() {
		if (jboxBody == null) {
			throw new RuntimeException("This method requires that the body has been added to the world first");
		}
	}
	
	/**
	 * Check if this body is "awake", i.e. it's moving any more. A sleeping body takes fewer CPU cycles.
	 * 
	 * @return True if this body is awake
	 */
	public boolean isAwake() {
		checkBody();
		return jboxBody.isAwake();
	}

	/**
	 * @deprecated Use isAwake() function instead.
	 * Check if this body is "sleeping", i.e. it's not moving any more
	 * 
	 * @return True if this body is sleeping
	 */
	public boolean isSleeping() {
		checkBody();
		return !jboxBody.isAwake();
	}

	/**
	 * Translate this body by the given amount
	 * 
	 * @param x The amount to move the body on the x axis
	 * @param y The amount to move the body on the y axis
	 */
	public void translate(float x, float y) {
		setPosition(getX()+x, getY()+y);
	}

	/**
	 * Set the linear damping to apply to this body. Higher 
	 * value slows the body acceleration. Maximum is 1.0
	 * 
	 * @param damping The amount to dampen the movement by
	 */
	public void setDamping(float damping) {
		if (jboxBody == null) bd.linearDamping = damping;
		else jboxBody.m_linearDamping = damping;
	}

	/**
	 * Set the linear velocity of this body
	 * 
	 * @param xVelocity The x component of the velocity
	 * @param yVelocity The y component of the velocity
	 */
	public void setVelocity(float xVelocity, float yVelocity) {
		checkBody();
		Vec2 vel = jboxBody.getLinearVelocity();
		vel.x = xVelocity;
		vel.y = yVelocity;
		jboxBody.setLinearVelocity(vel);
	}

	/**
	 * Set the angular velocity (the speed at which it rotates)
	 * 
	 * @param vel The angular velocity to apply
	 */
	public void setAngularVelocity(float vel) {
		checkBody();
		jboxBody.setAngularVelocity(vel);
	}
}
