package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.mail.handlers.image_gif;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;
import grimsby.networking.util.NoughtsCrossesMain;

/**
 * The Class NoughtsCrosses. Initialises the new window for the new game
 */
public class NoughtsCrosses extends JFrame implements Observer {

	/** The id of the game. */
	private String id;

	/** The Constant numberOfPuzzleTiles. */
	public static final int numberOfPuzzleTiles = 9;

	/** The buttons. */
	private JButton[] buttons;

	/** The chosen cells. */
	private int chosen[];

	/** The my number . */
	private int myNum;

	/** The oponent's number. */
	private int oponentNum;

	/** My picture. */
	private ImageIcon mPicture;

	/** The oponent's picture. */
	private ImageIcon oPicture;

	/** The games. */
	private HashMap<String, NoughtsCrossesMain> games;

	/** The my login. */
	private String myLogin;
	private ModifyPicture modifyPicture;

	/**
	 * Create the tic-tac-toe frame. Allows to play online with the other user
	 *
	 * @param queueOfCommands the queue of commands
	 * @param games the games currently alive
	 * @param id the id game i
	 * @param myLogin my login
	 * @param isFirst the flag saying who starts first
	 */
	public NoughtsCrosses(LinkedBlockingQueue<MessageUS> queueOfCommands, HashMap<String, NoughtsCrossesMain> games,
			String id, String myLogin, boolean isFirst, boolean bothNull) {
		super(id);
		this.id = id;
		this.games = games;
		this.myLogin = myLogin;
		this.chosen = new int[9];
		this.modifyPicture = new ModifyPicture();
		File mainProfilePhoto = new File("temporary Data/" + myLogin + "/" + myLogin + "main profile photo.jpg");
		File gameProfilePhoto = new File("temporary Data/" + myLogin + "/" + myLogin + "gaming profile photo.jpg");
		this.modifyPicture.resizeImage(mainProfilePhoto, gameProfilePhoto, 133, 133, "jpg");

		File opponentPhoto = new File("temporary Data/" + this.myLogin + "/Game/" + id + "Gaming photo.jpg");

		if (bothNull) {
			gameProfilePhoto = new File("o.jpg");
			opponentPhoto = new File("x.jpg");
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				games.remove(id);
				queueOfCommands.add(new MessageUS(Constants.END_TIC_TAC, id, ""));
				dispose();
			}
		});

		myNum = 1;

		oponentNum = 2;
		if (!isFirst) {
			myNum = 2;
			oponentNum = 1;
		}
		if ((!isFirst) && bothNull) {
			gameProfilePhoto = new File("x.jpg");
			opponentPhoto = new File("o.jpg");
		}
		try {
			mPicture = new ImageIcon(gameProfilePhoto.getAbsolutePath());
			mPicture.getImage().flush();
			oPicture = new ImageIcon(opponentPhoto.getAbsolutePath());
			oPicture.getImage().flush();
		} catch (Exception e) {
		}
		setSize(400, 400);

		getContentPane().setLayout(new GridLayout(3, 3));

		JPanel picturesPanel = new JPanel();
		picturesPanel.setBackground(Color.WHITE);

		buttons = new JButton[9];
		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();

			final int k = i;
			buttons[i].addActionListener(e -> {
				chosen[k] = myNum;
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
						MessageUS mess = new MessageUS(Constants.END_OF_GAME, id, id);
						mess.setPlayer((myNum == 1) ? true : false);
						mess.setX(k);
						mess.setReason(myLogin);
						queueOfCommands.offer(mess);

					}
				} else if (all == 9) {
					MessageUS mess = new MessageUS(Constants.END_OF_GAME, id, id);
					mess.setPlayer((myNum == 1) ? true : false);
					mess.setReason("both of us are winners!");
					queueOfCommands.offer(mess);
				} else {
					MessageUS mess = new MessageUS(Constants.MOVE_TIC_TAC, id, k);
					queueOfCommands.offer(mess);
				}
			});
			getContentPane().add(buttons[i]);
		}

		for (int i = 0; i < 9; i++)
			if (!isFirst)
				buttons[i].setEnabled(false);

		this.setVisible(true);
		this.setResizable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MessageUS) {
			MessageUS message = (MessageUS) arg;
			if (message.getInstruction().equals(Constants.MOVE_TIC_TAC)) {
				chosen[message.getX()] = oponentNum;
				buttons[message.getX()].setIcon(oPicture);
				buttons[message.getX()].setDisabledIcon(oPicture);
				buttons[message.getX()].setVisible(true);

				for (int i = 0; i < 9; i++) {
					buttons[i].setEnabled(true);
					if (chosen[i] != 0)
						buttons[i].setEnabled(false);
				}
			} else if (message.getInstruction().equals(Constants.END_OF_GAME)) {
				if ((message.isPlayer1() ? 1 : 2) != myNum) {
					buttons[message.getX()].setIcon(oPicture);
					buttons[message.getX()].setDisabledIcon(oPicture);
					buttons[message.getX()].setVisible(true);
				}
				JOptionPane.showMessageDialog(null, "Game is over. The winner is.... : " + message.getReason(),
						"Game with " + id + " has ended", JOptionPane.INFORMATION_MESSAGE);

				this.dispose();
				games.remove(id);
			} else if (message.getInstruction().equals(Constants.END_TIC_TAC)) {
				for (int i = 0; i < 9; i++)
					buttons[i].setEnabled(false);

				JOptionPane.showMessageDialog(null, "Your oppenont gave up! You are the winner!" + message.getReason(),
						"Game with " + id + " has ended", JOptionPane.INFORMATION_MESSAGE);
				games.remove(id);
				this.dispose();
			}
		}
	}

	/**
	 * Finds whether there is a winner. If there is, return the name of the
	 * winner
	 *
	 * @return the name of the winner
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
		if (num == myNum)
			return myLogin;
		return id;

	}
}