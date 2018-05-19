package grimsby.networking.client;

import java.util.concurrent.BlockingQueue;

import grimsby.networking.util.MessageUS;
import view.Model;

/**
 * The Class ClientWorker. Takes the answers from the server and gives the tasks
 * to the model
 */
public class ClientWorker extends Thread {

	/** The queue of answers. */
	private BlockingQueue<MessageUS> queueOfAnswers;

	/** The model. */
	private Model model;

	/**
	 * Instantiates a new client worker.
	 *
	 * @param queueOfAnswers the queue of answers
	 * @param queueOfTasks the queue of tasks
	 * @param model the model
	 */
	public ClientWorker(BlockingQueue<MessageUS> queueOfAnswers, Model model) {
		this.queueOfAnswers = queueOfAnswers;
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			MessageUS c = null;
			try {
				c = queueOfAnswers.take();
				model.SetChangeGui(c);
			} catch (InterruptedException e) {

			}
		}
	}
}