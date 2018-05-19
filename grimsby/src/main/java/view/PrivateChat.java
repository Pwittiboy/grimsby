package view;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
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

import grimsby.networking.client.AES;
import grimsby.networking.util.Constants;
import grimsby.networking.util.Conversation;
import grimsby.networking.util.MessageObject;
import grimsby.networking.util.MessageUS;

/**
 * The Class PrivateChat.
 */
public class PrivateChat extends JFrame implements Observer {

	/** The content pane. */
	private JPanel contentPane;

	/** The model. */
	private Model model;

	/** The chatbox. */
	private JTextPane chatbox = new JTextPane();

	/** The my login. */
	private String myLogin;

	/** The partner login. */
	private String partnerLogin;

	/** The caret. */
	private DefaultCaret caret;

	/** The emoji manage. */
	private EmojiManage emojiManage;
	
		/** The viewing profile. */
	private HashSet<String> viewingProfile;

	/** Emoji panel */
	private JLayeredPane emojiLayer;

	/**
	 * Instantiates a new private chat window.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param myConversation the my conversation
	 * @param myLogin the my login
	 * @param parnerLogin the partner login
	 * @param emojiManage the emoji manage
	 */
	public PrivateChat(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, Conversation myConversation,
			String myLogin, String partnerLogin, EmojiManage emojiManage, HashSet<String> viewingProfile) {
		super("My chat with: " + partnerLogin);
		this.model = model;
		this.model.addObserver(this);
		this.myLogin = myLogin;
		this.partnerLogin = partnerLogin;
		this.caret = new DefaultCaret();
		this.emojiManage = emojiManage;
		this.viewingProfile = viewingProfile;


		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 494);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JMenuBar menuBar = new JMenuBar();
		Design.backgroundColour(menuBar);
		setJMenuBar(menuBar);

		chatbox.setBorder(null);
		chatbox.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		Design.backgroundColour(chatbox);

		chatbox.setEditable(false);
		// chatbox.setLineWrap(true);
		caret = (DefaultCaret) chatbox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JTextField txtrMessage = new JTextField();

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
		emojiLayer.setLayout(new GridLayout(4, 8));

		JScrollPane scrollChat = new JScrollPane(chatbox);
		Design.backgroundColour(scrollChat);
		scrollChat.setBounds(0, 86, 641, 313);
		contentPane.add(scrollChat);

		requestFocus();
		GridBagConstraints gbc_txtrMessage = new GridBagConstraints();
		gbc_txtrMessage.fill = GridBagConstraints.BOTH;
		gbc_txtrMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtrMessage.gridx = 0;
		gbc_txtrMessage.gridy = 1;

		JLabel lblOtherUser = new JLabel();
		lblOtherUser.setBounds(551, 12, 66, 66);
		lblOtherUser.setVerticalAlignment(SwingConstants.TOP);
		lblOtherUser.setBorder(null);

