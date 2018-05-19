package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.NoughtsCrossesMain;

/**
 * The Class GameRequestReceived.
 */
public class GameRequestReceived extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/**
	 * Create the frame if the game request is received. Allows user to accept
	 * or decline the invitation
	 *
	 * @param queueOfCommands the queue of commands
	 * @param partnerLogin the partner login
	 * @param games the games
	 */
	public GameRequestReceived(LinkedBlockingQueue<MessageUS> queueOfCommands, String partnerLogin,
			HashMap<String, NoughtsCrossesMain> games) {
		games.put(partnerLogin, null);
		setResizable(false);
		setSize(303, 118);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				queueOfCommands.offer(new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, partnerLogin, partnerLogin));
				games.remove(partnerLogin);
				e.getWindow().dispose();
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUserWantsTo = new JLabel(partnerLogin + " wants to play with you tick tack toe");
		Design.textColour(lblUserWantsTo);
		lblUserWantsTo.setBounds(6, 6, 260, 16);
		contentPane.add(lblUserWantsTo);

		JButton btnAccept = new JButton("Accept");
		Design.primaryButton(btnAccept);
		btnAccept.addActionListener(e -> {

			queueOfCommands.offer(new MessageUS(Constants.PLAY_TIC_TAC_ACCEPTED, partnerLogin, partnerLogin));
			this.dispose();

		});
		btnAccept.setBounds(160, 55, 117, 29);
		contentPane.add(btnAccept);

		JButton btnDecline = new JButton("Decline");
		btnDecline.setBounds(20, 55, 117, 29);
		Design.secondaryButton(btnDecline);
		contentPane.add(btnDecline);

		btnDecline.addActionListener(e -> {
			queueOfCommands.offer(new MessageUS(Constants.PLAY_TIC_TAC_DECLINED, partnerLogin, ""));
			games.remove(partnerLogin);
			this.dispose();
		});
		Design.backgroundColour(this);

		Design.backgroundColour(contentPane);
		this.setVisible(true);
	}

}
