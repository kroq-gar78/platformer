package org.newdawn.fizzy;

import java.util.ArrayList;

/**
 * Decription of any class representing a shape for collision. The shape 
 * both defines the geometry and the collision properties. However, these
 * can be augmented at the Body level.
 * 
 * @author kevin
 */
public interface Shape {
	
	/**
	 * The body this shape is being used in if any 
	 * 
	 * @return The body this shape is being used in if any
 	 */
	Body getBody();
	
	/**
	 * Create this shape against the body given
	 * 
	 * @param body The body to create the shape within
	 */
	void createInBody(Body body);
	
	/**
	 * Get the JBox2D shapes that build up this fizzy shape
	 *
	 * @return The shapes that build up this fizzy shape 
	 */
    ArrayList<org.jbox2d.collision.shapes.Shape> getJBoxShapes();
}
