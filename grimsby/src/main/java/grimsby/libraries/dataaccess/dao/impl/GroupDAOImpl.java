/**
 * 
 */
package grimsby.libraries.dataaccess.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import grimsby.libraries.dataaccess.dao.GroupDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;

/**
 * DAO implementation for Group entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 3 Mar 2018
 */
public class GroupDAOImpl implements GroupDAO {
	
	/**
	 * Connection configuration to the database.
	 */
	private final Connection connection;
	
	/**
	 * Access to the account DAO.
	 */
	private final AccountDAOImpl adao;
	
	/**
	 * DAO constructor.
	 * @param connection The connection to the database.
	 */
	public GroupDAOImpl(Connection connection) {
		this.connection = connection;
		this.adao = new AccountDAOImpl(connection);
	}

	@Override
	public List<GroupEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupEntity> groups = new ArrayList<>();
		String query = "SELECT * FROM groups";
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while (result.next()) {
				groups.add(new GroupEntity(
						result.getString("group_id"),
						adao.findById(result.getInt("owner_id")),
						result.getString("group_name"),
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
		return groups;
	}

	@Override
	public GroupEntity findById(String id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		GroupEntity group = null;
		String query = "SELECT * FROM groups WHERE group_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				group = new GroupEntity(
						result.getString("group_id"),
						adao.findById(result.getInt("owner_id")),
						result.getString("group_name"),
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
		return group;
	}

	@Override
	public boolean create(GroupEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO groups (group_id, owner_id, group_name, created_at) VALUES (?,?,?,?)";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, entity.getId());
			statement.setInt(2, entity.getOwner().getId());
			statement.setString(3, entity.getGroupName());
			statement.setTimestamp(4, entity.getTimestamp());
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
	public boolean delete(GroupEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM groups WHERE group_id LIKE ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, entity.getId());
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
	public boolean deleteById(String entityId) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM groups WHERE group_id LIKE ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, entityId);
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
	public List<GroupEntity> findByOwner(AccountEntity owner) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupEntity> groups = new ArrayList<>();
		String query = "SELECT * FROM groups WHERE owner_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, owner.getId());
			result = statement.executeQuery();
			while (result.next()) {
				groups.add(new GroupEntity(
						result.getString("group_id"),
						adao.findById(result.getInt("owner_id")),
						result.getString("group_name"),
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
		return groups;
	}

	@Override
	public List<GroupEntity> findByGroupName(String groupName) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupEntity> groups = new ArrayList<>();
		String query = "SELECT * FROM groups WHERE group_name ILIKE ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, groupName);
			result = statement.executeQuery();
			while (result.next()) {
				groups.add(new GroupEntity(
						result.getString("group_id"),
						adao.findById(result.getInt("owner_id")),
						result.getString("group_name"),
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
		return groups;
	}
	
	@Override
	public GroupEntity findByTimestamp(Timestamp timestamp) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		GroupEntity group = null;
		String query = "SELECT * FROM groups WHERE created_at = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setTimestamp(1, timestamp);
			result = statement.executeQuery();
			if (result.next()) {
				group = new GroupEntity(
						result.getString("group_id"),
						adao.findById(result.getInt("owner_id")),
						result.getString("group_name"),
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
		return group;
	}

}
