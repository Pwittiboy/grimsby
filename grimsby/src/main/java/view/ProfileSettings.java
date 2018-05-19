package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.ActionEvent;

/**
 * The Class ProfileSettings.
 */
public class ProfileSettings extends JFrame implements Observer {

	/** The content pane. */
	private JPanel contentPane;

	/** The name. */
	private JTextField txtName;

	/** The email. */
	private JTextField txtEmail;

	/** The characters max. */
	private JTextField txtCharactersMax;

	/** The modify picture. */
	private ModifyPicture modifyPicture = new ModifyPicture();

	/** The model. */
	private Model model;

	/** The my login. */
	private String myLogin;

	/** My profile photo. */
	JLabel profilePhoto = new JLabel("");

	/** The viewing profile. */
	private HashSet<String> viewingProfile;

	/**
	 * Instantiates a window to view and update profile settings.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param model the model
	 * @param realName the real name
	 * @param myLogin the my login
	 * @param Email the email
	 * @param Bio the bio
	 * @param viewingProfile the viewing profile
	 */
	public ProfileSettings(LinkedBlockingQueue<MessageUS> queueOfCommands, Model model, String realName,
			String surename, String myID, String Email, String Bio, HashSet<String> viewingProfile) {
		this.model = model;
		this.myLogin = myID;
		this.viewingProfile = viewingProfile;
		model.addObserver(this);

		setBackground(new Color(232, 239, 241));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(512, 345);
		this.setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(232, 239, 241));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBackground(Color.WHITE);
		panel.setBounds(17, 5, 481, 74);
		contentPane.add(panel);

