/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;
import grimsby.libraries.entities.GroupMemberEntity;

/**
 * DAO for group_member entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 3 Mar 2018
 */
public interface GroupMemberDAO extends AbstractDAO<GroupMemberEntity> {
	
	/**
	 * Typical SELECT statement for group members by the group they belong to.
	 * @param group The group which the group member belongs to.
	 * @return A list of group members belonging to the group.
	 * @throws SQLException
	 */
	List<GroupMemberEntity> findByGroup(GroupEntity group) throws SQLException;
	
	/**
	 * Typical SELECT statement for group members by the account they represent. 
	 * @param account The account which the group member represents.
	 * @return A list of group members representing the account.
	 * @throws SQLException
	 */
	List<GroupMemberEntity> findByAccount(AccountEntity account) throws SQLException;

}
