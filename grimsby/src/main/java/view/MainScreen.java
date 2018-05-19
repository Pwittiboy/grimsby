package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sun.mail.handlers.message_rfc822;

import grimsby.networking.util.Constants;
import grimsby.networking.util.Friends;
import grimsby.networking.util.MessageObject;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.NoughtsCrossesMain;
import grimsby.networking.util.User;

// TODO: Auto-generated Javadoc
/**
 * The Class MainScreen.
 */
public class MainScreen extends JFrame implements Observer {

	/** The content pane. */
	private JPanel contentPane;

	/** The message text. */
	private JTextField txtrMessage;

	/** The queue of commands. */
	private LinkedBlockingQueue<MessageUS> queueOfCommands;

	/** The model. */
	private Model model;

	/** The my login. */
	private String myLogin;

	/** The possible current connections. */
	private HashSet<String> possibleCurrentConnections;

	/** The caret. */
	private DefaultCaret caret;

	/** The scroll users. */
	private JScrollPane scrollUsers;

	/** The group chat. */
	private JTextPane groupChat;

	/** The lbl photo. */
	private JLabel lblPhoto;

	/** The top. */
	private Set<User> top;

	/** The list online. */
	private JList<String> listOnline;

	/** The emoji manage. */
	private EmojiManage emojiManage;

	/** The my best friends. */
	private ArrayList<Friends> myBestFriends;

	/** The viewing profile. */
	private HashSet<String> viewingProfile;

	/** The games. */
	private HashMap<String, NoughtsCrossesMain> games;

	/** The emoji layer. */
	private JLayeredPane emojiLayer;

	/** The view logs option. */
	private JMenuItem mntmViewLogs;

	/** The other player option. */
	private JMenuItem otherPlayer;

	/** The change password option. */
	private JMenuItem changePass;

	/** The start group chat option . */
	private JMenuItem mntmStartGroupChat;

	/** The profiles. */
	private HashMap<String, Profile> profiles;

	/** The delete account. */
	private JMenuItem deleteAccount;

	/**
	 * Instantiates a new main screen.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param possibleCurrentConnections the possible current connections
	 * @param model the model
	 * @param myID the my ID
	 * @param myRegistrationTime the my registration time
	 * @param myTop the my top
	 * @param emojiManage the emoji manage
	 * @param games the games
	 * @param viewingProfile the viewing profile
	 * @param profiles the profiles
	 */
	public MainScreen(LinkedBlockingQueue<MessageUS> queueOfCommands, HashSet<String> possibleCurrentConnections,
			Model model, String myID, String myRegistrationTime, Set<User> myTop, EmojiManage emojiManage,
			HashMap<String, NoughtsCrossesMain> games, HashSet<String> viewingProfile,
			HashMap<String, Profile> profiles) {
		setIconImage(Toolkit.getDefaultToolkit().getImage("/resources/logo_128.png"));
		setTitle("Grimsby");
		this.queueOfCommands = queueOfCommands;
		this.model = model;
		this.lblPhoto = new JLabel("");
		this.model.addObserver(this);
		this.myLogin = myID;
		this.possibleCurrentConnections = possibleCurrentConnections;
		this.emojiManage = emojiManage;
		this.top = myTop;
		this.myBestFriends = checkBestFriend(this.top);
		this.viewingProfile = viewingProfile;
		this.games = games;
		this.profiles = profiles;
		initComponents();
	}

