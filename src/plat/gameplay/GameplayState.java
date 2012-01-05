package plat.gameplay;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.Tile;
import org.newdawn.slick.tiled.TiledMapPlus;

/*import plat.collision.CollisionManager;
import plat.collision.PlayerAndFloorCollisionHandler;*/

public class GameplayState extends BasicGameState
{
	// just a generic state class; extend as necessary (e.g. different levels and stages)
	
	@Override
	public void init( GameContainer gc , StateBasedGame game ) throws SlickException
	{

		gc.setVSync( true );
		//collisionManager = new CollisionManager(); // initialize collision manager
		
		// load resources
		playerImage = new Image("res/player.png");
		platformImage = new Image("res/platform.png");
		map = new TiledMapPlus("res/map.tmx");
		
		// tilemap initializations, besides the TileMapPlus itself
		collisionLayer = map.getLayer("collision");
		collisionID = collisionLayer.getTileID(0, collisionLayer.height-1); // bottom left corner should be a collision tile
		//System.out.println( collisionLayer.getTileID(0, 59));
		
		player = new Player("Player", playerImage, new Vector2f(32,gc.getHeight()/2), new Rectangle(0,0,playerImage.getWidth(),playerImage.getHeight()));
		
		/*platforms = new ArrayList<Platform>();
		platforms.add( new Platform("Platform", platformImage, new Vector2f(0,gc.getHeight()-platformImage.getHeight()), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*1+40*1 , gc.getHeight()-platformImage.getHeight()-20*1 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*2+40*2 , gc.getHeight()-platformImage.getHeight()-20*2 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		platforms.add( new Platform("Platform", platformImage, new Vector2f( platformImage.getWidth()*3+40*3 , gc.getHeight()-platformImage.getHeight()-20*3 ), new Rectangle(0,0 , platformImage.getWidth() , platformImage.getHeight()), 2) );
		
		for( Platform platform : platforms ) { collisionManager.addCollidable(platform); }*/
		//collisionManager.addCollidable(player);
		//collisionManager.addHandler(new PlayerAndFloorCollisionHandler() );
		
		//bgImage = new Image( "res/bg.jpg" );
	}

	@Override
	public void render( GameContainer gc , StateBasedGame game , Graphics g ) throws SlickException
	{
		g.setColor(Color.white);
		//bgImage.draw( 0 , 0 , gc.getWidth() , gc.getHeight() );
		map.render(0, 0);
		player.render(g);
		//for( Platform platform : platforms ) { platform.render(g); }
		
		// show colliding tiles
		g.setColor(Color.red);
		for( int x = 0; x < collisionLayer.width; x++ )
		{
			for( int y = 0; y < collisionLayer.height; y++ )
			{
				Rectangle tilePoly=new Rectangle( x*map.getTileWidth() , y*map.getTileHeight() , map.getTileWidth() , map.getTileHeight() );
				if( tilePoly.intersects(player.getCollisionShape()) && collisionLayer.getTileID(x,y)==collisionID )
				{
					g.draw(tilePoly);
				}
			}
		}
	}

	@Override
	public void update( GameContainer gc , StateBasedGame game , int delta ) throws SlickException
	{
		//get input
		input = gc.getInput();
		
		//look for pressed keys
		if( input.isKeyDown( Input.KEY_ESCAPE ) ) gc.exit(); // if escape pressed, exit game
		
		//make sure paddle is within bounds of the canvas/window
		/*if( input.isKeyDown( Input.KEY_W ) ) player.getPosition().y-=1*delta*PADDLE_SPEED;
		if( input.isKeyDown( Input.KEY_S ) ) player.getPosition().y+=1*delta*PADDLE_SPEED;
		if( player.getPosition().y < 10 ) player.getPosition().y=11;
		if( player.getPosition().y+player.getImage().getHeight() > gc.getHeight()-10 ) player.getPosition().y=gc.getHeight()-11-player.getImage().getHeight();*/
		
		// process collisions
		for( int x = 0; x < collisionLayer.width; x++ )
		{
			for( int y = 0; y < collisionLayer.height; y++ )
			{
				Rectangle tilePoly=new Rectangle( x*map.getTileWidth() , y*map.getTileHeight() , map.getTileWidth() , map.getTileHeight() );
				if( tilePoly.intersects(player.getCollisionShape()) && collisionLayer.getTileID(x,y)==collisionID )
				{
					//System.out.println( x*map.getTileWidth() + " " + y*map.getTileHeight() );
					Vector2f direction = player.getDirection().copy();
					do
					{
						Vector2f pos = player.getPosition();
						player.setPosition(new Vector2f( pos.x, pos.y + direction.negate().y ) );
					}
					while( tilePoly.intersects(player.getCollisionShape()) );
					player.setVelocity(new Vector2f(player.getVelocity().x,0f));
					player.setJumpsTaken(0);
				}
			}
		}
		
		if( input.isKeyPressed( Input.KEY_UP ) )
		{
			player.jump();
		}
		if( input.isKeyDown( Input.KEY_LEFT ) )
		{
			player.getPosition().add(new Vector2f(-player.getHorizSpeed(),0f));
		}
		if( input.isKeyDown( Input.KEY_RIGHT ) )
		{
			player.getPosition().add(new Vector2f(player.getHorizSpeed(),0f));	
		}
		
		/*switch( currentState )
		{
		
		}*/
		player.update(gc, game, delta);
		
		
	}
	
	/*private boolean playerColliding() throws SlickException
	{
		for( int i = 0; i < )
	}*/

	public int getPlayerLives() { return playerLives; }
	public void setPlayerLives( int lives ) { this.playerLives = lives; }
	
	//public CollisionManager getCollisionManager() { return collisionManager; }
	
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

	private Image platformImage;
	private Image playerImage;
	//private Image bgImage;
	
	// tilemap objects
	private TiledMapPlus map;
	private Layer collisionLayer;
	private int collisionID;
	
	private Input input;
	
	//private CollisionManager collisionManager;
}
