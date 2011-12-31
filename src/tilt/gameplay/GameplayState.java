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
import tilt.collision.BoundsAndPaddleBallCollisionHandler;

public class GameplayState extends BasicGameState
{

	@Override
	public void init( GameContainer gc , StateBasedGame sbg ) throws SlickException
	{

		gc.setVSync( true );
		collisionManager = new CollisionManager(); // initialize collision manager
		bgImage = new Image( "res/bg.jpg" );
		
		currentState = STATES.COUNTDOWN;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg , Graphics g ) throws SlickException
	{
		bgImage.draw( 0 , 0 , gc.getWidth() , gc.getHeight() );

		player.render(g);
		enemy.render(g);
		
		if( currentState == STATES.NORMAL_GAME ) ball.render(g);
		else if( currentState == STATES.GAME_OVER )
		{
			font.drawString( gc.getWidth()/2-96*6/2+18 , gc.getHeight()/2 - (int)(96*.75) , "GAME OVER" );
			font2.drawString(  gc.getWidth()/2-108 , gc.getHeight()/2 + 44*2 , "Press SPACE to play again." );
		}
		else if( currentState == STATES.COUNTDOWN )
		{
			 font.drawString( gc.getWidth()/2-96/2 , gc.getHeight()/2 - 96/2 , Integer.toString(countdownRounded) );
		}
		
		g.drawString( "Lives: " +  playerLives + "       " + enemyLives , gc.getWidth()/2-100 , 0 );
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
		
		//"enemy" controls; later to be AI; uses arrows
		if( input.isKeyDown( Input.KEY_UP ) ) enemy.getPosition().y-=1*delta*PADDLE_SPEED;
		if( input.isKeyDown( Input.KEY_DOWN ) ) enemy.getPosition().y+=1*delta*PADDLE_SPEED;
		if( enemy.getPosition().y < 10) enemy.getPosition().y=11;
		if( enemy.getPosition().y+enemy.getImage().getHeight() > gc.getHeight()-10 ) enemy.getPosition().y=gc.getHeight()-11-enemy.getImage().getHeight();
		
		//if( input.isKeyDown( Input.KEY_EQUALS ) || input.isKeyDown( Input.KEY_ADD ) ) gc.setTargetFrameRate( gc.getFPS()+5/25*delta );
		//if( input.isKeyDown( Input.KEY_SUBTRACT ) ) gc.setTargetFrameRate( gc.getFPS()-5/25*delta );// gc.setTargetFrameRate( gc.getFPS()-(5/50*delta) );//gc.setTargetFrameRate( SlickPongGame.MAXFPS-5/1000*delta );
		
		switch( currentState )
		{
		case NORMAL_GAME:
		{
			if( ball == null ) ball = createBall( gc );
			
			
			
			//update paddle objects (AI; player updated in previous lines)
			
			//check whether or not the ball has gone off the screen
			//if went off left side of screen, deduct from player's lives and re-create ball
			ball.update( gc , sbg , delta );
			
			if( ball.getPosition().x <= 0 )
			{
				playerLives--;
				this.ball = createBall(gc);
				countdown = 3*1000; //multiply by 1000 for use of int delta; 1000 delta = 1 sec
				currentState = STATES.COUNTDOWN;
			}
			//if went off right side of screen, deduct from enemy's lives and re-create ball
			else if( ball.getPosition().x+ball.getImage().getWidth() > gc.getWidth() )
			{
				enemyLives--;
				this.ball = createBall(gc);
				countdown = 3*1000; //multiply by 1000 for use of int delta; 1000 delta = 1 sec
				currentState = STATES.COUNTDOWN;
			}
			//if either players' lives are less than zero, game over
			if( playerLives <= 0 || enemyLives <= 0 )
			{
				System.out.println( "GAME OVERRRRRRRR" );
				currentState = STATES.GAME_OVER;
			}
			//System.out.println( "NORMAL_GAME" );
			break;
		}
		case COUNTDOWN:
		{
			if( countdown <= 0 )
			{
				currentState = STATES.NORMAL_GAME;
				break;
			}
			countdown -= delta;
			countdownRounded = (int)Math.ceil( (double)countdown/1000 );
			//System.out.println( "COUNTDOWN" );
			break;
		}
		case GAME_OVER:
		{
			//maybe put some "return to menu" or "restart" command
			if( input.isKeyDown( Input.KEY_SPACE ) )
			{
				ball = createBall(gc);
				countdown = 5*1000; //multiply by 1000 for use of int delta; 1000 delta = 1 sec
				player.setPosition( new Vector2f( 10 , gc.getHeight()/2-paddleImage.getHeight()/2 ) );
				enemy.setPosition( new Vector2f( gc.getWidth()-10-enemyImage.getWidth() , gc.getHeight()/2-enemyImage.getHeight()/2 ) );
				playerLives = enemyLives = 7;
				currentState = STATES.COUNTDOWN;
				//System.out.println( "GAME_OVER" );
			}
			break;
		}
		}
		
		collisionManager.processCollisions();
	}

	private Ball createBall( GameContainer gc ) throws SlickException //do everything that is necessary to create and set up a ball
	{
		ballImage = new Image( "res/ball.png" );
		Ball ball = new Ball( "Ball" , ballImage , new Vector2f( gc.getWidth()/2-ballImage.getWidth()/2 , gc.getHeight()/2-ballImage.getHeight()/2 ) , BALL_SPEED , new Vector2f( 0 , 0 ) , new org.newdawn.slick.geom.Circle( 0+ballImage.getWidth()/2 , 0+ballImage.getHeight()/2 , ballImage.getWidth()/2 ) , 2 );
		
		//randomize direction
		Vector2f direction = new Vector2f(1,1);
		Random r = new Random();
		direction.add( r.nextInt(35)+10 * ( r.nextBoolean() ? -1 : 1 ) );
		direction.x*=( r.nextBoolean() ? -1 : 1 );
		direction.y*=( r.nextBoolean() ? -1 : 1 );
		ball.setDirection(direction);
		
		collisionManager.addCollidable( ball );
		return ball;
	}
	
	public int getPlayerLives() { return playerLives; }
	public void setPlayerLives( int lives ) { this.playerLives = lives; }
	public int getEnemyLives() { return enemyLives; }
	public void setEnemyLives( int lives ) { this.enemyLives = lives; }
	
	public CollidableImageObject getTopBound() { return topBounds; }
	public CollidableImageObject getBottomBound() { return bottomBounds; }
	
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
	private static float BALL_SPEED = 0.41f; //initial ball speed
	private static float BALL_ACCEL = 0.012f; //ball acceleration on bounce
	
	private int playerLives;
	private int enemyLives;
	
	private CollidableImageObject topBounds;
	private CollidableImageObject bottomBounds;
	
	private Image paddleImage;
	private Image enemyImage;
	private Image ballImage;
	private Image bgImage;
	
	private Input input;
	private int countdown;
	private int countdownRounded;
	private UnicodeFont font;
	private UnicodeFont font2;
	
	private CollisionManager collisionManager;
	
}
