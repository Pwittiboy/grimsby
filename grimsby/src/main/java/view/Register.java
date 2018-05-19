package view;

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
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;

import java.awt.BorderLayout;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;

/**
 * The Class Register.
 */
public class Register extends JFrame implements Observer {

	/** The model. */
	private Model model;

	/** The username. */
	private JTextField txtUsername;

	/** The password. */
	private JPasswordField password;

	/** The main panel. */
	private JPanel mainPanel;

	/** The register panel. */
	private JPanel registerPanel;

	/** The email. */
	private JTextField txtEmail;

	/** The content panel. */
	private final JPanel contentPanel = new JPanel();

	/** The Constant VALID_EMAIL_ADDRESS_REGEX. */
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public Login login;

	/**
	 * Check validity of the given string
	 *
	 * @param input the input string
	 * @return true, if invalid: contains symbols that are not allowed
	 * @MODIFIED search for illegal input, return false if there is no illegal
	 * strings contains
	 */
	public static boolean checkInput(String input) {
		return (input.contains(";") || input.contains("/") || input.contains("\"") || input.contains(" ")
				|| input.contains("*") || input.contains("'") || input.contains("'") || input.contains("[")
				|| input.contains("]") || input.contains("|") || input.contains("=") || input.contains(",")
				|| input.contains("<") || input.contains(">") || input.contains("~") || input.contains("_"));

	}

	/**
	 * Create the window for user to register.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 */
	public Register(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, Login login) {
		this.login = login;
		this.model = model;
		this.model.addObserver(this);
		setBackground(Color.WHITE);
		setResizable(false);
		setSize(176, 365);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		this.setVisible(true);
		/** create observable */

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBounds(0, 0, 176, 357);
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		registerPanel = new JPanel();
		registerPanel.setLocation(0, 105);
		registerPanel.setSize(176, 229);
		registerPanel.setBorder(new LineBorder(new Color(255, 255, 255)));
		registerPanel.setBackground(new Color(255, 255, 255));
		registerPanel.setLayout(null);
		mainPanel.add(registerPanel, null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblUsername.setToolTipText("");
		Design.textColour(lblUsername);
		lblUsername.setBounds(31, 14, 145, 27);
		registerPanel.add(lblUsername);

		txtUsername = new JTextField();
		Design.designField(txtUsername);
		txtUsername.setBounds(31, 40, 114, 19);
		txtUsername.setColumns(10);
		registerPanel.add(txtUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPassword.setToolTipText("");
		Design.textColour(lblPassword);
		lblPassword.setBounds(31, 69, 124, 27);
		registerPanel.add(lblPassword);

		password = new JPasswordField();
		Design.designField(password);
		password.setBounds(31, 95, 114, 19);
		password.setColumns(10);
		registerPanel.add(password);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setToolTipText("");
		Design.textColour(lblEmail);
		lblEmail.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEmail.setBounds(31, 126, 124, 27);
		registerPanel.add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		Design.designField(txtEmail);
		txtEmail.setBounds(31, 148, 114, 19);
		registerPanel.add(txtEmail);

		JButton btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				lblUsername.setForeground(new Color(168, 189, 189));
				lblUsername.setText("Username");
				lblPassword.setForeground(new Color(168, 189, 189));
				lblPassword.setText("Password");
				lblEmail.setForeground(new Color(168, 189, 189));
				lblEmail.setText("Email");
				String user = txtUsername.getText();
				String txtPassword = new String(password.getPassword());
				if (user.length() > 12 || user.length() < 6) {
					lblUsername.setForeground(Color.RED);
					lblUsername.setText("Must be 6-12 letters");
				} else if (checkInput(user)) {
					lblUsername.setForeground(Color.RED);
					lblUsername.setText("Illegal characters");
				} else if (txtPassword.length() < 6 || txtPassword.length() > 20) {
					lblPassword.setForeground(Color.RED);
					lblPassword.setText("Must be 6-20 letters");
				} else if (checkInput(txtPassword)) {
					lblPassword.setForeground(Color.RED);
					lblPassword.setText("Illegal characters");
				} else if (!validate(txtEmail.getText())) {
					lblEmail.setForeground(Color.RED);
					lblEmail.setText("Invalid email");
				}

				else {
					String[] registerInformation = { user, txtPassword, txtEmail.getText() };
					queueOfCommands.offer(new MessageUS(Constants.CREATE_ACCOUNT, registerInformation));
				}
			}

		});
		btnRegister.setActionCommand("OK");
		btnRegister.setBounds(91, 192, 64, 25);
		btnRegister.setForeground(new Color(255, 255, 255));
		btnRegister.setBorder(null);
		btnRegister.setBackground(new Color(140, 236, 67));
		registerPanel.add(btnRegister);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(e -> {
			this.dispose();
			model.deleteObserver(this);
			login.setRCloseWindow();
		});
		btnCancel.setForeground(new Color(140, 236, 67));
		btnCancel.setBorder(new LineBorder(new Color(140, 236, 67)));
		btnCancel.setBackground(Color.WHITE);
		btnCancel.setBounds(15, 192, 64, 25);
		registerPanel.add(btnCancel);

		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(Register.class.getResource("/resources/grimsby.png")));
		logo.setBounds(33, 12, 104, 95);
		mainPanel.add(logo);

		Register register = this;
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.setRCloseWindow();
				model.deleteObserver(register);
			}
		});

	}

	/**
	 * Validate. Check whether the given email address is valid
	 *
	 * @param emailStr the email adress
	 * @return true, if valid
	 */
	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals(Constants.ACCEPTED_CREATE_ACCOUNT)) {
			JOptionPane.showMessageDialog(null, "Your account was successfully created! Please log in");
			this.dispose();
			model.deleteObserver(this);
			this.login.setRCloseWindow();
			return;
		}
		if (arg instanceof MessageUS) {
			MessageUS message = (MessageUS) arg;
			if (message.getInstruction().equals(Constants.DECLINED_CREATE_ACCOUNT)) {
				JOptionPane.showMessageDialog(null, "Your account was not created. " + message.getReason(),
						"Account was not created", JOptionPane.WARNING_MESSAGE);
				txtUsername.setText("");
				txtEmail.setText("");
				password.setText("");
			}
		}

	}

}