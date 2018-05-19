/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;

/**
 * DAO for group entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 3 Mar 2018
 */
public interface GroupDAO {
	
	/**
	 * Typical SELECT statement for a group by its owner account.
	 * @param owner The group's owner.
	 * @return A list of groups owned by the given account.
	 * @throws SQLException
	 */
	List<GroupEntity> findByOwner(AccountEntity owner) throws SQLException;
	
	/**
	 * Typical SELECT statement for a group by its name.
	 * @param groupName The group's name.
	 * @return A list of groups with the given group name.
	 * @throws SQLException
	 */
	List<GroupEntity> findByGroupName(String groupName) throws SQLException;
	
	/**
	 * Create a new record for the entity and insert it into the database.
	 * @param entity The group being entered into the database.
	 * @return True if the group is successfully entered into the database.
	 * @throws SQLException 
	 */
	boolean create(GroupEntity entity) throws SQLException;
	
	/**
	 * Delete a group from database.
	 * @param entity Group to be deleted.
	 * @return True if the group is successfully removed from the database.
	 * @throws SQLException
	 */
	boolean delete(GroupEntity entity) throws SQLException;
	
	/**
	 * Delete a group from the database by its unique id.
	 * @param entityId The unique id of a group being deleted from the database.
	 * @return True if the group is successfully removed from the database.
	 * @throws SQLException
	 */
	boolean deleteById(String entityId) throws SQLException;
	
	/**
	 * Typical SELECT statement for all groups.
	 * @return All groups stored in the database.
	 * @throws SQLException
	 */
	List<GroupEntity> findAll() throws SQLException;
	
	/**
	 * Typical SELECT statement for a group of a given id.
	 * @param id The group's unique id.
	 * @return The group which the id belongs.
	 * @throws SQLException
	 */
	GroupEntity findById(String id) throws SQLException;
	
	/**
	 * Typical SELECT statement for a group by its creation timestamp.
	 * @param timestamp The timestamp the group was created.
	 * @return The group created at the given timestamp.
	 * @throws SQLException
	 */
	GroupEntity findByTimestamp(Timestamp timestamp) throws SQLException;
	
}
