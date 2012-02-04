package plat.entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import plat.AnimationEntity;

import net.phys2d.raw.World;

import com.n3wt0n.G2DP.Entity;
import com.n3wt0n.G2DP.SoundWrapper;

public class Projectile extends Entity
{
	public Projectile(World world, float x, float y, float mass, String name, SoundWrapper soundWrapper, Image image)
	{
		super(world, x, y, image.getWidth(), image.getHeight(), mass, name, soundWrapper);
		setImage(image);
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		getImage().draw(this.getX(), this.getY());
		//super.render(gc, g);
	}
	
	public void update(GameContainer gc, int delta) throws SlickException
	{
		super.update(delta);
	}
}
