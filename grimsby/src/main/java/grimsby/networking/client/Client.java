package grimsby.networking.client;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;
import view.Model;

/**
 * The Class Client. Creates two thread: one for listening server, another for
 * sending messages to the server
 */
public class Client extends Thread {

	/** The server socket. */
	private Socket server;

	/** The output stream to write to the server. */
	private ObjectOutputStream toServer;

	/** The input stream to listen from the server. */
	private ObjectInputStream fromServer;

	/** The queue of commands. */
	private BlockingQueue<MessageUS> queueOfCommands;

	/** The queue of answers. */
	private BlockingQueue<MessageUS> queueOfAnswers;

	/** The model. */
	private Model model;

	/**
	 * Instantiates a new client.
	 *
	 * @param serverName the server name
	 * @param queueOfCommands the queue of commands
	 * @param queueOfAnswers the queue of answers
	 * @param model the model
	 */

	public Client(String serverName, BlockingQueue<MessageUS> queueOfCommands, BlockingQueue<MessageUS> queueOfAnswers,
			Model model) {
		try {
			server = new Socket(serverName, Constants.SOCKET_NUMBER);
			toServer = new ObjectOutputStream(server.getOutputStream());
			fromServer = new ObjectInputStream(server.getInputStream());
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		this.queueOfCommands = queueOfCommands;
		this.queueOfAnswers = queueOfAnswers;
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		ClientToServer sender = new ClientToServer(toServer, queueOfCommands, this);
		ClientWorker worker = new ClientWorker(queueOfAnswers, model);
		ClientFromServer receiver = new ClientFromServer(fromServer, queueOfAnswers, this);

		sender.start();
		receiver.start();
		worker.start();
		while (true) {
			if (this.isInterrupted()) {
				System.out.println("ending...");
				sender.interrupt();
				receiver.interrupt();
				worker.interrupt();
				break;
			}
		}
	}
}
