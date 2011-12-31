package tilt.gameplay;

import org.newdawn.slick.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
//import org.newdawn.slick.util.FastTrig;


public class Unit extends CollidableImageObject
{
	public static final int COLLISION_TYPE = 1;
	
	public Unit( String name , Image image , Vector2f position , Shape collisionShape , Vector2f direction , float speed )
	{
		super( name , image , position , collisionShape , COLLISION_TYPE );
	}
	public Unit(String name, Image image, Vector2f position, Shape collisionShape ) //same as prev. but no speed
	{
		this( name , image , position , collisionShape, new Vector2f(0,0) , 0f );
	}
	
	public void update( Graphics g , Game game , int delta )
	{
		// update velocity
		Vector2f velocity = direction.copy();
		velocity.scale( (float)(Math.hypot( velocity.x , velocity.y )) );
	}
	
	public float getSpeed() { return this.speed; }
	public void getSpeed( float speed ) { this.speed = speed; }
	
	public Vector2f getDirection() { return this.direction; }
	public void setDirection( Vector2f direction )
	{
		this.direction = direction;
		this.direction.scale( 1/(float)(Math.hypot( this.direction.x , this.direction.y )));
	}
	
	public Vector2f getVelocity()
	{
		return direction.copy().scale( (float)(Math.hypot(direction.x, direction.y)) );
	}
	public void setVelocity( Vector2f velocity )
	{
		this.speed = velocity.length();
		this.direction = velocity.copy().scale( 1/velocity.length() );
	}
	public void setVelocity( Vector2f direction , float speed ) { this.direction = direction; this.speed = speed; }
	
	private float speed;
	private Vector2f direction;
}