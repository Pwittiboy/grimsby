package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The Class ForgotPass. Creates a window for reminding password where user can
 * enter one's name
 */
public class ForgotPass extends JFrame implements Observer {

	/** The content pane. */
	private JPanel contentPane;

	/** The txt name. */
	private JTextField txtName;

	private Login login;
	private Model model;

	/**
	 * Instantiates a new forgot pass.
	 *
	 * @param queueOfCommands the queue of commands
	 */
	public ForgotPass(BlockingQueue<MessageUS> queueOfCommands, Login login, Model model) {
		super("Password reminder");
		this.login = login;
		this.setVisible(true);
		this.model = model;
		setBounds(100, 100, 224, 157);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);

		txtName = new JTextField();
		Design.designField(txtName);
		txtName.setBounds(51, 50, 130, 26);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JLabel lblName = new JLabel("Username");
		Design.textColour(lblName);
		lblName.setBounds(51, 22, 155, 16);
		contentPane.add(lblName);

		JButton btnOk = new JButton("Send Email");
		btnOk.setBounds(104, 94, 102, 29);
		Design.primaryButton(btnOk);

		btnOk.addActionListener(e -> {
			if (Register.checkInput(txtName.getText())) {
				lblName.setForeground(Color.RED);
				lblName.setText("A-Z, 0-9 only");
			} else if (txtName.getText().length() < 6 || txtName.getText().length() > 12) {
				lblName.setForeground(Color.RED);
				lblName.setText("Invalid name");
			} else {
				lblName.setForeground(Design.BLUE_TEXT);
				lblName.setText("Username");
				MessageUS newCommand = new MessageUS(Constants.FORGOT_PASSWORD, txtName.getText(), "");
				queueOfCommands.offer(newCommand);
			}
		});
		contentPane.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(17, 93, 75, 29);
		Design.secondaryButton(btnCancel);

		btnCancel.addActionListener(e -> {
			this.dispose();
			login.setfpCloseWindow();
			model.deleteObserver(this);
		});

		contentPane.add(btnCancel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MessageUS) {
			MessageUS message = (MessageUS) arg;
			if (message.getInstruction().equals(Constants.EMAIL_SENT)) {

				dispose();
				this.login.setfpCloseWindow();
				JOptionPane.showMessageDialog(null, "Email was sent!", "Email was sent",
						JOptionPane.INFORMATION_MESSAGE);
				model.deleteObserver(this);
			} else if (message.getInstruction().equals(Constants.EMAIL_NOT_SENT)) {
				txtName.setText("");
				JOptionPane.showMessageDialog(null, "Email was not sent. " + message.getReason(), "Email was not sent",
						JOptionPane.WARNING_MESSAGE);

			}
		}

	}

}