	/**
	 * Create the frame for main screen.
	 */
	private void initComponents() {
		this.caret = new DefaultCaret();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(722, 505);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		ModifyPicture modifyPicture = new ModifyPicture();

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);
		Design.backgroundColour(menuBar);
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		Design.menuText(mnFile);
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
				if (ret == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		mntmViewLogs = new JMenuItem("View Logs");
		mntmViewLogs.addActionListener(e -> queueOfCommands.offer(new MessageUS(Constants.LOAD_ALL_HISTORY)));
		mnFile.add(mntmViewLogs);
		mnFile.add(mntmExit);

		JMenu mnNewMenu = new JMenu("Edit");
		Design.menuText(mnNewMenu);
		menuBar.add(mnNewMenu);

		JMenu theme = new JMenu("Theme");
		Design.menuText(theme);
		menuBar.add(theme);

		changePass = new JMenuItem("Change Password");
		changePass.addActionListener(e -> {
			ChangePassword changePassword = new ChangePassword(queueOfCommands, this);
			changePass.setEnabled(false);
			changePassword.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					e.getWindow().dispose();
					changePass.setEnabled(true);
				}
			});
		});
		mnNewMenu.add(changePass);

		JMenu mnGroups = new JMenu("Groups");
		Design.menuText(mnGroups);
		menuBar.add(mnGroups);

		JMenu mnGames = new JMenu("Games");

		Design.menuText(mnGames);

		JMenu mnTicTacToe = new JMenu("Tic Tac Toe");
		mnGames.add(mnTicTacToe);
		JMenu computer = new JMenu("Computer");
		JMenuItem player1 = new JMenuItem("As player 1");
		JMenuItem player2 = new JMenuItem("As player 2");
		mnTicTacToe.add(computer);

		player1.addActionListener(e -> {
			startAIGame(true, computer);

		});
		player2.addActionListener(e-> startAIGame(false, computer));
		computer.add(player1);
		computer.add(player2);
		if (Design.theme == 1)
			mnTicTacToe.setBackground(Color.BLACK);

		otherPlayer = new JMenuItem("Other player");
		mnTicTacToe.add(otherPlayer);
		otherPlayer.addActionListener(e -> {
			otherPlayer.setEnabled(false);
			PotentialOponents potentialOponents = new PotentialOponents(queueOfCommands, model, games, myLogin, this);
			potentialOponents.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					e.getWindow().dispose();
					otherPlayer.setEnabled(true);
				}
			});
		});

		Design.menuText(mnGames);
		menuBar.add(mnGames);

		mntmStartGroupChat = new JMenuItem("Start new...");
		mntmStartGroupChat.addActionListener(e -> {
			CreateChat chat = new CreateChat(queueOfCommands, model, this);
			mntmStartGroupChat.setEnabled(false);
			chat.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					e.getWindow().dispose();
					mntmStartGroupChat.setEnabled(true);
				}
			});
		});

		mnGroups.add(mntmStartGroupChat);

		contentPane.setLayout(null);
		File photo = new File("temporary Data/" + this.myLogin + "/" + this.myLogin + "main profile photo.jpg");

		JMenuItem viewEditProfile = new JMenuItem("View/Edit profile");
		viewEditProfile.addActionListener(e -> {
			MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE, myLogin, "");
			queueOfCommands.offer(viewProfileRequest);
		});
		mnNewMenu.add(viewEditProfile);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		emojiLayer = new JLayeredPane();
		emojiLayer.setOpaque(true);
		emojiLayer.setBackground(Color.WHITE);
		emojiLayer.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				emojiLayer.setVisible(false);
				txtrMessage.requestFocus();
			}
		});
		emojiLayer.setOpaque(true);
		emojiLayer.setBounds(409, 232, 302, 202);
		Design.backgroundColour(emojiLayer);
		contentPane.add(emojiLayer);
		emojiLayer.setVisible(false);

		JLabel[] labels = new JLabel[32];
		emojiLayer.setLayout(new GridLayout(4, 8));
		EmojiManage test = new EmojiManage();

		int z = 0;
		for (String p : test.EmojiMap.keySet()) {
			labels[z] = new JLabel("");
			labels[z].setIcon(new ImageIcon(test.EmojiMap.get(p)));
			labels[z].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int caret = txtrMessage.getCaretPosition();
					String emoticon = getKeyByValue(test.EmojiMap, test.EmojiMap.get(p));
					txtrMessage.setText(txtrMessage.getText().substring(0, txtrMessage.getCaretPosition()) + " " + "/=?"
							+ emoticon + " " + txtrMessage.getText().substring(txtrMessage.getCaretPosition()));
					txtrMessage.setCaretPosition(caret + emoticon.length() + 5);
					emojiLayer.setVisible(false);
				}
			});
			emojiLayer.add(labels[z]);
			z++;
		}

		if (model.getTheme() == 0) {
			JMenuItem mntmDark = new JMenuItem("Dark");
			mntmDark.addActionListener(e -> {
				int ret = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to switch the theme? If yes, public messages will be erased",
						"An Inane Question", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					MessageUS settingTheme = new MessageUS(Constants.SET_THEME);
					settingTheme.setStyle(1);
					queueOfCommands.offer(settingTheme);
					model.setTheme(1);
					Design.setTheme(1);
					validate();
					initComponents();
					prepareList();
					repaint();
					setVisible(true);
				}
			});
			theme.add(mntmDark);
		} else if (model.getTheme() == 1) {
			JMenuItem mntmTheme = new JMenuItem("Default");
			mntmTheme.addActionListener(e -> {
				int ret = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to switch the theme? If yes, public messages will be erased",
						"An Inane Question", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					MessageUS settingTheme = new MessageUS(Constants.SET_THEME);
					settingTheme.setStyle(0);
					queueOfCommands.offer(settingTheme);
					model.setTheme(0);
					Design.setTheme(0);
					validate();
					initComponents();
					prepareList();
					repaint();
					setVisible(true);
				}
			});
			theme.add(mntmTheme);
		}

		JPanel contentLayer = new JPanel();
		Design.backgroundColour(contentLayer);
		contentLayer.setBounds(0, 0, 722, 450);
		contentPane.add(contentLayer);
		contentLayer.setLayout(null);

		JPanel panel = new JPanel();
		Design.backgroundChatColour(panel);
		panel.setBounds(0, 411, 722, 39);
		contentLayer.add(panel);
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);

		JLabel lblEmoji = new JLabel("");
		lblEmoji.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				emojiLayer.setVisible(true);
				emojiLayer.grabFocus();
				emojiLayer.requestFocus();
			}
		});
		lblEmoji.setIcon(new ImageIcon(MainScreen.class.getResource(model.getSmileyPath())));
		lblEmoji.setBounds(679, 8, 31, 28);
		panel.add(lblEmoji);

		JLabel lblAddPhoto = new JLabel("");
		lblAddPhoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		lblAddPhoto.setIcon(new ImageIcon(MainScreen.class.getResource(model.getPicPath())));
		lblAddPhoto.setBounds(633, 8, 31, 26);
		panel.add(lblAddPhoto);
		Design.backgroundColour(panel);

		lblAddPhoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser jf = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				jf.setFileFilter(filter);
				jf.setCurrentDirectory(new File("C:"));
				int option = jf.showOpenDialog(lblAddPhoto);
				if (jf.getSelectedFile() != null)
					try {
						File pictureSelected = new File(jf.getSelectedFile().getAbsolutePath());
						String resizedPirctureLocation = "temporary Data/" + myLogin + "/" + myLogin
								+ "public Conversation" + System.currentTimeMillis() + ".jpg";
						File pictureResized = new File(resizedPirctureLocation);

						try {

							BufferedImage SentPicture = ImageIO.read(pictureSelected);

							modifyPicture.resizeImage(pictureSelected, pictureResized, 100,
									100 * SentPicture.getHeight() / SentPicture.getWidth(), "jpg");

							byte[] convertedPhoto = Files.readAllBytes(pictureResized.toPath());

							MessageUS sendPhoto = new MessageUS(Constants.SEND_PUBLIC_PHOTO, convertedPhoto, myLogin,
									Constants.PUBLIC_MESSAGE);
							queueOfCommands.offer(sendPhoto);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					} catch (Exception ex) {

					}

			}
		});

		JLabel lblBF3Name = new JLabel("");
		String bf3Name = this.myBestFriends.get(2).getFriendsName();
		if (!bf3Name.equals("null"))
			lblBF3Name.setText(bf3Name);
		Design.textColour(lblBF3Name);
		lblBF3Name.setBounds(386, 70, 70, 15);
		contentLayer.add(lblBF3Name);

		JLabel lblBF2Name = new JLabel("");
		String bf2Name = this.myBestFriends.get(1).getFriendsName();
		if (!bf2Name.equals("null"))
			lblBF2Name.setText(bf2Name);
		lblBF2Name.setBounds(262, 70, 70, 15);
		Design.textColour(lblBF2Name);
		contentLayer.add(lblBF2Name);

		JLabel lblBF1Name = new JLabel("");
		String bf1Name = this.myBestFriends.get(0).getFriendsName();
		if (!bf1Name.equals("null"))
			lblBF1Name.setText(bf1Name);
		lblBF1Name.setBounds(144, 70, 70, 15);
		Design.textColour(lblBF1Name);
		contentLayer.add(lblBF1Name);

		JLabel lblBestFriend3 = new JLabel("");
		lblBestFriend3.setIcon(new ImageIcon(this.myBestFriends.get(2).getFriendsPhoto().getAbsolutePath()));
		lblBestFriend3.setBounds(380, 5, 66, 66);
		contentLayer.add(lblBestFriend3);
		lblBestFriend3.setBackground(Color.GRAY);

		JPopupMenu BF3Menu = new JPopupMenu();
		if (!bf3Name.equals("null"))
			addPopup(lblBestFriend3, BF3Menu);

		JMenuItem BF3ViewProfile = new JMenuItem("View Profile");
		BF3ViewProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE,
						myBestFriends.get(2).getFriendsName(), "");
				queueOfCommands.offer(viewProfileRequest);
			}
		});
		BF3Menu.add(BF3ViewProfile);

		JLabel lblBestFriend2 = new JLabel("");
		lblBestFriend2.setIcon(new ImageIcon(this.myBestFriends.get(1).getFriendsPhoto().getAbsolutePath()));
		lblBestFriend2.setBounds(260, 5, 66, 66);
		contentLayer.add(lblBestFriend2);
		lblBestFriend2.setBackground(Color.GRAY);

		JPopupMenu BF2Menu = new JPopupMenu();
		if (!bf2Name.equals("null"))
			addPopup(lblBestFriend2, BF2Menu);

		JMenuItem BF2ViewProfile = new JMenuItem("View Profile");
		BF2ViewProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE,
						myBestFriends.get(1).getFriendsName(), "");
				queueOfCommands.offer(viewProfileRequest);
			}
		});
		BF2Menu.add(BF2ViewProfile);

		JLabel lblBestFriend1 = new JLabel("");
		lblBestFriend1.setIcon(new ImageIcon(this.myBestFriends.get(0).getFriendsPhoto().getAbsolutePath()));
		lblBestFriend1.setBounds(140, 5, 66, 66);
		contentLayer.add(lblBestFriend1);
		lblBestFriend1.addMouseListener(new MouseAdapter() {

		});

		lblBestFriend1.setBackground(Color.GRAY);

		JPopupMenu BF1Menu = new JPopupMenu();
		if (!bf1Name.equals("null"))
			addPopup(lblBestFriend1, BF1Menu);

		JMenuItem BF1ViewProfile = new JMenuItem("View Profile");
		BF1ViewProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE,
						myBestFriends.get(0).getFriendsName(), "");
				queueOfCommands.offer(viewProfileRequest);
			}
		});
		BF1Menu.add(BF1ViewProfile);

		JLabel lblBestFriends = new JLabel("My best friends");
		lblBestFriends.setBounds(18, 31, 98, 37);
		contentLayer.add(lblBestFriends);
		Design.textColour(lblBestFriends);
		lblBestFriends.setFont(new Font("Lucida Grande", Font.PLAIN, 11));

		txtrMessage = new JTextField();
		txtrMessage.setBounds(0, 411, 613, 39);
		contentLayer.add(txtrMessage);
		Design.backgroundChatColour(txtrMessage);
		txtrMessage.setColumns(10);
		this.groupChat = new JTextPane();
		groupChat.setBorder(null);
		Design.setChatBackground(groupChat);

		caret = (DefaultCaret) groupChat.getCaret();
		groupChat.setEditable(false);
		JScrollPane scrollChat = new JScrollPane(groupChat);
		scrollChat.setBounds(0, 95, 614, 317);
		contentLayer.add(scrollChat);
		Design.scrollBorder(scrollChat);

		listOnline = new JList<>(model.getPeopleOnlineId());
		Design.backgroundColour(listOnline);

		scrollUsers = new JScrollPane(listOnline);
		scrollUsers.setBounds(613, 95, 109, 317);
		Design.scrollBorder(scrollUsers);
		contentLayer.add(scrollUsers);

		JLabel lblPhoto_1 = new JLabel("");
		lblPhoto_1.setBounds(630, 0, 80, 66);
		contentLayer.add(lblPhoto_1);
		lblPhoto_1.setBorder(null);

		lblPhoto_1.setIcon(new ImageIcon(photo.getAbsolutePath()));
		lblPhoto_1.setPreferredSize(new Dimension(64, 64));

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(lblPhoto_1, popupMenu);

		JMenuItem mntmViewProfile = new JMenuItem("View Profile");
		mntmViewProfile.addActionListener(e -> {
			MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE, myLogin, "");
			queueOfCommands.offer(viewProfileRequest);
		});
		popupMenu.add(mntmViewProfile);

		JSeparator separator = new JSeparator();
		popupMenu.add(separator);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popupMenu.add(mntmLogout);

		JLabel name = new JLabel(myLogin);
		name.setVerticalAlignment(SwingConstants.BOTTOM);
		Design.myTextColour(name);
		name.setBounds(621, 62, 89, 24);
		contentLayer.add(name);
		txtrMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtrMessage.getText().length() >= 255)
					txtrMessage.setText(txtrMessage.getText().substring(0, 255));
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txtrMessage.getText().length() >= 1)
						sendMessage();
					txtrMessage.requestFocusInWindow();
				}
			}
		});

		JMenuItem clearPublic = new JMenuItem("Clear public chat");
		clearPublic.addActionListener(e -> {
			groupChat.setText("");
			contentPane.revalidate();
			contentPane.repaint();
		});
		mnNewMenu.add(changePass);
		mnNewMenu.add(clearPublic);

		deleteAccount = new JMenuItem("Delete account");
		deleteAccount.addActionListener(e -> {
			Object[] options = { "Yes", "No" };
			int ret = JOptionPane.showOptionDialog(null, "Are you sure you want to delete account? ", "New Game",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (ret == JOptionPane.YES_OPTION) {
				queueOfCommands.offer(new MessageUS(Constants.DELETE_ACCOUNT));
				JOptionPane.showMessageDialog(null, "Your account is being deleted...");
				deleteAccount.setEnabled(false);
			}
		});
		mnNewMenu.add(deleteAccount);

	}

	/**
	 * Start AI game.
	 *
	 * @param isFirstPlayer indicates whether the user is first player
	 * @param computer the computer option in menu panel
	 */
	private void startAIGame(boolean isFirstPlayer, JMenu computer) {
		computer.setEnabled(false);
		NoughtsCrossesMain newGame = new NoughtsCrossesMain();
		NoughtsCrossesWithAI window = new NoughtsCrossesWithAI(queueOfCommands, games, "*AI*", myLogin, computer,
				isFirstPlayer);
		games.put("*AI*", newGame);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				games.remove("*AI*");
				computer.setEnabled(true);
				window.dispose();
			}
		});

	}

	/**
	 * Adds the popup.
	 *
	 * @param component the component
	 * @param popup the popup
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/**
	 * Gets the key by value.
	 *
	 * @param map the map
	 * @param value the value
	 * @return the key by value
	 */
	public static String getKeyByValue(Map<String, String> map, String value) {
		for (Entry<String, String> entry : map.entrySet())
			if (value.equals(entry.getValue()))
				return entry.getKey();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1.equals(Constants.DELETE_ACCOUNT_UNSUCCESSFUL))
			deleteAccount.setEnabled(true);

		if (arg1 instanceof MessageUS) {
			MessageUS m = (MessageUS) arg1;
			if (m.getInstruction().equals(Constants.PHOTO_UPLOADED)) {
				File photo = new File("temporary Data/" + this.myLogin + "/" + this.myLogin + "main profile photo.jpg");
				ImageIcon icon = new ImageIcon(photo.getAbsolutePath());
				icon.getImage().flush();
				lblPhoto.setIcon(icon);
				this.contentPane.revalidate();
				this.contentPane.repaint();

			} else if (m.getInstruction().equals(Constants.VIEW_PROFILE)) {
				if (!(viewingProfile.contains(m.getUserInformation()[0]))) {
					Profile profile = new Profile(queueOfCommands, model, m.getUserInformation(), myLogin,
							possibleCurrentConnections, m.getImage(), viewingProfile, profiles);
					profiles.put(m.getUserInformation()[0], profile);
					this.viewingProfile.add(m.getUserInformation()[0]);
					profile.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							viewingProfile.remove(profile.getUserName());

						}
					});
				}
			}

		}

		else if (arg1.equals(Constants.LIST_CHANGED)) {
			emojiLayer.setVisible(false);
			prepareList();

		}
		if (arg1 instanceof MessageUS) {
			MessageUS m = (MessageUS) arg1;
			if (m.getInstruction().equals(Constants.RECEIVE_PUBLIC_MESSAGE)) {
				emojiLayer.setVisible(false);
				String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(m.getTimeStamp());
				String login = m.getLogin();
				String sendMessage = m.getMessage();
				try {
					String[] textArray = sendMessage.split(" ");
					ArrayList<MessageObject> SplitedText = splitEmojiAndString(textArray);

					groupChat.getDocument().insertString(groupChat.getDocument().getLength(),
							timeStamp + " < " + login + " > :  ", null);
					SplitedText.forEach(e -> {
						if (e.isEmoji()) {
							BufferedImage img = null;
							try {
								img = ImageIO.read(new File(this.emojiManage.getEmojiPath(e.getMessage())));
								StyledDocument document = (StyledDocument) groupChat.getDocument();
								Style style = document.addStyle("StyleName", null);
								StyleConstants.setIcon(style, new ImageIcon(img));
								document.insertString(document.getLength(), " ", style);
							} catch (BadLocationException | IOException e1) {
								System.err.println("Could not insert the message");
							}

						} else {
							try {
								groupChat.getDocument().insertString(groupChat.getDocument().getLength(),
										e.getMessage() + " ", null);
							} catch (BadLocationException e1) {
								System.err.println("Could not insert the message");
							}
						}
					});
					groupChat.getDocument().insertString(groupChat.getDocument().getLength(), "\n", null);

				} catch (BadLocationException e) {
					System.err.println("Couln't insert the message");
				}
			} else if (m.getInstruction().equals(Constants.RECEIVE_PUBLIC_PHOTO)) {
				emojiLayer.setVisible(false);
				File recievedImage = new File("temporary Data/" + this.myLogin + "/Public Chat Photo" + m.getLogin()
						+ System.currentTimeMillis() + ".jpg");

				InputStream in = new ByteArrayInputStream(m.getImage());
				BufferedImage bImageFromConvert;
				try {
					bImageFromConvert = ImageIO.read(in);
					ImageIO.write(bImageFromConvert, "jpg", recievedImage);

					try {
						String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(m.getTimeStamp());

						groupChat.getDocument().insertString(groupChat.getDocument().getLength(),
								String.format("%3s %-10s:  %-10s%n", timeStamp, m.getLogin(), m.getMessage()), null);

						BufferedImage img = ImageIO.read(recievedImage);
						StyledDocument document = (StyledDocument) groupChat.getDocument();
						Style style = document.addStyle("StyleName", null);
						StyleConstants.setIcon(style, new ImageIcon(img));
						document.insertString(document.getLength(), " ", style);

						groupChat.getDocument().insertString(groupChat.getDocument().getLength(), "\n", null);

					} catch (BadLocationException e) {
						System.err.println("Couln't insert the message");
					}

				} catch (IOException e) {
					System.err.println("Problems inserting photo");
				}
			}
		}
	}

	/**
	 * Prepare list of the online people.
	 */
	private void prepareList() {
		profiles.keySet().forEach(e -> profiles.get(e).disableMessage());
		String[] people = model.getPeopleOnlineId();
		for (String name : people)
			if (profiles.containsKey(name))
				profiles.get(name).enableMessage();

		listOnline = new JList<>(people);
		Design.backgroundColour(listOnline);
		listOnline.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				String val = (String) ((JList) arg0.getSource()).getSelectedValue();
				if (val != null) {
					JPopupMenu popupMenu = new JPopupMenu();
					addPopup(listOnline, popupMenu);

					JMenuItem mntmViewProfile = new JMenuItem("View Profile");
					mntmViewProfile.addActionListener(
							e -> queueOfCommands.offer(new MessageUS(Constants.VIEW_PROFILE, val, "")));
					if (!viewingProfile.contains(val)) {
						popupMenu.add(mntmViewProfile);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		});
		scrollUsers.setVisible(true);
		scrollUsers.setViewportView(listOnline);
		this.setVisible(true);

	}

	/**
	 * Sends the public message.
	 */
	private void sendMessage() {
		queueOfCommands.offer(
				new MessageUS(Constants.PUBLIC_MESSAGE, txtrMessage.getText(), myLogin, Constants.PUBLIC_MESSAGE));
		txtrMessage.setText("");
	}

	/**
	 * Split emoji and string. Indicates emojis in the message
	 *
	 * @param StringArray the string array of the message text
	 * @return the array list of MessageObject which contains all the words
	 * received with message, and indicators saying whether the string is the
	 * emoji or not
	 */
	public ArrayList<MessageObject> splitEmojiAndString(String[] StringArray) {
		ArrayList<MessageObject> splitedString = new ArrayList<>();

		for (int i = 0; i < StringArray.length; i++) {
			if (StringArray[i].length() <= 3)
				splitedString.add(new MessageObject(StringArray[i], false));
			else {
				if (StringArray[i].substring(0, 3).equals("/=?")) {
					if (this.emojiManage.isEmoji(StringArray[i].substring(3, StringArray[i].length())))
						splitedString
								.add(new MessageObject(StringArray[i].substring(3, StringArray[i].length()), true));
					else
						splitedString
								.add(new MessageObject(StringArray[i].substring(3, StringArray[i].length()), false));
				} else
					splitedString.add(new MessageObject(StringArray[i], false));
			}
		}
		return splitedString;
	}

	/**
	 * Check best friend.
	 *
	 * @param topFriends the top friends
	 * @return the array list
	 */
	private ArrayList<Friends> checkBestFriend(Set<User> topFriends) {
		ArrayList<Friends> bestFriends = new ArrayList<>();
		int numberOfTop = topFriends.size();

		topFriends.forEach(e -> bestFriends.add(new Friends(e.getName(),
				new File("temporary Data/" + this.myLogin + "/Top contantcts/" + e.getName() + " Main photo.jpg"))));

		if (numberOfTop == 0) {
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
		} else if (numberOfTop == 1) {
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
		} else if (numberOfTop == 2) {
			bestFriends.add(new Friends("null", new File("placeholder.jpg")));
		}

		return bestFriends;
	}

	/**
	 * Sets the view logs menu item to non enabled.
	 */
	public void setViewLogsFalse() {
		mntmViewLogs.setEnabled(false);
	}

	/**
	 * Sets the view logs menu item to enabled.
	 */
	public void setViewLogsTrue() {
		mntmViewLogs.setEnabled(true);
	}

	/**
	 * Sets the other player item to enabled.
	 */
	public void setOtherPlayerTrue() {
		otherPlayer.setEnabled(true);
	}

	/**
	 * Sets the change pass true.
	 */
	public void setChangePassTrue() {
		changePass.setEnabled(true);
	}

	/**
	 * Sets the mntm start group chat true.
	 */
	public void setMntmStartGroupChatTrue() {
		mntmStartGroupChat.setEnabled(true);
	}
}