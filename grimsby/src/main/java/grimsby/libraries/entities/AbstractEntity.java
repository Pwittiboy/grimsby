/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Abstract class to hold high level details for all entities.
 *
 * @author Christopher Friis (cxf798)
 * @version 1 Mar 2018
 */
public abstract class AbstractEntity {
	
	/**
	 * Represents primary key and entity id column.
	 */
	protected int id;
	
	/**
	 * Represents created_at column, signifying when the entry was added to the database.
	 */
	protected Timestamp timestamp;
	
	/**
	 * Default getter method for entity id.
	 * @return The id of the entity.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Default getter method for entity timestamp.
	 * @return The timestamp when the entity was added to the database.
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

}
