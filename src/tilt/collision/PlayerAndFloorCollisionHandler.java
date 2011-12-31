package tilt.collision;

import org.newdawn.slick.geom.Vector2f;

import tilt.gameplay.ICollidableObject;
import tilt.gameplay.Player;

public class PlayerAndFloorCollisionHandler implements ICollisionHandler
{
	public PlayerAndFloorCollisionHandler()
	{
		// nothing to init
	}

	@Override
	public int getCollider1Type()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getCollider2Type()
	{
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void performCollision( ICollidableObject collidable1 , ICollidableObject collidable2 )
	{
		if( !collidable1.isCollidingWith(collidable2) ) return;
		
		Player player = null;
		ICollidableObject object = null; // which is the floor
		
		// cast correct objects
		if( collidable1 instanceof Player )
		{
			player = (Player)collidable1;
			object = collidable2;
		}
		else
		{
			player = (Player)collidable2;
			object = collidable1;
		}

		//obtain a copy of direction
		Vector2f direction = player.getDirection().copy();
		// reverse it
		//direction.set( direction.x*-1 , direction.y*-1 );
		// backtrack the position of the ball until it no longer collides with
		// the paddle/brick
		do
		{
			Vector2f pos = player.getPosition();
			player.setPosition(new Vector2f( pos.x + direction.x, pos.y - direction.y) );
		}
		while(player.isCollidingWith(object));
		
		player.setSpeed(0f);
		//player.setPosition(new Vector2f( player.getPosition().x , 300 ));
	}

}
