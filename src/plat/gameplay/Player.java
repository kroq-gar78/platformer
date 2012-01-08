package plat.gameplay;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import com.n3wt0n.G2DP.Entity;
import com.n3wt0n.G2DP.SoundWrapper;

public class Player extends Entity
{
	public Player(World world, float x, float y, int width, int height,
			float mass, String name, SoundWrapper soundWrapper)
	{
		super(world, x, y, width, height, mass, name, soundWrapper);
		// TODO Auto-generated constructor stub
	}
	public Player(World world, Vector2f pos, int width, int height,
			float mass, String name, SoundWrapper soundWrapper)
	{
		super(world, pos, width, height, mass, name, soundWrapper);
		// TODO Auto-generated constructor stub
	}

	public void update( GameContainer g , Game game , int delta ) throws SlickException
	{
		super.update(delta);
	}
	
	public void render(GameContainer gc , Graphics g) throws SlickException
	{
		//super.render(gc,g);
	}
	
	/*public float getHorizSpeed() { return horizSpeed; }
	
	public boolean isJumping() { return jumpsTaken>0; }
	public int getJumpsTaken() { return this.jumpsTaken; }
	public void setJumpsTaken(int jumps) { this.jumpsTaken=jumps; }
	
	private float horizSpeed=4f;
	private int jumpsTaken=0;*/
}
