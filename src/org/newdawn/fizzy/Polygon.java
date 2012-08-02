package org.newdawn.fizzy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class Polygon extends PolygonShape
{
	/**
	 * Create a new polygon with the given vertices
	 * 
	 * @param vertices - the vertices of the polygon
	 */
	public Polygon(Vec2[] vertices)
	{
		super();
		super.set(vertices, vertices.length);
	}
	
	/**
	 * Create a new axis-aligned rectangle with the given parameters
	 * 
	 * @param hx - the half-width (along the x-axis)
	 * @param hy - the half-height (along the y-axis)
	 */
	public Polygon(float hx, float hy)
	{
		super();
		super.setAsBox(hx, hy);
	}
	
	/**
	 * Create a new rectangle with the given parameters
	 * 
	 * @param hx - the half-width (along the x-axis)
	 * @param hy - the half-height (along the y-axis)
	 * @param center - the center of the box in local coordinates
	 * @param angle - the rotation of the box in local coordinates
	 */
	public Polygon(float hx, float hy, Vec2 center, float angle)
	{
		super();
		super.setAsBox(hx, hy, center, angle);
	}
}
