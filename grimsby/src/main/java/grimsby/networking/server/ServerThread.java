package grimsby.networking.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import grimsby.libraries.dataaccess.dao.factory.DAOFactory;
import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.GroupEntity;
import grimsby.libraries.entities.GroupMemberEntity;
import grimsby.libraries.entities.MessageEntity;
import grimsby.libraries.entities.ParticipantEntity;
import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.User;

/**
 * The Class ServerThread. Created for communication between a client and the
 * server. Each client has one thread and sends commands to the server.
 */
public class ServerThread extends Thread {

	/** The Constant EMAIL address of the server. */
	private static final String EMAIL = "your1chatroom@gmail.com";

	/** The Constant password of the server's email address. */
	private static final String PASS = "softwareworkshop";

	/** The client. */
	private Socket client;

	/** The online people. */
	private ConcurrentHashMap<String, ObjectOutputStream> onlinePeople;

	/** The connections. */
	private ConcurrentHashMap<String, HashSet<String>> connections;

	/** The connection information. */
	private ConcurrentHashMap<String, HashSet<String>> connectionInformation;

	/** The to client. */
	private ObjectOutputStream toClient;

	/** The from client. */
	private ObjectInputStream fromClient;

	/** The this login. */
	private String thisLogin;

	/** The list of connections. */
	private List<ServerThread> listOfConnections;

	/** The dao factory. */
	private DAOFactory daoFactory;

	/** The adao. */
	private AccountDAOImpl adao;

	/** The cdao. */
	private ConversationDAOImpl cdao;

	/** The gdao. */
	private GroupDAOImpl gdao;

	/** The gmdao. */
	private GroupMemberDAOImpl gmdao;

	/** The mdao. */
	private MessageDAOImpl mdao;

	/** The pdao. */
	private ParticipantDAOImpl pdao;

	/** The account. */
	private AccountEntity account;

