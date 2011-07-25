package tilt.collision;

import tilt.gameplay.ICollidableObject;;

public interface ICollisionHandler
{
	
	public int getCollider1Type();
	public int getCollider2Type();
	
	public void performCollision( ICollidableObject collidable1 , ICollidableObject collidable2 );
}
