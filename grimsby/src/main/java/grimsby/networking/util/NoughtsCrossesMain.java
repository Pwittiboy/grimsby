package grimsby.networking.util;

import java.util.Observable;

import org.omg.CORBA.ShortSeqHelper;

/**
 * The Class NoughtsCrossesMain. Creates a new games of tic tac toe.
 */
public class NoughtsCrossesMain extends Observable {

	/**
	 * Oponent's move.
	 *
	 * @param message the message
	 */
	public void oponentMove(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}

	/**
	 * End game.
	 *
	 * @param message the message
	 */
	public void endGame(MessageUS message) {
		setChanged();
		notifyObservers(message);
	}
}
