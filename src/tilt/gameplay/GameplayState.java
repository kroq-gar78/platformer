package tilt.gameplay;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import tilt.collision.CollisionManager;

public class GameplayState extends BasicGameState
{

	@Override
	public void init( GameContainer gc , StateBasedGame sbg ) throws SlickException
	{

		gc.setVSync( true );
		collisionManager = new CollisionManager(); // initialize collision manager
		//bgImage = new Image( "res/bg.jpg" );
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg , Graphics g ) throws SlickException
	{
		bgImage.draw( 0 , 0 , gc.getWidth() , gc.getHeight() );

		player.render(g);
	}

	@Override
	public void update( GameContainer gc , StateBasedGame sbg , int delta ) throws SlickException
	{
		//get input
		input = gc.getInput();
		
		//look for pressed keys
		if( input.isKeyDown( Input.KEY_ESCAPE ) ) gc.exit(); // if escape pressed, exit game
		
		//make sure paddle is within bounds of the canvas/window
		if( input.isKeyDown( Input.KEY_W ) ) player.getPosition().y-=1*delta*PADDLE_SPEED;
		if( input.isKeyDown( Input.KEY_S ) ) player.getPosition().y+=1*delta*PADDLE_SPEED;
		if( player.getPosition().y < 10 ) player.getPosition().y=11;
		if( player.getPosition().y+player.getImage().getHeight() > gc.getHeight()-10 ) player.getPosition().y=gc.getHeight()-11-player.getImage().getHeight();
		
		/*switch( currentState )
		{
		
		}*/
		
		collisionManager.processCollisions();
	}

	public int getPlayerLives() { return playerLives; }
	public void setPlayerLives( int lives ) { this.playerLives = lives; }
	
	public CollidableImageObject getPFloor() { return floor; }
	
	public CollisionManager getCollisionManager() { return collisionManager; }
	
	@Override
	public int getID()
	{
		return 1;
	}
	
	private static enum STATES { NORMAL_GAME , COUNTDOWN , GAME_OVER };
	private STATES currentState;
	
	private Player player;
	
	private static float PADDLE_SPEED = 0.8f; //paddle speed
	
	private int playerLives;
	
	private CollidableImageObject floor;
	
	private Image playerImage;
	private Image bgImage;
	
	private Input input;
	
	private CollisionManager collisionManager;
	
}
