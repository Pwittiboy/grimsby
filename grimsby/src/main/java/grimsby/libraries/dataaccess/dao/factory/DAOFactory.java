package grimsby.libraries.dataaccess.dao.factory;

import java.sql.Connection;

import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl;
import grimsby.libraries.dataaccess.jdbc.DbConnect;

/**
 * Factory class for getting DAOs.
 *  
 * @author Christopher Friis (cxf798)
 * @date 28 Feb 2018
 */
public class DAOFactory {
	
	/**
	 * The instance of the DAO factory that will be used to provide a single connection.
	 */
	private static DAOFactory INSTANCE = null;

	/**
	 * Connection configuration that all DAOs will utilise.
	 */
	private Connection connection;
	
	/**
	 * Constructor is private so that only one instance is ever created, and likewise only one connection session to the database.
	 */
	private DAOFactory() {
		connection = DbConnect.createConnection();
		INSTANCE = this;
	}
	
	/**
	 * Creates an instance of the DAO factory if it does not exist and returns it.
	 * @return Instance of the DAO factory.
	 */
	public static DAOFactory getInstance() {
		if (INSTANCE == null) {
			return new DAOFactory();
		} else {
			return INSTANCE;
		}
	}

	/**
	 * Getter method for the Account DAO.
	 * @return The account DAO.
	 */
	public AccountDAOImpl getAccountDAO() {
		return new AccountDAOImpl(connection);
	}
	
	/**
	 * Getter method for the Conversation DAO.
	 * @return The conversation DAO.
	 */
	public ConversationDAOImpl getConversationDAO() {
		return new ConversationDAOImpl(connection);
	}

	/**
	 * Getter method for the Group DAO.
	 * @return The group DAO.
	 */
	public GroupDAOImpl getGroupDAOImpl() {
		return new GroupDAOImpl(connection);
	}

	/**
	 * Getter method for the GroupMember DAO.
	 * @return The group member DAO.
	 */
	public GroupMemberDAOImpl getGroupMemberDAOImpl() {
		return new GroupMemberDAOImpl(connection);
	}

	/**
	 * Getter method for the Message DAO.
	 * @return The message DAO
	 */
	public MessageDAOImpl getMessageDAO() {
		return new MessageDAOImpl(connection);
	}

	/**
	 * Getter method for the Participant DAO.
	 * @return The participant DAO.
	 */
	public ParticipantDAOImpl getParticipantDAO() {
		return new ParticipantDAOImpl(connection);
	}

}
