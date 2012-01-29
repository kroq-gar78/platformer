package plat;

import net.phys2d.raw.World;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.n3wt0n.G2DP.Entity;
import com.n3wt0n.G2DP.SoundWrapper;

public abstract class AnimationEntity extends Entity
{
	public AnimationEntity(World world, float x, float y, int width, int height, float mass, String name, SoundWrapper soundWrapper)
	{
		super(world, x, y, width, height, mass, name, soundWrapper);
		setXY(x, y);
	}
	
	public AnimationEntity(World world, float x, float y, int width, int height, float mass, String name, SoundWrapper soundWrapper, Animation anim)
	{
		super(world, x, y, width, height, mass, name, soundWrapper);
		setXY(x, y);
		this.anim = anim;
	}
	
	public void update(GameContainer gc, int delta)
	{
		anim.update(delta);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		anim.draw(getX(), getY());
	}
	
	public Animation getAnimation() { return this.anim; }
	public void setAnimation(Animation newAnim) { this.anim = newAnim; }
	
	private Animation anim;
}
