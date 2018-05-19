package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import grimsby.networking.util.MessageUS;
import grimsby.networking.util.AI;
import grimsby.networking.util.NoughtsCrossesMain;

/**
 * The Class NoughtsCrossesWithAI. Creates a new tic tac toe game agains
 * computer
 */
public class NoughtsCrossesWithAI extends JFrame {

	/** The game id. */
	private String id;

	/** The Constant numberOfPuzzleTiles. */
	public static final int numberOfPuzzleTiles = 9;

	/** The buttons. */
	private JButton[] buttons;

	/** The chosen. */
	private int chosen[];

	/** The my picture. */
	private ImageIcon mPicture;

	/** The computer's picture. */
	private ImageIcon oPicture;
	private ModifyPicture modifyPicture;

	/** The games. */
	private HashMap<String, NoughtsCrossesMain> games;

	/** The computer. */
	private AI computer;

	/** The my login. */
	private String myLogin;

	/** The computer option. */
	private JMenuItem computerOption;

	private int myNumber;
	private int computerNumber;

	/**
	 * Create the tic-tac-toe frame.
	 *
	 * @param queueOfCommands the queue of commands
	 * @param games the games
	 * @param id the id of the game(used parner's id)
	 * @param myLogin the my login
	 * @param computerOption the computer option
	 */
	public NoughtsCrossesWithAI(LinkedBlockingQueue<MessageUS> queueOfCommands,
			HashMap<String, NoughtsCrossesMain> games, String id, String myLogin, JMenuItem computerOption,
			boolean isFirstPlayer) {
		super("Game against *Computer*");
		this.games = games;
		this.modifyPicture = new ModifyPicture();
		this.computerOption = computerOption;
		this.id = id;
		this.myLogin = myLogin;
		this.chosen = new int[9];
		computer = new AI(isFirstPlayer);

		myNumber = 1;
		computerNumber = 2;
		if (!isFirstPlayer) {
			myNumber = 2;
			computerNumber = 1;
		}

		File mainProfilePhoto = new File("temporary Data/" + myLogin + "/" + myLogin + "main profile photo.jpg");
		File gameProfilePhoto = new File("temporary Data/" + myLogin + "/" + myLogin + "gaming profile photo.jpg");

		this.modifyPicture.resizeImage(mainProfilePhoto, gameProfilePhoto, 133, 133, "jpg");
		new ImageIcon(gameProfilePhoto.getAbsolutePath()).getImage().flush();
		File computerP = new File("Computer.jpg");
		this.modifyPicture.resizeImage(computerP, computerP, 133, 133, "jpg");

		try {
			mPicture = new ImageIcon(gameProfilePhoto.getAbsolutePath());
			oPicture = new ImageIcon(computerP.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSize(400, 400);
		this.setLayout(new GridLayout(3, 3));
		JPanel picturesPanel = new JPanel();
		picturesPanel.setBackground(Color.WHITE);
		buttons = new JButton[9];
		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();

			final int k = i;
			buttons[i].addActionListener(e -> game(k));
			this.add(buttons[i]);
		}

		this.setVisible(true);
		this.setResizable(false);
		if (!isFirstPlayer) {
			for (int i = 0; i < 9; i++)
				buttons[i].setEnabled(false);
			int p = computer.findMove(chosen);
			chosen[p] = computerNumber;
			buttons[p].setIcon(oPicture);
			buttons[p].setDisabledIcon(oPicture);
			buttons[p].setVisible(true);
			for (int i = 0; i < 9; i++)
				if (chosen[i] == 0)
					buttons[i].setEnabled(true);

		}

	}

	/**
	 * Find winner. Finds winner name if there is a winner. Returns the empty
	 * string if there is no winner
	 *
	 * @return the name of the winner or empty string
	 */
	private String findWinner() {
		boolean winner = false;
		int num = -1;

		if (chosen[0] == chosen[4] && chosen[4] == chosen[8] && chosen[0] != 0) {
			winner = true;
			num = chosen[0];
		} else if (chosen[2] == chosen[4] && chosen[4] == chosen[6] && chosen[2] != 0) {
			winner = true;
			num = chosen[2];
		} else if (chosen[0] == chosen[1] && chosen[1] == chosen[2] && chosen[0] != 0) {
			winner = true;
			num = chosen[0];
		} else if (chosen[3] == chosen[4] && chosen[4] == chosen[5] && chosen[3] != 0) {
			winner = true;
			num = chosen[3];
		} else if (chosen[6] == chosen[7] && chosen[7] == chosen[8] && chosen[6] != 0) {
			winner = true;
			num = chosen[6];
		} else if (chosen[0] == chosen[3] && chosen[3] == chosen[6] && chosen[0] != 0) {
			winner = true;
			num = chosen[0];
		} else if (chosen[1] == chosen[4] && chosen[4] == chosen[7] && chosen[1] != 0) {
			winner = true;
			num = chosen[1];
		} else if (chosen[2] == chosen[5] && chosen[5] == chosen[8] && chosen[2] != 0) {
			winner = true;
			num = chosen[2];
		}

		if (!winner || num == -1)
			return "";
		if (num == myNumber)
			return myLogin;
		return id;
	}

	/**
	 * Close operatio
	 */
	private void close() {
		games.remove("*AI*");
		computerOption.setEnabled(true);
		dispose();

	}

	/**
	 * Game. Declares the game
	 *
	 * @param k the index of the button
	 */
	private void game(int k) {
		chosen[k] = myNumber;
		buttons[k].setIcon(mPicture);
		buttons[k].setDisabledIcon(mPicture);
		buttons[k].setVisible(true);
		int all = 0;
		for (int t = 0; t < 9; t++)
			if (chosen[t] != 0)
				all++;

		for (int l = 0; l < 9; l++)
			buttons[l].setEnabled(false);

		String winner = findWinner();
		if (!winner.equals("")) {
			if (winner.equals(myLogin)) {
				games.remove(id);
				for (int p = 0; p < 9; p++)
					buttons[p].setEnabled(false);
				JOptionPane.showMessageDialog(null, "Congratulations! You beat the computer!");

				this.close();
			}
		} else if (all == 9) {
			games.remove(id);
			JOptionPane.showMessageDialog(null, "Not bad! You have draw with computer");

			this.close();
		} else {
			for (int p = 0; p < 9; p++)
				buttons[p].setEnabled(false);

			int q = computer.findMove(chosen);
			chosen[q] = computerNumber;
			buttons[q].setEnabled(false);
			buttons[q].setIcon(oPicture);
			buttons[q].setDisabledIcon(oPicture);

			String computerIsWinner = findWinner();
			if (computerIsWinner.equals("*AI*")) {
				games.remove(id);
				JOptionPane.showMessageDialog(null, "You lost! try again..");
				this.close();
			} else if (all + 1 == 9) {
				games.remove(id);
				JOptionPane.showMessageDialog(null, "Not bad! You have draw with computer");
				this.close();
			} else
				for (int p = 0; p < 9; p++) {
					buttons[p].setEnabled(true);
					if (chosen[p] != 0)
						buttons[p].setEnabled(false);
				}
		}
	}

}