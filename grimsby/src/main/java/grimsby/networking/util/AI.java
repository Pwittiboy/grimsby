package grimsby.networking.util;

/**
 * The Class AI. Created for tic tac toe game against computer
 */
public class AI {
	private int compNum;
	private boolean isFirst;

	public AI(boolean isFirst) {
		compNum = 2;
		if (!isFirst)
			compNum = 1;
	}

	/**
	 * Find move. Finds the optimal move for the AI
	 *
	 * @param board the current board
	 * @return the next move for the AI
	 */
	public int findMove(int[] board) {
		return miniMax(board, isFirst, compNum).position;
	}

	/**
	 * Mini max algorithm. Finds the most optimal way to make the next move for
	 * min. The algorithm assumes that the opponent plays optimally.
	 *
	 * @param board the tic tac toe board
	 * @param max indicates whether it is max turn or not
	 * @return the move
	 */
	private Move miniMax(int[] board, boolean max, int playersNum) {
		int all = 0;

		for (int i = 0; i < board.length; i++)
			if (board[i] != 0)
				all++;

		int nextPlayer;
		if (playersNum == 1)
			nextPlayer = 2;
		else
			nextPlayer = 1;

		int ans = winLost(board, nextPlayer);
		if (max)
			ans = -1 * ans;
		if (all == 9 || ans != 0)
			return new Move(-1, ans, 0);

		if (max) {
			int bestVal = Integer.MIN_VALUE;
			int bestSpot = -1;
			int counter = Integer.MAX_VALUE;
			for (int i = 0; i < board.length; i++) {
				if (board[i] != 0)
					continue;
				board[i] = playersNum;
				Move value = miniMax(board, !max, nextPlayer);
				if (value.score > bestVal || (value.score == bestVal && value.counter < counter)) {
					bestVal = value.score;
					bestSpot = i;
					counter = value.counter;
				}
				board[i] = 0;
			}
			return new Move(bestSpot, bestVal, counter + 1);
		} else {
			int bestVal = Integer.MAX_VALUE;
			int bestSpot = -1;
			int counter = Integer.MAX_VALUE;
			for (int i = 0; i < board.length; i++) {
				if (board[i] != 0)
					continue;
				board[i] = playersNum;
				Move value = miniMax(board, !max, nextPlayer);
				if (value.score < bestVal || (value.score == bestVal && value.counter < counter)) {
					bestVal = value.score;
					bestSpot = i;
					counter = value.counter;
				}
				board[i] = 0;
			}
			return new Move(bestSpot, bestVal, counter + 1);
		}
	}

	/**
	 * Win or lost. Sends the answer who win/lost r result was equal as the
	 * particular stage.
	 *
	 * @param board the current stage of the game
	 * @return the number depicting who won at this stage. 1 if the oponent
	 * (max), -1 if computer (min) and 0 if nobody won
	 */
	private int winLost(int[] board, int me) {
		boolean winner = false;
		int num = -1;

		if (board[0] == board[4] && board[4] == board[8] && board[0] != 0) {
			winner = true;
			num = board[0];
		} else if (board[2] == board[4] && board[4] == board[6] && board[2] != 0) {
			winner = true;
			num = board[2];
		} else if (board[0] == board[1] && board[1] == board[2] && board[0] != 0) {
			winner = true;
			num = board[0];
		} else if (board[3] == board[4] && board[4] == board[5] && board[3] != 0) {
			winner = true;
			num = board[3];
		} else if (board[6] == board[7] && board[7] == board[8] && board[6] != 0) {
			winner = true;
			num = board[6];
		} else if (board[0] == board[3] && board[3] == board[6] && board[0] != 0) {
			winner = true;
			num = board[0];
		} else if (board[1] == board[4] && board[4] == board[7] && board[1] != 0) {
			winner = true;
			num = board[1];
		} else if (board[2] == board[5] && board[5] == board[8] && board[2] != 0) {
			winner = true;
			num = board[2];
		}

		if (!winner || num == -1)
			return 0;
		if (me == num)
			return 1;
		else
			return -1;
	}
}

class Move {
	int position;
	int score;
	int counter;

	public Move(int position, int score, int counter) {
		this.position = position;
		this.score = score;
		this.counter = counter;
	}

}
