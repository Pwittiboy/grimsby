/**
 * 
 */
package grimsby.libraries.dataaccess.dao;

import java.sql.SQLException;
import java.util.List;

import grimsby.libraries.entities.AccountEntity;


/**
 * DAO for account entity.
 *
 * @author Christopher Friis (cxf798)
 * @version 20 Feb 2018
 */
public interface AccountDAO extends AbstractDAO<AccountEntity> {
	
	/**
	 * Typical SELECT statement for an account by the username.
	 * @param username The account's username.
	 * @return The account entity.
	 * @throws SQLException
	 */
	AccountEntity findByUsername(String username) throws SQLException;

	/**
	 * Updates an accounts password to the given value.
	 * @param entity The account that's password is to be updated.
	 * @param password New value of the password.
	 * @return The update account entity.
	 * @throws SQLException 
	 */
	AccountEntity updatePassword(AccountEntity entity, String password) throws SQLException;

	/**
	 * Verifies the user's username and password against the values stored in the database.
	 * @param username The input username.
	 * @param password The input password.
	 * @return True if the user credentials are correct.
	 */
	boolean verifyUser(String username, String password);

	/**
	 * Calls a password generating class to create a new temporary password, which will be emailed to them.
	 * @param entity The account which will received a new password.
	 * @return The new password value.
	 * @throws SQLException
	 */
	String generateTempPassword(AccountEntity entity) throws SQLException;

	/**
	 * Set the account's password reminder to on or off. Changed either when the user forgets their password or when they actively update their password.
	 * @param entity Account that is to be updated.
	 * @param reminder Whether the account needs to be reminded to change their password on login.
	 * @throws SQLException
	 */
	void updateReminder(AccountEntity entity, boolean reminder) throws SQLException;

	/**
	 * Typical SELECT statement by email.
	 * @param email Search value for the account's email.
	 * @return The updated account.
	 * @throws SQLException
	 */
	AccountEntity findByEmail(String email) throws SQLException;

	/**
	 * Checks whether the given account is logging in for the first time.
	 * @param entity The account that is logging in.
	 * @return True if the user is logging in for the first time.
	 * @throws SQLException
	 */
	boolean isFirstLogin(AccountEntity entity) throws SQLException;

	/**
	 * Sets the 'first login' flag for an account.
	 * @param entity Account being updated.
	 * @param firstLogin Flag to determine whether it is a user's first time logging in.
	 * @return True if the first login flag is successfully updated.
	 * @throws SQLException
	 */
	boolean setFirstLogin(AccountEntity entity, boolean firstLogin) throws SQLException;

	/**
	 * Sets the first name detail of the given account.
	 * @param entity Account being updated.
	 * @param name First name of the account owner.
	 * @return True if the update runs successfully.
	 * @throws SQLException
	 */
	boolean setName(AccountEntity entity, String name) throws SQLException;

	/**
	 * Sets the surname detail of the given account.
	 * @param entity Account being updated.
	 * @param surname Surname of the account owner.
	 * @return True if the update runs successfully.
	 * @throws SQLException
	 */
	boolean setSurname(AccountEntity entity, String surname) throws SQLException;

	/**
	 * Sets the bio details of the given account.
	 * @param entity Account being updated.
	 * @param bio Bio field of the account owner.
	 * @return True if the bio is successfully updated.
	 * @throws SQLException
	 */
	boolean updateBio(AccountEntity entity, String bio) throws SQLException;

	/**
	 * Sets the email field of the given account.
	 * @param entity Account being updated.
	 * @param email Email field of the account owner.
	 * @return True if the email is successfully updated.
	 * @throws SQLException
	 */
	boolean updateEmail(AccountEntity entity, String email) throws SQLException;

	/**
	 * Gets the contacts whom a user has had the most messages sent to and received from.
	 * @param account The account whose top contacts are being requested.
	 * @param numberOfContacts How many contacts to return.
	 * @return The user's top contacts.
	 * @throws SQLException
	 */
	List<AccountEntity> getTopContacts(AccountEntity account, int numberOfContacts) throws SQLException;

	/**
	 * Gets a list showing all conversations and their timestamps in reverse chronological order.
	 * @param account The account whose history is being requested.
	 * @return A list showing all conversations and their timestamps in reverse chronological order.
	 * @throws SQLException
	 */
	List<String> getAllHistory(AccountEntity account) throws SQLException;

	/**
	 * Get all messages, in reverse chronological order, between sender and receiver, including private and group chats. 
	 * @param sender
	 * @param receiver
	 * @return
	 * @throws SQLException
	 */
	String[][] getParticularHistory(AccountEntity sender, AccountEntity receiver) throws SQLException;

	/**
	 * Get all messages from a given group's conversation.
	 * @param groupTimestamp
	 * @return
	 * @throws SQLException
	 */
	String[][] getParticularGroupHistory(String groupId) throws SQLException;

	void setDarkTheme(AccountEntity entity, boolean darkTheme) throws SQLException;

	/**
	 * Updates the accounts image and image_name.
	 * @param entity
	 * @param imageName
	 * @param image
	 * @return True if image is added successfully.
	 * @throws SQLException
	 */
	boolean updateImage(AccountEntity entity, String imageName, byte[] image) throws SQLException;
	
}
