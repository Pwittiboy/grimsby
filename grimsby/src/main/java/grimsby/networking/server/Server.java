package grimsby.networking.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import grimsby.networking.util.Constants;

/**
 * The Class Server. Creates a new server and initialises server threads for
 * users
 */
public class Server {

	/**
	 * The main method. Starts the new server if the given socket is available.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ConcurrentHashMap<String, ObjectOutputStream> clientsOnline = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, HashSet<String>> connections = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, HashSet<String>> connectionInformation = new ConcurrentHashMap<>();
		List<ServerThread> listOfConnections = Collections.synchronizedList(new ArrayList<>());
		ServerSocket sSocket = null;
		boolean whileFlag = true;

		try {
			sSocket = new ServerSocket(Constants.SOCKET_NUMBER, 100);
		} catch (IOException e) {
			System.err.println("Socket is not available, server not created");
			whileFlag = false;
		}
		while (whileFlag) {
			try {
				Socket socket = sSocket.accept();
				ServerThread newClient = new ServerThread(socket, clientsOnline, connections, connectionInformation,
						listOfConnections);
				newClient.start();
			} catch (IOException e) {
				try {
					sSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}
