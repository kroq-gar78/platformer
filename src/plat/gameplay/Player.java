package plat.gameplay;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Unit {

	public Player(String name, Image image, Vector2f position,
			Shape collisionShape, Vector2f direction, float speed)
	{
		super(name, image, position, collisionShape, direction, speed, 1);
		// TODO Auto-generated constructor stub
	}

	public Player(String name, Image image, Vector2f position,
			Shape collisionShape)
	{
		super(name, image, position, collisionShape, 1);
		// TODO Auto-generated constructor stub
	}
	
	public void update( GameContainer g , Game game , int delta )
	{
		super.update(g, game, delta);
	}
	
	public void jump()
	{
		if( jumpsTaken==1 ) // double jump; jumps==0 can never (and should never) happen
		{
			velocity.y=0;
			applyForce(new Vector2f( 0f, -2f ));
			applyForce(gravity.negate());
			jumpsTaken=2;
		}
		else if( jumpsTaken==0 ) // single jump
		{
			velocity.y=0;
			applyForce(new Vector2f( 0f, -1.5f ));
			applyForce(gravity.negate());
			jumpsTaken=1;
		}
	}
	
	public void render(Graphics g)
	{
		super.render(g);
	}
	
	public float getHorizSpeed() { return horizSpeed; }
	
	public int getJumpsTaken() { return this.jumpsTaken; }
	public void setJumpsTaken(int jumps) { this.jumpsTaken=jumps; }
	
	private float horizSpeed=4f;
	private int jumpsTaken=0;
}
