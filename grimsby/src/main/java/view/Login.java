package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Color;

import grimsby.networking.util.*;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The Class Login. Creates the login window with an options to remind password
 * and create the new account
 */
public class Login extends JFrame implements Observer {

	/** The queue of commands. */
	private LinkedBlockingQueue<MessageUS> queueOfCommands;

	/** The queue of answers. */
	private LinkedBlockingQueue<MessageUS> queueOfAnswers;

	/** The model. */
	private Model model;

	/** The my user name. */
	private String myUserName;

	/** The text username. */
	private JTextField txtUsername;

	/** The text password. */
	private JTextField txtPassword;

	/** The login panel. */
	private JPanel loginPanel;

	/** The main panel. */
	private JPanel mainPanel;

	/** The default photo. */
	private byte[] defaultPhoto;

	/** The modify picture. */
	private ModifyPicture modifyPicture = new ModifyPicture();

	/** The my top. */
	private Set<User> myTop;

	/** The flag for window close. */
	private boolean fpWindowClose;

	/** The flag window close. */
	private boolean rWindowClose;

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (args.length != 0) {
						try {
							Constants.HOST_IP = args[0];
							Constants.SOCKET_NUMBER = Integer.parseInt(args[1]);
						} catch (Exception e) {
							System.err.println("Host is set to be local host");
							Constants.HOST_IP = "127.0.0.1";
							Constants.SOCKET_NUMBER = 60222;
						}
					}
					new Login();
				} catch (Exception e) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Check input.
	 *
	 * @param input the input
	 * @return true, if successful
	 * @MODIFIED search for illegal input, return false if there is no illegal
	 * strings contains
	 */
	public boolean checkInput(String input) {

		return (input.contains(";") || input.contains("/") || input.contains("\"") || input.contains(" ")
				|| input.contains("*") || input.contains("'") || input.contains("'") || input.contains("[")
				|| input.contains("]") || input.contains("|") || input.contains("=") || input.contains(",")
				|| input.contains("<") || input.contains(">") || input.contains("~") || input.contains("_"));

	}

	/**
	 * Check qand A.
	 *
	 * @param input the input
	 * @return true, if successful
	 */
	public boolean checkQandA(String input) {
		return input.contains(";") || input.contains("/") || input.contains("\"") || input.contains("*")
				|| input.contains("'") || input.contains("'") || input.contains("[") || input.contains("]")
				|| input.contains("|") || input.contains("=") || input.contains(",") || input.contains("<")
				|| input.contains(">") || input.contains("~");
	}

	/**
	 * Setfp close window.
	 */
	public void setfpCloseWindow() {
		this.fpWindowClose = true;
	}

	/**
	 * Sets the R close window.
	 */
	public void setRCloseWindow() {
		this.rWindowClose = true;
	}

	/**
	 * Create the new Login window.
	 */
	public Login() {
		this.queueOfCommands = new LinkedBlockingQueue<>();
		this.queueOfAnswers = new LinkedBlockingQueue<>();
		this.model = new Model(this.queueOfCommands, this.queueOfAnswers);
		this.myTop = new HashSet<>();
		this.fpWindowClose = true;
		this.rWindowClose = true;
		model.addObserver(this);

		Design.backgroundColour(this);
		setBackground(new Color(255, 255, 255));
		setResizable(false);
		setSize(176, 377);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		mainPanel = new JPanel();
		Design.backgroundColour(mainPanel);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBounds(0, 0, 176, 357);
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);

		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(Login.class.getResource("/resources/grimsby.png")));
		logo.setBounds(33, 12, 104, 95);
		mainPanel.add(logo);
		loginScreen();
	}

	/**
	 * Adds components to the login panel.
	 */
	public void loginScreen() {
		Login l = this;
		loginPanel = new JPanel();
		loginPanel.setLocation(0, 129);
		loginPanel.setSize(176, 203);
		loginPanel.setBorder(new LineBorder(Color.WHITE));
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setLayout(null);
		mainPanel.add(loginPanel, null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblUsername.setToolTipText("");
		Design.textColour(lblUsername);
		lblUsername.setBounds(31, 14, 124, 27);
		loginPanel.add(lblUsername);

		txtUsername = new JTextField();
		Design.designField(txtUsername);
		txtUsername.setBounds(31, 40, 114, 19);
		txtUsername.setColumns(10);
		loginPanel.add(txtUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPassword.setToolTipText("");
		Design.textColour(lblPassword);
		lblPassword.setBounds(31, 69, 124, 27);
		loginPanel.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String password = txtPassword.getText();
					String username = txtUsername.getText();
					lblUsername.setForeground(Design.BLUE_TEXT);
					lblUsername.setText("Username");
					lblPassword.setForeground(Design.BLUE_TEXT);
					lblPassword.setText("Password");
					if (username.length() < 6 || username.length() > 12) {
						lblUsername.setForeground(Color.RED);
						lblUsername.setText("Username invalid");
					} else if (checkInput(username)) {
						lblUsername.setForeground(Color.RED);
						lblUsername.setText("Username invalid");
					} else if (password.length() < 6 || password.length() > 20) {
						lblPassword.setForeground(Color.RED);
						lblPassword.setText("Password invalid");
					} else if (checkInput(password)) {
						lblPassword.setForeground(Color.RED);
						lblPassword.setText("Password invalid");
					} else {
						attemptLogin(username, password);
					}
				}
			}
		});
		Design.designField(txtPassword);
		txtPassword.setBounds(31, 95, 114, 19);
		txtPassword.setColumns(10);
		loginPanel.add(txtPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(e -> {
			String password = txtPassword.getText();
			String username = txtUsername.getText();
			lblUsername.setForeground(Design.BLUE_TEXT);
			lblUsername.setText("Username");
			lblPassword.setForeground(Design.BLUE_TEXT);
			lblPassword.setText("Password");
			if (username.length() < 2 || username.length() > 12) {
				lblUsername.setForeground(Color.RED);
				lblUsername.setText("Username invalid");
			} else if (checkInput(username)) {
				lblUsername.setForeground(Color.RED);
				lblUsername.setText("Username invalid");
			} else if (password.length() < 6 || password.length() > 20) {
				lblPassword.setForeground(Color.RED);
				lblPassword.setText("Password invalid");
			} else if (checkInput(password)) {
				lblPassword.setForeground(Color.RED);
				lblPassword.setText("Password invalid");
			} else {
				attemptLogin(username, password);
			}
		});
		btnLogin.setBounds(98, 166, 57, 25);
		Design.primaryButton(btnLogin);
		loginPanel.add(btnLogin);

		JLabel lblForgotYourPassword = new JLabel("Forgot your password?");
		lblForgotYourPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fpWindowClose == true) {
					fpWindowClose = false;
					ForgotPass fp = new ForgotPass(queueOfCommands, l, model);
					model.addObserver(fp);
					fp.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							e.getWindow().dispose();
							model.deleteObserver(fp);
							fpWindowClose = true;
						}
					});
				}
			}
		});
		lblForgotYourPassword.setToolTipText("");
		lblForgotYourPassword.setForeground(new Color(51, 204, 255));
		lblForgotYourPassword.setFont(new Font("Dialog", Font.ITALIC, 9));
		lblForgotYourPassword.setBounds(31, 127, 124, 27);
		loginPanel.add(lblForgotYourPassword);

		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(e -> {
			if (rWindowClose == true) {
				rWindowClose = false;
				new Register(this.queueOfCommands, model, l);
			}

		});

		btnRegister.setBackground(Color.WHITE);
		btnRegister.setBounds(22, 166, 64, 25);
		Design.secondaryButton(btnRegister);
		loginPanel.add(btnRegister);
		this.setVisible(true);
	}

	/**
	 * Attempt login.
	 *
	 * @param username the username
	 * @param password the password
	 */
	private void attemptLogin(String username, String password) {
		queueOfCommands.add(new MessageUS(Constants.LOGIN, new String[] { username, password }));
		this.myUserName = username;
		txtPassword.setText(null);
		txtUsername.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof MessageUS))
			return;
		MessageUS message = (MessageUS) arg;
		String instruction = message.getInstruction();

		if (instruction.equals(Constants.ACCEPTED_LOGIN)) {
			this.dispose();

			File checkMainFolder = new File("temporary Data");

			if (checkMainFolder.exists()) {
				File personalFolder = new File("temporary Data/" + this.myUserName);

				if (personalFolder.exists()) {
					File privateFolder = new File("temporary Data/" + this.myUserName + "/Private Chat");
					File groupFolder = new File("temporary Data/" + this.myUserName + "/Group Chat");
					File myTopContacts = new File("temporary Data/" + this.myUserName + "/Top contantcts");
					File ortherProfilePicture = new File(
							"temporary Data/" + this.myUserName + "/Orther User's Porfile Picture");
					File publicChatPhoto = new File("temporary Data/" + this.myUserName + "/Public Chat Photo");
					File gamePhoto = new File("temporary Data/" + this.myUserName + "/Game");
					if (!privateFolder.exists())
						new File("temporary Data/" + this.myUserName + "/Profile Picture").mkdir();

					if (!groupFolder.exists())
						new File("temporary Data/" + this.myUserName + "/Group Chat").mkdir();

					if (!myTopContacts.exists())
						new File("temporary Data/" + this.myUserName + "/Top contantcts").mkdir();

					if (!ortherProfilePicture.exists())
						new File("temporary Data/" + this.myUserName + "/Orther User's Porfile Picture").mkdir();

					if (!publicChatPhoto.exists())
						new File("temporary Data/" + this.myUserName + "/Public Chat Photo").mkdir();
					if (!gamePhoto.exists()) {
						new File("temporary Data/" + this.myUserName + "/Game").mkdir();
					}

				} else {
					new File("temporary Data/" + this.myUserName).mkdirs();
					new File("temporary Data/" + this.myUserName + "/Private Chat").mkdir();
					new File("temporary Data/" + this.myUserName + "/Group Chat").mkdir();
					new File("temporary Data/" + this.myUserName + "/Top contantcts").mkdir();
					new File("temporary Data/" + this.myUserName + "/Orther User's Porfile Picture").mkdir();
					new File("temporary Data/" + this.myUserName + "/Public Chat Photo").mkdir();
					new File("temporary Data/" + this.myUserName + "/Game").mkdir();
				}
			} else {
				new File("temporary Data").mkdir();
				new File("temporary Data/" + this.myUserName).mkdirs();
				new File("temporary Data/" + this.myUserName + "/Private Chat").mkdir();
				new File("temporary Data/" + this.myUserName + "/Group Chat").mkdir();
				new File("temporary Data/" + this.myUserName + "/Top contantcts").mkdir();
				new File("temporary Data/" + this.myUserName + "/Orther User's Porfile Picture").mkdir();
				new File("temporary Data/" + this.myUserName + "/Public Chat Photo").mkdir();
				new File("temporary Data/" + this.myUserName + "/Game").mkdir();
			}

			File mainprofilePhoto = new File(
					"temporary Data/" + this.myUserName + "/" + this.myUserName + "main profile photo.jpg");
			File chatprofilePhoto = new File(
					"temporary Data/" + this.myUserName + "/" + this.myUserName + "chatting profile photo.jpg");

			if (message.getImage() != null) {
				this.modifyPicture.byteToImage(message.getImage(), mainprofilePhoto);
				this.modifyPicture.resizeImage(mainprofilePhoto, chatprofilePhoto, 40, 40, "jpg");

			} else {

				File photo = new File("placeholder.jpg");
				try {
					this.defaultPhoto = this.modifyPicture.ImageToByte(photo);
					this.modifyPicture.byteToImage(defaultPhoto, mainprofilePhoto);
					this.modifyPicture.resizeImage(mainprofilePhoto, chatprofilePhoto, 40, 40, "jpg");
				} catch (IOException e) {
					System.err.println("Photo was not uploaded");
				}
			}

			this.myTop.forEach(e -> {
				File photo = new File("placeholder.jpg");
				File topFriend = new File(
						"temporary Data/" + this.myUserName + "/Top contantcts/" + e.getName() + " Main photo.jpg");
				byte[] topPicture = e.getPhoto();
				if (topPicture == null) {

					try {
						this.defaultPhoto = this.modifyPicture.ImageToByte(photo);
						this.modifyPicture.byteToImage(defaultPhoto, topFriend);

					} catch (IOException e1) {
						System.err.println("The photo was not uploaded");
					}

				} else {
					this.modifyPicture.byteToImage(topPicture, topFriend.getAbsoluteFile());
				}
			});

		} else if (instruction.equals(Constants.GET_MY_TOP)) {
			this.myTop = message.getPeople();
		}
		if (arg.equals(Constants.DECLINED_LOGIN))
			JOptionPane.showMessageDialog(null, "Login or password details are incorrect! Try again", "Inane error",
					JOptionPane.WARNING_MESSAGE);

		if (instruction.equals(Constants.DECLINED_LOGIN))
			JOptionPane.showMessageDialog(null, "Login failed! " + message.getReason(), "Login failed",
					JOptionPane.WARNING_MESSAGE);
	}
}