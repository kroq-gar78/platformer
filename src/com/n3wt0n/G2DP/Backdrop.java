package com.n3wt0n.G2DP;

import java.util.Arrays;
import java.util.Hashtable;

import org.newdawn.slick.BigImage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Backdrop {

	/*
	 * The "backdrop layer manager" handles the rendering of background images.
	 * Gives the illusion of depth. Far away things move slower in your field of
	 * vision compared to things closer.
	 * 
	 * Images should be sorted by ascending image.width() so that wider images
	 * are rendered last
	 */

	protected BigImage[] image;

	protected int length;

	// Render the top-left-most portion of the image here
	protected float x;
	protected float y;

	protected int mapWidth;
	protected int mapHeight;
	protected int screenWidth;
	protected int screenHeight;
	protected int playerX, playerY;

	/**
	 * Create a Backdrop for parallax scrolling in the background.
	 * 
	 * @param mapWidth The width of the TiledMap (in pixels).
	 * @param mapHeight The height of the TiledMap (in pixels).
	 * @param screenWidth The width of the application (in pixels).
	 * @param screenHeight The height of the application (in pixels).
	 */
	public Backdrop(int mapWidth, int mapHeight, int screenWidth,
			int screenHeight) {
		length = 0;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	/** 
	 * Render the backdrop based on a certain location.
	 * 
	 * @param xVal Location along the x-plane (usually the player's X position in pixels).
	 * @param yVal Location along the y-plane (usually the player's Y position in pixels).
	 * @throws SlickException
	 */
	public void render(float xVal, float yVal) throws SlickException {
		float xPercent = (xVal - screenWidth) / (mapWidth - screenWidth);		// subtracting screenWidth prevents percent from
		float yPercent = (yVal - screenHeight) / (mapHeight - screenHeight);	// calculating as > 0 when at left-most part of world.
		
		float xOffset = 0, yOffset = 0;
		float xShift = 0, yShift = 0;
		
		for (int i = 0; i < length; i++) {
			xOffset = image[i].getWidth() - screenWidth;
			yOffset = image[i].getHeight() - screenHeight;
			
			xShift = (xOffset * xPercent) * (-1);
			yShift = (yOffset * yPercent) * (-1);
			
			image[i].draw(xShift, yShift);
		}
	}

	/**
	 * Add an image to the Backdrop. The image is placed in order of increasing width.
	 * 
	 * @param newImage The Image to add.
	 */
	public void add(BigImage newImage) {
		length = (this.image == null) ? 0 : this.image.length;
		BigImage[] temp = new BigImage[length + 1];
		int i = 0;
		for (; i < length; i++) {
			temp[i] = image[i];
		}
		temp[i] = newImage;
		image = temp;
		temp = null;
		length++;
	}

	/**
	 * Get the Image from the Backdrop at the specified index.
	 * 
	 * @param index The index reference of the image.
	 * @return The background image.
	 */
	public Image getBackdropAtIndex(int index) {
		return this.image[index];
	}

	/**
	 * Get the length of the Backdrop images array.
	 * 
	 * @return The number of images that make up the Backdrop.
	 */
	public int getLength() {
		return this.image.length;
	}

	/**
	 * Sort the Backdrop images array in order of ascending Image width.
	 */
	public void sort() {
		BigImage[] temp;
		Hashtable<Integer, Integer> originalIndex = new Hashtable<Integer, Integer>();
		int[] index = new int[image.length];

		// Create the hashtable indexing the original image locations
		// and record the width in the array to sort
		for (int i = 0; i < image.length; i++) {
			originalIndex.put(image[i].getWidth(), i);
			index[i] = image[i].getWidth();
		}

		Arrays.sort(index);

		temp = new BigImage[image.length];
		for (int i = 0; i < image.length; i++) {
			// System.out.print (index[i]);
			// System.out.println (" " + originalIndex.get(index[i]));

			// Pull the images from the original image array and
			// place them, sorted, into the temporary image array
			temp[i] = image[originalIndex.get(index[i])];
			// System.out.println("Copying into index " + i +
			// " from image index "
			// + originalIndex.get(index[i]) + " (image width: "
			// + image[i].getWidth() + ")");

			// remove the particular image index, to avoid confusion
			// with any other images that may have the same width
			originalIndex.remove(originalIndex.get(index[i]));
		}

		// finish it off by copying the temporary array over to the official
		// image array
		image = temp;
	}
}
