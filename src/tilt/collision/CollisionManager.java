package tilt.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tilt.gameplay.ICollidableObject;

public class CollisionManager
{
	public CollisionManager()
	{
		collidables = new HashMap<Integer , List<ICollidableObject>> ();
		collisionTypes = new HashMap<Integer , List<Integer>>();
		collisionHandlers = new HashMap<String , ICollisionHandler>();
	}
	
	/**
	 * Generates a key for the two collision types in the form of
	 * <smaller type>-<bigger type>
	 * @param type1 the first type
	 * @param type2 the second type
	 * @return a string with the key in the given format
	 */
	public static String getKey(int type1, int type2)
	{
		// generate key <smaller type>-<bigger type>
		return (type1 < type2) ? type1+"-"+type2 : type2+"-"+type1; 
	}
	
	/**
	 * Adds a new object for the collision bag, from this point on this object will be checked
	 * for collisions.
	 * 
	 * @param collidable - the collidable object to add
	 */
	public void addCollidable( ICollidableObject collidable )
	{
		//obtain the entry for this type
		List<ICollidableObject> collidableList = collidables.get( collidable.getCollisionType() );
		
		//if there is no entry for this type, add a new one 
		if( collidableList == null )
		{
			collidableList = new ArrayList<ICollidableObject>();
			collidables.put( collidable.getCollisionType() , collidableList );
		}
		
		collidableList.add( collidable );
	}
	/**
	 * Removes an object from the collidable bag, from this point on the object 
	 * shall not be used for collisions.
	 * 
	 * If there is no entry for this object nothing is done.
	 * 
	 * @param collidable the collidable object to remove
	 */
	public void removeCollidable( ICollidableObject collidable )
	{
		//obtain the entry for this type
		List<ICollidableObject> collidableList = collidables.get( collidable.getCollisionType() );
		
		//if entry exists, remove from 45list (if possible)
		if( collidableList != null )
		{
			collidableList.remove( collidable );
		}
	}
	
	/**
	 * Adds a new collision handler to the manager. From this point on all the types
	 * concerning this handler (type1 and type2) will perform collisions.
	 * 
	 * @param handler - the handler to add
	 */
	public void addHandler( ICollisionHandler handler )
	{
		//generate key
		String key = getKey( handler.getCollider1Type() , handler.getCollider2Type() );
		
		// add the handler to the map
		collisionHandlers.put( key , handler );
		
		//add collision type1 to 2
		addTypesToCollision( handler.getCollider1Type() , handler.getCollider2Type() );
		//add collision type2 to 1
		addTypesToCollision( handler.getCollider2Type() , handler.getCollider1Type() );
	}
	
	/**
	 * @param type1
	 * @param type2
	 */
	private void addTypesToCollision( int type1 , int type2 )
	{
		List<Integer> typeCollisions = collisionTypes.get( type1 );
		
		// if there is no entry, create one
		if( typeCollisions == null )
		{
			typeCollisions = new ArrayList<Integer>();
			collisionTypes.put( type1 , typeCollisions );
		}
		
		//add collision to list
		typeCollisions.add( type2 );
	}
	
	public void processCollisions()
	{
		//prepare set of all keys to collide
		Set<String> allCollisionKeys = new HashSet<String>();
		
		//prepare a list of all collisions to handle
		List<CollisionData> collisions = new ArrayList<CollisionData>();
		
		Set<Integer> types = collisionTypes.keySet();
		
		//obtain every type for collision
		for( Integer type : types )
		{
			// obtain for each type it collides with
			List<Integer> collidesWithTypes = collisionTypes.get( type );
			
			for( Integer collidingType : collidesWithTypes )
			{
				// if the pair was already treated ignore it; else, treat it
				if( !allCollisionKeys.contains( getKey( type , collidingType ) ) )
				{
					//obtain all objects of "type"
					List<ICollidableObject> collidableForType = collidables.get( type );
					//obtain all object of "collidingType"
					List<ICollidableObject> collidableForCollidingType = collidables.get( collidingType );
					for( ICollidableObject collidable : collidableForType )
					{
						for( ICollidableObject collidesWith : collidableForCollidingType )
						{
							if( collidable.isCollidingWith( collidesWith ) )
							{
								CollisionData cd = new CollisionData();
								cd.handler = collisionHandlers.get( getKey( type , collidingType ) );
								cd.obj1 = collidable;
								cd.obj2 = collidesWith;
								
								collisions.add( cd );
							}
						}
					}
					
					allCollisionKeys.add( getKey( type , collidingType ) );
				}
			}
		}
		
		for( CollisionData cd : collisions )
		{
			cd.handler.performCollision( cd.obj1 , cd.obj2 );
		}
	}
	
	public void performCollision( ICollidableObject obj1 , ICollidableObject obj2 )
	{
		collisionHandlers.get( getKey( obj1.getCollisionType() , obj2.getCollisionType() ) ).performCollision( obj1 , obj2 );
	}
	
	class CollisionData
	{
		public ICollisionHandler handler;
		public ICollidableObject obj1;
		public ICollidableObject obj2;
	}
	
	//list of objects per type
	private Map<Integer , List<ICollidableObject>> collidables;
	//list of collisions per type
	private Map<Integer , List<Integer>> collisionTypes;
	//list of handlers for collisions
	private Map<String, ICollisionHandler> collisionHandlers;
}