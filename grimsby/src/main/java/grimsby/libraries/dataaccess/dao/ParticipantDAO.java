/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.ParticipantEntity;

/**
 * DAO for participant DAO.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public interface ParticipantDAO extends AbstractDAO<ParticipantEntity> {

	/**
	 * Typical SELECT statement for participants by the account they represent.
	 * @param account The account that the participants represent.
	 * @return The participants representing the given account.
	 * @throws SQLException
	 */
	List<ParticipantEntity> findByAccount(AccountEntity account) throws SQLException;
	
	/**
	 * Typical SELECT statement for participants by the conversation they belong to.
	 * @param conversation The conversation that the participants belong to.
	 * @return The participants belonging to the given conversation.
	 * @throws SQLException
	 */
	List<ParticipantEntity>findByConversation(ConversationEntity conversation) throws SQLException;
	
}
