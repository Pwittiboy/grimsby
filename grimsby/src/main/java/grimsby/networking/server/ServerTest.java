package grimsby.networking.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import grimsby.libraries.dataaccess.dao.factory.DAOFactory;
import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

public class ServerTest {

	Socket server = null;
	ObjectOutputStream toServer = null;
	ObjectInputStream fromServer = null;

	@Before
	public void setUp() {
		// server should be already running
		try {
			server = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
			toServer = new ObjectOutputStream(server.getOutputStream());
			fromServer = new ObjectInputStream(server.getInputStream());
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	@Test
	public void testLogin1() {
		String[] accountInfo = { "samcarr17", "password17" };
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, accountInfo));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS message = (MessageUS) fromServer.readObject();
			MessageUS message2 = (MessageUS) fromServer.readObject();

			
			assertEquals(Constants.GET_MY_TOP, message.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, message2.getInstruction());
			
			message2 = (MessageUS) fromServer.readObject();

			assertEquals(Constants.LIST_CHANGED, message2.getInstruction());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLogin2() {
		String[] accountInfo = { "srDYTFU", "E^Tygu" };
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, accountInfo));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS message = (MessageUS) fromServer.readObject();
			assertEquals(Constants.DECLINED_LOGIN, message.getInstruction());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testForgot1() {
		try {
			toServer.writeObject(new MessageUS(Constants.FORGOT_PASSWORD, "samcarr21", ""));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS message = (MessageUS) fromServer.readObject();
			// TODO email_sent not in constants
			assertEquals(Constants.EMAIL_SENT, message.getInstruction());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testForgot2() {
		try {
			toServer.writeObject(new MessageUS(Constants.FORGOT_PASSWORD, "dummyNotPresent", ""));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS message = (MessageUS) fromServer.readObject();
			assertEquals(Constants.EMAIL_NOT_SENT, message.getInstruction());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	//remember extra accounts
	@Test
	public void testCreateAccount1() {
		String[] accountInfo = { "samcarr22", "password22", "sam22@carr.com" };
		try {
			toServer.writeObject(new MessageUS(Constants.CREATE_ACCOUNT, accountInfo));
			MessageUS message = (MessageUS) fromServer.readObject();
			assertEquals(Constants.ACCEPTED_CREATE_ACCOUNT, message.getInstruction());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// attempt to create the same account again
		try {
			toServer.writeObject(new MessageUS(Constants.CREATE_ACCOUNT, accountInfo));
			MessageUS message = (MessageUS) fromServer.readObject();
			assertEquals(Constants.DECLINED_CREATE_ACCOUNT, message.getInstruction());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConnect() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr1", "password1" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr3", "password3" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			toServer.writeObject(new MessageUS(Constants.CONNECT, "samcarr3", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			MessageUS msgListChanged = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.LIST_CHANGED, msgListChanged.getInstruction());
			
			MessageUS connectionRequest = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.CONNECTION_REQUEST, connectionRequest.getInstruction());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateGroup() {
		try {
			//login 3 users
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr4", "password4" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr5", "password5" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			
			Socket server2 = null;
			ObjectOutputStream toServer2 = null;
			ObjectInputStream fromServer2 = null;
			try {
				server2 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer2 = new ObjectOutputStream(server2.getOutputStream());
				fromServer2 = new ObjectInputStream(server2.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			// add third login dummy3
			toServer2.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr6", "password6" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged2 = (MessageUS) fromServer2.readObject();
			MessageUS messageLogin2 = (MessageUS) fromServer2.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged2.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin2.getInstruction());
			
			messageListChanged1 = (MessageUS) fromServer1.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged1.getInstruction());
			
			messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());
			
			
			//user creates group of 3 users
			toServer.writeObject(new MessageUS(Constants.CREATE_GROUP, "samcarr4,samcarr5,samcarr6", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS msgListChanged = (MessageUS) fromServer.readObject();
			MessageUS msgListChanged2 = (MessageUS) fromServer1.readObject();
			MessageUS msgListChanged3 = (MessageUS) fromServer2.readObject();
			
			assertEquals(Constants.LIST_CHANGED, msgListChanged.getInstruction());
			assertEquals(Constants.LIST_CHANGED, msgListChanged2.getInstruction());
			assertEquals(Constants.LIST_CHANGED, msgListChanged3.getInstruction());


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAcceptConnect() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr7", "password7" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageGetTop = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			MessageUS messageListChanged2 = (MessageUS) fromServer.readObject();

			assertEquals(Constants.LIST_CHANGED, messageListChanged2.getInstruction());
			
			assertEquals(Constants.GET_MY_TOP, messageGetTop.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr8", "password8" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			toServer.writeObject(new MessageUS(Constants.CONNECT, "samcarr8", ""));


			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			messageListChanged2 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.LIST_CHANGED, messageListChanged2.getInstruction());

			MessageUS connectionRequest = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.CONNECTION_REQUEST, connectionRequest.getInstruction());
			
			toServer1.writeObject(new MessageUS(Constants.ACCEPTED_CONNECT, "samcarr8", ""));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS connectionAccepted = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.ACCEPTED_CONNECT, connectionAccepted.getInstruction());
			
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeclineConnect() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr9", "password9" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			toServer.writeObject(new MessageUS(Constants.CONNECT, "dummyNotPresent", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			messageListChanged = (MessageUS) fromServer.readObject();

			assertEquals(Constants.LIST_CHANGED, messageListChanged.getInstruction());
			
			MessageUS connectionDeclined = (MessageUS) fromServer.readObject();

			assertEquals(Constants.DECLINED_CONNECT, connectionDeclined.getInstruction());
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSendMessage() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr10", "password10" }));
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr11", "password11" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());
			
			//connect dummy5 to dummy2
			toServer.writeObject(new MessageUS(Constants.CONNECT, "samcarr11", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			messageListChanged0 = (MessageUS) fromServer1.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			MessageUS connectionRequest = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.CONNECTION_REQUEST, connectionRequest.getInstruction());
			
			toServer1.writeObject(new MessageUS(Constants.ACCEPTED_CONNECT, "samcarr10", ""));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MessageUS connectionAccepted = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.ACCEPTED_CONNECT, connectionAccepted.getInstruction());

			toServer.writeObject(new MessageUS(Constants.SEND_MESSAGE, "this is the message sent", "samcarr11",
					connectionAccepted.getID()));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			MessageUS messageRecieved = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.RECEIVE_MESSAGE, messageRecieved.getInstruction());
			assertEquals("this is the message sent", messageRecieved.getMessage());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPublicMessage() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr12", "password12" }));
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr13", "password13" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			
			toServer.writeObject(new MessageUS(Constants.PUBLIC_MESSAGE, "this is the public message", "", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			messageListChanged0 = (MessageUS) fromServer.readObject();
			messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageListChanged2 = (MessageUS) fromServer.readObject();
			MessageUS acceptedMessage = (MessageUS) fromServer1.readObject();


			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());
			assertEquals(Constants.LIST_CHANGED, messageListChanged1.getInstruction());
			assertEquals(Constants.RECEIVE_PUBLIC_MESSAGE, messageListChanged2.getInstruction());
			assertEquals(Constants.RECEIVE_PUBLIC_MESSAGE, acceptedMessage.getInstruction());
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEndChat() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr14", "password14" }));
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();
			MessageUS messageLogin = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin.getInstruction());

			
			Socket server1 = null;
			ObjectOutputStream toServer1 = null;
			ObjectInputStream fromServer1 = null;
			try {
				server1 = new Socket(Constants.HOST_IP, Constants.SOCKET_NUMBER);
				toServer1 = new ObjectOutputStream(server1.getOutputStream());
				fromServer1 = new ObjectInputStream(server1.getInputStream());
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			}
			toServer1.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr15", "password15" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS messageListChanged1 = (MessageUS) fromServer1.readObject();
			MessageUS messageLogin1 = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged1.getInstruction());
			assertEquals(Constants.ACCEPTED_LOGIN, messageLogin1.getInstruction());
			
			MessageUS messageListChanged0 = (MessageUS) fromServer.readObject();
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			
			toServer.writeObject(new MessageUS(Constants.CONNECT, "samcarr15", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			messageListChanged0 = (MessageUS) fromServer1.readObject();
			
			assertEquals(Constants.LIST_CHANGED, messageListChanged0.getInstruction());

			MessageUS connectionRequest = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.CONNECTION_REQUEST, connectionRequest.getInstruction());
			
			toServer1.writeObject(new MessageUS(Constants.ACCEPTED_CONNECT, "samcarr14", ""));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MessageUS connectionAccepted = (MessageUS) fromServer1.readObject();

			assertEquals(Constants.ACCEPTED_CONNECT, connectionAccepted.getInstruction());
			
			
			toServer.writeObject(new MessageUS(Constants.END_CHAT, "samcarr14", connectionAccepted.getID()));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MessageUS chatEnded = (MessageUS) fromServer.readObject();
			MessageUS chatEnded2 = (MessageUS) fromServer1.readObject();
			MessageUS listChanged = (MessageUS) fromServer.readObject();
		

			assertEquals(Constants.LIST_CHANGED, chatEnded.getInstruction());
			assertEquals(Constants.CHAT_ENDED, chatEnded2.getInstruction());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEnd() {
		try {
			toServer.writeObject(new MessageUS(Constants.LOGIN, new String[] { "samcarr16", "password16" }));
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MessageUS messageListChanged = (MessageUS) fromServer.readObject();

			assertEquals(Constants.GET_MY_TOP, messageListChanged.getInstruction());

			
			toServer.writeObject(new MessageUS(Constants.END, "samcarr16", ""));

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MessageUS acceptedLogin = (MessageUS) fromServer.readObject();
			MessageUS chatEnded = (MessageUS) fromServer.readObject();
			messageListChanged = (MessageUS) fromServer.readObject();

			assertEquals(Constants.ACCEPTED_LOGIN, acceptedLogin.getInstruction());
			assertEquals(Constants.CHAT_ENDED, chatEnded.getInstruction());
//			assertEquals(Constants.LIST_CHANGED, messageListChanged.getInstruction());


		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws SQLException {
		AccountDAOImpl adao = DAOFactory.getInstance().getAccountDAO();
		AccountEntity account = adao.findByUsername("samcarr22");
		if (account != null) {
			adao.deleteById(account.getId());
		}
		
	}
	
	
}

