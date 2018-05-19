package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Class ConnectionRequestReceived.
 */
public class ConnectionRequestReceived extends JFrame {
	/** The content pane. */
	private JPanel contentPane;

	/**
	 * Create the frame for connection request window
	 *
	 * @param queueOfCommands the queue of commands
	 * @param possibleCurrentConnection the possible current connection
	 * @param model the model
	 * @param partnerLogin the partner's login
	 */
	public ConnectionRequestReceived(LinkedBlockingQueue<MessageUS> queueOfCommands,
			HashSet<String> possibleCurrentConnection, Model model, String partnerLogin,
			HashMap<String, Profile> profiles) {
		setResizable(false);
		setSize(303, 118);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				queueOfCommands.offer(new MessageUS(Constants.DECLINED_CONNECT, partnerLogin, ""));
				possibleCurrentConnection.remove(partnerLogin);
				if (profiles.containsKey(partnerLogin))
					profiles.get(partnerLogin).enableMessage();
				e.getWindow().dispose();
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUserWantsTo = new JLabel(partnerLogin + " wants to connect");
		Design.textColour(lblUserWantsTo);
		lblUserWantsTo.setBounds(6, 6, 240, 16);
		contentPane.add(lblUserWantsTo);

		
		JButton btnAccept = new JButton("Accept");
		Design.primaryButton(btnAccept);
		btnAccept.addActionListener(e -> {
			HashSet<String> onlinePeople = new HashSet<>(Arrays.asList(model.getPeopleOnlineId()));
			if (onlinePeople.contains(partnerLogin))
				queueOfCommands.offer(new MessageUS(Constants.ACCEPTED_CONNECT, partnerLogin, ""));
			else
				JOptionPane.showMessageDialog(null,
						partnerLogin + " is already logged off... try to connect when " + partnerLogin + " will login");
			this.dispose();

		});
		btnAccept.setBounds(160, 55, 117, 29);
		contentPane.add(btnAccept);

		JButton btnDecline = new JButton("Decline");
		btnDecline.setBounds(20, 55, 117, 29);
		Design.secondaryButton(btnDecline);
		contentPane.add(btnDecline);

		btnDecline.addActionListener(e -> {
			queueOfCommands.offer(new MessageUS(Constants.DECLINED_CONNECT, partnerLogin, ""));
			possibleCurrentConnection.remove(partnerLogin);
			if (profiles.containsKey(partnerLogin))
				profiles.get(partnerLogin).enableMessage();
			this.dispose();
		});
		Design.backgroundColour(this);

		Design.backgroundColour(contentPane);
		this.setVisible(true);

	}

}
