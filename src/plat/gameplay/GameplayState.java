package plat.gameplay;

import java.util.ArrayList;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.math.Vector2f;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.n3wt0n.G2DP.SoundWrapper;

public class GameplayState extends BasicGameState
{
	// just a generic state class; extend as necessary (e.g. different levels and stages)
	
	@Override
	public void init( GameContainer gc , StateBasedGame game ) throws SlickException
	{
		gc.setVSync( true );
		
		// load resources
		playerImage = new Image("res/player.png");
		platformImage = new Image("res/platform.png");
		
		worldIter=100;
		world = new World(new Vector2f(0f,3f), worldIter);
		
		player = new Player(world, 0, playerImage.getHeight(), playerImage.getWidth(), playerImage.getHeight(), 10, "Player", soundWrapper, playerImage);
		
		platforms = new ArrayList<Platform>();
		platforms.add(new Platform(new Box(platformImage.getWidth(), platformImage.getHeight()), new Vector2f(0,gc.getHeight()-platformImage.getHeight()), platformImage));
		/*platforms.add( new Platform("Platform", platformImage, new Vector2f(0,gc.getHeight()-platformImage.getHeight()), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*1+40*1 , gc.getHeight()-platformImage.getHeight()-20*1 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*2+40*2 , gc.getHeight()-platformImage.getHeight()-20*2 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*3+40*3 , gc.getHeight()-platformImage.getHeight()-20*3 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		*/
		//bgImage = new Image( "res/bg.jpg" );
		
		world.add(player.getBody());
		for( Platform platform : platforms )
		{
			world.add(platform);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game , Graphics g ) throws SlickException
	{
		//bgImage.draw( 0 , 0 , gc.getWidth() , gc.getHeight() );
		
		player.render(gc, g);
		for( Platform platform : platforms ) { platform.render(gc,g); }
	}

	@Override
	public void update( GameContainer gc , StateBasedGame game , int delta ) throws SlickException
	{
		//get input
		input = gc.getInput();
		System.out.println((float)delta/20);
		world.step((float)delta/20);
		
		//look for pressed keys
		if( input.isKeyDown( Input.KEY_ESCAPE ) ) gc.exit(); // if escape pressed, exit game
		
		//make sure paddle is within bounds of the canvas/window
		/*if( input.isKeyDown( Input.KEY_W ) ) player.getPosition().y-=1*delta*PADDLE_SPEED;
		if( input.isKeyDown( Input.KEY_S ) ) player.getPosition().y+=1*delta*PADDLE_SPEED;
		if( player.getPosition().y < 10 ) player.getPosition().y=11;
		if( player.getPosition().y+player.getImage().getHeight() > gc.getHeight()-10 ) player.getPosition().y=gc.getHeight()-11-player.getImage().getHeight();*/
		
		if( input.isKeyPressed( Input.KEY_UP ) )
		{
			player.jump(500);
		}
		/*if( input.isKeyDown( Input.KEY_LEFT ) )
		{
			player.getPosition().add(new Vector2f(-player.getHorizSpeed(),0f));
		}
		if( input.isKeyDown( Input.KEY_RIGHT ) )
		{
			player.getPosition().add(new Vector2f(player.getHorizSpeed(),0f));	
		}*/
		
		/*switch( currentState )
		{
		
		}*/
		player.update(gc, game, delta);
		System.out.println(player.getVelocity());
	}

	public int getPlayerLives() { return playerLives; }
	public void setPlayerLives( int lives ) { this.playerLives = lives; }
	
	@Override
	public int getID()
	{
		return 1;
		
		/*switch(currentState)
		{
		case(NORMAL_GAME)
		{
			return 1;
		}
		}*/
	}
	
	//private static enum STATES { NORMAL_GAME };
	//private STATES currentState;
	
	private Player player;
	private ArrayList<Platform> platforms;
	
	private int playerLives;
	
	private int worldIter;
	
	private Image platformImage;
	private Image playerImage;
	//private Image bgImage;
	
	private Input input;
	private SoundWrapper soundWrapper;
	private World world;
}
