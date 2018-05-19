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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import grimsby.libraries.dataaccess.dao.AccountDAO;
import grimsby.libraries.dataaccess.dao.factory.DAOFactory;
import grimsby.libraries.dataaccess.jdbc.PwGenerator;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;


/**
 * DAO implementation for Account entity. 
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public class AccountDAOImpl implements AccountDAO {

	/**
	 * Connection configuration to the database.
	 */
	private final Connection connection;
	
	/**
	 * Access to the group DAO for history methods.
	 */
	private GroupDAOImpl gdao;

	/**
	 * DAO constructor.
	 * @param connection The connection to the database.
	 */
	public AccountDAOImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<AccountEntity> findAll() throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<AccountEntity> accounts = new ArrayList<>();
		String query = "SELECT * FROM accounts";

		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
			while (result.next()) {
				accounts.add(new AccountEntity(
						result.getInt("account_id"),
						result.getTimestamp("created_at"),
						result.getString("username"),
						result.getString("password"),
						result.getString("name"),
						result.getString("surname"),
						result.getString("email"),
						result.getString("image_name"),
						result.getBytes("image"),
						result.getBoolean("reminder"),
						result.getBoolean("first_login"),
						result.getString("bio"),
						result.getBoolean("dark_theme")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return accounts;
	}

	@Override
	public AccountEntity findById(int id) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		AccountEntity account = null;
		String query = "SELECT * FROM accounts WHERE account_id = ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				account = new AccountEntity(
						result.getInt("account_id"),
						result.getTimestamp("created_at"),
						result.getString("username"),
						result.getString("password"),
						result.getString("name"),
						result.getString("surname"),
						result.getString("email"),
						result.getString("image_name"),
						result.getBytes("image"),
						result.getBoolean("reminder"),
						result.getBoolean("first_login"),
						result.getString("bio"),
						result.getBoolean("dark_theme")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return account;
	}

	@Override
	public AccountEntity findByUsername(String username) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		AccountEntity account = null;
		String query = "SELECT * FROM accounts WHERE username LIKE ?";

		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			result = statement.executeQuery();
			if (result.next()) {
				account = new AccountEntity(
						result.getInt("account_id"),
						result.getTimestamp("created_at"),
						result.getString("username"),
						result.getString("password"),
						result.getString("name"),
						result.getString("surname"),
						result.getString("email"),
						result.getString("image_name"),
						result.getBytes("image"),
						result.getBoolean("reminder"),
						result.getBoolean("first_login"),
						result.getString("bio"),
						result.getBoolean("dark_theme")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return account;
	}

	@Override
	public boolean create(AccountEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO accounts (username, password, name, surname, email, reminder, first_login, dark_Theme) VALUES (?,?,?,?,?,?,?,?)";

		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, entity.getUsername());
			statement.setString(2, entity.getPassword());
			statement.setString(3, entity.getName());
			statement.setString(4, entity.getSurname());
			statement.setString(5, entity.getEmail());
			statement.setBoolean(6, entity.isReminder());
			statement.setBoolean(7, entity.isFirstLogin());
			statement.setBoolean(8, entity.isDarkTheme());
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
	public AccountEntity updatePassword(AccountEntity entity, String password) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET password = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, password);
			statement.setInt(2, entity.getId());
			statement.executeUpdate();
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
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
		
		AccountEntity account = findById(entity.getId());
		updateReminder(account, false); // password updated, set reminder to false
		
		return account; // return the updated account
	}

	@Override
	public boolean delete(AccountEntity entity) throws SQLException {
		PreparedStatement statement = null;
		String query = "DELETE FROM accounts WHERE account_id = ?";

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
		String query = "DELETE FROM accounts WHERE account_id = ?";

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
	public boolean verifyUser(String username, String password) {
		AccountEntity account;
		try {
			if ((account = findByUsername(username)) != null) {
				if (account.getPassword().equals(password)) {
					return true;
				} 
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String generateTempPassword(AccountEntity entity) throws SQLException {
		String temp = PwGenerator.generate(12);
		updatePassword(entity, temp);
		updateReminder(entity, true);;
		return temp;
	}

	@Override
	public void updateReminder(AccountEntity entity, boolean reminder) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET reminder = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setBoolean(1, reminder);
			statement.setInt(2, entity.getId());
			statement.executeUpdate();
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
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
	}
	
	@Override
	public boolean updateImage(AccountEntity entity, String imageName, byte[] image) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET image_name = ?, image = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, imageName);
			statement.setBytes(2, image);
			statement.setInt(3, entity.getId());
			statement.executeUpdate();
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
		
		return true;
	}

	@Override
	public AccountEntity findByEmail(String email) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		AccountEntity account = null;
		String query = "SELECT * FROM accounts WHERE email ILIKE ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, email);
			result = statement.executeQuery();
			if (result.next()) {
				account = new AccountEntity(
						result.getInt("account_id"),
						result.getTimestamp("created_at"),
						result.getString("username"),
						result.getString("password"),
						result.getString("name"),
						result.getString("surname"),
						result.getString("email"),
						result.getString("image_name"),
						result.getBytes("image"),
						result.getBoolean("reminder"),
						result.getBoolean("first_login"),
						result.getString("bio"),
						result.getBoolean("dark_theme")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return account;
	}

	@Override
	public boolean isFirstLogin(AccountEntity entity) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		boolean isFirstLogin = false;
		String query = "SELECT first_login FROM accounts WHERE account_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, entity.getId());
			result = statement.executeQuery();
			if (result.next()) {
				isFirstLogin = result.getBoolean("first_login");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return isFirstLogin;
	}

	@Override
	public boolean setFirstLogin(AccountEntity entity, boolean firstLogin) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET first_login = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setBoolean(1, firstLogin);
			statement.setInt(2, entity.getId());
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
	public boolean setName(AccountEntity entity, String name) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET name = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setInt(2, entity.getId());
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
	public boolean setSurname(AccountEntity entity, String surname) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET surname = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, surname);
			statement.setInt(2, entity.getId());
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
	public boolean updateBio(AccountEntity entity, String bio) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET bio = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, bio);
			statement.setInt(2, entity.getId());
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
	public boolean updateEmail(AccountEntity entity, String email) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET email = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setInt(2, entity.getId());
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
	public List<AccountEntity> getTopContacts(AccountEntity account, int numberOfContacts) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<AccountEntity> accounts = new ArrayList<>();
		String query = "SELECT COUNT(sender_id) AS messages, receiver_id FROM messages WHERE sender_id = ? GROUP BY receiver_id ORDER BY messages DESC LIMIT ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, account.getId());
			statement.setInt(2, numberOfContacts);
			result = statement.executeQuery();
			while (result.next()) {
				accounts.add(findById(result.getInt("receiver_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return accounts;
	}

	@Override
	public List<String> getAllHistory(AccountEntity account) throws SQLException {
		List<String> history = new ArrayList<>();
		Map<Timestamp, String> strings = new TreeMap<>();
		PreparedStatement statement1 = null;
		PreparedStatement statement4 = null;
		ResultSet result1 = null;
		ResultSet result4 = null;
		String query1 = "SELECT g.group_id, MAX(g.created_at) FROM conversations c JOIN groups g ON c.group_id = g.group_id WHERE c.group_id IN (SELECT group_id FROM group_members WHERE account_id = ?) GROUP BY g.group_id ORDER BY max DESC;";
		String query4 = "SELECT * FROM (SELECT receiver_id, MAX(created_at) AS time1 FROM (SELECT * FROM messages WHERE sender_id = ? AND conversation_id IN (SELECT conversation_id FROM conversations WHERE group_id IS NULL ORDER BY created_at DESC)) AS inner1 GROUP BY receiver_id ORDER BY time1 DESC) AS q1 FULL OUTER JOIN (SELECT sender_id, MAX(created_at) AS time2 FROM (SELECT * FROM messages WHERE receiver_id = ? AND conversation_id IN (SELECT conversation_id FROM conversations WHERE group_id IS NULL ORDER BY created_at DESC)) AS inner1 GROUP BY sender_id ORDER BY time2 DESC) AS q2 ON receiver_id = sender_id";
		Timestamp timestamp1;
		Timestamp timestamp4;
		AccountEntity accountQuery;
		
		try {
			statement1 = connection.prepareStatement(query1);
			statement1.setInt(1, account.getId());
			result1 = statement1.executeQuery();
			while (result1.next()) {
				timestamp1 = result1.getTimestamp("max");
				strings.put(timestamp1, "group-"+result1.getString("group_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement1 != null) {
				statement1.close();
			}
		}
		
		try {
			statement4 = connection.prepareStatement(query4);
			statement4.setInt(1, account.getId());
			statement4.setInt(2, account.getId());
			result4 = statement4.executeQuery();
			
			while (result4.next()) {
				if (result4.getTimestamp("time1") != null) {
					timestamp4 = result4.getTimestamp("time1");
					accountQuery = findById(result4.getInt("receiver_id"));
				} else {
					timestamp4 = result4.getTimestamp("time2");
					accountQuery = findById(result4.getInt("sender_id"));
				}
				strings.put(timestamp4, accountQuery.getUsername()+"---"+timestamp4.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement4 != null) {
				statement4.close();
			}
		}
		
		history = new ArrayList<>(strings.values());
		List<?> shallowCopy = history.subList(0, history.size()); 
		Collections.reverse(shallowCopy);
		
		return (List<String>) shallowCopy;
	}
	
	@Override
	public String[][] getParticularHistory(AccountEntity sender, AccountEntity receiver) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		Map<Timestamp, List<String>> map = new TreeMap<>();
		List<String> mapValue = new ArrayList<>();
		
		int index;
		
		String[][] history;
		Timestamp timestamp;
		String query = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY created_at DESC";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, sender.getId());
			statement.setInt(2, receiver.getId());
			statement.setInt(3, receiver.getId());
			statement.setInt(4, sender.getId());
			result = statement.executeQuery();
			while (result.next()) {
				timestamp = result.getTimestamp("created_at");
				mapValue.add(findById(result.getInt("sender_id")).getUsername());
				mapValue.add(result.getString("message"));
				map.put(timestamp, mapValue);
				mapValue = new ArrayList<>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		index = map.size() - 1;
		history = new String[index+1][3];
		for (Map.Entry<Timestamp, List<String>> entry : map.entrySet()) {
			history[index][0] = entry.getValue().get(0); // sender
			history[index][1] = entry.getKey().toString(); // timestamp
			history[index--][2] = entry.getValue().get(1); // message
		}
		
		return history;
	}
	
	@Override
	public String[][] getParticularGroupHistory(String groupId) throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		Map<Timestamp, List<String>> map = new TreeMap<>();
		List<String> mapValue = new ArrayList<>();
		
		int index;
		
		String[][] history;
		Timestamp timestamp;
		if (gdao == null) {
			gdao = DAOFactory.getInstance().getGroupDAOImpl();
		}
		GroupEntity group = gdao.findById(groupId);
		String query = "SELECT * FROM messages WHERE conversation_id IN (SELECT conversation_id FROM conversations WHERE group_id LIKE ?)";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, group.getId());
			result = statement.executeQuery();
			String previous = "";
			while (result.next()) {
				if (result.getString("message").equals(previous)) continue;
				timestamp = result.getTimestamp("created_at");
				mapValue.add(findById(result.getInt("sender_id")).getUsername());
				mapValue.add(previous = result.getString("message"));
				map.put(timestamp, mapValue);
				mapValue = new ArrayList<>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
		index = map.size() - 1;
		history = new String[index+1][3];
		for (Map.Entry<Timestamp, List<String>> entry : map.entrySet()) {
			history[index][0] = entry.getValue().get(0); // sender
			history[index][1] = entry.getKey().toString(); // timestamp
			history[index--][2] = entry.getValue().get(1); // message
		}
		
		return history;
	}

	@Override
	public void setDarkTheme(AccountEntity entity, boolean darkTheme) throws SQLException {
		PreparedStatement statement = null;
		String query = "UPDATE accounts SET dark_theme = ? WHERE account_id = ?";
		
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setBoolean(1, darkTheme);
			statement.setInt(2, entity.getId());
			statement.executeUpdate();
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
		} finally {
			if (statement != null) {
				statement.close();
			}
			connection.setAutoCommit(true);
		}
	}
}
