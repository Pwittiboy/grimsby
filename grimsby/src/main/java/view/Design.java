package view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JFrame;

/**
 * Class to help easily redesign the application.
 *
 * @author Muhammad
 */
public class Design {

	/** The Constant GREEN_BUTTON. */
	public final static Color GREEN_BUTTON = new Color(140, 236, 67);

	/** The Constant BLUE_BORDER. */
	public final static Color BLUE_BORDER = new Color(203, 218, 239);

	/** The Constant BLUE_FIELD. */
	public final static Color BLUE_FIELD = new Color(236, 249, 255);

	/** The Constant BLUE_TEXT. */
	public final static Color BLUE_TEXT = new Color(168, 189, 189);

	/** The Constant PURPLE_BUTTON. */
	public final static Color PURPLE_BUTTON = new Color(115, 19, 188);

	/** The Constant DARK_BORDER. */
	public final static Color DARK_BORDER = new Color(133, 133, 133);

	/** The Constant DARK_FIELD. */
	public final static Color DARK_FIELD = new Color(149, 139, 152);

	/** The Constant DARK_TEXT. */
	public final static Color DARK_TEXT = new Color(216, 207, 223);

	/** The Constant DARK_BG. */
	public final static Color DARK_BG = new Color(41, 41, 41);

	/** The Constant DARK_CHATBOX. */
	public final static Color DARK_CHATBOX = new Color(51, 51, 51);

	/** The theme. */
	public static int theme;

	/**
	 * Allows a user to set the theme. At the moment, if atheme = 1, it returns
	 * the dark theme. Any other numbers return the default. However, this
	 * allows for easily expanding into more themes
	 * 
	 * @param atheme Integer representing the theme wanted
	 */
	public static void setTheme(int atheme) {
		if (atheme == 1)
			theme = 1;
		else
			theme = 0;
	}

	/**
	 * Modifies the text field colours. This includes the colours of the text
	 * field border, background and, if not the default theme, the text also.
	 * 
	 * @param txt The text field which needs design
	 */
	public static void designField(JTextField txt) {
		if (theme == 1) {
			txt.setBorder(new LineBorder(DARK_BORDER));
			txt.setBackground(DARK_FIELD);
			txt.setForeground(Color.WHITE);
		} else {
			txt.setBorder(new LineBorder(BLUE_BORDER));
			txt.setBackground(BLUE_FIELD);
		}
	}

	/**
	 * Modifies the colours of the primary button (i.e. the button most likely
	 * to be clicked on the screen). This modifies the text, background and
	 * removes borders.
	 * 
	 * @param btn Button that needs designing
	 */
	public static void primaryButton(JButton btn) {
		if (theme == 1) {
			btn.setForeground(Color.WHITE);
			btn.setBorder(null);
			btn.setBackground(PURPLE_BUTTON);
		} else {
			btn.setForeground(Color.WHITE);
			btn.setBorder(null);
			btn.setBackground(GREEN_BUTTON);
		}
	}

	/**
	 * Modifies the colours of the secondary button (i.e. the buttons less
	 * likely to be clicked on the screen). This modifies the text, background
	 * and removes the borders.
	 * 
	 * @param btn The button that needs designing
	 */
	public static void secondaryButton(JButton btn) {
		if (theme == 1) {
			btn.setForeground(Color.WHITE);
			btn.setBorder(null);
			btn.setBackground(PURPLE_BUTTON);
		} else {
			btn.setForeground(Color.WHITE);
			btn.setBorder(null);
			btn.setBackground(GREEN_BUTTON);
		}
	}

	/**
	 * Changes the text colour (here for future theme implementations)
	 * 
	 * @param label The text that needs designing
	 */
	public static void textColour(JLabel label) {
		if (theme == 1) {
			label.setForeground(BLUE_TEXT);
		} else {
			label.setForeground(BLUE_TEXT);
		}
	}

	/**
	 * Changes the text colour on main screen for the login of the user under
	 * his profile. This allows the user to easily distinguish where his profile
	 * is located.
	 * 
	 * @param label The text that needs designing
	 */
	public static void myTextColour(JLabel label) {
		if (theme == 1) {
			label.setForeground(Color.WHITE);
		} else {
			label.setBackground(Color.BLACK);
		}
	}

	/**
	 * Changes the text colour of a user's bio in the profile. TextArea used as
	 * labels were not sufficient.
	 * 
	 * @param label The label that needs designing
	 */
	public static void textColour(JTextArea label) {
		if (theme == 1) {
			label.setForeground(BLUE_TEXT);
		} else {
			label.setForeground(BLUE_TEXT);
		}
	}