		File partnerMainPhoto = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin + "/"
				+ partnerLogin + " Main photo.jpg");
		new ImageIcon(partnerMainPhoto.getAbsolutePath()).getImage().flush();
		lblOtherUser.setIcon(new ImageIcon(partnerMainPhoto.getAbsolutePath()));

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(lblOtherUser, popupMenu);

		JMenuItem mntmViewProfile = new JMenuItem("View Profile");
				mntmViewProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageUS viewProfileRequest = new MessageUS(Constants.VIEW_PROFILE,
						partnerLogin, "");
				queueOfCommands.offer(viewProfileRequest);
			}
		});
		popupMenu.add(mntmViewProfile);
		contentPane.add(lblOtherUser);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		Design.backgroundColour(panel);
		panel.setBounds(0, 397, 640, 39);
		panel.setLayout(null);

		txtrMessage.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (txtrMessage.getText().length() >= 255)
					e.consume();
			}
		});

		txtrMessage.setBorder(null);
		Design.backgroundChatColour(txtrMessage);
		txtrMessage.setBounds(0, 2, 523, 38);
		panel.add(txtrMessage);
		txtrMessage.setToolTipText("Enter text…");
		txtrMessage.setColumns(10);
		txtrMessage.requestFocus();

		JLabel[] labels = new JLabel[32];
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
					txtrMessage.requestFocus();
				}
			});
			emojiLayer.add(labels[z]);
			z++;
		}

		JPanel chatLayer = new JPanel();
		Design.backgroundColour(chatLayer);
		chatLayer.setBounds(0, 0, 641, 437);
		contentPane.add(chatLayer);
		chatLayer.setLayout(null);
		chatLayer.add(panel);

		chatbox.setBorder(null);
		chatbox.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		Design.backgroundColour(chatbox);

		chatbox.setEditable(false);
		caret = (DefaultCaret) chatbox.getCaret();

		JLabel lblAddPhoto = new JLabel("");
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
						String resizedPirctureLocation = "temporary Data/" + myLogin + "/" + myLogin + myConversation
								+ System.currentTimeMillis() + ".jpg";
						File pictureResized = new File(resizedPirctureLocation);

						try {

							BufferedImage SentPicture = ImageIO.read(pictureSelected);

							resizeSentImage(pictureSelected, pictureResized, 100,
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
		lblAddPhoto.setIcon(new ImageIcon(PrivateChat.class.getResource(model.getPicPath())));
		lblAddPhoto.setBounds(567, 7, 27, 28);
		panel.add(lblAddPhoto);

		JLabel lblEmoji = new JLabel("");
		lblEmoji.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				emojiLayer.setVisible(true);
				emojiLayer.grabFocus();
				emojiLayer.requestFocus();
			}
		});
		lblEmoji.setIcon(new ImageIcon(PrivateChat.class.getResource(model.getSmileyPath())));
		lblEmoji.setBounds(601, 7, 27, 28);
		panel.add(lblEmoji);

		txtrMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txtrMessage.getText().length() >= 255)
						txtrMessage.setText(txtrMessage.getText().substring(0, 255));
					if (txtrMessage.getText().length() >= 1) {
						queueOfCommands.offer(new MessageUS(Constants.SEND_MESSAGE,
								model.encrypt(txtrMessage.getText()), myLogin, myConversation.getConversationId()));
						txtrMessage.setText("");
					}
					txtrMessage.requestFocusInWindow();
				}
			}
		});
		txtrMessage.requestFocus();

		requestFocus();
	}

	/**
	 * Resize sent image.
	 *
	 * @param originalImage the original image
	 * @param resizedImage the resized image
	 * @param width the width
	 * @param height the height
	 * @param format the format
	 */
	public static void resizeSentImage(File originalImage, File resizedImage, int width, int height, String format) {

		BufferedImage original;
		try {
			original = ImageIO.read(originalImage);
			BufferedImage resized = new BufferedImage(width, height, original.getType());
			Graphics2D g2 = resized.createGraphics();
			g2.drawImage(original, 0, 0, width, height, null);
			g2.dispose();
			ImageIO.write(resized, format, resizedImage);

		} catch (IOException e) {
			System.err.println("Something went wrong with photo");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MessageUS) {
			MessageUS m = (MessageUS) arg;
			if (m.getInstruction().equals(Constants.RECEIVE_MESSAGE)) {
				emojiLayer.setVisible(false);
				String login = m.getLogin();

				File userWhoSendMessage = new File("temporary Data/" + this.myLogin + "/Private Chat/with "
						+ partnerLogin + "/" + login + " Chatting photo.jpg");
				AES aes = new AES(login + m.getAccountCreationTime());
				String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(m.getTimeStamp());
				String decryptedMessage = aes.decrypt(m.getMessage());
				BufferedImage img = null;
				try {
					String[] textArray = decryptedMessage.split(" ");
					ArrayList<MessageObject> SplitedText = splitEmojiAndString(textArray);

					img = ImageIO.read(new File(userWhoSendMessage.getAbsolutePath()));
					StyledDocument document = (StyledDocument) chatbox.getDocument();
					Style style = document.addStyle("StyleName", null);
					StyleConstants.setIcon(style, new ImageIcon(img));
					document.insertString(document.getLength(), " ", style);
					chatbox.getDocument().insertString(chatbox.getDocument().getLength(),
							timeStamp + " < " + m.getLogin() + " > :  ", null);
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
								chatbox.getDocument().insertString(chatbox.getDocument().getLength(),
										e.getMessage() + " ", null);
							} catch (BadLocationException e1) {
								System.err.println("Could not insert the message");
							}
						}
					});
					chatbox.getDocument().insertString(chatbox.getDocument().getLength(), "\n", null);

				} catch (BadLocationException | IOException e) {
					System.err.println("could not insert the new message");
				}

			} else if (m.getInstruction().equals(Constants.RECEIVE_PHOTO)) {
				emojiLayer.setVisible(false);
				File recievedImage = new File("temporary Data/" + this.myLogin + "/Private Chat/with " + partnerLogin
						+ "/" + m.getLogin() + System.currentTimeMillis() + ".jpg");
				File userWhoSendMessage = new File("temporary Data/" + this.myLogin + "/Private Chat/with "
						+ partnerLogin + "/" + m.getLogin() + " Chatting photo.jpg");
				InputStream in = new ByteArrayInputStream(m.getImage());
				BufferedImage bImageFromConvert;
				try {
					bImageFromConvert = ImageIO.read(in);
					ImageIO.write(bImageFromConvert, "jpg", recievedImage);
					try {
						String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(m.getTimeStamp());
						chatbox.insertIcon(new ImageIcon(userWhoSendMessage.getAbsolutePath()));
						chatbox.getDocument().insertString(chatbox.getDocument().getLength(),
								String.format("%3s %-10s:  %-10s%n", timeStamp, m.getLogin(), m.getMessage()), null);
						chatbox.insertIcon(new ImageIcon(recievedImage.getAbsolutePath()));
						chatbox.getDocument().insertString(chatbox.getDocument().getLength(), "\n", null);

					} catch (BadLocationException e) {
						System.err.println("Couln't insert the message");
					}

				} catch (IOException e) {
					System.err.println("Problems inserting photo");
				}
			} else if (m.getInstruction().equals(Constants.DECLINED_SEND_MESSAGE)) {
				AES aes = new AES(m.getLogin() + m.getAccountCreationTime());
				try {
					chatbox.getDocument().insertString(chatbox.getDocument().getLength(),
							String.format("Your message:  %-10s%n", aes.decrypt(m.getMessage()), " was not sent\n"),
							null);
				} catch (BadLocationException e) {
				}
			}
		} else if (arg.equals(Constants.CHAT_ENDED)) {
			this.dispose();
			model.deleteObserver(this);
		}

	}

	/**
	 * Adds the popup to the window.
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
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
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
}
