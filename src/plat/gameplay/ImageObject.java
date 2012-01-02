package plat.gameplay;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class ImageObject implements ILevelObject
{
	public ImageObject( String name , Image image , Vector2f position )
	{
		this.name = name;
		this.image = image;
		this.position = position;
	}
	
	public String getName() { return name; }

	@Override
	public Vector2f getPosition() { return position; }
	@Override
	public void setPosition(Vector2f position){ this.position = position; }
	
	public Image getImage() { return image; }
	public void setImage( Image img ) { this.image = img; }
	
	@Override
	public void render(Graphics graphics)
	{
		image.draw( position.x , position.y );
	}

	@Override
	public void gameUpdate(GameContainer gc , BasicGame bg , int delta) {}
	
	protected String name;
	protected Image image;
	protected Vector2f position;
}
