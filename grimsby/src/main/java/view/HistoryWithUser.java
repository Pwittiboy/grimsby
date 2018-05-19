package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import grimsby.networking.util.MessageObject;

public class HistoryWithUser extends JFrame {

	private JPanel contentPane;
	private EmojiManage emojiManage;

	public HistoryWithUser(String[][] history, EmojiManage emojiManage) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.emojiManage = emojiManage;
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 450 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0 };
		contentPane.setLayout(gbl_contentPane);

		JScrollPane scrollPane = new JScrollPane();
		Design.scrollBorder(scrollPane);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);

		JTextPane textPane = new JTextPane();

		for (int i = 0; i < history.length; i++) {
			String[] textArray = history[i][2].split(" ");
			ArrayList<MessageObject> SplitedText = splitEmojiAndString(textArray);
			try {
				textPane.getDocument().insertString(textPane.getDocument().getLength(),
						history[i][1] + " < " + history[i][0] + " > :  ", null);
			} catch (BadLocationException e2) {
			}

			SplitedText.forEach(e -> {
				if (e.isEmoji()) {
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File(this.emojiManage.getEmojiPath(e.getMessage())));
						StyledDocument document = (StyledDocument) textPane.getDocument();
						Style style = document.addStyle("StyleName", null);
						StyleConstants.setIcon(style, new ImageIcon(img));
						document.insertString(document.getLength(), " ", style);
					} catch (BadLocationException | IOException e1) {
						System.err.println("Could not insert the message");
					}

				} else {
					try {
						textPane.getDocument().insertString(textPane.getDocument().getLength(), e.getMessage() + " ",
								null);
					} catch (BadLocationException e1) {
						System.err.println("Could not insert the message");
					}
				}
			});
			try {
				textPane.getDocument().insertString(textPane.getDocument().getLength(), "\n", null);
			} catch (BadLocationException e1) {
			}
		}

		Design.setChatBackground(textPane);
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		setVisible(true);
	}

	/**
	 * Split emoji and string. Indicates emojis in the message
	 *
	 * @param StringArray the string array of the message text
	 * @return the array list of MessageObject which contains all the words
	 * received with message, and indicators saying whether the string is the
	 * emoji or not
	 */
	public ArrayList<MessageObject> splitEmojiAndString(String[] StringArray) {
		ArrayList<MessageObject> splitedString = new ArrayList<>();

		for (int i = 0; i < StringArray.length; i++) {
			if (StringArray[i].length() <= 3)
				splitedString.add(new MessageObject(StringArray[i], false));
			else {
				if (StringArray[i].substring(0, 3).equals("/=?")) {
					if (this.emojiManage.isEmoji(StringArray[i].substring(3, StringArray[i].length())))
						splitedString
								.add(new MessageObject(StringArray[i].substring(3, StringArray[i].length()), true));
					else
						splitedString
								.add(new MessageObject(StringArray[i].substring(3, StringArray[i].length()), false));
				} else
					splitedString.add(new MessageObject(StringArray[i], false));
			}
		}
		return splitedString;
	}

}