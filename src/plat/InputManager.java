package plat;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import plat.entities.Player;

public class InputManager
{
	public InputManager(Player player)
	{
		this.player = player;
	}
	
	public void init() {}
	
	// make sure that, somewhere in here, it checks for the different states, or make this an interface/abstract class
	public void update(GameContainer gc, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		player.preUpdate(delta);
		
		if( input.isKeyDown( Input.KEY_ESCAPE ) ) gc.exit(); // if escape pressed, exit game
		
		// move player
		
		player.setMoving(false);
		
		if( input.isKeyPressed( Input.KEY_UP ) )
		{
			player.jump(200f);
		}
		if( input.isKeyDown( Input.KEY_LEFT ) )
		{
			player.moveLeft(0.5f);
		}
		if( input.isKeyDown( Input.KEY_RIGHT ) )
		{
			player.moveRight(0.5f);
		}
	}
	
	private Player player;
}
