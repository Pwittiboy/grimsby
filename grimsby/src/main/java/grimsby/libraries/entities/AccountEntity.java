/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for accounts table.
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public class AccountEntity extends AbstractEntity {

	/**
	 * Represents username column.
	 */
	private String username;
	
	/**
	 * Represents password column.
	 */
	private String password;

	/**
	 * Represents name column.
	 */
	private String name;
	
	/**
	 * Represents surname column.
	 */
	private String surname;
	
	/**
	 * Represents email column.
	 */
	private String email;
	
	/**
	 * Represents image_name column.
	 */
	private String imageName;
	
	/**
	 * Represents image column.
	 */
	private byte[] image;
	
	/**
	 * Represents the reminder column.
	 */
	private boolean reminder;
	
	/**
	 * Represents first_login column.
	 */
	private boolean firstLogin;
	
	/**
	 * Represents bio column.
	 */
	private String bio;
	
	/**
	 * Represents dark_theme column.
	 */
	private boolean darkTheme;
	
	/**
	 * Convenience constructor for an account entity.
	 * @param username Account's username.
	 * @param password Account's password.
	 * @param name Name of the account owner.
	 * @param surname Surname of the account owner.
	 * @param email Email address of the account owner.
	 * @param reminder An 'update password' reminder upon login.
	 * @param firstLogin A 'first login' flag for the account.
	 * @param bio The account owner's bio.
	 */
	public AccountEntity(String username, String password, String name, String surname, String email, boolean reminder, boolean firstLogin, String bio) {
		this(0, null, username, password, name, surname, email, null, null, reminder, firstLogin, bio, false);
	}
	
	/**
	 * Main constructor for an account entity.
	 * @param id Account's unique id.
	 * @param timestamp Creation date timestamp for the account.
	 * @param username Account's username.
	 * @param password Account's password.
	 * @param name Name of the account owner.
	 * @param surname Surname of the account owner.
	 * @param email Email address of the account owner.
	 * @param imageName Name of the user's profile image file.
	 * @param image User's profile picture.
	 * @param reminder An 'update password' reminder upon login.
	 * @param firstLogin A 'first login' flag for the account.
	 * @param bio The account owner's bio.
	 * @param darkTheme A dark theme flag for the account.
	 */
	public AccountEntity(int id, Timestamp timestamp, String username, String password, String name, String surname, String email, String imageName, byte[] image, boolean reminder, boolean firstLogin, String bio, boolean darkTheme) {
		this.id = id;
		this.timestamp = timestamp;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.imageName = imageName;
		this.image = image;
		this.reminder = reminder;
		this.firstLogin = firstLogin;
		this.bio = bio;
		this.darkTheme = darkTheme;
	}
	
	/**
	 * Getter method for dark theme.
	 * @return True if user has selected dark theme as a preference.
	 */
	public boolean isDarkTheme() {
		return darkTheme;
	}

	/**
	 * Setter method for dark theme.
	 * @param darkTheme Dark theme flag for the account.
	 */
	public void setDarkTheme(boolean darkTheme) {
		this.darkTheme = darkTheme;
	}

	/**
	 * Getter method for account username. 
	 * @return The account's username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter method for the account's username.
	 * @param username The account's updated username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter method for the account's password.
	 * @return The account's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for the account's password.
	 * @param password The account's updated password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter method for the account owner's name.
	 * @return Account owner's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the account owner's name.
	 * @param name Account owner's updated name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the account owner's surname.
	 * @return Account owner's surname.
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Setter method for the account owner's surname.
	 * @param surname Account owner's surname.
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Getter method for the account owner's email.
	 * @return Account owner's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter method for the account owner's email.
	 * @param email Account owner's email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter method for the account's profile image file name.
	 * @return The image's file name.
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Setter method for the account's profile image name.
	 * @param imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * Getter method for the account's profile image.
	 * @return Account's profile image.
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Setter method for the account's profile image.
	 * @param image
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * Getter method for the accounts update-password reminder flag.
	 * @return True if the user needs to be reminded to change their password.
	 */
	public boolean isReminder() {
		return reminder;
	}

	/**
	 * Setter method for the accounts update-password reminder flag.
	 * @param reminder Reminder flag for whether the user needs to be reminded to update their password.
	 */
	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	/**
	 * Getter method for if the account is logging in for the first time.
	 * @return True if the account is logging in for the first time.
	 */
	public boolean isFirstLogin() {
		return firstLogin;
	}

	/**
	 * Setter method for if the account is logging in for the first time.
	 * @param firstLogin Flag for if the account is logging in for the first time.
	 */
	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	/**
	 * Getter method for the account's bio.
	 * @return Account's bio.
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * Setter method for the account's bio.
	 * @param bio Account's bio.
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	@Override
	public String toString() {
		return "Username: "+getUsername()+", account id: "+getId();
	}
	
	@Override
	public boolean equals(Object account) {
		return this.username == ((AccountEntity) account).getUsername() &&
				this.name == ((AccountEntity) account).getName() &&
				this.surname == ((AccountEntity) account).getSurname() &&
				this.email == ((AccountEntity) account).getEmail();
	}
	
	
}
