package plat;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import plat.entities.Player;
import plat.entities.Projectile;

// turn this into an interface later for versatility
public class InputManager
{
	public InputManager(Player player)
	{
		this.player = player;
		init();
	}
	
	public void init() {}
	
	// make sure that, somewhere in here, it checks for the different states, or make this an interface/abstract class
	public void update(GameContainer gc, int delta) throws SlickException
	{
		input = gc.getInput();

		if( input.isKeyDown( Input.KEY_ESCAPE ) ) gc.exit(); // if escape pressed, exit game
		
		player.preUpdate(delta);
		
		// THIS IS OLD CODE; IMPORTED FROM SlickPong
		//make sure paddle is within bounds of the canvas/window
		/*if( input.isKeyDown( Input.KEY_W ) ) player.getPosition().y-=1*delta*PADDLE_SPEED;
		if( input.isKeyDown( Input.KEY_S ) ) player.getPosition().y+=1*delta*PADDLE_SPEED;
		if( player.getPosition().y < 10 ) player.getPosition().y=11;
		if( player.getPosition().y+player.getImage().getHeight() > gc.getHeight()-10 ) player.getPosition().y=gc.getHeight()-11-player.getImage().getHeight();*/
		// ^^^ THIS IS OLD CODE; IMPORTED FROM SlickPong ^^^
		
		// move player
		//player.setMoving(false);
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
		/*if( input.isKeyPressed( Input.KEY_SPACE ) )
		{
			player.shoot();
		}*/
	}
	
	private Input input;
	private Player player;
}
