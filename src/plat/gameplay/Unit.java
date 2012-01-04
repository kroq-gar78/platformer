package plat.gameplay;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;


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
		this.position = this.position.add(velocity.copy().scale((float)delta));
		//System.out.println( getVelocity().y );
		
	}
	
	public float getSpeed() { return velocity.length(); }
	public void setSpeed( float speed ) { velocity=velocity.normalise().scale(speed); }
	
	public Vector2f getDirection() { return velocity.copy().normalise(); }
	public void setDirection( Vector2f direction )
	{
		float speed = velocity.length();
		velocity=direction.copy().normalise().scale(speed);
	}
	
	public Vector2f getVelocity() {	return velocity; }
	public void setVelocity( Vector2f velocity ) { this.velocity=velocity; }
	public void setVelocity( Vector2f direction , float speed ) { this.velocity=direction.copy().normalise().scale(speed); }
	
	public void applyForce(Vector2f force)
	{
		this.velocity=velocity.add(force);
	}
	public Vector2f getGravity() { return gravity; }
	public void setGravity( Vector2f gravity ) { this.gravity = gravity; }
	
	protected Vector2f velocity;
	protected Vector2f gravity = new Vector2f( 0 , 0.1f );
}
