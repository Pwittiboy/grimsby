package grimsby.dataaccess.dao.factory;

import java.sql.Connection;

import grimsby.dataaccess.jdbc.DbConnectTest;
import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl;

/**
 * DAO factory that connects to the local test database.
 * 
 * @author Christopher Friis (cxf798)
 * @version 9 Mar 2018
 */
public class DAOFactoryTest {

	/**
	 * The instance of the DAO factory that will be used to provide a single connection.
	 */
	private static DAOFactoryTest INSTANCE = null;

	/**
	 * Connection configuration that all DAOs will utilise.
	 */
	private Connection connection;

	/**
	 * Constructor is private so that only one instance is ever created, and likewise only one connection session to the database.
	 */
	private DAOFactoryTest() {
		connection = DbConnectTest.createConnection();
		INSTANCE = this;
	}
	
	/**
	 * Creates an instance of the test DAO factory if it does not exist and returns it.
	 * @return Instance of the test DAO factory.
	 */
	public static DAOFactoryTest getInstance() {
		if (INSTANCE == null) {
			return new DAOFactoryTest();
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
