package plat.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.phys2d.raw.World;

import com.n3wt0n.G2DP.SoundWrapper;

public class Projectile extends com.n3wt0n.G2DP.Entity
{
	public Projectile(World world, float x, float y, float mass, String name, SoundWrapper soundWrapper, Image image)
	{
		super(world, x, y, image.getWidth(), image.getHeight(), mass, name, soundWrapper);
		setImage(image);
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		getImage().draw(this.getX(), this.getY());
	}
}
