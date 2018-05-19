package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class Profile.
 */
public class Profile extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/** The my login. */
	private String myLogin;

	/** The modify picture. */
	private ModifyPicture modifyPicture;

	/** The username. */
	private String username;

	/** The viewing profile. */
	private HashSet<String> viewingProfile;

	/** The button message. */
	private JButton btnMessage;

	/**
	 * Create the frame for viewing person's profile. Edit button is present if
	 * the profile if the person's who is watching it, otherwise, the message
	 * button is present instead of edit.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param userInformation the user information
	 * @param myLogin the my login
	 * @param possibleCurrentConnections the possible current connections
	 * @param photo the photo
	 * @param viewingProfile the viewing profile
	 * @param profile the profile
	 */
	public Profile(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, String[] userInformation,
			String myLogin, HashSet<String> possibleCurrentConnections, byte[] photo, HashSet<String> viewingProfile,
			HashMap<String, Profile> profile) {
		this.myLogin = myLogin;
		this.modifyPicture = new ModifyPicture();
		btnMessage = new JButton();
		Design.profileBackground(this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				profile.remove(userInformation[0]);
			}
		});
		setSize(450, 360);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		username = userInformation[0];
		this.viewingProfile = viewingProfile;

		Set<String> online = new HashSet<String>(Arrays.asList(model.getPeopleOnlineId()));

		if (!userInformation[0].equals(myLogin)) {

			btnMessage = new JButton("Message");
			Design.primaryButton(btnMessage);
			btnMessage.addActionListener(e -> {

				profile.remove(userInformation[0]);
				this.dispose();
				queueOfCommands.offer(new MessageUS(Constants.CONNECT, userInformation[0], ""));
				JOptionPane.showMessageDialog(null, "Please wait other side to respond", "Request has been sent!",
						JOptionPane.INFORMATION_MESSAGE);
				possibleCurrentConnections.add(userInformation[0]);

			});
			
			if (possibleCurrentConnections.contains(userInformation[0]) || !online.contains(userInformation[0]))
				btnMessage.setEnabled(false);
			btnMessage.setBounds(20, 268, 106, 29);
			contentPane.add(btnMessage);
		}
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		Design.backgroundColour(panel);
		panel.setBounds(17, 5, 421, 74);
		contentPane.add(panel);

		JLabel lblUsername = new JLabel(userInformation[0]);
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblUsername.setBounds(88, 12, 177, 58);
		panel.add(lblUsername);
		panel.setLayout(null);
		Design.textColour(lblUsername);

		JLabel profilePhoto = new JLabel("");
		profilePhoto.setHorizontalAlignment(SwingConstants.CENTER);

		File userProfilePhoto;
		if (userInformation[0].equals(myLogin)) {
			userProfilePhoto = new File("temporary Data/" + myLogin + "/" + myLogin + "main profile photo.jpg");

		} else {
			userProfilePhoto = new File("temporary Data/" + this.myLogin + "/Orther User's Porfile Picture/"
					+ userInformation[0] + "Main profile photo.jpg");
		}
		if (photo == null) {
			try {
				this.modifyPicture.byteToImage(this.modifyPicture.ImageToByte(new File("placeholder.jpg")),
						userProfilePhoto);
			} catch (IOException e) {
			}
		} else {
			this.modifyPicture.byteToImage(photo, userProfilePhoto);
		}
		ImageIcon icon = new ImageIcon(userProfilePhoto.getAbsolutePath());
		icon.getImage().flush();
		profilePhoto.setIcon(icon);
		profilePhoto.setBounds(12, 5, 64, 64);
		panel.add(profilePhoto);

		if (userInformation[0].equals(myLogin)) {
			JButton btnEdit = new JButton("EDIT");
			btnEdit.setBorder(new LineBorder(Color.LIGHT_GRAY));
			btnEdit.setForeground(Color.LIGHT_GRAY);
			btnEdit.setBackground(Color.WHITE);
			btnEdit.setBounds(351, 30, 58, 25);
			btnEdit.setFocusable(false);
			Design.secondaryButton(btnEdit);
			panel.add(btnEdit);
			btnEdit.addActionListener(e -> {
				this.dispose();
				new ProfileSettings(queueOfCommands, model, userInformation[1], userInformation[2], userInformation[0],
						userInformation[3], userInformation[4], this.viewingProfile);
			});
		}
		JPanel panel_1 = new JPanel();
		Design.backgroundColour(panel_1);
		panel_1.setBounds(17, 91, 421, 171);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel iconInfo = new JLabel("");
		iconInfo.setHorizontalAlignment(SwingConstants.CENTER);

		iconInfo.setBounds(0, 0, 33, 36);
		panel_1.add(iconInfo);

		JLabel lblGeneralInfo = new JLabel("GENERAL INFO");
		lblGeneralInfo.setBounds(12, 12, 104, 15);
		Design.profileQs(lblGeneralInfo);
		panel_1.add(lblGeneralInfo);

		JLabel lblName = new JLabel("NAME");
		Design.profileQs(lblName);
		lblName.setBounds(12, 48, 44, 16);
		panel_1.add(lblName);

		JLabel lblNameInfo = new JLabel(userInformation[1]);
		lblNameInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		Design.textColour(lblNameInfo);
		lblNameInfo.setBounds(12, 68, 106, 29);
		panel_1.add(lblNameInfo);
		lblNameInfo.setVerticalAlignment(SwingConstants.TOP);

		JLabel lblEmailInfo = new JLabel(userInformation[3]);
		lblEmailInfo.setBounds(148, 68, 220, 29);
		panel_1.add(lblEmailInfo);
		lblEmailInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEmailInfo.setVerticalAlignment(SwingConstants.TOP);
		Design.textColour(lblEmailInfo);

		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setBounds(148, 48, 47, 16);
		panel_1.add(lblEmail);
		lblEmail.setForeground(Color.GRAY);
		Design.profileQs(lblEmail);

		JLabel lblBio = new JLabel("BIO");
		lblBio.setBounds(12, 106, 61, 16);
		panel_1.add(lblBio);
		Design.profileQs(lblBio);

		JTextArea lblBioInfo = new JTextArea(userInformation[4]);
		lblBioInfo.setEditable(false);
		lblBioInfo.setLineWrap(true);
		lblBioInfo.setWrapStyleWord(true);
		lblBioInfo.setOpaque(false);
		lblBioInfo.setBounds(12, 123, 399, 85);
		panel_1.add(lblBioInfo);
		Design.textColour(lblBioInfo);
		lblBioInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.setVisible(true);
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * Disable message.
	 */
	public void disableMessage() {
		btnMessage.setEnabled(false);
		revalidate();
		repaint();
	}

	/**
	 * Enable message.
	 */
	public void enableMessage() {
		btnMessage.setEnabled(true);
		revalidate();
		repaint();
	}
}
