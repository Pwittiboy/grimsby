package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;
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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import grimsby.networking.client.AES;
import grimsby.networking.util.Constants;
import grimsby.networking.util.Conversation;
import grimsby.networking.util.MessageObject;
import grimsby.networking.util.MessageUS;

/**
 * The Class GroupChat.
 */
public class GroupChat extends JFrame implements Observer {

	/** The content pane. */
	private JPanel contentPane;

	/** The txtr message. */
	private JTextField txtrMessage;

	/** The queue of commands. */
	private LinkedBlockingQueue<MessageUS> queueOfCommands;

	/** The model. */
	private Model model;

	/** The group chat. */
	private JTextPane groupChat;

	/** The caret. */
	private DefaultCaret caret;

	/** The scroll users. */
	private JScrollPane scrollUsers;

	/** The panel. */
	private final JPanel panel = new JPanel();

	/** The group name. */
	private String groupName;

	/** The chat id. */
	private String chatId;

	/** The my login. */
	private String myLogin;

	/** The my conversation. */
	private Conversation myConversation;

	/** The people in group. */
	private JList<String> peopleInGroup;

	/** The emoji manage. */
	private EmojiManage emojiManage;

	/** The modifypicture. */
	private ModifyPicture modifypicture;

	/** Add users from menu */
	private JMenuItem mntmAddUser;
	
	/** Emoji panel */
	private JLayeredPane emojiLayer;

	/**
	 * Creates the group chat frame.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param myLogin is my login
	 * @param groupName is the group name
	 * @param chatId is the chat id
	 * @param myConversation is an observable conversation which oberver is this
	 * class
	 * @param emojiManage the emoji manage
	 */
	public GroupChat(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, String myLogin, String groupName,
			String chatId, Conversation myConversation, EmojiManage emojiManage) {
		super("Group chat: " + groupName);
		setIconImage(Toolkit.getDefaultToolkit().getImage("/resources/logo_128.png"));
		this.queueOfCommands = queueOfCommands;
		this.model = model;
		this.model.addObserver(this);
		this.groupName = groupName;
		this.chatId = chatId;
		this.myLogin = myLogin;
		this.myConversation = myConversation;
		this.emojiManage = emojiManage;
		this.caret = new DefaultCaret();
		this.modifypicture = new ModifyPicture();
		initComponents();
	}

