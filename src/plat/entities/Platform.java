package plat.entities;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class Platform extends Body
{

	/*public Platform(String name, Image image, Vector2f position, Shape collisionShape, int collisionType)
	{
		super(name, image, position, collisionShape, collisionType);
		// TODO Auto-generated constructor stub
	}*/
	
	// there's gotta be a way to condense all these constructors...	
	public Platform(Shape shape,float x, float y,Image img)
	{
		super(shape,x,y,true);
		//this.setPosition(x,y);
		this.img=img;
		this.setFriction(200f);
	}
	public Platform(Shape shape,Vec2 pos,Image img)
	{
		this(shape,pos.x,pos.y,img);
	}
	public Platform(String name,Shape shape,float x,float y,Image img)
	{
		super(shape,x,y,true);
		this.img=img;
		this.setFriction(200f);
	}
	public Platform(String name,Shape shape,Vec2 pos,Image img)
	{
		this(shape,pos.x,pos.y,img);
	}
	public Platform(float x, float y, Image img)
	{
		this( null , x , y , img );
		PolygonShape rect = new PolygonShape();
		rect.setAsBox((float)img.getWidth()/2, (float)img.getHeight()/2);
		setShape(rect);
		//System.out.println("Platform width: " + img.getWidth() + "\nPlatform height: " + img.getHeight());
	}
	public Platform(Vec2 pos, Image img)
	{
		this( pos.x , pos.y , img );
	}

	public void render(GameContainer gc,Graphics g)
	{
		img.draw(this.getPosition().x,this.getPosition().y);
	}
	
	private Image img;
}
