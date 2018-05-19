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

import grimsby.libraries.dataaccess.dao.GroupMemberDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;
import grimsby.libraries.entities.GroupMemberEntity;

/**
 * DAO implementation for GroupMember entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 3 Mar 2018
 */
public class GroupMemberDAOImpl implements GroupMemberDAO {
	
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
	public GroupMemberDAOImpl(Connection connection) {
		this.connection = connection;
		adao = new AccountDAOImpl(connection);
		gdao = new GroupDAOImpl(connection);
	}

	@Override
	public List<GroupMemberEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupMemberEntity> groupMembers = new ArrayList<>();
		String query = "SELECT * FROM group_members";
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while (result.next()) {
				groupMembers.add(new GroupMemberEntity(
						result.getInt("group_member_id"),
						gdao.findById(result.getString("group_id")),
						adao.findById(result.getInt("account_id")),
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
		
		return groupMembers;
	}

	@Override
	public GroupMemberEntity findById(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		GroupMemberEntity groupMember = null;
		String query = "SELECT * FROM group_members WHERE group_member_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				groupMember = new GroupMemberEntity(
						result.getInt("group_member_id"),
						gdao.findById(result.getString("group_id")),
						adao.findById(result.getInt("account_id")),
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
		return groupMember;
	}

	@Override
	public boolean create(GroupMemberEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO group_members (group_id, account_id) VALUES (?,?)";

		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, entity.getGroup().getId());
			statement.setInt(2, entity.getAccount().getId());
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
	public boolean delete(GroupMemberEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM group_members WHERE group_member_id = ?";
		
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
		String query = "DELETE FROM group_members WHERE group_member_id = ?";
		
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

	@Override
	public List<GroupMemberEntity> findByGroup(GroupEntity group) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupMemberEntity> groupMembers = new ArrayList<>();
		String query = "SELECT * FROM group_members WHERE group_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, group.getId());
			result = statement.executeQuery();
			while (result.next()) {
				groupMembers.add(new GroupMemberEntity(
						result.getInt("group_member_id"),
						gdao.findById(result.getString("group_id")),
						adao.findById(result.getInt("account_id")), 
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
		return groupMembers;
	}

	@Override
	public List<GroupMemberEntity> findByAccount(AccountEntity account) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<GroupMemberEntity> groupMembers = new ArrayList<>();
		String query = "SELECT * FROM group_members WHERE account_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, account.getId());
			result = statement.executeQuery();
			while (result.next()) {
				groupMembers.add(new GroupMemberEntity(
						result.getInt("group_member_id"),
						gdao.findById(result.getString("group_id")),
						adao.findById(result.getInt("account_id")), 
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
		return groupMembers;
	}

}
