package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.omg.CORBA.ShortSeqHelper;

import grimsby.networking.client.AES;
import grimsby.networking.client.Client;
import grimsby.networking.util.Constants;
import grimsby.networking.util.Conversation;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.NoughtsCrossesMain;
import grimsby.networking.util.User;

/**
 * The Class Model. Is used for interpreting the commands received from the
 * server. Connects the answers to the GUI
 */
public class Model extends Observable {

	/** The client. */
	private Client client;

	/** The queue of commands. */
	private LinkedBlockingQueue<MessageUS> queueOfCommands;

	/** The chats. */
	private HashMap<String, Conversation> chats;

	/** The array of online people login's. */
	private String[] arrOnlineLogin;

	/** The my login. */
	private String myLogin;

	/** The possible current connections. */
	private HashSet<String> possibleCurrentConnections;

	/** The registration time. */
	private String registrationTime;

	/** The top contacts (best friends). */
	private Set<User> top;

	/** The my photo. */
	private byte[] myPhoto;

	/** The emoji manage. */
	private EmojiManage emojiManage;

	/** The modify picture. */
	private ModifyPicture modifyPicture;

	/** The theme. */
	private int theme = 0;

	/** The games. */
	private HashMap<String, NoughtsCrossesMain> games;

	private HashSet<String> conversationLogins;

	private HashSet<String> viewingProfile;

	private MainScreen mainScreen;

	private HashMap<String, Profile> profiles;

	/**
	 * Instantiates a new model. Model class starts client which starts
	 * communication with server
	 *
	 * @param queueOfCommands the queue of commands
	 * @param queueOfAnswers the queue of answers
	 */
	public Model(LinkedBlockingQueue<MessageUS> queueOfCommands, LinkedBlockingQueue<MessageUS> queueOfAnswers) {
		client = new Client(Constants.HOST_IP, queueOfCommands, queueOfAnswers, this);
		this.queueOfCommands = queueOfCommands;
		client.start();
		this.chats = new HashMap<>();
		this.arrOnlineLogin = null;
		this.myLogin = new String();
		this.possibleCurrentConnections = new HashSet<>();
		this.top = new HashSet<>();
		this.emojiManage = new EmojiManage();
		this.modifyPicture = new ModifyPicture();
		this.games = new HashMap<>();
		this.conversationLogins = new HashSet<>();
		this.profiles = new HashMap<>();
	}

