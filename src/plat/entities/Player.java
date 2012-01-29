package plat.entities;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.n3wt0n.G2DP.Entity;
import com.n3wt0n.G2DP.SoundWrapper;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class Player extends Entity
{
	public Player(World world, float x, float y, float mass,
			String name, SoundWrapper soundWrapper, Image image)
	{
		super(world, x, y, image.getWidth(), image.getHeight(), mass, name, soundWrapper);
		setImage(image);
	}
	public Player(World world, Vector2f pos, float mass,
			String name, SoundWrapper soundWrapper, Image image)
	{
		super(world, pos, image.getWidth(), image.getHeight(), mass, name, soundWrapper);
		setImage(image);
	}

	public void update( GameContainer g , Game game , int delta ) throws SlickException
	{
		super.update(delta);
	}
	
	public void render(GameContainer gc , Graphics g) throws SlickException
	{
		getImage().draw(this.getX()-this.getWidth()/2, this.getY()-this.getHeight()/2);
		if( proj != null ) proj.render(gc, g);
	}
	
	public void shoot() throws SlickException
	{
		if( proj != null ) getWorld().remove(proj.getBody());
		proj = new Projectile(getWorld(), getX()+20*(isFacingRight()?1:-1), getY(), 10f, "projectile", getSoundWrapper(), new Image("res/projectile.png"));
		getWorld().add(proj.getBody());
		proj.getBody().setGravityEffected(false);
		proj.setVelocity(20f*(isFacingRight()?1:-1), 0f);
	}
	
	private Projectile proj;
	
	/*public float getHorizSpeed() { return horizSpeed; }
	
	public boolean isJumping() { return jumpsTaken>0; }
	public int getJumpsTaken() { return this.jumpsTaken; }
	public void setJumpsTaken(int jumps) { this.jumpsTaken=jumps; }
	
	private float horizSpeed=4f;
	private int jumpsTaken=0;*/
	
	//private Image image;
}
