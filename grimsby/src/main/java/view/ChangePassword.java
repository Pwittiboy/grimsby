package view;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class ChangePassword. Creates a window for changing the password.
 */
public class ChangePassword extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The txt old password. */
	private JPasswordField txtOldPassword;

	/** The txt new password. */
	private JPasswordField txtNewPassword;

	/** The lbl new password. */
	private JLabel lblNewPassword;

	/** The txt confirm password. */
	private JPasswordField txtConfirmPassword;

	/** The lbl confirm password. */
	private JLabel lblConfirmPassword;

	/** The btn ok. */
	private JButton btnOk;

	/** The btn cancel. */
	private JButton btnCancel;

	/**
	 * Creates the window for changing the password
	 *
	 * @param queueOfCommands the queue of commands
	 */
	public ChangePassword(LinkedBlockingQueue<MessageUS> queueOfCommands, MainScreen mainScreen) {
		setBounds(100, 100, 183, 334);
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtOldPassword = new JPasswordField();
		Design.designField(txtOldPassword);
		txtOldPassword.setBounds(27, 76, 130, 26);
		contentPane.add(txtOldPassword);
		txtOldPassword.setColumns(10);

		JLabel lblOldPassword = new JLabel("Old password");
		Design.textColour(lblOldPassword);
		lblOldPassword.setBounds(29, 58, 123, 16);
		contentPane.add(lblOldPassword);

		txtNewPassword = new JPasswordField();
		Design.designField(txtNewPassword);
		txtNewPassword.setColumns(10);
		txtNewPassword.setBounds(27, 132, 130, 26);
		contentPane.add(txtNewPassword);

		lblNewPassword = new JLabel("New password");
		Design.textColour(lblNewPassword);
		lblNewPassword.setBounds(29, 114, 123, 16);
		contentPane.add(lblNewPassword);

		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setColumns(10);
		Design.designField(txtConfirmPassword);
		txtConfirmPassword.setBounds(27, 188, 130, 26);
		contentPane.add(txtConfirmPassword);

		lblConfirmPassword = new JLabel("Confirm password");
		Design.textColour(lblConfirmPassword);
		lblConfirmPassword.setBounds(29, 170, 123, 16);
		contentPane.add(lblConfirmPassword);

		btnOk = new JButton("OK");
		btnOk.addActionListener(e -> {
			if (new String(txtNewPassword.getPassword()).equals(new String(txtConfirmPassword.getPassword()))
					&& txtNewPassword.getPassword().length > 6 && txtNewPassword.getPassword().length < 20) {
				queueOfCommands.offer(new MessageUS(Constants.CHANGE_PASSWORD, new String[] {
						new String(txtOldPassword.getPassword()), new String(txtNewPassword.getPassword()) }));
				mainScreen.setChangePassTrue();
				this.dispose();
			} else if (!new String(txtNewPassword.getPassword()).equals(new String(txtConfirmPassword.getPassword()))) {
				txtNewPassword.setText("");
				txtOldPassword.setText("");
				txtConfirmPassword.setText("");
				JOptionPane.showMessageDialog(null, "The new password does not mach. Try again",
						"Passwords do not match", JOptionPane.WARNING_MESSAGE);

			} else {
				txtNewPassword.setText("");
				txtOldPassword.setText("");
				txtConfirmPassword.setText("");
				JOptionPane.showMessageDialog(null, "The length of the password must be >6 and <20",
						"Passwords was not changed", JOptionPane.WARNING_MESSAGE);
			}
		});
		Design.primaryButton(btnOk);
		btnOk.setBounds(97, 245, 75, 29);
		contentPane.add(btnOk);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			mainScreen.setChangePassTrue();
			this.dispose();
		});
		Design.secondaryButton(btnCancel);
		btnCancel.setBounds(0, 245, 92, 29);
		contentPane.add(btnCancel);

		this.setVisible(true);
	}
}