	/**
	 * Sets the change gui. Calls particular methos to deal with the given
	 * command
	 *
	 * @param message the message from the server
	 */
	public void SetChangeGui(MessageUS message) {
		String commandFromServer = message.getInstruction();
		if (commandFromServer.equals(Constants.ACCEPTED_LOGIN)) {
			acceptLogin(message);
		} else if (commandFromServer.equals(Constants.ACCEPTED_CREATE_ACCOUNT)) {
			acceptedCreateAccount(message);
		} else if (commandFromServer.equals(Constants.EMAIL_SENT)) {
			emailSent(message);
		} else if (commandFromServer.equals(Constants.EMAIL_NOT_SENT)) {
			emailNotSent(message);
		} else if (commandFromServer.equals(Constants.ACCEPTED_CONNECT)) {
			acceptedConnect(message);
		} else if (commandFromServer.equals(Constants.ACCEPTED_CONNECT_GROUP)) {
			acceptedConnectGroup(message);
		} else if (commandFromServer.equals(Constants.CONNECTION_REQUEST)) {
			connectionRequest(message);
		} else if (commandFromServer.equals(Constants.RECEIVE_MESSAGE)) {
			receiveMessage(message);
		} else if (commandFromServer.equals(Constants.RECEIVE_PUBLIC_MESSAGE)) {
			receivePublicMessage(message);
		} else if (commandFromServer.equals(Constants.DECLINED_LOGIN)) {
			declinedLogin(message);
		} else if (commandFromServer.equals(Constants.DECLINED_CREATE_ACCOUNT)) {
			declinedCreateAccount(message);
		} else if (commandFromServer.equals(Constants.DECLINED_CONNECT)) {
			declinedConnect(message);
		} else if (commandFromServer.equals(Constants.DECLINED_SEND_MESSAGE)) {
			declinedSendMessage(message);
		} else if (commandFromServer.equals(Constants.SOMETHING_WENT_WRONG)) {
			somethingWentWrong(message);
		} else if (commandFromServer.equals(Constants.RECEIVE_PHOTO)) {
			recievePhoto(message);
		} else if (commandFromServer.equals(Constants.RECEIVE_PUBLIC_PHOTO)) {
			receivePublicPhoto(message);
		} else if (commandFromServer.equals(Constants.ACCEPTED_ADD)) {
			acceptedAdd(message);
		} else if (commandFromServer.equals(Constants.DECLINED_ADD)) {
			declinedAdd(message);
		} else if (commandFromServer.equals(Constants.CHAT_ENDED)) {
			chatEnded(message);
		} else if (commandFromServer.equals(Constants.LIST_CHANGED)) {
			listChanged(message);
		} else if (commandFromServer.equals(Constants.CONNECTION_WITH_SERVER_BROKEN)) {
			connectionBroken();
		} else if (commandFromServer.equals(Constants.LOAD_ALL_HISTORY)) {
			loadAllHistory(message);
		} else if (commandFromServer.equals(Constants.LOAD_PARTICULAR_HISTORY)) {
			loadParticularHistory(message);
		} else if (commandFromServer.equals(Constants.GET_MY_TOP)) {
			getMyTop(message);
		} else if (commandFromServer.equals(Constants.PHOTO_UPLOADED)) {
			photoUploaded(message);
		} else if (commandFromServer.equals(Constants.PHOTO_DECLINED)) {
			photoDeclined(message);
		} else if (commandFromServer.equals(Constants.VIEW_PROFILE)) {
			viewProfile(message);
		} else if (commandFromServer.equals(Constants.ACCEPT_PASSWORD)) {
			acceptTempPassword(message);
		} else if (commandFromServer.equals(Constants.DECLINE_PASSWORD)) {
			declineTempPassword(message);
		} else if (commandFromServer.equals(Constants.PLAY_TIC_TAC_REQUEST)) {
			ticTacRequest(message);
		} else if (commandFromServer.equals(Constants.PLAY_TIC_TAC_ACCEPTED)) {
			acceptedTicTac(message);
		} else if (commandFromServer.equals(Constants.PLAY_TIC_TAC_DECLINED)) {
			declinedTicTac(message);
		} else if (commandFromServer.equals(Constants.MOVE_TIC_TAC)) {
			moveTicTac(message);
		} else if (commandFromServer.equals(Constants.END_TIC_TAC) || commandFromServer.equals(Constants.END_OF_GAME)) {
			endOfGame(message);
		} else if (commandFromServer.equals(Constants.CHANGE_NAME_ACCEPTED)
				|| commandFromServer.equals(Constants.CHANGE_NAME_DECLINED)
				|| commandFromServer.equals(Constants.CHANGE_SURENAME_ACCEPTED)
				|| commandFromServer.equals(Constants.CHANGE_SURENAME_DECLINED)
				|| commandFromServer.equals(Constants.CHANGE_EMAIL_ACCEPTED)
				|| commandFromServer.equals(Constants.CHANGE_EMAIL_DECLINED)
				|| commandFromServer.equals(Constants.CHANGE_BIO_ACCEPTED)
				|| commandFromServer.equals(Constants.CHANGE_BIO_DECLINED)) {
			updateProfile(message);
		} else if (commandFromServer.equals(Constants.DELETE_ACCOUNT_SUCCESSFUL)) {
			deleteAccount();
		} else if (commandFromServer.equals(Constants.DELETE_ACCOUNT_UNSUCCESSFUL)) {
			deleteAccountUnsuccessful();
		} else
			System.err.println("unknown command");
	}

	private void deleteAccountUnsuccessful() {
		JOptionPane.showMessageDialog(null, "Your account was not deleted! Try again if you really want...",
				"Account wa not deleted", JOptionPane.INFORMATION_MESSAGE);
		notifyObservers(Constants.DELETE_ACCOUNT_UNSUCCESSFUL);
	}

	private void deleteAccount() {
		JOptionPane.showMessageDialog(null, "Your account was successfully deleted!", "Account deleted",
				JOptionPane.INFORMATION_MESSAGE);
		client.interrupt();
		System.exit(0);
	}

