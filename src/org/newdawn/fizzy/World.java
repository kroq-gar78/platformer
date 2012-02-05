package org.newdawn.fizzy;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
//import org.jbox2d.dynamics.contacts.ContactPoint;
//import org.jbox2d.dynamics.contacts.ContactResult;

/**
 * The central object of the simulation. The world contains the bodies (and joints) which
 * model the world and react to the physics engine.
 * 
 * @author kevin
 */
public class World {
	/** The default gravity applied if none is specified (-10) */
	public static final Vec2 DEFAULT_GRAVITY = new Vec2(0f,-10f);
	/** The default number of iteration used in the integration if none specified (10) */
	public static final int DEFAULT_ITERATIONS = 10;
	
	/** The JBox2D world this World object is wrapping */
	private org.jbox2d.dynamics.World jboxWorld;
	/** The list of bodies added to the world */
	private ArrayList<Body> bodies = new ArrayList<Body>();
	/** A map from shapes that will be reported from collision to the bodies that own them */
	private HashMap<Fixture, Body> fixtureMap = new HashMap<Fixture, Body>();
	/** The list of listeners to be notified of collision events */
	private ArrayList<WorldListener> listeners = new ArrayList<WorldListener>();
	/** The number of iterations to integrate over */
	private int iterations;
	
	/**
	 * Create a new world simulation
	 * 
	 * @param iterations The number of iterations to apply integration across, higher number
	 * becomes more accurate but slower.
	 */
	public World(int iterations) {
		this(DEFAULT_GRAVITY, iterations);
	}
	
	/**
	 * Create a new world simulation
	 * 
	 * @param iterations The number of iterations to apply integration across, higher number
	 * becomes more accurate but slower.
	 */
	public World(Vec2 grav) {
		this(grav, DEFAULT_ITERATIONS);
	}
	
	/**
	 * Create a new world simulation with default settings
	 */
	public World() {
		this(DEFAULT_ITERATIONS);
	}
	
	/**
	 * Create a new world simulation
	 * 
	 * @param x1 The left bound of the physics world
	 * @param y1 The top bound of the physics world
	 * @param x2 The right bound of the physics world
	 * @param y2 The bottom bound of the physics world
	 * @param g The gravity to apply
	 * @param iterations The number of iterations to integrate over
	 */
	public World(Vec2 gravity, int iterations) {
		this.iterations = iterations;

		//Vec2 gravity = new Vec2(0.0f, g);
		boolean doSleep = true;
		jboxWorld = new org.jbox2d.dynamics.World(gravity, doSleep);
		jboxWorld.setContactListener(new ProxyContactListener());		
	}
	
	/**
	 * Get the JBox2D world that is being wrapped
	 * 
	 * @return The JBox2D world that is being wrapped
	 */
	public org.jbox2d.dynamics.World getJBoxWorld() {
		return jboxWorld;
	}
	
	/**
	 * Add a body to the world
	 * 
	 * @param body The body to be added to the world
	 */
	public void add(Body body) {
		body.addToWorld(this);
		fixtureMap.put(body.getFixture(), body);
		bodies.add(body);
	}

	/**
	 * Remove a body from the world
	 * 
	 * @param body The body to be removed from the world
	 */
	public void remove(Body body) {
		fixtureMap.remove(body.getFixture());
		body.removeFromWorld(this);
		bodies.remove(body);
	}
	
	/**
	 * Get the number of bodies in the world
	 * 
	 * @return The number of bodies in the world
	 */
	public int getBodyCount() {
		return bodies.size();
	}
	
	/**
	 * Get a body at a particular index in the list of bodies
	 * 
	 * @param index The index of the body to retrieve
	 * @return The body at the given index
	 */
	public Body getBody(int index) {
		return bodies.get(index);
	}
	
	/**
	 * Update the world
	 * 
	 * @param timeStep The amount of time to simulate
 	 */
	public void update(float timeStep) {
		jboxWorld.setContinuousPhysics(true);
		//jboxWorld.setPositionCorrection(true); // this is already set in the "positionIterationCount" field in World's constructor
		jboxWorld.setWarmStarting(true);
		
		jboxWorld.step(timeStep, iterations,iterations);
	}
	
	/**
	 * Add a listener to be notified of collisions
	 * 
	 * @param listener The listener to be notified of collisions
	 */
	public void addListener(WorldListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener that will no longer receive events
	 * 
	 * @param listener The listener to be removed
	 */
	public void removeListener(WorldListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fire a notification to all listeners that a collision has occured
	 * 
	 * @param bodyA The first body in the collision
	 * @param bodyB The second body in the collision
	 */
	private void fireCollision(Body bodyA, Body bodyB) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB);
		for (int i=0;i<listeners.size();i++) {
			listeners.get(i).collided(event);
		}
	}

	/**
	 * Fire a notification to all listeners that a separation has occured
	 * 
	 * @param bodyA The first body in the separation
	 * @param bodyB The second body in the separation
	 */
	private void fireSeparated(Body bodyA, Body bodyB) {
		CollisionEvent event = new CollisionEvent(bodyA, bodyB);
		for (int i=0;i<listeners.size();i++) {
			listeners.get(i).separated(event);
		}	
	}
	
	/**
	 * A contact listener to collect effects and proxy them on to 
	 * world listeners
	 * 
	 * @author kevin | kroq-gar78
	 */
	private class ProxyContactListener implements ContactListener
	{

		@Override
		public void beginContact(Contact contact)
		{
			Body bodyA = fixtureMap.get(contact.m_fixtureA);
			Body bodyB = fixtureMap.get(contact.m_fixtureB);
			
			if ((bodyA != null) && (bodyB != null)) {
				bodyA.touch(bodyB);
				bodyB.touch(bodyA);
			}
		}

		@Override
		public void endContact(Contact contact)
		{
			Body bodyA = fixtureMap.get(contact.m_fixtureA);
			Body bodyB = fixtureMap.get(contact.m_fixtureB);
			
			if( bodyA != null && bodyB != null)
			{
				if( bodyA.touchCount(bodyB) == 1 )
				{
					fireCollision(bodyA, bodyB);
				}
			}
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold)
		{
			Body bodyA = fixtureMap.get(contact.m_fixtureA);
			Body bodyB = fixtureMap.get(contact.m_fixtureB);
			
			if ((bodyA != null) && (bodyB != null)) {
				bodyA.untouch(bodyB);
				bodyB.untouch(bodyA);
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse)
		{
			Body bodyA = fixtureMap.get(contact.m_fixtureA);
			Body bodyB = fixtureMap.get(contact.m_fixtureB);
			
			if( bodyA != null && bodyB != null)
			{
				if( bodyA.touchCount(bodyB) == 1 )
				{
					fireSeparated(bodyA, bodyB);
				}
			}
		}
		
	}
}
