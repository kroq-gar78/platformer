package plat;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public interface ILevelObject
{
	public String getName();
	
	public void setPosition( Vector2f position );
	
	public Vector2f getPosition();
	
	public void render( Graphics graphics );
	
	public void update( GameContainer gc , BasicGame bg , int delta );
}
