package org.newdawn.fizzy;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class FixtureShape
{
	public FixtureDef fd;
	public Fixture fixture;
	public Shape shape;
	
	public FixtureShape()
	{
		this.fd = new FixtureDef();
	}
	public FixtureShape(FixtureDef fd, Shape shape)
	{
		this.fd = fd;
		this.shape = shape;
	}
	public FixtureShape(Fixture fix)
	{
		this.fixture = fix;
		this.shape = fix.getShape();
	}
	
	/**
	 * Set the restitution applied when this body collides
	 * 
	 * @param rest The restitution applied when this body collides
	 */
	public void setRestitution(float rest) {
		if(fixture == null) fd.restitution = rest;
		else fixture.m_restitution = rest;
	}
	
	/**
	 * Set the density of this body
	 * 
	 * @param den The density of this body
	 */
	public void setDensity(float den)
	{
		if(fixture==null) fd.density = den;
		else fixture.m_density = den;
	}
	
	public void setShape(Shape newShape)
	{
		if(fixture==null) fd.shape=newShape;
		else fixture.m_shape=newShape;
	}
}
