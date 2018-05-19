package grimsby.networking.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class ClientFromServer. Receives messages from the server
 */
public class ClientFromServer extends Thread {

	/** The input stream. */
	private ObjectInputStream fromServer;

	/** The queue of answers. */
	private BlockingQueue<MessageUS> queueOfAnswers;

	/** The client. */
	private Client client;

	/**
	 * Instantiates a new client from server.
	 *
	 * @param fromServer the from server
	 * @param queueOfAnswers the queue of answers
	 * @param sender the sender
	 * @param client the client
	 */
	public ClientFromServer(ObjectInputStream fromServer, BlockingQueue<MessageUS> queueOfAnswers, Client client) {
		this.fromServer = fromServer;
		this.queueOfAnswers = queueOfAnswers;
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			try {
				MessageUS message = (MessageUS) fromServer.readObject();
				System.out.println("new instruction : "+message.getInstruction());
				queueOfAnswers.offer(message);
			} catch (SocketException e) {
				queueOfAnswers.add(new MessageUS(Constants.CONNECTION_WITH_SERVER_BROKEN));
				try {
					fromServer.close();
				} catch (IOException e1) {
				}
				client.interrupt();
				break;
			} catch (NullPointerException e) {
				queueOfAnswers.offer(new MessageUS(Constants.CONNECTION_WITH_SERVER_BROKEN));
				try {
					fromServer.close();
				} catch (IOException e1) {
				} finally {
					client.interrupt();
					break;
				}
			} catch (ClassNotFoundException | IOException e) {
				try {
					queueOfAnswers.offer(new MessageUS(Constants.CONNECTION_WITH_SERVER_BROKEN));
					fromServer.close();
					client.interrupt();
				} catch (IOException e1) {
				}
				break;
			}
		}
	}

}
