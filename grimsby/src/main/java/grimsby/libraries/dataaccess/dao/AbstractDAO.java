/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * An abstract DAO interface for DAO interfaces to extend.
 *
 * @author Christopher Friis (cxf798)
 * @version 1 Mar 2018
 * @param <T>
 */
public interface AbstractDAO<T> {
	
	/**
	 * Find all records of entity type in the database.
	 * @return All records of the entity type in the database.
	 * @throws SQLException
	 */
	List<T> findAll() throws SQLException;
	
	/**
	 * Find the entity by its unique id.
	 * @param id The entity's unique id.
	 * @return The entity that matches the given id.
	 * @throws SQLException
	 */
	T findById(int id) throws SQLException;
	
	/**
	 * Create a new record for the entity and insert it into the database.
	 * @param entity Entity to be added to the database.
	 * @return True if the entity is successfully added to the database.
	 * @throws SQLException 
	 */
	boolean create(final T entity) throws SQLException;
	
	/**
	 * Delete an existing record for the given entity in the database if it exists.
	 * @param entity Entity to be deleted from the database.
	 * @return True if the entity is successfully removed from the database.
	 * @throws SQLException
	 */
	boolean delete(final T entity) throws SQLException;
	
	/**
	 * Delete an existing record for the given entity in the database if it exists.
	 * @param entityId Unique id of entity to be deleted from the database.
	 * @return True if the entity is successfully removed from the database.
	 * @throws SQLException
	 */
	boolean deleteById(final int entityId) throws SQLException;

}
