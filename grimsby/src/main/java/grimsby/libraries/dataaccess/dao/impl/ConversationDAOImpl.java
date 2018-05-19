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

import grimsby.libraries.dataaccess.dao.ConversationDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.GroupEntity;

/**
 * DAO implementation for Conversation entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public class ConversationDAOImpl implements ConversationDAO {

	/**
	 * Connection configuration to the database.
	 */
	private final Connection connection;

	/**
	 * Access to the account DAO.
	 */
	private final AccountDAOImpl adao;
	
	/**
	 * Access to the group DAO.
	 */
	private final GroupDAOImpl gdao;

	/**
	 * DAO constructor.
	 * @param connection The connection to the database.
	 */
	public ConversationDAOImpl(Connection connection) {
		this.connection = connection;
		this.adao = new AccountDAOImpl(connection);
		this.gdao = new GroupDAOImpl(connection);
	}

	@Override
	public List<ConversationEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<ConversationEntity> conversations = new ArrayList<>();
		String query = "SELECT * FROM conversations";

		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while(result.next()) {
				conversations.add(new ConversationEntity(
						result.getInt("conversation_id"),
						adao.findById(result.getInt("owner_id")),
						gdao.findById(result.getString("group_id")),
						result.getTimestamp("created_at")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return conversations;
	}

	@Override
	public ConversationEntity findById(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		ConversationEntity conversation = null;
		String query = "SELECT * FROM conversations WHERE conversation_id = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				conversation = new ConversationEntity(
						result.getInt("conversation_id"),
						adao.findById(result.getInt("owner_id")),
						gdao.findById(result.getString("group_id")),
						result.getTimestamp("created_at")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return conversation;
	}

	@Override
	public List<ConversationEntity> findByOwner(AccountEntity account) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<ConversationEntity> conversations = new ArrayList<>();
		String query = "SELECT * FROM conversations WHERE owner_id = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, account.getId());
			result = statement.executeQuery();
			while (result.next()) {
				conversations.add(new ConversationEntity(
						result.getInt("conversation_id"),
						adao.findById(result.getInt("owner_id")),
						gdao.findById(result.getString("group_id")),
						result.getTimestamp("created_at")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return conversations;
	}
	
	@Override
	public ConversationEntity findByGroup(GroupEntity group) throws SQLException {
		// TODO potential bug by returning a single entity and not a list
		PreparedStatement statement = null;
		ResultSet result = null;
		ConversationEntity conversation = null;
		String query = "SELECT * FROM conversations WHERE group_id ILIKE ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, group.getId());
			result = statement.executeQuery();
			while (result.next()) {
				conversation = new ConversationEntity(
						result.getInt("conversation_id"),
						adao.findById(result.getInt("owner_id")),
						gdao.findById(result.getString("group_id")),
						result.getTimestamp("created_at")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return conversation;
	}

	@Override
	public boolean create(ConversationEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO conversations (owner_id, group_id) VALUES (?, ?)";

		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setInt(1, entity.getAccount().getId());
			if (entity.getGroup()!=null) {
				statement.setString(2, entity.getGroup().getId());
			} else {
				statement.setString(2, null);
			}
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
	public boolean delete(ConversationEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM conversations WHERE conversation_id = ?";

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
		String query = "DELETE FROM conversations WHERE conversation_id = ?";

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