	/**
	 * Changes the background colour of a JPanel
	 * 
	 * @param panel Panel that needs a design
	 */
	public static void backgroundColour(JPanel panel) {
		if (theme == 1) {
			panel.setBackground(DARK_BG);
			panel.setBorder(null);
		} else {
			panel.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour of a JFrame
	 * 
	 * @param frame Frame that needs a design
	 */
	public static void backgroundColour(JFrame frame) {
		if (theme == 1) {
			frame.setBackground(DARK_BG);
		} else {
			frame.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour of a menu bar
	 * 
	 * @param menuBar menuBar that needs a design
	 */
	public static void backgroundColour(JMenuBar menuBar) {
		if (theme == 1) {
			menuBar.setBackground(DARK_BG);
			menuBar.setForeground(Color.WHITE);
		} else {
			menuBar.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour of a text pane
	 * 
	 * @param textPane textPane that needs a design
	 */
	public static void backgroundColour(JTextPane textPane) {
		if (theme == 1) {
			textPane.setBackground(DARK_BG);
			textPane.setForeground(DARK_TEXT);
		} else {
			textPane.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour of a layered pane
	 * 
	 * @param layeredPane layeredPane that needs a design
	 */
	public static void backgroundColour(JLayeredPane layeredPane) {
		if (theme == 1) {
			layeredPane.setBackground(DARK_BG);
		} else {
			layeredPane.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour of a scroll pane
	 * 
	 * @param scrollPane scrollPane that needs a design
	 */
	public static void backgroundColour(JScrollPane scrollPane) {
		if (theme == 1) {
			scrollPane.setBackground(DARK_BG);
			scrollPane.setBorder(new MatteBorder(1, 1, 1, 0, (Color) DARK_CHATBOX));
		} else {
			scrollPane.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the colours for the information a user provides on his profile
	 * 
	 * @param lbl JLabel that needs a design
	 */
	public static void profileLabels(JLabel lbl) {
		if (theme == 1) {
			lbl.setForeground(BLUE_TEXT);
		} else {
			lbl.setForeground(Color.BLACK);
		}
	}

	/**
	 * Sets the colours for the profile questions above the text fields
	 * 
	 * @param lbl JLabel that needs a design
	 */
	public static void profileQs(JLabel lbl) {
		if (theme == 1) {
			lbl.setForeground(Color.GRAY);
		} else {
			lbl.setForeground(Color.GRAY);
		}
	}

	/**
	 * Sets the background colour for a user's profile
	 * 
	 * @param profile Profile that needs a design
	 */
	public static void profileBackground(Profile profile) {
		if (theme == 1) {
			profile.setBackground(new Color(232, 239, 241));
		} else {

		}
	}

	/**
	 * Sets the background colour for a user's profile settings page
	 * 
	 * @param profileSettings The profile settings window that need a design
	 */
	public static void profileBackground(ProfileSettings profileSettings) {
		if (theme == 1) {
			profileSettings.setBackground(new Color(232, 239, 241));
		} else {

		}
	}

	/**
	 * Sets the background colour for the chatbox
	 * 
	 * @param chat The text pane that needs a design
	 */
	public static void setChatBackground(JTextPane chat) {
		if (theme == 1) {
			chat.setBackground(DARK_BG);
			chat.setForeground(Color.WHITE);
			chat.setBorder(new MatteBorder(1, 1, 1, 0, (Color) DARK_CHATBOX));
		} else {
			chat.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the background colour for the lists (e.g. users)
	 * 
	 * @param list The JList that needs a design
	 */
	public static void backgroundColour(JList<String> list) {
		if (theme == 1) {
			list.setBackground(DARK_BG);
			list.setForeground(Color.WHITE);
		} else {
			list.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the background colour of each chat JPanel to align with the chatbox
	 * 
	 * @param panel The JPanel that needs a design
	 */
	public static void backgroundChatColour(JPanel panel) {
		if (theme == 1) {
			panel.setBackground(DARK_CHATBOX);
			panel.setForeground(Color.WHITE);
			panel.setBorder(new LineBorder(DARK_CHATBOX));
		} else {
			panel.setBorder(new MatteBorder(1, 1, 0, 1, (Color) Color.LIGHT_GRAY));
			panel.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the background colour of the text field where the user types into on
	 * each chatbox
	 * 
	 * @param field Field that needs a design
	 */
	public static void backgroundChatColour(JTextField field) {
		if (theme == 1) {
			field.setBorder(new MatteBorder(1, 1, 1, 0, (Color) DARK_CHATBOX));
			field.setBackground(DARK_BG);
			field.setForeground(Color.WHITE);
		} else {
			field.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(192, 192, 192)));
			field.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the border on the list of users
	 * 
	 * @param scrollUsers The list of users that need a design
	 */
	public static void scrollBorder(JScrollPane scrollUsers) {
		if (theme == 1) {
			scrollUsers.setBorder(new LineBorder(Color.DARK_GRAY));
		} else {
			scrollUsers.setBorder(new LineBorder(Color.LIGHT_GRAY));
		}
	}

	/**
	 * Sets the colour of each item on a menu. This includes the background of
	 * the particular item and the text
	 * 
	 * @param item The item that needs a design
	 */
	public static void menuText(JMenu item) {
		if (theme == 1) {
			item.setForeground(Color.WHITE);
			item.setBackground(DARK_BG);
		} else {
			item.setForeground(Color.BLACK);
			item.setBackground(Color.WHITE);
		}
	}

	/**
	 * Sets the colour of each item on a submenu. This includes the background
	 * of the particular item and the text
	 * 
	 * @param item The item that needs a design
	 */
	public static void menuText(JMenuItem item) {
		if (theme == 1) {
			item.setForeground(Color.WHITE);
			item.setBackground(DARK_BG);
		} else {
			item.setForeground(Color.BLACK);
			item.setBackground(Color.WHITE);
		}
	}

	/**
	 * Changes the background colour on the CreateChat.java window, so that it
	 * follows the colour scheme
	 * 
	 * @param list The list that needs a design
	 */
	public static void backgroundColourCreateChat(JList<String> list) {
		if (theme == 1) {
			list.setBackground(DARK_FIELD);
		} else {
			list.setBackground(BLUE_FIELD);
		}
	}

	/**
	 * Sets the colours of a chatbox
	 * 
	 * @param chat Jtextarea that needs a design
	 */
	public static void setChatBackground(JTextArea chat) {
		if (theme == 1) {
			chat.setBackground(DARK_BG);
			chat.setForeground(Color.WHITE);
			chat.setBorder(new MatteBorder(1, 1, 1, 0, (Color) DARK_CHATBOX));
		} else {
			chat.setBackground(Color.WHITE);
		}
	}
}
