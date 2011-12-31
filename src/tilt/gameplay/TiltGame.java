package tilt.gameplay;

import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import tilt.collision.CollisionManager;

public class TiltGame extends BasicGame
{
	public TiltGame( String title )
	{
		super( title );
	}
	public TiltGame()
	{
		this("Tilt");
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		g.setColor( Color.white );
		g.drawRect(0 , 0 , gc.getWidth() , gc.getHeight() );
		
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException
	{
		gc.setVSync( true );
		collisionManager = new CollisionManager(); 
	}
	
	public void render(GameContainer gc, BasicGame game, Graphics g) throws SlickException
	{
		
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		collisionManager.processCollisions();
	}
	
	public static void main( String[] args ) throws SlickException
	{
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		
		AppGameContainer app = new AppGameContainer( new TiltGame() );
		
		app.setDisplayMode( WIDTH , HEIGHT , FULLSCREEN );
		app.setSmoothDeltas(SMOOTH_DELTAS);
		app.setShowFPS(SHOW_FPS);
		app.setTargetFrameRate(MAX_FPS);
		
		app.start();
	}
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final boolean FULLSCREEN = false;
	public static final boolean SMOOTH_DELTAS = true;
	public static final boolean SHOW_FPS = true;
	public static final int MAX_FPS = 60;
	
	private CollisionManager collisionManager;
	
	private Unit player;
	private Platform floor;
	
	private int lives;
}
