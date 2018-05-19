package view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.chainsaw.Main;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class CreateChat. Creates a window for creating a group
 */
public class CreateChat extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The btn cancel. */
	private JButton btnCancel;

	/** The txt chat name. */
	private JTextField txtChatName;

	/**
	 * Instantiates a new create chat window.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 */
	public CreateChat(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, MainScreen mainScreen) {
		Design.backgroundColour(this);
		setResizable(false);
		setSize(280, 310);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		txtChatName = new JTextField();
		txtChatName.setBounds(5, 19, 263, 26);
		Design.designField(txtChatName);
		txtChatName.setText("Enter chat name");
		contentPane.setLayout(null);
		contentPane.add(txtChatName);
		txtChatName.setColumns(10);

		JList<String> listOnline = new JList<>(model.getPeopleOnlineId());
		Design.backgroundColour(listOnline);
		listOnline.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane scrollUsers = new JScrollPane(listOnline);
		Design.backgroundColourCreateChat(listOnline);
		scrollUsers.setBounds(5, 58, 263, 167);
		contentPane.add(scrollUsers);

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(5, 237, 126, 29);
		Design.secondaryButton(btnCancel);
		btnCancel.addActionListener(e -> {
			mainScreen.setMntmStartGroupChatTrue();
			dispose();
		});
		contentPane.add(btnCancel);

		JButton btnCreate = new JButton("Create Group");
		btnCreate.addActionListener(e -> {
			if (!checkValidity(txtChatName.getText())) {
				ArrayList<String> selectedValues = new ArrayList<>();
				selectedValues.addAll(listOnline.getSelectedValuesList());
				if (!selectedValues.isEmpty()) {
					JOptionPane.showMessageDialog(null, "The group \"" + txtChatName.getText() + "\" is being created",
							"Creating group", JOptionPane.INFORMATION_MESSAGE);
					MessageUS createGroup = new MessageUS(Constants.CREATE_GROUP, selectedValues);
					createGroup.setName(txtChatName.getText());
					queueOfCommands.offer(createGroup);
					mainScreen.setMntmStartGroupChatTrue();
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "You cannot create an empty group", "Request has been sent!",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				txtChatName.setText("");
				JOptionPane.showMessageDialog(null,
						"The group name has to be a single words and does not contain - , / or ;", "Illegal group name",
						JOptionPane.INFORMATION_MESSAGE);

			}
		});
		Design.primaryButton(btnCreate);
		btnCreate.setBounds(141, 237, 126, 29);
		contentPane.add(btnCreate);
		Design.backgroundColour(contentPane);
		this.setVisible(true);
	}

	/**
	 * Check validity.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	private boolean checkValidity(String name) {
		return name.contains(" ") || name.contains("-") || name.contains(";") || name.contains("/")
				|| name.contains(",");
	}
}