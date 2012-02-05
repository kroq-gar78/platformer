package com.n3wt0n.G2DP;

import net.phys2d.math.Vector2f;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class Entity extends SimpleEntity {
	
	private float jumpPower;

	private static final int MAX_JUMP_VEL = 20000;
	/** The amount of time the actor has been considered to be off the ground */
	private int offGroundTimer = 0;

	private boolean jumping = false, facingRight = false;
	private int maximumJumps = 2;
	private int jumpsTaken=0;
	private float velx;

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
	public Entity(World world, float x, float y, int width, int height, float mass,	String name, SoundWrapper soundWrapper) {
		super(world, x, y, width, height, mass, name, soundWrapper);
		setXY(x, y);
	}
	
	/**
	 * Create an Entity. Extend it for use as a Lamp, NPC, or Player, etc.
	 * 
	 * @param pos
	 *            Initial location in both planes (x and y)
	 * @param width
	 *            Width to assign the physical body.
	 * @param height
	 *            Height to assign the physical body.
	 * @param mass
	 *            Mass to assign the physical body.
	 * @param name
	 *            Name of the Entity.
	 */
	public Entity(World world, Vector2f pos, int width, int height, float mass,	String name, SoundWrapper soundWrapper) {
		super(world, pos.x, pos.y, width, height, mass, name, soundWrapper);
		setXY(pos.x, pos.y);
	}

	/**
	 * Update the variables of the Entity each step.
	 * 
	 * @param delta
	 *            Time passed since last update.
	 * @throws SlickException
	 */
	public void update(int delta) throws SlickException {
		// update the flag for the actor being on the ground. The
		// physics engine will cause constant tiny bounces as the
		// the body tries to settle - so don't consider the body
		// to have left the ground until it's done so for some time
		boolean on = onGroundImpl(getBody());
		if (!on) {
			offGroundTimer += delta;
			if (offGroundTimer > 100) {
				setOnGround(false);
			}
		} else {
			offGroundTimer = 0;
			setOnGround(true);
			jumpsTaken = 0;
			jumping = false;
			//System.out.print(jumpsTaken);
		}

		// if we've been pushed back from a collision horizontally
		// then kill the velocity - don't want to keep pushing during
		// this frame
		if ((getVelX() > 0) && (!facingRight)) {
			velx = 0;
		}
		if ((getVelX() < 0) && (facingRight)) {
			velx = 0;
		}

		// keep velocity constant throughout the updates
		setVelocity(velx, getVelY());
		// if we're standing on the ground negate gravity. This stops
		// some instability in physics
		//getBody().setGravityEffected(!on);

		// clamp y
		if (getVelY() < -MAX_JUMP_VEL) {
			setVelocity(getVelX(), -MAX_JUMP_VEL);
		}

		// handle jumping as opposed to be moving up. This prevents
		// bounces on edges
		if ((!jumping) && (getVelY() < 0)) {
			setVelocity(getVelX(), getVelY() * 0.95f);
			// doesn't even need the 0.95f here... investigate later.
		}

		if (jumping) {
			if (getVelY() > 0) {
				jumping = false;
			}
		}
	}

	/**
	 * Render the Entity.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param g
	 *            Java Graphics.
	 * @throws SlickException
	 */
	public abstract void render(GameContainer gc, Graphics g)
			throws SlickException;

	/**
	 * Called before Update.
	 * 
	 * @param delta
	 *            Time passed since last update.
	 */
	public void preUpdate(int delta) {
		// at the start of each frame kill the x velocity
		// if the actor isn't being moved
		if (!super.isMoving()) {
			setVelocity(0, getVelY());
		}

		super.setFalling((getVelY() > 0) && (!isOnGround()));
		velx = getVelX();
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
		getBody().applyForce(x, y);

		// if the force applied is up into the air the actor is
		// considered to be jumping
		if (y < 0) {
			setJumping(true);
		}

		// if the actor has just changed direction kill the x velocity
		// cause thats what happens in platformers
		if (x > 0) {
			if (!facingRight) {
				setVelocity(0, getVelY());
			}
			setFacingRight(true);
		}
		if (x < 0) {
			if (facingRight) {
				setVelocity(0, getVelY());
			}
			setFacingRight(false);
		}
	}
	
	/**
	 * Cause the Entity to jump upward with the default force jumpPower defined in the Entity.
	 * 
	 * @throws SlickException
	 */
	public void jump() throws SlickException
	{
		this.jump(this.jumpPower);
	}

	/**
	 * Cause the Entity to jump upward with the force jumpPower.
	 * 
	 * @param jumpPower
	 *            The force to apply.
	 * @throws SlickException
	 */
	public void jump(float jumpPower) throws SlickException {
		if ( (!isFalling()||isJumping()) && (jumpsTaken < maximumJumps) )
		{
			//System.out.println(jumpsTaken + " " + (int)jumpsTaken+1 );
			System.out.println(getVelY());
			if( ++jumpsTaken == 2 )
			{
				this.applyForce(0, -jumpPower*1.5f);
				System.out.println("Double jump");
			}
			else
			{
				System.out.println((int)(jumpsTaken));
				this.applyForce(0, -jumpPower);
			}
			System.out.println(jumpsTaken);
		}
	}

	/**
	 * Set the jumping flag to True or False.
	 * 
	 * @param b
	 *            True if jumping upward. False if on the ground or falling.
	 */
	public void setJumping(boolean b) {
		jumping = b;
		//jumpsTaken = b?jumpsTaken:0;
		/*if(!b)
		{
			System.out.println("Jumps reset");
		}*/
		/*if( !b )
		{
			jumpsTaken=0;
		}*/
	}

	/**
	 * Check to see if the Entity is jumping.
	 * 
	 * @return True if jumping upward. False if on the ground or falling.
	 */
	public boolean isJumping() {
		return jumping;
	}
	
	/**
	 * Get the number of maximum jumps the Entity can take without touching the ground.
	 * 
	 * @return The maximum number of jumps the Entity can take without touching the ground
	 */
	public int getMaximumJumps()
	{
		return maximumJumps;
	}
	
	/**
	 * Set the number of maximum jumps the Entity can take without touching the ground.
	 * 
	 * @param max
	 * 			The new maximum number of jumps the Entity can take without touching the ground
	 */
	public void setMaximumJumps(int max)
	{
		this.maximumJumps = max;
	}

	/**
	 * Set the facingRight flag to True or False.
	 * 
	 * @param b
	 *            True or False if Entity and Entity Image is facing Right.
	 */
	public void setFacingRight(boolean b) {
		facingRight = b;
	}

	/**
	 * Check if the Entity is facing right.
	 * 
	 * @return True if facing to the right.
	 */
	public boolean isFacingRight() {
		return facingRight;
	}

	/**
	 * Move Entity to the right.
	 * 
	 * @param vel
	 *            Force to apply in the positive X direction.
	 */
	public void moveRight(float vel) {
		setMoving(true);
		setFacingRight(true);
		this.applyForce(vel, 0);
	}

	/**
	 * Move Entity to the left.
	 * 
	 * @param vel
	 *            Force to apply in the negative X direction.
	 */
	public void moveLeft(float vel) {
		setMoving(true);
		setFacingRight(false);
		this.applyForce(-vel, 0);
	}

	/**
	 * Get the jump force.
	 * 
	 * @return Jump force.
	 */
	public float getJumpPower() {
		return this.jumpPower;
	}

	/**
	 * Set the jump force.
	 * 
	 * @param jumpPower
	 *            Force to set.
	 */
	public void setJumpPower(float jumpPower) {
		this.jumpPower = jumpPower;
	}

	public float getXVelocity() {
		return velx;
	}

	public void setXVelocity(float velx) {
		this.velx = velx;
	}

}