	/**
	 * End of game. Notifies about the end of the game(if there is a winner or
	 * the result is equal)
	 *
	 * @param message the message from the server
	 */
	private void endOfGame(MessageUS message) {
		try {
			games.get(message.getLogin()).endGame(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Move tic tac. Calls the method which displays oponent's move.
	 *
	 * @param message the message from the server
	 */
	private void moveTicTac(MessageUS message) {
		games.get(message.getLogin());
		games.get(message.getLogin()).oponentMove(message);
	}

	/**
	 * Accepted tic tac. Creates a new game with the partner
	 *
	 * @param message the message from the server
	 */
	private void acceptedTicTac(MessageUS message) {
		NoughtsCrossesMain newGame = new NoughtsCrossesMain();
		byte[] opponentPhoto = message.getImage();
		boolean bothNull = false;
		if (opponentPhoto == null) {
			if (this.myPhoto == null) {
				bothNull = true;
				try {
					opponentPhoto = this.modifyPicture.ImageToByte(new File("placeholder.jpg"));
				} catch (IOException e1) {
				}
			} else {
				bothNull = false;
				try {
					opponentPhoto = this.modifyPicture.ImageToByte(new File("placeholder.jpg"));
				} catch (IOException e1) {
				}
			}
		}
		File opponentProfilePicture = new File(
				"temporary Data/" + this.myLogin + "/Game/" + message.getLogin() + "Gaming photo.jpg");

		this.modifyPicture.byteToImage(opponentPhoto, opponentProfilePicture);
		this.modifyPicture.resizeImage(opponentProfilePicture, opponentProfilePicture, 133, 133, "jpg");

		NoughtsCrosses newGameWindow = new NoughtsCrosses(queueOfCommands, games, message.getLogin(), myLogin,
				message.isPlayer1(), bothNull);

		games.put(message.getLogin(), newGame);
		newGame.addObserver(newGameWindow);

	}

	/**
	 * Declined tic tac. Notifies user that the game request was declined
	 *
	 * @param message the message from the server
	 */
	private void declinedTicTac(MessageUS message) {
		games.remove(message.getLogin());
		JOptionPane.showMessageDialog(null, message.getLogin() + " declined your game request!",
				"Game request declined", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Tic tac request. Creates new GameRequestWindow
	 *
	 * @param message the message from the server
	 */
	private void ticTacRequest(MessageUS message) {
		new GameRequestReceived(queueOfCommands, message.getLogin(), games);
	}

	/**
	 * Update profile. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void updateProfile(MessageUS message) {
		this.setChanged();
		this.notifyObservers(message);

	}

	/**
	 * View profile. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void viewProfile(MessageUS message) {
		this.setChanged();
		this.notifyObservers(message);

	}

	/**
	 * Photo uploaded. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void photoUploaded(MessageUS message) {
		this.myPhoto = message.getImage();
		this.setChanged();
		this.notifyObservers(message);

	}

	/**
	 * Decline temp password. Notifies the user that the password was not
	 * updated
	 *
	 * @param message the message from the server
	 */
	private void declineTempPassword(MessageUS message) {
		JOptionPane.showMessageDialog(null, message.getReason(), "Your password was not updated. Try again",
				JOptionPane.WARNING_MESSAGE);

	}

	/**
	 * Accept temp password. Notifies the user that the password was updated
	 *
	 * @param message the message from the server
	 */
	private void acceptTempPassword(MessageUS message) {
		JOptionPane.showMessageDialog(null, "Your password was changed", "Your password was updated",
				JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Photo declined. Notifies user that the photo was not sent
	 *
	 * @param message the message from the server
	 */
	private void photoDeclined(MessageUS message) {
		JOptionPane.showMessageDialog(null, message.getReason(), "Your photo was not updated. Try again",
				JOptionPane.WARNING_MESSAGE);

	}

	/**
	 * Gets the top friends. notifies observers
	 *
	 * @param message the message from the server
	 * @return the my top
	 */
	private void getMyTop(MessageUS message) {
		top = message.getPeople();
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Loads particular history.
	 *
	 * @param message the message from the server
	 */
	private void loadParticularHistory(MessageUS message) {
		String[][] allInfo = message.getHistory();
		String[] history = new String[allInfo.length];
		HashMap<String, String> creationT = message.getCreationT();
		for (int i = 0; i < allInfo.length; i++)
			allInfo[i][2] = decrypt(allInfo[i], creationT.get(allInfo[i][0]));
		new HistoryWithUser(allInfo, emojiManage);
	}

	/**
	 * Loads all history.
	 *
	 * @param message the message from the server
	 */
	private void loadAllHistory(MessageUS message) {
		String[][] hiStrings = message.getHistory();
		String[] allHistory = new String[hiStrings.length];
		for (int i = 0; i < hiStrings.length; i++)
			allHistory[i] = hiStrings[i][0];
		new History(queueOfCommands, allHistory, mainScreen);
	}

	/**
	 * Declined add. Notifies user that the person was not added to the group
	 *
	 * @param message the message from the server
	 */
	private void declinedAdd(MessageUS message) {
		JOptionPane.showMessageDialog(null, message.getReason(), message.getReason(), JOptionPane.WARNING_MESSAGE);

	}

	/**
	 * Accepted add. Adds a new person to the specific group chat
	 *
	 * @param message the message from the server
	 */
	private void acceptedAdd(MessageUS message) {
		JOptionPane.showMessageDialog(null,
				"Person " + message.getLogin() + " was added to the group \"" + message.getName() + "\" ");
		String groupId = message.getID();

		chats.get(groupId).addPerson(message);

	}

	/**
	 * Gets the people online id.
	 *
	 * @return the people online id
	 */
	public String[] getPeopleOnlineId() {
		return arrOnlineLogin;
	}

	/**
	 * Accept login. Creates a new instance of the mains screen
	 *
	 * @param message the message from the server
	 */
	private void acceptLogin(MessageUS message) {
		viewingProfile = new HashSet<>();
		this.theme = message.getStyle();
		Design.setTheme(theme);
		String[] userData = message.getUserInformation();
		this.myLogin = userData[0];
		this.registrationTime = userData[1];
		this.myPhoto = message.getImage();
		setChanged();
		notifyObservers(message);
		mainScreen = new MainScreen(queueOfCommands, possibleCurrentConnections, this, myLogin, registrationTime,
				this.top, emojiManage, games, viewingProfile, profiles);
	}

	/**
	 * Email sent. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void emailSent(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Email not sent. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void emailNotSent(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Accepted create account. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void acceptedCreateAccount(MessageUS message) {
		setChanged();
		notifyObservers(message.getInstruction());
	}

	/**
	 * Accepted connect. Creates a new private chat
	 *
	 * @param message the message from the server
	 */
	private void acceptedConnect(MessageUS message) {
		Conversation newCon = new Conversation(message.getID());
		String partnerLogin = message.getLogin();
		possibleCurrentConnections.add(partnerLogin);

		File privateChatFolder = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin);
		if (!privateChatFolder.exists())
			privateChatFolder.mkdirs();
		File myMainPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin + "/"
				+ this.myLogin + " Main photo.jpg");
		File myChattingPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin + "/"
				+ this.myLogin + " Chatting photo.jpg");
		File partnerMainPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin + "/"
				+ partnerLogin + " Main photo.jpg");
		File partnerChattingPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin
				+ "/" + partnerLogin + " Chatting photo.jpg");

		byte[] defaultPhoto;
		try {
			defaultPhoto = this.modifyPicture.ImageToByte(new File("placeholder.jpg"));
			byte[] partnerPhoto = message.getImage();

			if (!(partnerPhoto == null) && !(myPhoto == null)) {

			} else if (message.getImage() == null && !(myPhoto == null)) {
				partnerPhoto = defaultPhoto;
			} else if (!(message.getImage() == null) && myPhoto == null) {
				myPhoto = defaultPhoto;
			} else {
				this.myPhoto = defaultPhoto;
				partnerPhoto = defaultPhoto;
			}
			this.modifyPicture.byteToImage(myPhoto, myMainPhoto.getAbsoluteFile());
			this.modifyPicture.resizeImage(myMainPhoto, myChattingPhoto, 40, 40, "jpg");
			this.modifyPicture.byteToImage(partnerPhoto, partnerMainPhoto);
			this.modifyPicture.resizeImage(partnerMainPhoto, partnerChattingPhoto, 40, 40, "jpg");
		} catch (IOException e1) {
		}

		PrivateChat privateChatWindow = new PrivateChat(queueOfCommands, this, newCon, myLogin, partnerLogin,
				emojiManage, viewingProfile);
		privateChatWindow.setVisible(true);
		privateChatWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				possibleCurrentConnections.remove(message.getLogin());
				chats.remove(message.getID());
				queueOfCommands.add(new MessageUS(Constants.END_CHAT, myLogin, message.getID()));
				e.getWindow().dispose();
				privateChatFolder.delete();
			}
		});
		newCon.addObserver(privateChatWindow);
		chats.put(message.getID(), newCon);
		this.viewingProfile.remove(message.getLogin());
	}

