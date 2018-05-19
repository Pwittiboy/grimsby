package grimsby.dataaccess.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author Christopher Friis (cxf798)
 * @version 9 Mar 2018
 */
public class DbConnectTest {

	private static final Logger LOGGER = Logger.getLogger(DbConnectTest.class);

	/**
	 * Opens a connection to the database.
	 * @return Connection instance to the database.
	 */
	public static Connection createConnection() {

		Properties p = new Properties();
		InputStream input = null;

		String url;
		String database;
		String username;
		String password;

		Connection c;

		try {
			// database connection details all taken from properties file
			input = new FileInputStream("testdbsettings.properties");

			p.load(input);

			url = p.getProperty("dbUrl");
			database = p.getProperty("dbName");
			username = p.getProperty("dbUsername");
			password = p.getProperty("dbPassword");

			try {
				c = DriverManager.getConnection(url+database, username, password);
				LOGGER.info("Database connection: Successful");
				System.out.println("Database connection: Successful");
				return c;
			} catch (SQLException e) {
				LOGGER.error("Database connection: Failed");
				e.printStackTrace();
				return null;
			}

		} catch (IOException e) {
			LOGGER.error(e);
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error(e);
					e.printStackTrace();
				}
			}
		}
	}

}
