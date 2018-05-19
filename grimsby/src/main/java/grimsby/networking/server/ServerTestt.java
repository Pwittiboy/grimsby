package grimsby.networking.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

public class ServerTestt {

	// @Test
	public void createAccounts() {
		for (int i = 0; i < 1100; i++) {
			try {
				Socket socket = new Socket("localhost", Constants.SOCKET_NUMBER);
				ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

				toServer.writeObject(new MessageUS(Constants.CREATE_ACCOUNT,
						new String[] { "dummy" + i, "password" + i, i + "dnakjs@sadhsdc.sd" }));

				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					MessageUS m = (MessageUS) fromServer.readObject();
				} catch (ClassNotFoundException e) {
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	// @Test
	public void testHeavyLoggingPublicMessages() {
		List<Thread> threads = new ArrayList<>();

		for (int i = 111; i < 255; i++) {
			final int k = i;
			threads.add(new Thread(() -> {
				try {
					Socket socket = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
					ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

					toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "dummy" + k, "password" + k }));

					try {
						MessageUS val = (MessageUS) fromServer.readObject();
						while (!val.getInstruction().equals(Constants.ACCEPTED_LOGIN)) {
							val = (MessageUS) fromServer.readObject();
						}
						toServer.writeObject(new MessageUS(Constants.PUBLIC_MESSAGE, "testing hard", "dummy" + k, ""));
						while (!val.getInstruction().equals(Constants.RECEIVE_PUBLIC_MESSAGE)) {
							val = (MessageUS) fromServer.readObject();
						}

					} catch (ClassNotFoundException e) {
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
		}
		threads.stream().forEach(Thread::start);
		threads.stream().forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e1) {
			}
		});
	}

	// @Test
	public void testHeavyLoggingNotConnecting() {
		List<Thread> threads = new ArrayList<>();

		for (int i = 100; i < 250; i++) {
			final int k = i;
			threads.add(new Thread(() -> {
				try {
					Socket socket = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
					ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

					toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "dummy" + k, "password" }));

					try {
						MessageUS val = (MessageUS) fromServer.readObject();
						while (!val.getInstruction().equals(Constants.ACCEPTED_LOGIN))
							val = (MessageUS) fromServer.readObject();

						try {
							Thread.currentThread().sleep(60000);
						} catch (InterruptedException e) {
						}

						if (k % 2 == 0)
							while (val.getInstruction().equals(Constants.DECLINED_CONNECT))
								val = (MessageUS) fromServer.readObject();

						else {
							toServer.writeObject(new MessageUS(Constants.DECLINED_CONNECT, "dummy" + (k - 1) + "", ""));

							while (val.getInstruction().equals(Constants.DECLINED_CONNECT))
								val = (MessageUS) fromServer.readObject();
						}

					} catch (ClassNotFoundException e) {
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
		}
		threads.stream().forEach(Thread::start);
		threads.stream().forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e1) {
			}
		});

	}

	@Test
	public void testHeavyLoggingConnectingChatting() {
		List<Thread> threads = new ArrayList<>();
		for (int i = 2; i < 20; i++) {
			final int k = i;
			threads.add(new Thread(() -> {
				try {
					Socket socket = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
					ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

					toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "dummy" + k, "password" + k }));

					try {
						MessageUS val = (MessageUS) fromServer.readObject();
						while (!val.getInstruction().equals(Constants.ACCEPTED_LOGIN))
							val = (MessageUS) fromServer.readObject();

						try {
							Thread.currentThread().sleep(20000);
						} catch (InterruptedException e) {
						}

						if (k % 2 == 0) {
							while (!val.getInstruction().equals(Constants.ACCEPTED_CONNECT))
								val = (MessageUS) fromServer.readObject();

							for (int p = 0; p < 5; p++)
								toServer.writeObject(
										new MessageUS(Constants.SEND_MESSAGE, "user1", "dummy" + (k + 1), val.getID()));

						} else {
							toServer.writeObject(new MessageUS(Constants.ACCEPTED_CONNECT, "dummy" + (k - 1), ""));
							while (!val.getInstruction().equals(Constants.ACCEPTED_CONNECT)
									&& !val.getInstruction().equals(Constants.DECLINED_CONNECT))
								val = (MessageUS) fromServer.readObject();

							for (int p = 0; p < 5; p++)
								toServer.writeObject(
										new MessageUS(Constants.SEND_MESSAGE, "user2", "dummy" + k, val.getID()));
						}

					} catch (ClassNotFoundException e) {
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.currentThread().sleep(20000);
				} catch (InterruptedException e) {
				}
			}));
		}
		threads.stream().forEach(Thread::start);
		threads.stream().forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e1) {
			}
		});
	}

}
