package view;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.Conversation;
import grimsby.networking.util.MessageUS;

/**
 * The Class OnlineUsers.
 */
public class OnlineUsers extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The chat box. */
	private JTextArea chatbox = new JTextArea();

	/** The button send. */
	private JButton btnSend;

	/**
	 * Instantiates a new online users window. Shows the people that are online
	 * and available to be invited to the new group
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param groupName the group name
	 * @param groupId the group id
	 * @param myConversation the my conversation
	 */
	public OnlineUsers(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, String groupName, String groupId,
			Conversation myConversation, GroupChat groupChat) {
		this.setVisible(true);

		setResizable(false);
		setSize(160, 340);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		String[] all = model.getPeopleOnlineId();
		ArrayList<String> potentialParticipants = new ArrayList<>();
		Set<String> peopleInChat = myConversation.getPeopleInConversation();
		for (String name : all)
			if (!peopleInChat.contains(name))
				potentialParticipants.add(name);

		String[] properList = potentialParticipants.toArray(new String[potentialParticipants.size()]);
		JList<String> listOnline = new JList<>(properList);
		Design.backgroundColour(listOnline);
		listOnline.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

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
		btnSend.setBounds(5, 270, 69, 29);
		btnSend.addActionListener(e -> {
			if (listOnline.getSelectedValue() != null) {
				MessageUS invitation = new MessageUS(Constants.ADD_PERSON, listOnline.getSelectedValue(), groupId);
				invitation.setName(groupName);
				queueOfCommands.offer(invitation);
				JOptionPane.showMessageDialog(null, "The person " + listOnline.getSelectedValue()
						+ " will be added to the group chat \"" + groupName + "\"");
				groupChat.setmntmAddUserTrue();
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Please select a person from the given list or click exit");
			}
		});
		Design.primaryButton(btnSend);
		contentPane.setLayout(null);
		contentPane.add(btnSend);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(90, 270, 69, 29);
		btnCancel.addActionListener(e -> {
			this.dispose();
			groupChat.setmntmAddUserTrue();
		});
		Design.secondaryButton(btnCancel);
		contentPane.add(btnCancel);
		Design.backgroundColour(contentPane);
		Design.backgroundColour(this);
	}
}