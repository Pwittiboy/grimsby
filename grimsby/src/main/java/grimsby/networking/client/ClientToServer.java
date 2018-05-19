package grimsby.networking.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class ClientToServer. Sends messages tot he server
 */
public class ClientToServer extends Thread {

	/** The output stream. */
	private ObjectOutputStream toServer;

	/** The queue of commands. */
	private BlockingQueue<MessageUS> queueOfCommands;

	/** The client. */
	private Client client;

	/**
	 * Instantiates a new client to server.
	 *
	 * @param toServer the to server
	 * @param queueOfCommands the queue of commands
	 * @param client the client
	 */
	public ClientToServer(ObjectOutputStream toServer, BlockingQueue<MessageUS> queueOfCommands, Client client) {
		this.queueOfCommands = queueOfCommands;
		this.toServer = toServer;
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		MessageUS com = null;

		while (true) {
			try {
				com = queueOfCommands.take();
				toServer.writeObject(com);
				toServer.flush();
				if (com.getInstruction().equals(Constants.END)) {
					toServer.close();
					client.interrupt();
				}

			} catch (InterruptedException e) {
				System.out.println("breaking sender");
				try {
					toServer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
