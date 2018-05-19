package view;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.NoughtsCrossesMain;

/**
 * The Class PotentialOponents. Creates a new window displaying the list of
 * potential partners for tic tac toe game match
 */
public class PotentialOponents extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The button send. */
	private JButton btnSend;

	/**
	 * Instantiates a new window with potential tic tac toe partners
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param games the games that are currently going
	 * @param myLogin the my login
	 */
	public PotentialOponents(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model,
			HashMap<String, NoughtsCrossesMain> games, String myLogin, MainScreen mainScreen) {
		super("Let's play!");
		this.setVisible(true);

		setResizable(false);
		setSize(160, 340);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		String[] all = model.getPeopleOnlineId();
		ArrayList<String> potentialParticipants = new ArrayList<>();

		for (String name : all)
			if (!games.containsKey(name))
				potentialParticipants.add(name);

		String[] properList = potentialParticipants.toArray(new String[potentialParticipants.size()]);

		JList<String> listOnline = new JList<>(properList);
		Design.backgroundColour(listOnline);

		JScrollPane scrollUsers = new JScrollPane(listOnline);
		Design.backgroundColour(scrollUsers);
		scrollUsers.setBounds(5, 5, 185, 250);

		GridBagConstraints gbc_chatbox = new GridBagConstraints();
		gbc_chatbox.gridheight = 2;
		gbc_chatbox.insets = new Insets(0, 0, 5, 5);
		gbc_chatbox.fill = GridBagConstraints.BOTH;
		gbc_chatbox.gridx = 0;
		gbc_chatbox.gridy = 1;
		contentPane.add(scrollUsers, gbc_chatbox);

		btnSend = new JButton("Invite");
		btnSend.setBounds(80, 270, 100, 29);
		btnSend.addActionListener(e -> {
			if (listOnline.getSelectedValue() != null) {
				MessageUS invitation = new MessageUS(Constants.PLAY_TIC_TAC_REQUEST, listOnline.getSelectedValue(), "");
				queueOfCommands.offer(invitation);
				games.put(listOnline.getSelectedValue(), null);
				mainScreen.setOtherPlayerTrue();
				this.dispose();
				JOptionPane.showMessageDialog(null, "The person has received your play request");
			} else
				JOptionPane.showMessageDialog(null, "Please select a person from the given list or click exit");

		});
		Design.primaryButton(btnSend);
		contentPane.setLayout(null);
		contentPane.add(btnSend);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(5, 270, 69, 29);
		btnCancel.addActionListener(e -> {
			mainScreen.setOtherPlayerTrue();
			this.dispose();
		});
		Design.secondaryButton(btnCancel);
		contentPane.add(btnCancel);
		Design.backgroundColour(contentPane);
		Design.backgroundColour(this);
	}
}