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

import grimsby.libraries.dataaccess.dao.ParticipantDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.ParticipantEntity;

/**
 * DAO implementation for Participant entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public class ParticipantDAOImpl implements ParticipantDAO {
	
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
	public ParticipantDAOImpl(Connection connection) {
		this.connection = connection;
		this.adao = new AccountDAOImpl(connection);
		this.cdao = new ConversationDAOImpl(connection);
	}

	@Override
	public List<ParticipantEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<ParticipantEntity> participants = new ArrayList<>();
		String query = "SELECT * FROM participants";
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while (result.next()) {
				participants.add(new ParticipantEntity(
						result.getInt("participant_id"),
						adao.findById(result.getInt("account_id")),
						cdao.findById(result.getInt("conversation_id")),
						result.getTimestamp("created_at")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (statement != null) {
				statement.close();
			}
		}
		return participants;
	}

	@Override
	public ParticipantEntity findById(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		ParticipantEntity participant = null;
		String query = "SELECT * FROM participants WHERE participant_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				participant = new ParticipantEntity(
						result.getInt("participant_id"),
						adao.findById(result.getInt("account_id")),
						cdao.findById(result.getInt("conversation_id")),
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
		return participant;
	}

	@Override
	public List<ParticipantEntity> findByAccount(AccountEntity account) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<ParticipantEntity> participants = new ArrayList<>();
		String query = "SELECT * FROM participants WHERE account_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, account.getId());
			result = statement.executeQuery();
			while (result.next()) {
				participants.add(new ParticipantEntity(
						result.getInt("participant_id"),
						adao.findById(result.getInt("account_id")),
						cdao.findById(result.getInt("conversation_id")),
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
		return participants;
	}

	@Override
	public List<ParticipantEntity> findByConversation(ConversationEntity conversation) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<ParticipantEntity> participants = new ArrayList<>();
		String query = "SELECT * FROM participants WHERE conversation_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, conversation.getId());
			result = statement.executeQuery();
			while (result.next()) {
				participants.add(new ParticipantEntity(
						result.getInt("participant_id"),
						adao.findById(result.getInt("account_id")),
						cdao.findById(result.getInt("conversation_id")),
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
		return participants;
	}

	@Override
	public boolean create(ParticipantEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO participants (account_id, conversation_id) VALUES (?,?)";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setInt(1, entity.getAccount().getId());
			statement.setInt(2, entity.getConversation().getId());
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
	public boolean delete(ParticipantEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM participants WHERE participant_id = ?";
		
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
		String query = "DELETE FROM participants WHERE participant_id = ?";
		
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