	/**
	 * Accepted connect group. Creates a new group
	 *
	 * @param message the message from the server
	 */
	private void acceptedConnectGroup(MessageUS message) {
		String groupName = message.getLogin();
		Set<String> peopleInChatNames = new HashSet<String>();
		Set<User> peopleInGroup = message.getPeople();
		peopleInGroup.forEach(user -> peopleInChatNames.add(user.getName()));
		Conversation newCon = new Conversation(message.getID(), peopleInChatNames);
		GroupChat gc = new GroupChat(queueOfCommands, this, myLogin, groupName, message.getID(), newCon,
				this.emojiManage);

		File groupChatFolder = new File("temporary Data/" + this.myLogin + "/Group Chat/" + groupName);
		File groupProfilePicture = new File(
				"temporary Data/" + this.myLogin + "/Group Chat/" + groupName + "/Group Profile Picture");
		File groupCommunicationPicture = new File(
				"temporary Data/" + this.myLogin + "/Group Chat/" + groupName + "/Group Communication Picture");

		if (!groupChatFolder.exists()) {
			groupChatFolder.mkdir();
			groupProfilePicture.mkdir();
			groupCommunicationPicture.mkdir();
		} else {
			if (!groupProfilePicture.exists()) {
				groupProfilePicture.mkdir();
			}
			if (!groupCommunicationPicture.exists()) {
				groupCommunicationPicture.mkdir();
			}
		}
		byte[] defaultPhoto;

		try {
			defaultPhoto = this.modifyPicture.ImageToByte(new File("placeholder.jpg"));

			peopleInGroup.forEach(e -> {
				byte[] photoFromDataBase = e.getPhoto();
				File profilePhoto = new File("temporary Data/" + this.myLogin + "/Group Chat/" + groupName
						+ "/Group Profile Picture/" + e.getName() + " Chatting profile photo.jpg");
				if (photoFromDataBase == null) {
					photoFromDataBase = defaultPhoto;
				}
				this.modifyPicture.byteToImage(photoFromDataBase, profilePhoto);
				this.modifyPicture.resizeImage(profilePhoto, profilePhoto, 40, 40, "jpg");
				new ImageIcon(profilePhoto.getAbsolutePath()).getImage().flush();
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		gc.setVisible(true);
		gc.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				possibleCurrentConnections.remove(message.getLogin());
				chats.remove(message.getID());
				queueOfCommands.add(new MessageUS(Constants.END_CHAT, "group \"" + groupName + "\"", message.getID()));
				e.getWindow().dispose();
			}
		});
		newCon.addObserver(gc);
		chats.put(message.getID(), newCon);
	}

