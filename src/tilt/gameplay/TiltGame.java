package tilt.gameplay;

import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import tilt.collision.CollisionManager;

public class TiltGame extends StateBasedGame
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
	public void initStatesList(GameContainer gc) throws SlickException
	{
		this.addState(new GameplayState());
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
}
