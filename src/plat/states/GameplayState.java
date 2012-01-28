package plat.states;

import com.n3wt0n.G2DP.Backdrop;
import com.n3wt0n.G2DP.Camera;
import com.n3wt0n.G2DP.MapUtil;
import com.n3wt0n.G2DP.SoundWrapper;

import java.util.ArrayList;

import net.phys2d.raw.World;
import net.phys2d.math.Vector2f;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMapPlus;

import plat.InputManager;
import plat.entities.Platform;
import plat.entities.Player;

public class GameplayState extends BasicGameState
{
	// just a generic state class; extend as necessary (e.g. different stages; use ILevelObject for levels)
	
	@Override
	public void init( GameContainer gc , StateBasedGame game ) throws SlickException
	{
		gc.setVSync( true );
		
		// load resources
		playerImage = new Image("res/player.png");
		//platformImage = new Image("res/platform.png");
		map = new TiledMapPlus("res/map.tmx");
		//bgImage = new BigImage( "res/bg.jpg" );
		
		soundWrapper = new SoundWrapper();
		world = new World(new Vector2f(0f,1.5f), 20);
		mapUtil = new MapUtil(map, world);
		mapUtil.buildMap();
		backdrop = new Backdrop( map.getWidth() , map.getHeight() , gc.getWidth() , gc.getHeight() );
		//backdrop.add(new BigImage("res/background_sunSmiley.jpg"));
		//backdrop.sort();
		
		player = new Player(world, 30, playerImage.getHeight()-50, 10, "Player", soundWrapper, playerImage);
		player.getBody().setFriction(200f);
		world.add(player.getBody());
		
		/*platforms = new ArrayList<Platform>();
		platforms.add(new Platform(new Vector2f(0,gc.getHeight()-platformImage.getHeight()-50), platformImage));
		platforms.add( new Platform("Platform", platformImage, new Vector2f(0,gc.getHeight()-platformImage.getHeight()), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*1+40*1 , gc.getHeight()-platformImage.getHeight()-20*1 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*2+40*2 , gc.getHeight()-platformImage.getHeight()-20*2 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*3+40*3 , gc.getHeight()-platformImage.getHeight()-20*3 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		*/
		
		camera = new Camera(gc, map, mapUtil, player, backdrop);
		inputManager = new InputManager(player);
		
		/*for( Platform platform : platforms )
		{
			world.add(platform);
		}*/
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game , Graphics g ) throws SlickException
	{
		//bgImage.draw( 0 , 0 , gc.getWidth() , gc.getHeight() );
		//map.render(0, 0, g);
		camera.render(gc, g);
		
		//g.draw(new Rectangle(platforms.get(0).getPosition().getX() , platforms.get(0).getPosition().getY() , ((Box)platforms.get(0).getShape()).getSize().getX() , ((Box)platforms.get(0).getShape()).getSize().getY()) );
		//player.render(gc, g);
		//for( Platform platform : platforms ) { platform.render(gc,g); }
	}

	@Override
	public void update( GameContainer gc , StateBasedGame game , int delta ) throws SlickException
	{
		//System.out.println((float)delta/20);
		world.step(1f);
		
		//look for pressed keys

		//player.preUpdate(delta);
		
		inputManager.update(gc, delta);
		
		/*switch( currentState )
		{
		
		}*/
		//player.update(gc, game, delta);
		camera.update(gc, delta);
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
	private int playerLives;
	private Image playerImage;
	
	private Backdrop backdrop;
	private Camera camera;
	private InputManager inputManager;
	private MapUtil mapUtil;
	private SoundWrapper soundWrapper;
	private TiledMapPlus map;
	private World world;
}
