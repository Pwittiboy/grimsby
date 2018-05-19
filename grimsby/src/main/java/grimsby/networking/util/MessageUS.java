/*
 * 
 */
package grimsby.networking.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class MessageUS. The class object is user for communication between
 * client and server.
 */
public class MessageUS implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id of the person/group. */
	private String ID;

	/** The account creation time. */
	private String accountCreationTime;

	/** The message. */
	private String message;

	/** The image. */
	private byte[] image;

	/** The instruction to follow. */
	private String instruction;

	/** The reason why some event did not happen if failure occurs. */
	private String reason;

	/** The login of the user. Also used for other purposes. */
	private String login;

	/** The people online list. */
	private ArrayList<String> peopleList;

	/** The time stamp. */
	private Timestamp timeStamp;

	/** The people. */
	private Set<User> people;

	/** The user information. */
	private String[] userInformation;

	/** The name. */
	private String name;

	/** The flag whether the password is temporary. */
	private boolean isTemporary;

	/** The flag for indicating whether it is first time when person logins. */
	private boolean isFirstTime;

	/** The temporary data. */
	private String tempData;

	/** The history. */
	private String[][] history;

	/** The style. 0 or 1 */
	private int style;

	/** The creation time. */
	private HashMap<String, String> creationT;

	/** The player 1. */
	private boolean player1 = true;

	/** The x. */
	private int x;

	/**
	 * Instantiates a new message. Default constructor
	 */
	private MessageUS() {
		this.userInformation = null;
		this.ID = new String();
		this.message = new String();
		this.image = null;
		this.instruction = new String();
		this.reason = new String();
		this.login = new String();
		this.peopleList = new ArrayList<>();
		this.userInformation = new String[4];
		this.timeStamp = null;
		this.people = new HashSet<>();
		this.name = new String();
		this.isTemporary = false;
		this.isFirstTime = false;
		this.tempData = new String();
		this.accountCreationTime = new String();
		history = new String[0][0];
		creationT = new HashMap<>();
		style = 1;

	}

	/**
	 * Instantiates a new message US. Used for simple instructions which do not
	 * require more details.
	 *
	 * @param instruction the instruction
	 */
	public MessageUS(String instruction) {
		this();
		this.instruction = instruction;
	}

	/**
	 * Instantiates a new message US. Used for connection requests, viewing
	 * profile, ending chat
	 *
	 * @param instruction the instruction
	 * @param login the login is the username of group name
	 * @param ID the person's, group's or chat's id
	 */
	public MessageUS(String instruction, String login, String ID) {
		this();
		this.instruction = instruction;
		this.login = login;
		this.ID = ID;

	}

	/**
	 * Instantiates a new message US.
	 *
	 * @param instruction the instruction
	 * @param peopleList the list of people's logins
	 */
	public MessageUS(String instruction, ArrayList<String> peopleList) {
		this();
		this.instruction = instruction;
		this.peopleList = peopleList;
	}

	/**
	 * Instantiates a new message US.
	 *
	 * @param instruction the instruction
	 * @param userInformation the information about the user (login, name,
	 * surename, password etc.)
	 */
	public MessageUS(String instruction, String[] userInformation) {
		this();
		this.instruction = instruction;
		this.userInformation = userInformation;
	}

	/**
	 * Instantiates a new message. Used for sending text messages for
	 * communication
	 *
	 * @param instruction the instruction
	 * @param message the message text
	 * @param login the author of the message
	 * @param ID the chat id of the chat
	 */
	public MessageUS(String instruction, String message, String login, String ID) {
		this();
		this.message = message;
		this.ID = ID;
		this.instruction = instruction;
		this.login = login;
	}

	/**
	 * Instantiates a new message. Used for sending photos in coversations
	 *
	 * @param instruction the instruction
	 * @param i the i
	 * @param login the login
	 * @param ID the chat id
	 */
	public MessageUS(String instruction, byte[] i, String login, String ID) {
		this();
		this.ID = ID;
		this.image = i;
		this.login = login;
		this.instruction = instruction;
	}

	/**
	 * Instantiates a new message US.
	 *
	 * @param instruction the instruction
	 * @param people the people in the chat or online
	 * @param name the name group name
	 * @param ID the chat id
	 */
	// in the group chat or even in private chat
	public MessageUS(String instruction, Set<User> people, String name, String ID) {
		this();
		this.instruction = instruction;
		this.people = people;
		this.ID = ID;
		// group name etc
		this.name = name;
	}

	/**
	 * Instantiates a new message US. Used for getting the history of the
	 * specific chat
	 *
	 * @param instruction the instruction
	 * @param history the history
	 * @param timeStamps the time stamps
	 */
	public MessageUS(String instruction, String[][] history, HashMap<String, String> timeStamps) {
		this();
		this.instruction = instruction;
		this.history = history;
		this.creationT = timeStamps;
	}

	/**
	 * Instantiates a new message US.
	 *
	 * @param instruction the instruction
	 * @param loginID the login ID
	 * @param isPlayer1 the is player 1
	 */
	public MessageUS(String instruction, String loginID, boolean isPlayer1) {
		this();
		this.instruction = instruction;
		this.login = loginID;
		this.ID = loginID;
		this.player1 = isPlayer1;
	}

	/**
	 * Instantiates a new message US.
	 *
	 * @param instruction the instruction
	 * @param loginID the login ID
	 * @param x the x
	 */
	public MessageUS(String instruction, String loginID, int x) {
		this();
		this.instruction = instruction;
		this.login = loginID;
		this.x = x;
	}

	/**
	 * Gets the history.
	 *
	 * @return the history
	 */
	public String[][] getHistory() {
		return history;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public HashMap<String, String> getCreationT() {
		return creationT;
	}

	/**
	 * Gets the chat id.
	 *
	 * @return the chat id
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the instruction.
	 *
	 * @return the instruction
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Gets the people online.
	 *
	 * @return the people online
	 */
	public ArrayList<String> getPeopleOnline() {
		return peopleList;
	}

	/**
	 * Gets the user information.
	 *
	 * @return the user information
	 */
	public String[] getUserInformation() {
		return userInformation;
	}

	/**
	 * Gets the time stamp.
	 *
	 * @return the time stamp
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Gets the reason of what happened wrong.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the people.
	 *
	 * @return the people
	 */
	public Set<User> getPeople() {
		return people;
	}

	/**
	 * Checks if password is temporary.
	 *
	 * @return the checks if password is temporary
	 */
	public boolean getIsTemporary() {
		return isTemporary;
	}

	/**
	 * Checks if is first time when user logs in.
	 *
	 * @return the checks if is first time
	 */
	public boolean getIsFirstTime() {
		return isFirstTime;
	}

	/**
	 * Sets the time stamp. Used for message sending
	 *
	 * @param timeStamp the new time stamp
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Sets the reason why something went wrong.
	 *
	 * @param reason the new reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * Sets the flag indicating whether user uses temporary password.
	 */
	public void setTemporaty() {
		this.isTemporary = true;
	}

	/**
	 * Sets the is flag saying whether this login is the first time.
	 */
	public void setIsFirstTime() {
		this.isFirstTime = true;
	}

	/**
	 * Sets the people.
	 *
	 * @param setOfPeople the new people
	 */
	public void setPeople(Set<User> setOfPeople) {
		this.people = setOfPeople;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds the photo.
	 *
	 * @param photo the photo
	 */
	public void addPhoto(byte[] photo) {
		this.image = photo;
	}

	/**
	 * Sets the account creation time.
	 *
	 * @param creationTime the new account creation time
	 */
	public void setAccountCreationTime(String creationTime) {
		this.accountCreationTime = creationTime;
	}

	/**
	 * Gets the account creation time.
	 *
	 * @return the account creation time
	 */
	public String getAccountCreationTime() {
		return accountCreationTime;
	}

	/**
	 * Gets the temporary data.
	 *
	 * @return the temporary data
	 */
	public String getTempData() {
		return tempData;
	}

	/**
	 * Sets the temporary data data.
	 *
	 * @param tempData the new temp data
	 */
	public void setTempData(String tempData) {
		this.tempData = tempData;
	}

	/**
	 * Sets the user's style.
	 *
	 * @param style the new app style
	 */
	public void setStyle(int style) {
		if (true)
			this.style = style;
	}

	/**
	 * Gets the user's style.
	 *
	 * @return the app style
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the coordinate.
	 *
	 * @param x the new coordinte
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Flag whether the person who is receiving the message is player 1
	 *
	 * @return true, if is player 1
	 */
	public boolean isPlayer1() {
		return player1;
	}
	
	public void setPlayer(boolean player1) {
		this.player1 = player1;
	}

}
