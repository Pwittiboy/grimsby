/**
 * 
 */
package grimsby.libraries.dataaccess.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import grimsby.libraries.dataaccess.dao.MessageDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.MessageEntity;


/**
 * DAO implementation for the Message entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public class MessageDAOImpl implements MessageDAO {

	/**
	 * Connection configuration to the database.
	 */
	private final Connection connection;
	
	/**
	 * Access to the account DAO.
	 */
	private final AccountDAOImpl adao;
	
	/**
	 * Access to the conversation DAO.
	 */
	private final ConversationDAOImpl cdao;
	
	/**
	 * DAO constructor.
	 * @param connection The connection to the database.
	 */
	public MessageDAOImpl(Connection connection) {
		this.connection = connection;
		this.adao = new AccountDAOImpl(connection);
		this.cdao = new ConversationDAOImpl(connection);
	}

	@Override
	public List<MessageEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<MessageEntity> messages = new ArrayList<>();
		String query = "SELECT * FROM messages";
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while(result.next()) {
				messages.add(new MessageEntity(result.getInt("message_id"),
						adao.findById(result.getInt("sender_id")),
						adao.findById(result.getInt("receiver_id")),
						cdao.findById(result.getInt("conversation_id")),
						result.getTimestamp("created_at"),
						result.getString("message")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return messages;
	}

	@Override
	public List<MessageEntity> findByAccount(AccountEntity account) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<MessageEntity> messages = new ArrayList<>();
		String query = "SELECT * FROM messages WHERE sender_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, account.getId());
			result = statement.executeQuery();
			while (result.next()) {
				messages.add(new MessageEntity(result.getInt("message_id"),
						adao.findById(result.getInt("sender_id")),
						adao.findById(result.getInt("receiver_id")),
						cdao.findById(result.getInt("conversation_id")),
						result.getTimestamp("created_at"),
						result.getString("message")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return messages;
	}

	@Override
	public List<MessageEntity> findByConversation(ConversationEntity conversation) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<MessageEntity> messages = new ArrayList<>();
		String query = "SELECT * FROM messages WHERE conversation_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, conversation.getId());
			result = statement.executeQuery();
			while (result.next()) {
				messages.add(new MessageEntity(result.getInt("message_id"),
						adao.findById(result.getInt("sender_id")),
						adao.findById(result.getInt("receiver_id")),
						cdao.findById(result.getInt("conversation_id")),
						result.getTimestamp("created_at"),
						result.getString("message")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return messages;
	}

	@Override
	public MessageEntity findById(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		MessageEntity message = null;
		String query = "SELECT * FROM messages WHERE message_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			result = statement.executeQuery();
			while (result.next()) {
				message = new MessageEntity(result.getInt("message_id"),
						adao.findById(result.getInt("sender_id")),
						adao.findById(result.getInt("receiver_id")),
						cdao.findById(result.getInt("conversation_id")),
						result.getTimestamp("created_at"),
						result.getString("message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		return message;
	}

	@Override
	public boolean create(MessageEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO messages (sender_id, receiver_id, conversation_id, message) VALUES (?, ?, ?, ?)";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setInt(1, entity.getSender().getId());
			statement.setInt(2, entity.getReceiver().getId());
			statement.setInt(3, entity.getConversation().getId());
			statement.setString(4, entity.getMessage());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (connection != null) {
				System.err.println("Transaction is being rolled back");
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.err.println("Rollback failed");
				}
			}
			return false;
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
	}

	@Override
	public boolean delete(MessageEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM messages WHERE message_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setInt(1, entity.getId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (connection != null) {
				System.err.println("Transaction is being rolled back");
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.err.println("Rollback failed");
				}
			}
			return false;
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
	}

	@Override
	public boolean deleteById(int entityId) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM messages WHERE message_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setInt(1, entityId);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (connection != null) {
				System.err.println("Transaction is being rolled back");
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.err.println("Rollback failed");
				}
			}
			return false;
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
	}

	
}
