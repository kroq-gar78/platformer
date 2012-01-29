package com.n3wt0n.G2DP;

import java.util.LinkedList;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Camera {

	protected String gstr = "";

	// to draw platform boundaries
	protected boolean drawBounds = false;

	protected int[][] foreAlpha;
	protected Image[] foreAlphaImage;
	protected int foreAlphaIndex = 0;
	private int foregroundLayerIndex;
	protected boolean updateForeground = false;
	private byte[][] foregroundTiles;

	protected int lastPlayerX = 0, lastPlayerY = 0;
	protected int curPlayerX = lastPlayerX, curPlayerY = lastPlayerY;
	float playerStartX;

	protected float playerStartY;
	protected boolean changedPosition = false;
	// True if player moved from foreground area to open space
	private boolean movedIntoOpen = false;

	LinkedList<Entity> entity;

	// The player's location on the screen is different
	// than 'x' and 'y'. Imagine the viewing area of
	// the screen is 400 pixels wide and x = 600 then
	// we would not be able to see the player. He would
	// be off screen. 'playerX' will be set to an appropriate
	// screen position.
	protected float vPlayerX, vPlayerY;

	// Where the map would be drawn, if we did not
	// care about rendering only the visible tiles
	protected int camX = 0, camY = 0;

	// The TOP LEFT tile of the map that is drawn
	// When tiles are off screen, they do not need to be drawn.
	protected int tileOffsetX = 0, tileOffsetY = 0;
	// The viewing area of the screen
	protected int screenWidth, screenHeight;

	// The actual width (including what is off-screen) of the map
	protected int mapWidth, mapHeight;

	// The width and height of tiles
	protected int tileWidth, tileHeight;

	protected int tileIndexX;
	protected int tileIndexY;

	// initial number of pixels to the right of the screen.
	protected float initDiffX = 0.0f;
	protected float initDiffY = 0.0f;
	// Pixels of map still to the right of the screen.
	protected float screenRight = 0.0f;
	protected float screenDown = 0.0f;

	protected int foregroundAlphaRadius = 1;

	protected Entity player;
	protected TiledMap map;
	protected Backdrop backdrop;
	protected MapUtil mapUtil;

	int vX;
	int vY;

	int vAcross;
	int vDown;

	/**
	 * Create a Camera.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param map
	 *            The TiledMap to associate with the Camera.
	 * @param mapUtil
	 *            The MapUtil to associate with the Camera.
	 * @param player
	 *            The Entity representing the player.
	 * @param backdrop
	 *            The Backdrop to associate with the Camera.
	 */
	public Camera(GameContainer gc, TiledMap map, MapUtil mapUtil,
			Entity player, Backdrop backdrop) {
		this.player = player;
		this.map = map;
		this.mapUtil = mapUtil;
		this.backdrop = backdrop;

		this.screenWidth = gc.getWidth();
		this.screenHeight = gc.getHeight();

		this.tileWidth = map.getTileWidth();
		this.tileHeight = map.getTileHeight();

		// Width only returns the number of tiles across
		// So we multiply by the width of the tiles.
		this.mapWidth = map.getWidth() * tileWidth;
		this.mapHeight = map.getHeight() * tileHeight;

		entity = new LinkedList<Entity>();

		this.init();
	}

	/**
	 * Create a Camera.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param map
	 *            The TiledMap to associate with the Camera.
	 * @param mapUtil
	 *            The MapUtil to associate with the Camera.
	 * @param player
	 *            The Entity representing the player.
	 */
	public Camera(GameContainer gc, TiledMap map, MapUtil mapUtil, Entity player) {
		this.player = player;
		this.map = map;
		this.mapUtil = mapUtil;

		this.screenWidth = gc.getWidth();
		this.screenHeight = gc.getHeight();

		this.tileWidth = map.getTileWidth();
		this.tileHeight = map.getTileHeight();

		// Width only returns the number of tiles across
		// So we multiply by the width of the tiles.
		this.mapWidth = map.getWidth() * tileWidth;
		this.mapHeight = map.getHeight() * tileHeight;

		entity = new LinkedList<Entity>();

		this.init();
	}

	/**
	 * Initialise the Camera.
	 */
	public void init() {

		foregroundLayerIndex = map.getLayerIndex("FOREGROUND");

		/*
		 * Required for the maths. That's the truthiness of the matter. Trust me.
		 */
		if (mapWidth < screenWidth)
			screenWidth = mapWidth;
		if (mapHeight < screenHeight)
			screenHeight = mapHeight;

		// these two lines for use with the backdrop
		initDiffX = mapWidth - screenWidth;
		initDiffY = mapHeight - screenHeight;
		screenRight = initDiffX;
		screenDown = initDiffY;

		this.initToPlayerPosition();

		this.initForegroundTiles();

		setForegroundAlphaRadius(3);
	}

	/**
	 * Map the foreground tile locations.
	 */
	private void initForegroundTiles() {

		foregroundTiles = new byte[map.getWidth()][map.getHeight()];

		for (int y = 0; y < foregroundTiles[0].length; y++) {
			for (int x = 0; x < foregroundTiles.length; x++) {
				if (mapUtil.isForegroundAtTile(x, y)) {
					foregroundTiles[x][y] = 1;
				} else {
					foregroundTiles[x][y] = 0;
				}
			}
		}
	}

	/**
	 * Render the TiledMap, Player and Backdrop.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param g
	 *            Java Graphics.
	 * @throws SlickException
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {

		if (backdrop != null) {
			backdrop.render(screenRight, screenDown);
		}

		g.translate(camX, camY);

		tileOffsetX = (camX % mapWidth);
		tileOffsetY = (camY % mapHeight);
		tileIndexX = -1 * (camX / mapWidth);
		tileIndexY = -1 * (camY / mapHeight);
		
		// Render background, then entities, then foreground.
		
		renderLayer("BACKGROUND");

		for (Entity e : entity) {
			e.render(gc, g);
		}

		renderLayer("PLATFORMS");
		player.render(gc, g);

		// Foreground gets a special method due to the "secret area" factor
		renderFore();

		g.translate(-camX, -camY);
	}

	/**
	 * Render a TiledMap layer with name layerName.
	 * 
	 * @param layerName
	 *            The name of the layer to render (BACKGROUND, PLATFORMS,
	 *            FOREGROUND).
	 */
	private void renderLayer(String layerName) {
		map.render(-camX + tileOffsetX, -camY + tileOffsetY, tileIndexX,
				tileIndexY, (screenWidth - tileOffsetX) / tileWidth + 1,
				(screenHeight - tileOffsetY) / tileHeight + 1, map
						.getLayerIndex(layerName), false);
	}

	/**
	 * Update Camera variables in the step.
	 * 
	 * @param gc
	 *            Slick GameContainer.
	 * @param delta
	 *            Time passed since last update.
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {

		vPlayerX = player.getX();
		vPlayerY = player.getY();

		if (vPlayerX < (screenWidth / 2)) {
			camX = 0;
			screenRight = screenWidth;
		} else if (vPlayerX > mapWidth - (screenWidth / 2)) {
			camX = screenWidth - mapWidth;
			screenRight = mapWidth;
		} else {
			camX = (int) ((screenWidth / 2) - vPlayerX);
			screenRight = vPlayerX + (screenWidth / 2);
		}
		// Same as above but for 'Y' axis
		if (vPlayerY < (screenHeight / 2)) {
			camY = 0;
			screenDown = screenHeight;
		} else if (vPlayerY > mapHeight - (screenHeight / 2)) {
			camY = screenHeight - mapHeight;
			screenDown = mapHeight;
		} else {
			camY = (int) ((screenHeight / 2) - vPlayerY);
			screenDown = vPlayerY + (screenHeight / 2);
		}

		player.setVisualLocation(vPlayerX, vPlayerY);
		player.update(delta);

		vX = (int) player.getVisualX();
		vY = (int) player.getVisualY();

		vAcross = vX / tileWidth;
		vDown = vY / tileHeight;

		curPlayerX = (int) (vPlayerX / tileWidth);
		curPlayerY = (int) (vPlayerY / tileHeight);

		movedIntoOpen = false;
		try {
			if (this.foregroundTiles[lastPlayerX][lastPlayerY] == 1 && this.foregroundTiles[curPlayerX][curPlayerY] == 0) {
				movedIntoOpen = true;
			}
		}
		catch (Exception e) {
			// This error occurs when the player character walks off screen,
			// out of the bounds of the tiled map.
		}

		changedPosition = false;
		if (this.curPlayerX != this.lastPlayerX) {
			lastPlayerX = curPlayerX;
			changedPosition = true;
		}
		if (this.curPlayerY != this.lastPlayerY) {
			lastPlayerY = curPlayerY;
			changedPosition = true;
		}

		if (changedPosition) {
			updateForeground = true;
		}
	}

	/**
	 * Something you shouldn't bother yourself with. Storing tile information
	 * for alpha blending.
	 * 
	 * @param x
	 *            Tile location in x-plane.
	 * @param y
	 *            Tile location in y-plane.
	 * @param id
	 *            TiledMap Tile id.
	 * @param image
	 *            TiledMap Tile image.
	 */
	private void addToForegroundAlpha(int x, int y, int id, Image image) {
		// I forget what's going on here, but it works. #twomonthslater
		if (image != null) {
			foreAlphaImage[foreAlphaIndex] = image;
			foreAlpha[foreAlphaIndex][0] = x;
			foreAlpha[foreAlphaIndex][1] = y;
			foreAlpha[foreAlphaIndex++][2] = id;
			foreAlphaIndex %= foreAlpha.length;
		}
	}

	/**
	 * Something you shouldn't bother yourself with. Determine the size of the
	 * array to store tile information for alpha blending.
	 * 
	 * @param rad
	 *            Radius of alpha blended tiles.
	 * @return Number of tiles effected within a radius.
	 */
	public int determineForegroundAlphaSize(int rad) {
		int size = 0;
		// circumference is twice the radius (left side + right side)
		// plus one for the tile the player is occupying.
		int circ = (rad * 2) + 1;
		int pos = circ / 2; // rounding up does not matter as we have x=0..circ
		// anyway
		for (int y = pos - rad; y <= pos + rad; y++) {
			for (int x = pos - rad; x <= pos + rad; x++) {
				int dX = pos - x;
				int dY = pos - y;
				float dd = (float) Math.sqrt((dX * dX) + (dY * dY));
				if (dd <= rad + .5) {
					size++;
				}
			}
		}
		return size;
	}

	/**
	 * Something you shouldn't bother yourself with. Sets the size of the array
	 * for storing tile information for alpha blending.
	 * 
	 * @param radius
	 *            Radius of alpha blended tiles.
	 */
	public void setForegroundAlphaRadius(int radius) {

		foregroundAlphaRadius = radius + 1;
		foreAlpha = new int[determineForegroundAlphaSize(foregroundAlphaRadius)][3];
		foreAlphaImage = new Image[foreAlpha.length];

		// initialize it with the 0th tile information (even if it's null)
		for (int i = 0; i < foreAlpha.length; i++) {
			foreAlpha[i][0] = 0;
			foreAlpha[i][1] = 0;
			foreAlpha[i][2] = map.getTileId(0, 0, foregroundLayerIndex);
			foreAlphaImage[i] = map.getTileImage(0, 0, foregroundLayerIndex);
		}
		foreAlphaIndex = 0;
	}

	/**
	 * Get the radius that determines which tiles are alpha blended.
	 * 
	 * @return The radius.
	 */
	public int getForegroundAlphaRadius() {
		return foregroundAlphaRadius - 1;
	}

	/**
	 * Check to see if the player's location (x,y) is behind a foreground tile.
	 * 
	 * @param x
	 *            Location in the x-plane.
	 * @param y
	 *            Location in the y-plane.
	 * @return True if the player is behind a foreground tile.
	 */
	public boolean playerIsBehindForeground(int x, int y) {
		boolean isBehind = false;
		try {
			isBehind = (foregroundTiles[x][y] == 0) ? false : true;
		}
		catch (Exception e) {
			// Error occurs when the player character walks off screen,
			// out of the bounds of the map.
		}
		return isBehind;
	}

	/**
	 * Special render method for the TiledMap foreground layer.
	 */
	private void renderFore() {

		if (playerIsBehindForeground(curPlayerX, curPlayerY) || movedIntoOpen) {

			// Update the non-rendered tiles if the player
			// is occupying a new tile this frame
			if (updateForeground || movedIntoOpen) {
				updateForeground = false;

				for (int i = 0; i < foreAlpha.length; i++) {
					map.setTileId(foreAlpha[i][0], foreAlpha[i][1],
							foregroundLayerIndex, foreAlpha[i][2]);
				}
				foreAlphaIndex = 0;
			}

			if (changedPosition && !movedIntoOpen) {
				for (int y = curPlayerY - foregroundAlphaRadius; y <= curPlayerY
						+ foregroundAlphaRadius; y++) {
					for (int x = curPlayerX - foregroundAlphaRadius; x <= curPlayerX
							+ foregroundAlphaRadius; x++) {
						int dX = curPlayerX - x;
						int dY = curPlayerY - y;
						float dd = (float) Math.sqrt((dX * dX) + (dY * dY));
						if (dd <= foregroundAlphaRadius + 0.5 && x >= 0
								&& y >= 0 && x < map.getWidth()
								&& y < map.getHeight()) {
							addToForegroundAlpha(x, y, map.getTileId(x, y,
									foregroundLayerIndex), map.getTileImage(x,
									y, foregroundLayerIndex));
							map.setTileId(x, y, foregroundLayerIndex, 0);
						}
					}
				}
			}
		}
		renderLayer("FOREGROUND");

		if (playerIsBehindForeground(curPlayerX, curPlayerY)) {
			for (int i = 0; i < foreAlphaImage.length; i++) {
				try {
					int x = foreAlpha[i][0];
					int y = foreAlpha[i][1];
					float dX = (vPlayerX / tileWidth) - x;
					float dY = (vPlayerY / tileHeight) - y;
					float dd = (float) Math.sqrt((dX * dX) + (dY * dY));
					float alpha = (dd / foregroundAlphaRadius) - 0.25f;
					Image image = foreAlphaImage[i];
					image.setAlpha(alpha);
					image.draw(x * tileWidth, y * tileHeight);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Initialise the Camera variables to the player position.
	 */
	private void initToPlayerPosition() {
		// Cycle through all of the map objects
		// initialise the player when the player object is found
		for (int i = 0; i < map.getObjectGroupCount(); i++) {
			for (int j = 0; j < map.getObjectCount(i); j++) {
				if (map.getObjectName(i, j).equalsIgnoreCase("Player")) {
					player.setX(map.getObjectX(i, j) + (player.getWidth() / 2));
					player.setY(map.getObjectY(i, j));
					this.setPlayerStartXY(player.getX(), player.getY());
				}
			}
		}
		vPlayerX = player.getX();
		vPlayerY = player.getY();

		lastPlayerX = (int) vPlayerX / tileWidth;
		lastPlayerY = (int) vPlayerY / tileHeight;
		curPlayerX = lastPlayerX;
		curPlayerY = lastPlayerY;
	}

	public void drawPlatformBodies(Graphics g, World world) {
		if (drawBounds) {
			BodyList bodies = world.getBodies();
			for (int i = 0; i < bodies.size(); i++) {
				Body body = bodies.get(i);
				if (body instanceof StaticBody) {
					drawBody(g, body);
				}
			}
		}
	}

	public void setDrawBounds(boolean boo) {
		drawBounds = boo;
	}

	public boolean drawBounds() {
		return drawBounds;
	}

	public void drawBody(Graphics g, Body body) {
		if (body.getShape() instanceof Box) {
			drawBoxBody(g, body, (Box) body.getShape());
		}
	}

	public void drawBoxBody(Graphics g, Body body, Box box) {
		Vector2f[] pts = box.getPoints(body.getPosition(), body.getRotation());

		Vector2f v1 = pts[0];
		Vector2f v2 = pts[1];
		Vector2f v3 = pts[2];
		Vector2f v4 = pts[3];

		g.translate(camX, camY);
		g.setColor(new Color(255, 255, 255));
		g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
		g.drawLine((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
		g.drawLine((int) v3.x, (int) v3.y, (int) v4.x, (int) v4.y);
		g.drawLine((int) v4.x, (int) v4.y, (int) v1.x, (int) v1.y);
		g.translate(-camX, -camY);
	}

	/**
	 * Record the starting position of the player, in the map.
	 * 
	 * @param x
	 *            The starting x-location of the map
	 * @param y
	 *            The starting y-location of the map
	 */
	public void setPlayerStartXY(float x, float y) {
		this.playerStartX = x;
		this.playerStartY = y;
	}

	/**
	 * Get the starting x-location of the player on the map.
	 * 
	 * @return The player's starting x-location
	 */
	public float getPlayerStartX() {
		return this.playerStartX;
	}

	/**
	 * Get the starting y-location of the player on the map.
	 * 
	 * @return The player's starting y-location
	 */
	public float getPlayerStartY() {
		return this.playerStartY;
	}
	
	/**
	 * Add entity to the drawing list
	 * @param e Entity to add to the drawing list
	 */
	public void addEntity(Entity e) {
		entity.add(e);
	}
	/**
	 * Remove entity to the drawing list
	 * @param e Entity to remove from the drawing list
	 */
	public void removeEntity(Entity e)
	{
		entity.remove(e);
	}

	public void drawEntity(Entity e, GameContainer gc, Graphics g)
			throws SlickException {
		g.translate(camX, camY);
		e.render(gc, g);
		g.translate(-camX, -camY);
	}

	public static void main(String[] args) {

		int mapHeight = 12;
		int mapWidth = 30;
		int[][] map = new int[mapWidth][mapHeight];

		int radius = 3;
		int pX = mapWidth / 2;
		int pY = mapHeight / 2;

		String out = "";
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				map[x][y] = 1;
				out += map[x][y];
			}
			out += "\n";
		}

		System.out.println("Map initialised\nPlaying with radius.");
		int theSize = 0;
		for (int y = pY - radius; y <= pY + radius; y++) {
			for (int x = pX - radius; x <= pX + radius; x++) {
				int dX = pX - x;
				int dY = pY - y;
				float dd = (float) Math.sqrt((dX * dX) + (dY * dY));
				if (dd <= radius) {
					map[x][y] = 0;
					theSize++;
				}
			}
		}
		map[pX][pY] = 8;

		out = "";
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				out += map[x][y];
			}
			out += "\n";
		}

		System.out.println(out + "Done");

		// Camera cam = new Camera();
		// int size = cam.determineBucketSize(radius);
		// System.out.println ("Size of " + radius + ": " + size);

	}
}