	/**
	 * Connection request. Created a new instance of ConnectionRequest.
	 *
	 * @param message the message from the server
	 */
	private void connectionRequest(MessageUS message) {
		possibleCurrentConnections.add(message.getLogin());
		if (profiles.containsKey(message.getLogin()))
			profiles.get(message.getLogin()).disableMessage();

		// if exit or cancel: enable. if accepted leave disabled
		ConnectionRequestReceived recived = new ConnectionRequestReceived(queueOfCommands, possibleCurrentConnections,
				this, message.getLogin(), profiles);
		setChanged();
		notifyObservers(recived);
	}

	/**
	 * Receive message. Adds message to the specific chat
	 *
	 * @param message the message from the server
	 */
	private void receiveMessage(MessageUS message) {
		chats.get(message.getID()).newMessage(message);
	}

	/**
	 * Receive public message. Notifies observers
	 *
	 * @param message the message from the server
	 */
	private void receivePublicMessage(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Declined login. Notifies observers about the answer from the server
	 *
	 * @param message the message from the server
	 */
	private void declinedLogin(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Declined create account. Notifies the observers
	 *
	 * @param message the message from the server
	 */
	private void declinedCreateAccount(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Declined connect. Notifies client that the partner declined connection
	 * request
	 *
	 * @param message the message from the server
	 */
	private void declinedConnect(MessageUS message) {
		possibleCurrentConnections.remove(message.getLogin());
		String mess = message.getLogin() + " declined your connection request.";
		if (!message.getReason().equals(""))
			mess = message.getReason();
		JOptionPane.showMessageDialog(null, mess);
		this.viewingProfile.remove(message.getLogin());
	}

	/**
	 * Declined send message. Notifies user that one message was not sent for
	 * some reasons
	 *
	 * @param message the message from the server
	 */
	private void declinedSendMessage(MessageUS message) {
		chats.get(message.getID()).declinedMessage(message);
	}

	/**
	 * Chat ended. Calls the method which notifies specific observer about the
	 * ended chat
	 *
	 * @param message the message from the server
	 */
	private void chatEnded(MessageUS message) {
		if (!message.getLogin().contains("group "))
			possibleCurrentConnections.remove(message.getLogin());
		if (profiles.containsKey(message.getLogin()))
			profiles.get(message.getLogin()).enableMessage();
		File myMainPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + message.getLogin());
		myMainPhoto.delete();
		chats.get(message.getID()).chatEnded();
		chats.remove(message.getID());
		JOptionPane.showMessageDialog(null, "Your conversation with " + message.getLogin() + " has ended.");
	}

	/**
	 * List changed. Indicates observers about the changes online people list
	 *
	 * @param message the message from the server
	 */
	private void listChanged(MessageUS message) {
		List<String> list = message.getPeopleOnline();
		list.remove(myLogin);
		arrOnlineLogin = list.toArray(new String[list.size()]);
		setChanged();
		notifyObservers(message.getInstruction());
	}

	/**
	 * Connection broken. Creates a window to indicate that the connection with
	 * server has broken.
	 */
	private void connectionBroken() {
		JOptionPane.showMessageDialog(null, "Connection with server disrupted. Try to login later",
				"Connection disrupted", JOptionPane.ERROR_MESSAGE);
		client.interrupt();
		System.exit(0);
	}

	/**
	 * Something went wrong. Creates a window indicating that an operation has
	 * failed. Displays as a text which operation has failed and the reason
	 *
	 * @param message the message from the server from the server
	 */
	private void somethingWentWrong(MessageUS message) {
		JOptionPane.showMessageDialog(null, message.getReason(), "Something went wrong", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Receive photo. Adds the photo to the specific chat
	 *
	 * @param message the message from the server
	 */
	private void recievePhoto(MessageUS message) {
		setChanged();
		this.notifyObservers(message);
	}

	/**
	 * Receive public photo.
	 *
	 * @param message the message from the server from the server containing
	 * public photo
	 */
	private void receivePublicPhoto(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Encrypt. Ecrypts the given message
	 *
	 * @param message the message from the server
	 * @return the encrypted message
	 */
	public String encrypt(String message) {
		AES aes = new AES(myLogin + registrationTime);
		try {
			return aes.encrypt(message);
		} catch (Exception e) {
			return "<<<<<<<<<< The message was demaged >>>>>>>>";
		}
	}

	/**
	 * Decrypt. Decrypts the given message
	 *
	 * @param aMessage the a message
	 * @param creationTime the user account creation time
	 * @return the decrypted message
	 */
	private String decrypt(String[] aMessage, String creationTime) {
		AES aes = new AES(aMessage[0] + creationTime);
		return aes.decrypt(aMessage[2]);
	}

	/**
	 * Gets the smiley path.
	 *
	 * @return the smiley path
	 */
	public String getSmileyPath() {
		if (theme == 0)
			return "/resources/emoji.png";
		return "/resources/light_emoji.png";
	}

	/**
	 * Gets the picture path.
	 *
	 * @return the picture path
	 */
	public String getPicPath() {
		if (theme == 0)
			return "/resources/addphoto.png";
		return "/resources/light_photo.png";
	}

	/**
	 * Gets the theme.
	 *
	 * @return the theme
	 */
	public int getTheme() {
		return theme;
	}

	/**
	 * Sets the theme.
	 *
	 * @param newT the new Theme
	 * @return the theme number
	 */
	public int setTheme(int newT) {
		return theme = newT;
	}
}
