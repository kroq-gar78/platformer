package plat.gameplay;

import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;


public class AnimationObject implements ILevelObject
{
	public AnimationObject( String name , Animation animation , Vector2f position )
	{
		this.name = name;
		this.animation = animation;
		this.position = position;
	}
	
	@Override
	public String getName() { return name; }

	@Override
	public void setPosition(Vector2f position) { this.position = position; }
	@Override
	public Vector2f getPosition() { return position; }

	@Override
	public void render(Graphics graphics)
	{
		animation.draw( position.x , position.y );
	}

	@Override
	public void gameUpdate(GameContainer gc, BasicGame bg, int delta)
	{
		animation.update( delta );
	}
	
	protected String name;
	protected Animation animation;
	protected Vector2f position;
}