	/**
	 * Initialises the components of the frame
	 */
	private void initComponents() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		Design.backgroundColour(contentPane);
		setContentPane(contentPane);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);
		Design.backgroundColour(menuBar);
		setJMenuBar(menuBar);

		JMenu mnInvite = new JMenu("Invite");
		Design.menuText(mnInvite);
		menuBar.add(mnInvite);

		mntmAddUser = new JMenuItem("Add User...");
		Design.menuText(mntmAddUser);
		mntmAddUser.addActionListener(e -> {
			mntmAddUser.setEnabled(false);
			OnlineUsers onlineUsers = new OnlineUsers(queueOfCommands, model, groupName, chatId, myConversation, this);
			onlineUsers.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					mntmAddUser.setEnabled(true);
					e.getWindow().dispose();
				}
			});
		});

		mnInvite.add(mntmAddUser);

		contentPane.setLayout(null);

		emojiLayer = new JLayeredPane();
		emojiLayer.setOpaque(true);
		emojiLayer.setBounds(319, 202, 302, 202);
		emojiLayer.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				emojiLayer.setVisible(false);
				txtrMessage.requestFocus();
			}
		});
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
					txtrMessage.setText(txtrMessage.getText().substring(0, txtrMessage.getCaretPosition()) + "/=?"
							+ emoticon + txtrMessage.getText().substring(txtrMessage.getCaretPosition()));
					txtrMessage.setCaretPosition(caret + emoticon.length() + 3);
					emojiLayer.setVisible(false);
				}
			});
			emojiLayer.add(labels[z]);
			z++;
		}

		JPanel chatLayer = new JPanel();
		chatLayer.setBounds(0, 0, 640, 457);
		Design.backgroundColour(chatLayer);
		contentPane.add(chatLayer);
		chatLayer.setLayout(null);
		ArrayList<String> l = new ArrayList<>(myConversation.getPeopleInConversation());
		String[] list = l.toArray(new String[l.size()]);
		peopleInGroup = new JList<>(list);
		Design.backgroundColour(peopleInGroup);
		makeListListener();

		scrollUsers = new JScrollPane(peopleInGroup);
		scrollUsers.setBounds(520, 0, 120, 400);
		chatLayer.add(scrollUsers);
		scrollUsers.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Design.backgroundColour(scrollUsers);

		scrollUsers.add(peopleInGroup);
		scrollUsers.setViewportView(peopleInGroup);
		this.groupChat = new JTextPane();

		caret = (DefaultCaret) groupChat.getCaret();
		groupChat.setEditable(false);
		Design.backgroundColour(groupChat);
		JScrollPane scrollChat = new JScrollPane(groupChat);
		scrollChat.setBounds(0, 0, 521, 400);
		chatLayer.add(scrollChat);
		scrollChat.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Design.backgroundColour(scrollChat);
		txtrMessage = new JTextField();
		txtrMessage.setBounds(0, 400, 520, 39);
		chatLayer.add(txtrMessage);
		txtrMessage.setBorder(null);
		Design.backgroundChatColour(txtrMessage);
		panel.setBounds(0, 399, 640, 40);
		chatLayer.add(panel);
		// txtrMessage.setColumns(10);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Design.backgroundColour(panel);
		panel.setLayout(null);

		JLabel lblAddPhoto = new JLabel("");
		lblAddPhoto.setIcon(new ImageIcon(GroupChat.class.getResource(model.getPicPath())));
		lblAddPhoto.setBounds(561, 8, 35, 27);
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

						File pictureResized = new File("temporary Data/" + myLogin + "/Group Chat/" + groupName + "/"
								+ "Group Communication Picture/" + System.currentTimeMillis() + ".jpg");

						try {
							// Used to calculate the factor between width and
							// height
							BufferedImage SentPicture = ImageIO.read(pictureSelected);

							modifypicture.resizeImage(pictureSelected, pictureResized, 100,
									100 * SentPicture.getHeight() / SentPicture.getWidth(), "jpg");

							byte[] convertedPhoto = Files.readAllBytes(pictureResized.toPath());

							MessageUS sendPhoto = new MessageUS(Constants.SEND_PHOTO, convertedPhoto, myLogin,
									myConversation.getConversationId());
							queueOfCommands.offer(sendPhoto);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					} catch (Exception ex) {

					}
			}
		});
		panel.add(lblAddPhoto);
		Design.backgroundColour(panel);

		JLabel lblEmoji = new JLabel("");
		lblEmoji.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				emojiLayer.setVisible(true);
				emojiLayer.grabFocus();
				emojiLayer.requestFocus();
			}
		});
		lblEmoji.setIcon(new ImageIcon(GroupChat.class.getResource(model.getSmileyPath())));
		lblEmoji.setBounds(593, 8, 35, 27);
		panel.add(lblEmoji);

		Design.backgroundColour(panel);
		txtrMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtrMessage.getText().length() >= 255)
					txtrMessage.setText(txtrMessage.getText().substring(0, 255));
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txtrMessage.getText().length() >= 1) {
						queueOfCommands.offer(new MessageUS(Constants.SEND_MESSAGE,
								model.encrypt(txtrMessage.getText()), myLogin, chatId));
						txtrMessage.setText("");
					}
					txtrMessage.requestFocusInWindow();
				}
			}
		});
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		Design.backgroundColour(contentPane);
		Design.backgroundColour(this);
		setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg) {
		if (arg.equals(Constants.CHAT_ENDED))
			this.dispose();
		if (arg instanceof MessageUS) {
			MessageUS message = (MessageUS) arg;
			String login = message.getLogin();
			if (message.getInstruction().equals(Constants.RECEIVE_MESSAGE)) {
				emojiLayer.setVisible(false);
				File userWhoSendMessage = new File("temporary Data/" + this.myLogin + "/Group Chat/" + this.groupName
						+ "/Group Profile Picture/" + login + " Chatting profile photo.jpg");
				AES aes = new AES(login + message.getAccountCreationTime());
				String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(message.getTimeStamp());
				String decryptedMessage = aes.decrypt(message.getMessage());
				BufferedImage img = null;
				try {
					String[] textArray = decryptedMessage.split(" ");
					ArrayList<MessageObject> SplitedText = splitEmojiAndString(textArray);
					img = ImageIO.read(new File(userWhoSendMessage.getAbsolutePath()));
					StyledDocument document = (StyledDocument) groupChat.getDocument();
					Style style = document.addStyle("StyleName", null);
					StyleConstants.setIcon(style, new ImageIcon(img));
					document.insertString(document.getLength(), " ", style);
					groupChat.getDocument().insertString(groupChat.getDocument().getLength(),
							timeStamp + " < " + message.getLogin() + " > :  ", null);
					SplitedText.forEach(e -> {
						if (e.isEmoji()) {
							BufferedImage img2 = null;
							try {
								img2 = ImageIO.read(new File(this.emojiManage.getEmojiPath(e.getMessage())));
								StyleConstants.setIcon(style, new ImageIcon(img2));
								document.insertString(document.getLength(), " ", style);
							} catch (BadLocationException | IOException e1) {
								System.err.println("Could not insert the message");
							}
						} else {
							BufferedImage profileImg = null;
							try {
								groupChat.getDocument().insertString(groupChat.getDocument().getLength(),
										e.getMessage() + " ", null);
							} catch (BadLocationException e1) {
								System.err.println("Could not insert the message");
							}
						}
					});
					groupChat.getDocument().insertString(groupChat.getDocument().getLength(), "\n", null);

				} catch (BadLocationException | IOException e) {
					System.err.println("could not insert the new message");
				}

			} else if (message.getInstruction().equals(Constants.RECEIVE_PHOTO)) {
				emojiLayer.setVisible(false);
				File recievedImage = new File("temporary Data/" + this.myLogin + "/Group Chat/" + groupName
						+ "/Group Communication Picture/" + login + System.currentTimeMillis() + ".jpg");
				File userWhoSendMessage = new File("temporary Data/" + this.myLogin + "/Group Chat/" + this.groupName
						+ "/Group Profile Picture/" + login + " Chatting profile photo.jpg");
				InputStream in = new ByteArrayInputStream(message.getImage());
				BufferedImage bImageFromConvert;
				try {
					bImageFromConvert = ImageIO.read(in);
					ImageIO.write(bImageFromConvert, "jpg", recievedImage);
					try {
						String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(message.getTimeStamp());
						groupChat.insertIcon(new ImageIcon(userWhoSendMessage.getAbsolutePath()));
						groupChat.getDocument().insertString(groupChat.getDocument().getLength(), String.format(
								"%3s %-10s:  %-10s%n", timeStamp, message.getLogin(), message.getMessage()), null);
						groupChat.insertIcon(new ImageIcon(recievedImage.getAbsolutePath()));
						groupChat.getDocument().insertString(groupChat.getDocument().getLength(), "\n", null);
					} catch (BadLocationException e) {
						System.err.println("Couln't insert the message");
					}

				} catch (IOException e) {
					System.err.println("Problems inserting photo");
				}
			}
			if (message.getInstruction().equals(Constants.ACCEPTED_ADD)) {
				emojiLayer.setVisible(true);
				ArrayList<String> l = new ArrayList<>(myConversation.getPeopleInConversation());
				String[] list = l.toArray(new String[l.size()]);
				peopleInGroup = new JList<>(list);

				makeListListener();
				scrollUsers.add(peopleInGroup);
				scrollUsers.setViewportView(peopleInGroup);
				Design.backgroundColour(peopleInGroup);
				Design.backgroundColour(scrollUsers);

				byte[] photoOfAdded = message.getImage();
				File addedPeopleprofilePhoto = new File(
						"temporary Data/" + this.myLogin + "/Group Chat/" + this.groupName + "/Group Profile Picture/"
								+ message.getLogin() + " Chatting profile photo.jpg");

				try {
					byte[] defaultPhoto = this.modifypicture.ImageToByte(new File("placeholder.jpg"));
					if (photoOfAdded == null)
						photoOfAdded = defaultPhoto;

				} catch (IOException e) {
					System.err.println("placeholder was not added");
				}

				this.modifypicture.byteToImage(photoOfAdded, addedPeopleprofilePhoto);
				this.modifypicture.resizeImage(addedPeopleprofilePhoto, addedPeopleprofilePhoto, 40, 40, "jpg");

			}
		}

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
				if (e.isPopupTrigger())
					showMenu(e);

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
	 * Make list listener.
	 */
	private void makeListListener() {
		peopleInGroup.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String val = (String) ((JList) e.getSource()).getSelectedValue();
				if (val != null) {
					JPopupMenu popupMenu = new JPopupMenu();

					addPopup(peopleInGroup, popupMenu);

					JMenuItem mntmViewProfile = new JMenuItem("View Profile");

					mntmViewProfile.addActionListener(
							ac -> queueOfCommands.offer(new MessageUS(Constants.VIEW_PROFILE, val, "")));
					popupMenu.add(mntmViewProfile);
				}
			}
		});
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

	public void setmntmAddUserTrue() {
		mntmAddUser.setEnabled(true);
	}
}