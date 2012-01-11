package plat.gameplay;

import net.phys2d.raw.StaticBody;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.shapes.Shape;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class Platform extends StaticBody
{

	/*public Platform(String name, Image image, Vector2f position, Shape collisionShape, int collisionType)
	{
		super(name, image, position, collisionShape, collisionType);
		// TODO Auto-generated constructor stub
	}*/
	
	public Platform(Shape shape,Vector2f pos,Image img)
	{
		super(shape);
		this.img=img;
	}
	public Platform(String name,Shape shape,Vector2f pos,Image img)
	{
		super(name,shape);
		this.setPosition(pos.x,pos.y);
		this.img=img;
	}
	public Platform(String name,Shape shape,float x,float y,Image img)
	{
		super(name,shape);
		this.setPosition(x,y);
		this.img=img;
	}

	public void render(Graphics g)
	{
		img.draw(this.getPosition().getX(),this.getPosition().getY());
	}
	
	private Image img;
}
