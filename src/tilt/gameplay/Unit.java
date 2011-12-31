package tilt.gameplay;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
//import org.newdawn.slick.util.FastTrig;


public class Unit extends CollidableImageObject
{
	public static final int COLLISION_TYPE = 1;
	
	public Unit( String name , Image image , Vector2f position , Shape collisionShape , Vector2f direction , float speed , int collisionType )
	{
		super( name , image , position , collisionShape , collisionType );
		this.setVelocity(direction, speed);
	}
	public Unit(String name, Image image, Vector2f position, Shape collisionShape , int collisionType ) //same as prev. but no speed
	{
		this( name , image , position , collisionShape, new Vector2f(0,0) , 0f , collisionType );
	}
	
	public void update( GameContainer gc , Game game , int delta )
	{
		// apply gravity
		applyForce(gravity);
		// really would be nice if I could retrieve both direction and speed as 2 variables from 1 variable
		this.position = this.position.add(getVelocity().scale((float)delta));
		//System.out.println( getVelocity().y );
		
	}
	
	public float getSpeed() { return this.speed; }
	public void setSpeed( float speed ) { this.speed = speed; }
	
	public Vector2f getDirection() { return this.direction; }
	public void setDirection( Vector2f direction )
	{
		this.direction = direction;
		this.direction.scale( 1/(float)(Math.hypot( this.direction.x , this.direction.y )));
	}
	
	public Vector2f getVelocity() // does all the things needed to update the velocity
	{
		return direction.copy().scale( (float)( Math.hypot(direction.x, direction.y)) );
	}
	public void setVelocity( Vector2f velocity )
	{
		this.speed = velocity.length();
		this.direction = velocity.copy().scale( 1/velocity.length() );
	}
	public void setVelocity( Vector2f direction , float speed ) { this.direction = direction; this.speed = speed; }
	
	public void applyForce(Vector2f force)
	{
		setVelocity(getVelocity().add(force));
	}
	
	public Vector2f getGravity() { return gravity; }
	public void setGravity( Vector2f gravity ) { this.gravity = gravity; }
	
	private float speed;
	private Vector2f direction;
	private Vector2f gravity = new Vector2f( 0 , 0.001f );
}