		JLabel lblUsername = new JLabel(myID);
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 16));
		Design.textColour(lblUsername);
		lblUsername.setBounds(88, 12, 177, 58);
		panel.add(lblUsername);
		panel.setLayout(null);

		JLabel profilePhoto = new JLabel("");
		profilePhoto.setHorizontalAlignment(SwingConstants.CENTER);

		File myProfilePhoto = new File("temporary Data/" + myID + "/" + myID + "main profile photo.jpg");
		profilePhoto.setIcon(new ImageIcon(myProfilePhoto.getAbsolutePath()));
		profilePhoto.setBounds(12, 5, 64, 64);
		panel.add(profilePhoto);

		JButton btnUpdatePhoto = new JButton("UPLOAD");
		btnUpdatePhoto.setFocusable(false);
		btnUpdatePhoto.setBorder(null);
		btnUpdatePhoto.setBounds(383, 30, 86, 25);
		btnUpdatePhoto.setForeground(Color.WHITE);
		btnUpdatePhoto.setFont(new Font("Dialog", Font.PLAIN, 9));
		btnUpdatePhoto.setBackground(new Color(82, 109, 65));
		panel.add(btnUpdatePhoto);
		btnUpdatePhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				jf.setFileFilter(filter);
				jf.setCurrentDirectory(new File("C:"));
				int option = jf.showOpenDialog(btnUpdatePhoto);
				if (jf.getSelectedFile() == null || !jf.getSelectedFile().exists()) {
				} else {
					File pictureSelected = new File(jf.getSelectedFile().getAbsolutePath());
					File updatedMainPhoto = new File(
							"temporary Data/" + myID + "/" + myID + "updatedmain profile photo.jpg");
					modifyPicture.resizeImage(pictureSelected, updatedMainPhoto, 64, 64, "jpg");
					MessageUS commands = new MessageUS(Constants.UPLOAD_PHOTO);
					try {
						commands.addPhoto(modifyPicture.ImageToByte(updatedMainPhoto));
						queueOfCommands.offer(commands);
					} catch (IOException e1) {
						System.err.println("photo not added");
					}
				}
			}
		});

		JPanel panel_1 = new JPanel();
		Design.backgroundColour(panel_1);
		panel_1.setBounds(17, 91, 481, 206);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel iconInfo = new JLabel("");
		iconInfo.setHorizontalAlignment(SwingConstants.CENTER);
		// iconInfo.setIcon(new
		// ImageIcon(Profile.class.getResource("/resources/info.png")));
		iconInfo.setBounds(0, 0, 33, 33);
		panel_1.add(iconInfo);

		JLabel lblGeneralInfo = new JLabel("GENERAL INFO");
		lblGeneralInfo.setBounds(12, 12, 104, 15);
		Design.profileQs(lblGeneralInfo);
		panel_1.add(lblGeneralInfo);

		JLabel lblFirstName = new JLabel("NAME");
		lblFirstName.setForeground(Color.GRAY);
		lblFirstName.setBounds(12, 48, 44, 16);
		panel_1.add(lblFirstName);

		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setBounds(221, 48, 47, 16);
		panel_1.add(lblEmail);
		lblEmail.setForeground(Color.GRAY);

		JLabel lblBio = new JLabel("BIO");
		lblBio.setBounds(12, 103, 61, 16);
		panel_1.add(lblBio);
		lblBio.setForeground(Color.GRAY);

		txtName = new JTextField();
		txtName.setBounds(12, 69, 114, 19);
		panel_1.add(txtName);
		txtName.setText(realName);
		txtName.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(221, 69, 153, 19);
		panel_1.add(txtEmail);
		txtEmail.setText(Email);
		txtEmail.setColumns(10);

		txtCharactersMax = new JTextField();
		// txtCharactersMax.setFont(new Font("Dialog", Font.PLAIN, 10));
		txtCharactersMax.setText(Bio);
		txtCharactersMax.setBounds(12, 124, 334, 19);
		panel_1.add(txtCharactersMax);
		txtCharactersMax.setColumns(10);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(363, 166, 106, 29);
		panel_1.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBorder(new LineBorder(new Color(199, 99, 101)));
		Design.primaryButton(btnCancel);

		JButton btnName = new JButton("UPDATE");
		btnName.setFont(new Font("Dialog", Font.PLAIN, 9));
		btnName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstName = txtName.getText();
				if (firstName.length() > 13)
					txtName.setText(firstName.substring(0, 13));
				MessageUS changeName = new MessageUS(Constants.CHANGE_NAME, new String[] { txtName.getText() });
				queueOfCommands.offer(changeName);
			}
		});
		btnName.setHorizontalAlignment(SwingConstants.LEFT);
		btnName.setForeground(Color.WHITE);
		btnName.setBackground(new Color(82, 109, 65));
		btnName.setBounds(135, 69, 73, 19);
		panel_1.add(btnName);

		JButton btnEmail = new JButton("UPDATE");
		btnEmail.addActionListener(e -> {
			if (txtEmail.getText().contains("@") && txtEmail.getText().contains(".")) {
				MessageUS changeEmail = new MessageUS(Constants.CHANGE_EMAIL, new String[] { txtEmail.getText() });
				queueOfCommands.offer(changeEmail);
			} else {
				JOptionPane.showMessageDialog(null, "Updated failed, invalid Email account", "Update failure",
						JOptionPane.WARNING_MESSAGE);

			}
		});
		btnEmail.setHorizontalAlignment(SwingConstants.LEFT);
		btnEmail.setForeground(Color.WHITE);
		btnEmail.setFont(new Font("Dialog", Font.PLAIN, 9));
		btnEmail.setBackground(new Color(82, 109, 65));
		btnEmail.setBounds(384, 69, 73, 19);
		panel_1.add(btnEmail);

		JButton btnBio = new JButton("UPDATE");
		btnBio.addActionListener(e -> {
			if (txtCharactersMax.getText().length() <= 140) {
				MessageUS changeBio = new MessageUS(Constants.CHANGE_BIO, new String[] { txtCharactersMax.getText() });
				queueOfCommands.offer(changeBio);
			} else {
				JOptionPane.showMessageDialog(null, "Updated failed, bio is too long", "Update failure",
						JOptionPane.WARNING_MESSAGE);
				txtCharactersMax.setText(Bio);
			}

		});
		btnBio.setHorizontalAlignment(SwingConstants.LEFT);
		btnBio.setForeground(Color.WHITE);
		btnBio.setFont(new Font("Dialog", Font.PLAIN, 9));
		btnBio.setBackground(new Color(82, 109, 65));
		btnBio.setBounds(384, 124, 73, 19);
		panel_1.add(btnBio);

		Design.backgroundColour(this);
		Design.backgroundColour(contentPane);
		Design.backgroundColour(panel);
		this.setVisible(true);
		ProfileSettings p = this;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				viewingProfile.remove(myID);
				model.deleteObserver(p);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof MessageUS) {
			MessageUS m = (MessageUS) arg1;
			if (m.getInstruction().equals(Constants.PHOTO_UPLOADED)) {
				byte[] newProfile = m.getImage();
				File mainprofilePhoto = new File(
						"temporary Data/" + myLogin + "/" + myLogin + "main profile photo.jpg");
				File chatprofilePhoto = new File(
						"temporary Data/" + myLogin + "/" + myLogin + "chatting profile photo.jpg");

				this.modifyPicture.byteToImage(newProfile, mainprofilePhoto);
				this.modifyPicture.resizeImage(mainprofilePhoto, chatprofilePhoto, 40, 40, "jpg");
				ImageIcon icon = new ImageIcon(mainprofilePhoto.getAbsolutePath());
				icon.getImage().flush();
				profilePhoto.setIcon(icon);
				this.viewingProfile.remove(myLogin);
				this.dispose();
				model.deleteObserver(this);
			}
			if (m.getInstruction().equals(Constants.CHANGE_NAME_ACCEPTED)) {
				this.dispose();
				model.deleteObserver(this);
				JOptionPane.showMessageDialog(null, "Your name has been updated", "Update success!",
						JOptionPane.INFORMATION_MESSAGE);
				this.viewingProfile.remove(myLogin);
			}
			if (m.getInstruction().equals(Constants.CHANGE_BIO_ACCEPTED)) {
				this.dispose();
				model.deleteObserver(this);
				JOptionPane.showMessageDialog(null, "Your Bio has been updated", "Update success!",
						JOptionPane.INFORMATION_MESSAGE);
				this.viewingProfile.remove(myLogin);
			}
			if (m.getInstruction().equals(Constants.CHANGE_EMAIL_ACCEPTED)) {
				this.dispose();
				model.deleteObserver(this);
				JOptionPane.showMessageDialog(null, "Your Email has been updated", "Update success!",
						JOptionPane.INFORMATION_MESSAGE);
				this.viewingProfile.remove(myLogin);
			}
			if (m.getInstruction().equals(Constants.CHANGE_NAME_DECLINED)
					|| m.getInstruction().equals(Constants.CHANGE_EMAIL_DECLINED)
					|| m.getInstruction().equals(Constants.CHANGE_BIO_DECLINED)) {
				JOptionPane.showMessageDialog(null, m.getReason(), "Update failure", JOptionPane.WARNING_MESSAGE);
				this.viewingProfile.remove(myLogin);
			}
		}
	}
}
