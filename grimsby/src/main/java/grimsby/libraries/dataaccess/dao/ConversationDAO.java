/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.GroupEntity;

/**
 * DAO for conversation entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public interface ConversationDAO extends AbstractDAO<ConversationEntity>{
	
	/**
	 * Typical SELECT statement for a conversation by the owner account. 
	 * @param account The conversation's owner.
	 * @return A list of conversations owned by the given account.
	 * @throws SQLException
	 */
	List<ConversationEntity> findByOwner(AccountEntity account) throws SQLException;

	/**
	 * Typical SELECT statement for a conversation by the group it belongs to.
	 * @param group The group the conversation belongs to.
	 * @return The conversation owned by the group.
	 * @throws SQLException
	 */
	ConversationEntity findByGroup(GroupEntity group) throws SQLException;

}
