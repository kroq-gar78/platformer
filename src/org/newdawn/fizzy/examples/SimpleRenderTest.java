package org.newdawn.fizzy.examples;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.World;

/**
 * Simple test for the debug rendering system
 * 
 * @author kevin
 */
public class SimpleRenderTest extends AbstractTest {
	/**
	 * Create a simulation and run it through the renderer
	 * 
	 * @param argv Arguments passed to the test
	 */
	public static void main(String[] argv) {
		SimpleRenderTest test = new SimpleRenderTest();
		test.startInWindow();
	}

	@Override
	public World createWorld() {
		World world = new World(new Vec2(800,800));
		
		for (int i=0;i<5;i++) {
			CircleShape cs = new CircleShape();
			cs.m_radius = 10.0f;
			Body body = new Body(cs, i, 20*i);
			world.add(body);
		}
		PolygonShape floorShape = new PolygonShape();
		floorShape.setAsBox(100.0f, 10.0f);
		Body floor = new Body(floorShape, 0, -50.0f, true);
		world.add(floor);
		
		return world;
	}
}