	/**
	 * Instantiates a new server thread.
	 *
	 * @param client the client
	 * @param onlinePeople the online people
	 * @param connections the connections
	 * @param connectionInformation the connection information
	 * @param listOfConnections the list of connections
	 */
	public ServerThread(Socket client, ConcurrentHashMap<String, ObjectOutputStream> onlinePeople,
			ConcurrentHashMap<String, HashSet<String>> connections,
			ConcurrentHashMap<String, HashSet<String>> connectionInformation, List<ServerThread> listOfConnections) {
		super("ServerThread");
		try {
			fromClient = new ObjectInputStream(client.getInputStream());
			toClient = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			System.err.println("connection with client was not created...");
			this.interrupt();
		}
		this.connections = connections;
		this.client = client;
		this.onlinePeople = onlinePeople;
		this.listOfConnections = listOfConnections;
		this.connectionInformation = connectionInformation;
		this.thisLogin = null;

		setUpDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			try {
				MessageUS message = (MessageUS) fromClient.readObject();
				String comand = message.getInstruction();
				System.out.println("I got new command: " + comand);
				if (comand.equals(Constants.LOGIN)) {
					login(message);
				} else if (comand.equals(Constants.FORGOT_PASSWORD)) {
					forgotPassword(message);
				} else if (comand.equals(Constants.CREATE_ACCOUNT)) {
					createAccount(message);
				} else if (thisLogin != null && onlinePeople.containsKey(thisLogin)) {
					if (comand.equals(Constants.CONNECT)) {
						connect(message);
					} else if (comand.equals(Constants.CREATE_GROUP)) {
						createGroup(message);
					} else if (comand.equals(Constants.ADD_PERSON)) {
						addPersonToGroup(message);
					} else if (comand.equals(Constants.ACCEPTED_CONNECT)) {
						acceptConnect(message);
					} else if (comand.equals(Constants.DECLINED_CONNECT)) {
						declineConnect(message);
					} else if (comand.equals(Constants.SEND_MESSAGE)) {
						sendMessage(message);
					} else if (comand.equals(Constants.PUBLIC_MESSAGE)) {
						publicMessage(message);
					} else if (comand.equals(Constants.SEND_PHOTO)) {
						sendPhoto(message);
					} else if (comand.equals(Constants.SEND_PUBLIC_PHOTO)) {
						sendPublicPhoto(message);
					} else if (comand.equals(Constants.END_CHAT)) {
						endChat(message);
					} else if (comand.equals(Constants.LOAD_ALL_HISTORY)) {
						loadAllHistory(message);
					} else if (comand.equals(Constants.LOAD_PARTICULAR_HISTORY)) {
						loadParticularHistory(message);
					} else if (comand.equals(Constants.GET_MY_TOP)) {
						getTop();
					} else if (comand.equals(Constants.UPLOAD_PHOTO)) {
						uploadPhoto(message);
					} else if (comand.equals(Constants.VIEW_PROFILE)) {
						viewProfile(message);
					} else if (comand.equals(Constants.CHANGE_PASSWORD)) {
						changePassword(message);
					} else if (comand.equals(Constants.CHANGE_NAME)) {
						changeName(message);
					} else if (comand.equals(Constants.CHANGE_SURENAME)) {
						changeSurname(message);
					} else if (comand.equals(Constants.CHANGE_EMAIL)) {
						changeEmail(message);
					} else if (comand.equals(Constants.CHANGE_BIO)) {
						changeBio(message);
					} else if (comand.equals(Constants.PLAY_TIC_TAC_REQUEST)) {
						play2TicTac(message);
					} else if (comand.equals(Constants.PLAY_TIC_TAC_ACCEPTED)) {
						accept2TicTac(message);
					} else if (comand.equals(Constants.PLAY_TIC_TAC_DECLINED)) {
						decline2TicTac(message);
					} else if (comand.equals(Constants.MOVE_TIC_TAC)) {
						move2TicTac(message);
					} else if (comand.equals(Constants.END_TIC_TAC)) {
						endTicTac(message);
					} else if (comand.equals(Constants.END_OF_GAME)) {
						endOfGame(message);
					} else if (comand.equals(Constants.SET_THEME)) {
						setTheme(message);
					} else if (comand.equals(Constants.END)) {
						end();
						break;
					} else if (comand.equals(Constants.DELETE_ACCOUNT)) {
						deleteAccount();
					} else {
						System.err.println("unknown command " + comand);
					}
				} else {
					MessageUS m = new MessageUS(Constants.SOMETHING_WENT_WRONG);
					m.setReason("You are logged off! You need to log in first");
					synchronized (toClient) {
						toClient.writeObject(m);
					}
				}
			} catch (SocketException e) {
				// System.err.println("Socket exception.");
				end();
				break;
			} catch (Exception e) {
				System.err.println("Other exception.");
				e.printStackTrace();
				end();
				break;
			}
		}
		System.out.println("bye bye " + thisLogin);
	}

	private void deleteAccount() throws IOException {
		boolean deleted = true;
		try {
			deleted = adao.delete(adao.findByUsername(thisLogin));
		} catch (Exception e) {
			deleted = false;
		}
		MessageUS informationMessage = new MessageUS(Constants.DELETE_ACCOUNT_SUCCESSFUL);
		if (!deleted)
			informationMessage = new MessageUS(Constants.DELETE_ACCOUNT_UNSUCCESSFUL);
		synchronized (toClient) {
			toClient.writeObject(informationMessage);
		}
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
		}
		end();

	}

	/**
	 * Sets the theme. in the database. If operation is unsuccessful, sends
	 * answer something went wrong.
	 *
	 * @param message the new theme
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setTheme(MessageUS message) throws IOException {
		try {
			adao.setDarkTheme(adao.findByUsername(thisLogin), message.getStyle() == 1);
		} catch (SQLException e) {
			MessageUS mess = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			mess.setReason("Server error! the theme you chosen was not saved");
			synchronized (toClient) {
				toClient.writeObject(mess);
			}
		}

	}

	/**
	 * End of game. Sends to both of the players the message informing that the
	 * game has ended.
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void endOfGame(MessageUS message) throws IOException {
		MessageUS messageWinner = new MessageUS(message.getInstruction(), message.getLogin(), message.getLogin());
		messageWinner.setReason(message.getReason());
		messageWinner.setX(message.getX());
		messageWinner.setPlayer(message.isPlayer1());

		MessageUS messageLooser = new MessageUS(message.getInstruction(), thisLogin, thisLogin);
		messageLooser.setReason(message.getReason());
		messageLooser.setX(message.getX());
		messageLooser.setPlayer(message.isPlayer1());

		try {
			synchronized (onlinePeople.get(message.getLogin())) {
				onlinePeople.get(message.getLogin()).writeObject(messageLooser);
			}
		} catch (Exception e) {
		}
		synchronized (toClient) {
			toClient.writeObject(messageWinner);
		}

	}

	private void endTicTac(MessageUS message) throws IOException {
		MessageUS messageWinner = new MessageUS(message.getInstruction(), thisLogin, thisLogin);
		messageWinner.setReason(message.getReason());
		messageWinner.setX(message.getX());

		try {
			synchronized (onlinePeople.get(message.getLogin())) {
				onlinePeople.get(message.getLogin()).writeObject(messageWinner);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Play 2 tic tac toe. Sends request to play tick tack toe to the other
	 * person
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void play2TicTac(MessageUS message) throws IOException {
		try {
			if (onlinePeople.containsKey(thisLogin) && onlinePeople.containsKey(message.getLogin())
					&& !thisLogin.equals(message.getLogin())) {
				synchronized (onlinePeople.get(message.getLogin())) {
					onlinePeople.get(message.getLogin())
							.writeObject(new MessageUS(Constants.PLAY_TIC_TAC_REQUEST, thisLogin, ""));
				}

			} else {
				MessageUS mess = new MessageUS(Constants.SOMETHING_WENT_WRONG);
				mess.setReason("The person you are trying to play game with is offline");
				synchronized (toClient) {
					toClient.writeObject(mess);
				}
			}
		} catch (Exception e) {
			synchronized (toClient) {
				toClient.writeObject(new MessageUS(Constants.SOMETHING_WENT_WRONG));
			}
		}

	}

	/**
	 * Accept 2 tic tac toe play
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	private void accept2TicTac(MessageUS message) throws IOException {
		if (onlinePeople.containsKey(thisLogin) && onlinePeople.containsKey(message.getLogin())) {
			try {
				// Game id will be our partner's login

				MessageUS messPlayer1 = new MessageUS(Constants.PLAY_TIC_TAC_ACCEPTED, thisLogin, true);
				try {
					messPlayer1.addPhoto(adao.findByUsername(thisLogin).getImage());
				} catch (Exception e) {
				}
				MessageUS messPlayer2 = new MessageUS(Constants.PLAY_TIC_TAC_ACCEPTED, message.getLogin(), false);
				try {
					messPlayer2.addPhoto(adao.findByUsername(message.getLogin()).getImage());
				} catch (Exception e) {
				}
				synchronized (toClient) {
					toClient.writeObject(messPlayer2);
				}
				synchronized (onlinePeople.get(message.getLogin())) {
					onlinePeople.get(message.getLogin()).writeObject(messPlayer1);
				}
			} catch (Exception e) {
				try {
					MessageUS mes = new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, message.getLogin(), "");

					synchronized (toClient) {
						toClient.writeObject(mes);
					}
					mes = new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, thisLogin, "");
					synchronized (onlinePeople.get(mes.getLogin())) {
						try {
							onlinePeople.get(mes.getLogin()).writeObject(mes);
						} catch (Exception e1) {
							System.err.println("Could not send a message");
						}
					}
				} catch (Exception ex) {
				}
			}
		} else
			synchronized (toClient) {
				toClient.writeObject(new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, message.getLogin(), ""));
			}
	}

	/**
	 * Decline 2 tic tac toegame.
	 *
	 * @param message the message
	 */
	private void decline2TicTac(MessageUS message) {
		MessageUS messPlayer1 = new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, thisLogin, "");
		try {
			onlinePeople.get(message.getLogin()).writeObject(messPlayer1);
		} catch (IOException e) {
		}
	}

	/**
	 * Move 2 tic tac.
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void move2TicTac(MessageUS message) throws IOException {
		MessageUS mess = new MessageUS(Constants.MOVE_TIC_TAC, thisLogin, message.getX());
		System.out.println("login of the partner: " + message.getLogin());
		synchronized (onlinePeople.get(message.getLogin())) {
			try {
				onlinePeople.get(message.getLogin()).writeObject(mess);
			} catch (IOException e) {
				toClient.writeObject(new MessageUS(Constants.END_TIC_TAC, mess.getLogin(), ""));
				try {
					if (onlinePeople.contains(message.getLogin())) {
						synchronized (onlinePeople.get(message.getLogin())) {
							onlinePeople.get(message.getLogin())
									.writeObject(new MessageUS(Constants.END_TIC_TAC, thisLogin, ""));
						}
					}
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * Change bio information. Sends message to the user informing whether the
	 * action was successful.
	 *
	 * @param message the message containing instruction and updated bio.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void changeBio(MessageUS message) throws IOException {
		boolean wasSuccessful = false;
		String userInfo = message.getUserInformation()[0];
		try {
			wasSuccessful = adao.updateBio(adao.findByUsername(thisLogin), userInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MessageUS information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		if (wasSuccessful) {
			information = new MessageUS(Constants.CHANGE_BIO_ACCEPTED, new String[] { userInfo });
			information.setReason("Your bio was successfully updated!");
		} else {
			information = new MessageUS(Constants.CHANGE_BIO_DECLINED);
			information.setReason("Error! Your bio was not updated. Try again");
		}
		synchronized (toClient) {
			toClient.writeObject(information);
		}
	}

	/**
	 * Changes user's email address containing instruction and new email
	 * address. The new email address is set only if it is unique in the
	 * database. Sends message to the user informing whether the action was
	 * successful.
	 *
	 * @param message the message containing instruction and new email adress
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void changeEmail(MessageUS message) throws IOException {
		boolean wasSuccessful = false;
		String userInfo = message.getUserInformation()[0];
		try {
			wasSuccessful = adao.updateEmail(adao.findByUsername(thisLogin), userInfo);
		} catch (SQLException e) {
		}
		MessageUS information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		if (wasSuccessful) {
			information = new MessageUS(Constants.CHANGE_EMAIL_ACCEPTED, new String[] { userInfo });
			information.setReason("Your email address was successfully updated!");
		} else {
			information = new MessageUS(Constants.CHANGE_EMAIL_DECLINED);
			try {
				if (null != adao.findByEmail(message.getUserInformation()[0]))
					information.setReason("The email address: " + userInfo + " is already registered. Try another one");
				else
					information.setReason("Server error. Email address could not be updated");
			} catch (SQLException e1) {
				System.err.println("SQL exception");
				information.setReason("Error! Email address could not be updated");
			}
		}
		synchronized (toClient) {
			toClient.writeObject(information);
		}
	}

	/**
	 * Changes client's surname. Sends message to the user informing whether the
	 * action was successful.
	 *
	 * @param message the message containing instruction and new surname.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void changeSurname(MessageUS message) throws IOException {
		boolean wasSuccessful = false;
		String userInfo = message.getUserInformation()[0];
		try {
			wasSuccessful = adao.setSurname(adao.findByUsername(thisLogin), userInfo);
		} catch (SQLException e) {
			System.err.println("SQL exception");
		}
		MessageUS information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		if (wasSuccessful) {
			information = new MessageUS(Constants.CHANGE_SURENAME_ACCEPTED, new String[] { userInfo });
			information.setReason("Your surname was successfully changed!");
		} else {
			information = new MessageUS(Constants.CHANGE_SURENAME_DECLINED);
			information.setReason("Error! your surname was not changed. Try again");
		}
		synchronized (toClient) {
			toClient.writeObject(information);
		}
	}

	/**
	 * Changes client's name. Sends message to the user informing whether the
	 * action was successful.
	 *
	 * @param message the message containing instruction and new name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void changeName(MessageUS message) throws IOException {
		boolean wasSuccessful = false;
		String userInfo = message.getUserInformation()[0];
		try {
			wasSuccessful = adao.setName(adao.findByUsername(thisLogin), userInfo);
		} catch (SQLException e) {
			System.err.println("SQL exception");
		}
		MessageUS information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		if (wasSuccessful) {
			information = new MessageUS(Constants.CHANGE_NAME_ACCEPTED, new String[] { userInfo });
			information.setReason("Your name was successfully updated!");
		} else {
			information = new MessageUS(Constants.CHANGE_NAME_DECLINED);
			information.setReason("Error! your name was not updated. Try again");
		}
		synchronized (toClient) {
			toClient.writeObject(information);
		}
	}

	/**
	 * View profile. Sends the message to the client containing the profile
	 * information of the requested user.
	 *
	 * @param message the message containing instruction and login of the user
	 * whose profile information is requested
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void viewProfile(MessageUS message) throws IOException {
		MessageUS information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		try {
			String username = message.getLogin();
			AccountEntity theUsersAccount = adao.findByUsername(username);
			String[] info = { username, theUsersAccount.getName(), theUsersAccount.getSurname(),
					theUsersAccount.getEmail(), theUsersAccount.getBio() };
			information = new MessageUS(Constants.VIEW_PROFILE, info);
			byte[] photo = adao.findByUsername(message.getLogin()).getImage();
			information.addPhoto(photo);
		} catch (Exception e) {
			e.printStackTrace();
			information = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			information.setReason("The users " + message.getLogin() + " information could not be loaded. try again");
		}
		synchronized (toClient) {
			toClient.writeObject(information);
		}
	}

	/**
	 * Uploads a new photo. Sends message to the user informing whether the
	 * action was successful.
	 *
	 * @param message the message containing instruction and byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void uploadPhoto(MessageUS message) throws IOException {
		MessageUS uploaded = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		try {
			byte[] photo = message.getImage();
			adao.updateImage(adao.findByUsername(thisLogin), thisLogin + System.currentTimeMillis(), photo);
			uploaded = new MessageUS(Constants.PHOTO_UPLOADED);
			uploaded.addPhoto(photo);
		} catch (Exception e) {
			uploaded = new MessageUS(Constants.PHOTO_DECLINED);
		}
		toClient.writeObject(uploaded);

	}

	/**
	 * Gets the favourite users and sends the message containing these users to
	 * the client.
	 *
	 * @return the top
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void getTop() throws IOException {
		MessageUS messageOfTop = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		try {
			List<AccountEntity> accounts = adao.getTopContacts(adao.findByUsername(thisLogin), 4);
			Set<User> myTopContacts = new HashSet<>();
			accounts.forEach(ac -> myTopContacts.add(new User(ac.getImage(), ac.getUsername())));

			messageOfTop = new MessageUS(Constants.GET_MY_TOP, myTopContacts, thisLogin, "");
		} catch (SQLException e) {
			e.printStackTrace();
			messageOfTop = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			messageOfTop.setReason("Something went wrong. Could not give top contacts list");
		}
		synchronized (toClient) {
			toClient.writeObject(messageOfTop);
		}
	}

	/**
	 * Load all history. Gets and sends the whole history of the client
	 *
	 * @param message the message containing instruction
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadAllHistory(MessageUS message) throws IOException {
		MessageUS sendingHistory = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		try {
			List<String> historyList = adao.getAllHistory(adao.findByUsername(thisLogin));
			String[][] temp = new String[historyList.size()][1];
			for (int i = 0; i < historyList.size(); i++)
				temp[i][0] = historyList.get(i);

			sendingHistory = new MessageUS(Constants.LOAD_ALL_HISTORY, temp, new HashMap<>());
		} catch (Exception e) {
			sendingHistory = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			sendingHistory.setReason("The history could not be loaded. Try again");
		}
		synchronized (sendingHistory) {
			toClient.writeObject(sendingHistory);
		}
	}

	/**
	 * Load particular history. Gets and sends particular history to the user
	 *
	 * @param message the message containing instruction and the information
	 * about which chat should be loaded
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadParticularHistory(MessageUS message) throws IOException {
		MessageUS messageUS = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		String[] info = message.getUserInformation()[0].split("---");
		try {
			if (info[0].contains("-")) {
				String groupId = info[0].split("-")[1] + "---" + info[1];

				String[][] history = adao.getParticularGroupHistory(groupId);
				HashMap<String, String> creationT = new HashMap<>();

				for (String[] arr : history)
					creationT.put(arr[0], adao.findByUsername(arr[0]).getTimestamp() + "");

				messageUS = new MessageUS(Constants.LOAD_PARTICULAR_HISTORY, history, creationT);

			} else {
				String[][] history = adao.getParticularHistory(adao.findByUsername(thisLogin),
						adao.findByUsername(info[0]));
				HashMap<String, String> creationT = new HashMap<>();
				for (String[] arr : history)
					creationT.put(arr[0], adao.findByUsername(arr[0]).getTimestamp() + "");

				messageUS = new MessageUS(Constants.LOAD_PARTICULAR_HISTORY, history, creationT);
			}

			synchronized (toClient) {
				toClient.writeObject(messageUS);
			}
		} catch (Exception e) {
			MessageUS mess = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			mess.setReason("Unable to load the history with: " + info[0]);
			synchronized (toClient) {
				toClient.writeObject(mess);
			}
		}

	}

	/**
	 * Changes password.Sends message to the user informing whether the action
	 * was successful.
	 *
	 * @param message the message containing instruction, old and new passwords
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void changePassword(MessageUS message) throws IOException {
		MessageUS answer = new MessageUS(Constants.DECLINE_PASSWORD);
		String[] accoutnInfo = message.getUserInformation();
		String oldPass = accoutnInfo[0];
		String newPass = accoutnInfo[1];
		try {
			if (oldPass.equals((account = adao.findByUsername(thisLogin)).getPassword())) {
				adao.updatePassword(account, newPass);
				answer = new MessageUS(Constants.ACCEPT_PASSWORD);
			} else
				answer.setReason("Your old password is incorrect!");
		} catch (Exception e) {
			answer.setReason("Something went wrong... Your password was not changed");
			System.err.println("Password was not changed");
		}
		synchronized (toClient) {
			toClient.writeObject(answer);
		}
	}

	/**
	 * Login. Logins the user or declines the logging in. Sends the outcome to
	 * the user. If logging in was successful, additional information is added
	 * to the message: username, registration time, flags for temporary password
	 * and first time logging in as well as photo as byte[]. otherwise declined
	 * login instruction is sent
	 *
	 * @param message the message containing instruction and relevant
	 * information
	 * @throws Exception the exception
	 */
	private void login(MessageUS message) throws Exception {
		MessageUS answer = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		String[] details = message.getUserInformation();
		String username = details[0];
		String password = details[1];
		account = adao.findByUsername(username);
		answer = new MessageUS(Constants.DECLINED_LOGIN);
		if (adao.findByUsername(username) == null)
			answer.setReason("The username " + username + " does not exist!");
		else {
			boolean verified = adao.verifyUser(username, password);
			if (verified) {
				this.thisLogin = username;
				if (!onlinePeople.containsKey(thisLogin)) {
					getTop();
					String[] userInformation = new String[] { thisLogin,
							adao.findByUsername(thisLogin).getTimestamp() + "" };
					answer = new MessageUS(Constants.ACCEPTED_LOGIN, userInformation);
					answer.setTempData(adao.findByUsername(thisLogin).getEmail());
					if (account.isReminder())
						answer.setTemporaty();
					if (account.isFirstLogin())
						answer.setIsFirstTime();

					byte[] photo = adao.findByUsername(username).getImage();
					answer.addPhoto(photo);
					int style = adao.findByUsername(thisLogin).isDarkTheme() ? 1 : 0;
					answer.setStyle(style);

					onlinePeople.put(thisLogin, toClient);
					connections.put(thisLogin, new HashSet<>());
					listOfConnections.add(this);
					System.out.println(thisLogin + " is logged!");
					adao.setFirstLogin(account, false);
				} else
					answer.setReason("You cannot login whilst being online...");
			} else
				answer.setReason("The password is incorrect. Try again or send reminder to the email.");
		}
		synchronized (toClient) {
			toClient.writeObject(answer);
		}
		listIsChanged();
	}

	/**
	 * Forgot password. Sends reminder to the clients email adress which is
	 * stored in database. Information message about the success or failure is
	 * sent to the client.
	 *
	 * Reference:
	 * https://crunchify.com/java-mailapi-example-send-an-email-via-gmail-smtp/
	 *
	 * @param m the message containing instruction and clients login.
	 * @throws Exception the exception
	 */
	private void forgotPassword(MessageUS m) throws Exception {
		MessageUS messageUS = new MessageUS(Constants.SOMETHING_WENT_WRONG);
		try {
			System.out.println("forgot password");
			String userEmailAdress = null;
			if (adao.findByUsername(m.getLogin()) == null) {
				System.out.println("account does not exist");
				messageUS = new MessageUS(Constants.EMAIL_NOT_SENT);
				messageUS.setReason("The account with username: " + m.getLogin() + " does not exist!");
			} else {
				String password = adao.generateTempPassword(adao.findByUsername(m.getLogin()));
				userEmailAdress = adao.findByUsername(m.getLogin()).getEmail();

				Properties props = System.getProperties();
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				Session getMailSession = Session.getDefaultInstance(props, null);
				MimeMessage message = new MimeMessage(getMailSession);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmailAdress));
				message.setSubject("Password reminder");
				String emailBody = "Dear " + m.getLogin() + ","
						+ "<br><br>Thank you for using our Chatroom. <br>Your temporary password is: " + password
						+ " . Use this password when logging in.<br><br> Regards, <br>Chatroom team";
				message.setContent(emailBody, "text/html");
				Transport transport = getMailSession.getTransport("smtp");
				transport.connect("smtp.gmail.com", EMAIL, PASS);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
				messageUS = (new MessageUS(Constants.EMAIL_SENT));
			}
		} catch (Exception e) {
			System.out.println("The letter was not sent");
			e.printStackTrace();
			messageUS = (new MessageUS(Constants.EMAIL_NOT_SENT));
			messageUS.setReason("Error in server...");
		} finally {
			synchronized (toClient) {
				toClient.writeObject(messageUS);
			}
		}
	}

	/**
	 * Creates the account. Sends confirmation or failure message to the client.
	 *
	 * @param messageUS the message containing instructions and potential
	 * client's information: username, password, email address
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void createAccount(MessageUS messageUS) throws IOException {
		try {
			String[] userDetails = messageUS.getUserInformation();
			String username = userDetails[0];
			String password = userDetails[1];
			String email = userDetails[2];
			String name = "Unknown";
			String surname = "Unknown";
			boolean usernameNotTaken = false;

			if ((usernameNotTaken = ((account = adao.findByUsername(username)) == null))
					&& adao.findByEmail(email) == null) {
				account = new AccountEntity(username, password, name, surname, email, false, true,
						"You have no bio :) ");
				adao.create(account);
				synchronized (toClient) {
					toClient.writeObject(new MessageUS(Constants.ACCEPTED_CREATE_ACCOUNT));
				}
			} else {
				MessageUS m = new MessageUS(Constants.DECLINED_CREATE_ACCOUNT);
				if (!usernameNotTaken) {
					m.setReason("The username " + username + " is taken. Try another one");
					System.out.println("username taken");
				} else
					m.setReason("The email adress " + email + " is taken. Try another one");
				synchronized (toClient) {
					toClient.writeObject(m);
				}
			}
		} catch (SQLException e) {
			synchronized (toClient) {
				toClient.writeObject(new MessageUS(Constants.SOMETHING_WENT_WRONG));
			}
		}

	}

	/**
	 * Connect. Sends a connection request to the chosen client.
	 *
	 * @param messageUS the message containing instruction and the potential
	 * partner's username
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void connect(MessageUS messageUS) throws IOException {
		try {
			if (onlinePeople.containsKey(thisLogin) && onlinePeople.containsKey(messageUS.getLogin())
					&& !thisLogin.equals(messageUS.getLogin())) {
				synchronized (onlinePeople.get(messageUS.getLogin())) {
					onlinePeople.get(messageUS.getLogin())
							.writeObject(new MessageUS(Constants.CONNECTION_REQUEST, thisLogin, ""));
				}
				System.out.println("Asking for the permission to connect...");
			} else
				synchronized (toClient) {
					MessageUS message = new MessageUS(Constants.DECLINED_CONNECT, messageUS.getLogin(), "");
					message.setReason("The person you are trying to connect with is offline");
					toClient.writeObject(message);
				}
		} catch (Exception e) {
			synchronized (toClient) {
				toClient.writeObject(new MessageUS(Constants.SOMETHING_WENT_WRONG));
			}
		}
	}

	/**
	 * Creates the group. Sends information message to all the members of the
	 * group containing accepted connect group instruction, group name and group
	 * id.
	 *
	 * @param message the message containing instruction and group name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void createGroup(MessageUS message) throws IOException {
		System.out.println("creating group");
		// people online is list of people in this group chat
		ArrayList<String> peopleInGroup = message.getPeopleOnline();
		peopleInGroup.add(thisLogin);

		for (String person : peopleInGroup)
			if (!onlinePeople.containsKey(person))
				peopleInGroup.remove(person);

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String groupID = message.getName() + "---" + currentTime;
		System.out.println("Potential group id: " + groupID);

		try {
			account = adao.findByUsername(thisLogin);
			GroupEntity groupEntity = new GroupEntity(groupID, account, message.getName(), currentTime);
			gdao.create(groupEntity);
			MessageUS messageConnection = new MessageUS(Constants.ACCEPTED_CONNECT_GROUP, message.getName(), groupID);
			Set<User> potentialParticipants = new HashSet<>();

			peopleInGroup.forEach(name -> {
				try {
					User newUser = new User(adao.findByUsername(name).getImage(), name);
					potentialParticipants.add(newUser);
				} catch (SQLException e1) {
					System.err.println("Could not add image of " + name + " to the array");
					e1.printStackTrace();
				}
			});

			// even some of them might not be successful, but it is better to
			// have them all and do not use uneccessary data in user
			messageConnection.setPeople(potentialParticipants);

			peopleInGroup.forEach(e -> {
				if (onlinePeople.containsKey(e))
					synchronized (onlinePeople.get(e)) {
						try {
							GroupMemberEntity groupMember = new GroupMemberEntity(groupEntity, adao.findByUsername(e));
							gmdao.create(groupMember);

							onlinePeople.get(e).writeObject(messageConnection);
							synchronized (connections.get(e)) {
								connections.get(e).add(groupID);
							}

						} catch (Exception e1) {
							peopleInGroup.remove(e);
							e1.printStackTrace();
						}
					}
				else
					peopleInGroup.remove(e);
			});
			connectionInformation.put(groupID, new HashSet<>(peopleInGroup));

			ConversationEntity conversation = new ConversationEntity(adao.findByUsername(thisLogin),
					gdao.findById(groupID));
			cdao.create(conversation);
		} catch (SQLException e2) {
			MessageUS messageConnection = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			messageConnection.setReason("The group " + message.getName() + " could not be created");
			synchronized (messageConnection) {
				toClient.writeObject(messageConnection);
			}
		}
	}

	/**
	 * Adds the person to group. Sends the notification to all members of the
	 * group.
	 *
	 * @param message the message containing instruction, login of the person to
	 * be added and the chat id.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addPersonToGroup(MessageUS message) throws IOException {
		String personToAdd = message.getLogin();
		String chatID = message.getID();
		String chatName = message.getName();
		if (onlinePeople.containsKey(personToAdd)) {
			if (connectionInformation.containsKey(personToAdd)) {
				try {
					MessageUS mess = new MessageUS(Constants.DECLINED_ADD, personToAdd, chatID);
					mess.setName(chatName);
					mess.setReason(personToAdd + " is already in the group chat \"" + chatName + "\"");
					try {
						mess.addPhoto(adao.findByUsername(personToAdd).getImage());
					} catch (SQLException e) {
						mess.addPhoto(null);
					}
					synchronized (toClient) {
						toClient.writeObject(mess);
					}
				} catch (IOException e) {
				}
			} else {
				HashSet<String> peopleInTheChat = connectionInformation.get(chatID);
				peopleInTheChat.add(personToAdd);

				HashSet<User> setOfPeople = new HashSet<>();

				peopleInTheChat.forEach(name -> {
					try {
						User thisUser = new User(adao.findByUsername(name).getImage(), name);
						setOfPeople.add(thisUser);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				try {
					gmdao.create(new GroupMemberEntity(gdao.findById(chatID), adao.findByUsername(personToAdd)));
				} catch (SQLException e1) {
					MessageUS mess = new MessageUS(Constants.DECLINED_ADD, personToAdd, chatID);
					mess.setReason("Server error ");
					synchronized (toClient) {
						toClient.writeObject(mess);
					}
					e1.printStackTrace();
				}
				MessageUS messageConnection = new MessageUS(Constants.ACCEPTED_CONNECT_GROUP, chatName, chatID);
				messageConnection.setPeople(setOfPeople);
				synchronized (onlinePeople.get(personToAdd)) {
					try {
						onlinePeople.get(personToAdd).writeObject(messageConnection);
					} catch (IOException e) {
						return;
					}
				}

				MessageUS mess = new MessageUS(Constants.ACCEPTED_ADD, personToAdd, chatID);
				mess.setName(chatName);
				try {
					mess.addPhoto(adao.findByUsername(personToAdd).getImage());
				} catch (SQLException e1) {
				}

				peopleInTheChat.forEach(x -> {
					if (!x.equals(personToAdd))
						synchronized (onlinePeople.get(x)) {
							try {
								onlinePeople.get(x).writeObject(mess);
							} catch (IOException e) {
								System.out.println("Could not send the message to " + x);
							}
						}
				});
			}
			synchronized (connections.get(personToAdd)) {
				connections.get(personToAdd).add(message.getID());
			}
			synchronized (connectionInformation.get(message.getID())) {
				connectionInformation.get(message.getID()).add(personToAdd);
			}
		}
	}

	/**
	 * Accept connect. Sends confirmation message to the person who sent the
	 * connection request. Creates new private chat
	 *
	 * @param messageUS the message containing instruction and login of the
	 * partner
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void acceptConnect(MessageUS messageUS) throws IOException {
		if (onlinePeople.containsKey(thisLogin) && onlinePeople.containsKey(messageUS.getLogin())) {
			grimsby.libraries.entities.ConversationEntity conversation = null;
			List<grimsby.libraries.entities.ConversationEntity> conversations = null;
			int conversationID = -1;
			try {
				conversation = new grimsby.libraries.entities.ConversationEntity(
						adao.findByUsername(messageUS.getLogin()), null);

				cdao.create(conversation);

				conversations = cdao.findByOwner(adao.findByUsername(messageUS.getLogin()));
				conversation = conversations.get(conversations.size() - 1);

				ParticipantEntity sender = new ParticipantEntity(adao.findByUsername(messageUS.getLogin()),
						conversation);
				ParticipantEntity receiver = new ParticipantEntity(adao.findByUsername(thisLogin), conversation);
				pdao.create(sender);
				pdao.create(receiver);

				conversationID = conversation.getId();
				synchronized (connections.get(thisLogin)) {
					connections.get(thisLogin).add(conversationID + "");
				}
				synchronized (connections.get(messageUS.getLogin())) {
					connections.get(messageUS.getLogin()).add(conversationID + "");
				}

				HashSet<String> peopleInChat = new HashSet<>();
				peopleInChat.add(thisLogin);
				peopleInChat.add(messageUS.getLogin());
				connectionInformation.put(conversationID + "", peopleInChat);

				MessageUS mess = new MessageUS(Constants.ACCEPTED_CONNECT, messageUS.getLogin(), conversationID + "");
				mess.addPhoto(adao.findByUsername(messageUS.getLogin()).getImage());
				synchronized (toClient) {
					toClient.writeObject(mess);
				}
				try {
					MessageUS mess2 = new MessageUS(Constants.ACCEPTED_CONNECT, thisLogin, conversationID + "");
					mess2.addPhoto(adao.findByUsername(thisLogin).getImage());
					synchronized (onlinePeople.get(messageUS.getLogin())) {
						onlinePeople.get(messageUS.getLogin()).writeObject(mess2);
					}
				} catch (IOException e) {
				}
			} catch (SQLException e) {
				synchronized (connections.get(thisLogin)) {
					connections.get(thisLogin).remove(conversationID + "");
				}
				synchronized (connections.get(messageUS.getLogin())) {
					connections.get(messageUS.getLogin()).remove(conversationID + "");
				}
				connectionInformation.remove(conversation + "");
				MessageUS mes = new MessageUS(Constants.SOMETHING_WENT_WRONG);
				mes.setReason("Your connection with " + messageUS.getLogin()
						+ " was not established due to server problems. Try again");
				synchronized (toClient) {
					toClient.writeObject(mes);
				}
				mes.setReason(
						"Your connection with " + thisLogin + " was not established due to server problems. Try again");

				synchronized (onlinePeople.get(mes.getLogin())) {
					try {
						onlinePeople.get(mes.getLogin()).writeObject(mes);
					} catch (Exception e1) {
						System.err.println("Could not send the message");
					}
				}
				e.printStackTrace();
			}
		} else
			synchronized (toClient) {
				toClient.writeObject(new MessageUS(Constants.DECLINED_CONNECT, messageUS.getLogin(), ""));
			}
	}

	/**
	 * Decline connect. Sends message to the client who asked to connect
	 * connection declined answer.
	 *
	 * @param messageUS the message containing instruction, and the login of the
	 * person that sent a connection request
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void declineConnect(MessageUS messageUS) throws IOException {
		try {
			synchronized (onlinePeople.get(messageUS.getLogin())) {
				onlinePeople.get(messageUS.getLogin())
						.writeObject(new MessageUS(Constants.DECLINED_CONNECT, thisLogin, ""));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Sends a message to the particular chat. Used for both private and group
	 * chats
	 *
	 * @param messageUS the message containing instruction and required
	 * information: author of the message, message text and conversation id
	 * @throws IOException
	 */
	private void sendMessage(MessageUS messageUS) throws IOException {
		try {
			if (onlinePeople.containsKey(thisLogin) && connectionInformation.containsKey(messageUS.getID())) {
				System.out.println("got message " + messageUS.getMessage() + " from " + thisLogin);
				HashSet<String> peopleInConversation = connectionInformation.get(messageUS.getID());
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

				MessageUS newMessage = new MessageUS(Constants.RECEIVE_MESSAGE, messageUS.getMessage(),
						messageUS.getLogin(), messageUS.getID());
				newMessage.setTimeStamp(timeStamp);
				newMessage.setAccountCreationTime(adao.findByUsername(thisLogin).getTimestamp() + "");

				peopleInConversation.forEach(e -> {
					synchronized (onlinePeople.get(e)) {
						try {
							onlinePeople.get(e).writeObject(newMessage);
						} catch (IOException e1) {
							System.err.println("The message was not sent to " + e);
						}
					}
				});

				AccountEntity sender = adao.findByUsername(thisLogin);
				ConversationEntity conversation;
				GroupEntity group;
				try {
					// if exception is thrown, means that we are dealing with
					// group chat
					conversation = cdao.findById(Integer.parseInt(messageUS.getID()));
					List<ParticipantEntity> participants = pdao.findByConversation(conversation);
					for (ParticipantEntity participant : participants)
						if (participant.getAccount().getId() != sender.getId()) {
							MessageEntity messageEntity = new MessageEntity(sender, participant.getAccount(),
									conversation, messageUS.getMessage());
							mdao.create(messageEntity);
						}
				} catch (Exception e) {
					group = gdao.findById(messageUS.getID());
					List<GroupMemberEntity> members = gmdao.findByGroup(group);

					for (GroupMemberEntity groupMember : members)
						if (groupMember.getAccount().getId() != sender.getId()) {

							MessageEntity messageEntity = new MessageEntity(sender, groupMember.getAccount(),
									cdao.findByGroup(group), messageUS.getMessage());
							mdao.create(messageEntity);
						}
				}
			} else {
				MessageUS message = new MessageUS(Constants.DECLINED_SEND_MESSAGE, messageUS.getMessage(),
						messageUS.getLogin(), messageUS.getID());
				message.setAccountCreationTime(adao.findByUsername(thisLogin).getTimestamp() + "");
				synchronized (toClient) {
					toClient.writeObject(message);
				}
			}
		} catch (Exception e) {
			MessageUS message = new MessageUS(Constants.SOMETHING_WENT_WRONG);
			message.setReason("The message was not sent");
			synchronized (toClient) {
				toClient.writeObject(message);
			}
		}
	}

	/**
	 * Send photo to the particular chat.
	 *
	 * @param messageUS the message containing instruction and information
	 * required to send a photo: author, conversation id, and the byte[]
	 * (representing photo)
	 */
	private void sendPhoto(MessageUS messageUS) {
		try {
			if (onlinePeople.containsKey(thisLogin) && connectionInformation.containsKey(messageUS.getID())) {
				System.out.println("got photo " + messageUS.getMessage() + " from " + thisLogin);
				HashSet<String> peopleInConversation = connectionInformation.get(messageUS.getID());
				peopleInConversation.forEach(e -> {
					Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
					MessageUS newMessage = new MessageUS(Constants.RECEIVE_PHOTO, messageUS.getImage(),
							messageUS.getLogin(), messageUS.getID());
					newMessage.setTimeStamp(timeStamp);
					synchronized (onlinePeople.get(e)) {
						try {
							onlinePeople.get(e).writeObject(newMessage);
						} catch (IOException e1) {
						}
					}
				});
			} else {
				synchronized (toClient) {
					MessageUS message = new MessageUS(Constants.PHOTO_DECLINED);
					message.setReason("Error! The photo was not sent");
					toClient.writeObject(message);
				}
			}
		} catch (Exception e) {
			System.err.println("Something went wrong");
		}

	}

	/**
	 * Sends public message to all online users.
	 *
	 * @param message the message containing instruction and required
	 * information to send a public message: author, message text
	 */
	private void publicMessage(MessageUS message) {
		MessageUS publicMessage = new MessageUS(Constants.RECEIVE_PUBLIC_MESSAGE, message.getMessage(),
				message.getLogin(), message.getID());

		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		publicMessage.setTimeStamp(timeStamp);

		onlinePeople.keySet().forEach(e -> {
			synchronized (onlinePeople.get(e)) {
				try {
					onlinePeople.get(e).writeObject(publicMessage);
				} catch (IOException e1) {
					System.out.println("The message was not sent");
				}
			}
		});
	}

	/**
	 * Send public photo.
	 *
	 * @param message the message
	 */
	private void sendPublicPhoto(MessageUS message) {
		MessageUS publicPhoto = new MessageUS(Constants.RECEIVE_PUBLIC_PHOTO, message.getImage(), message.getLogin(),
				message.getID());

		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		publicPhoto.setTimeStamp(timeStamp);

		onlinePeople.keySet().forEach(e -> {
			synchronized (onlinePeople.get(e)) {
				try {
					onlinePeople.get(e).writeObject(publicPhoto);
				} catch (IOException e1) {
					System.out.println("The message was not sent");
					e1.printStackTrace();
				}
			}
		});

	}

	/**
	 * Ends the particular chat by sending message containing chat end
	 * instruction, chat name and chat id.
	 *
	 * @param message the message containing instructions and required
	 * information to end the chat: chat/partner's name and conversation id.
	 */
	private void endChat(MessageUS message) {
		HashSet<String> peopleInChat = connectionInformation.get(message.getID());
		connectionInformation.remove(message.getID());
		MessageUS newMessage = new MessageUS(Constants.CHAT_ENDED, message.getLogin(), message.getID());
		try {
			peopleInChat.forEach(e -> {
				synchronized (connections.get(e)) {
					connections.get(e).remove(message.getID());
				}
				synchronized (onlinePeople.get(e)) {
					if (onlinePeople.containsKey(e) && !e.equals(thisLogin))
						try {
							onlinePeople.get(e).writeObject(newMessage);
						} catch (IOException e1) {
							System.err.println("The message was not sent...");
						}
				}
			});
		} catch (Exception e) {
			System.err.println("Something went wrong...");
		}
	}

	/**
	 * Ending connection.
	 */
	public synchronized void end() {
		if (thisLogin != null)
			try {
				HashSet<String> myConnections = connections.get(thisLogin);
				if (myConnections == null)
					myConnections = new HashSet<>();
				myConnections.forEach(e -> {
					MessageUS sendBye = new MessageUS(Constants.CHAT_ENDED, thisLogin, e);
					connectionInformation.get(e).forEach(q -> {
						try {
							onlinePeople.get(q).writeObject(sendBye);
							connections.get(q).remove(e);
						} catch (Exception e1) {
							System.err.println("Could not send bye to " + q);
						}
					});
					connectionInformation.remove(e);

				});
				connections.remove(thisLogin);
				onlinePeople.remove(thisLogin);

				listOfConnections.remove(this);
				listIsChanged();
				fromClient.close();
				client.close();
			} catch (Exception e) {
				connections.remove(thisLogin);
				onlinePeople.remove(thisLogin);

				listOfConnections.remove(this);
				listIsChanged();
				try {
					fromClient.close();
					client.close();
				} catch (IOException e1) {

				}
				System.out.println("catching..");
			}
	}

	/**
	 * List is changed. Called when list of online users changes(due to logging
	 * in or logging out)
	 */
	private void listIsChanged() {
		Set<String> idPeopleOnline = onlinePeople.keySet();
		ArrayList<String> peopleOnline = new ArrayList<>(idPeopleOnline);
		MessageUS messageToEverybody = new MessageUS(Constants.LIST_CHANGED, peopleOnline);
		listOfConnections.forEach(x -> x.notifyAboutChanges(messageToEverybody));
	}

	/**
	 * Notifies users about changed list of online users by sending a message.
	 *
	 * @param toSend the message to send to the users, containing the list of
	 * online people
	 */
	private void notifyAboutChanges(MessageUS toSend) {
		synchronized (toClient) {
			try {
				toClient.writeObject(toSend);
			} catch (IOException e) {
				System.err.println("Unable to send message to " + thisLogin);
			}
		}
	}

	/**
	 * Sets the up database.
	 */
	private void setUpDatabase() {
		daoFactory = DAOFactory.getInstance();
		this.adao = (AccountDAOImpl) daoFactory.getAccountDAO();
		this.cdao = (ConversationDAOImpl) daoFactory.getConversationDAO();
		this.gdao = daoFactory.getGroupDAOImpl();
		this.gmdao = daoFactory.getGroupMemberDAOImpl();
		this.mdao = (MessageDAOImpl) daoFactory.getMessageDAO();
		this.pdao = daoFactory.getParticipantDAO();
	}
}