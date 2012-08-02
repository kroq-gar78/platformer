package org.newdawn.fizzy;

import org.jbox2d.collision.shapes.CircleShape;

/**
 * A simple wrapper class which only has a constructor for JBox2D's CircleShape
 * 
 * @author kroq-gar78
 */
public class Circle extends CircleShape
{
	/**
	 * Create a new circle with the given radius
	 * 
	 * @param radius - the radius of the new circle
	 */
	public Circle(float radius)
	{
		super();
		super.m_radius = radius;
	}
}
