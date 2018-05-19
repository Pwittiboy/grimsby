/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.MessageEntity;


/**
 * DAO for message entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public interface MessageDAO extends AbstractDAO<MessageEntity> {
	
	/**
	 * Typical SELECT statement for messages that belong to a given account.
	 * @param account The account which the message belongs to.
	 * @return A list of messages belonging to the given account.
	 * @throws SQLException
	 */
	List<MessageEntity> findByAccount(AccountEntity account) throws SQLException;
	
	/**
	 * Typical SELECT statement for messages that belong to a given conversation.
	 * @param conversation The conversation which the message belongs to.
	 * @return A list of messages belonging to the given conversation.
	 * @throws SQLException
	 */
	List<MessageEntity> findByConversation(ConversationEntity conversation) throws SQLException;
	
}
