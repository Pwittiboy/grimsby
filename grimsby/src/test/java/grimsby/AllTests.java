package grimsby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import grimsby.dataaccess.dao.factory.DAOFactoryTest;
import grimsby.dataaccess.dao.impl.AccountDAOImplTest;
import grimsby.dataaccess.dao.impl.ConversationDAOImplTest;
import grimsby.dataaccess.dao.impl.GroupDAOImplTest;
import grimsby.dataaccess.dao.impl.GroupMemberDAOImplTest;
import grimsby.dataaccess.dao.impl.MessageDAOImplTest;
import grimsby.dataaccess.dao.impl.ParticipantDAOImplTest;

/**
 * 
 * @author Christopher Friis (cxf798)
 * @version 9 Mar 2018
 */
@RunWith(Suite.class)
@SuiteClasses({
	AccountDAOImplTest.class,
	ConversationDAOImplTest.class,
	GroupDAOImplTest.class,
	GroupMemberDAOImplTest.class,
	MessageDAOImplTest.class,
	ParticipantDAOImplTest.class
	})
public class AllTests {

	@BeforeClass
	public static void setUp() {
		// run the script to set up the dummy database
		runScript("testDatabaseScript.sql");
		
		// very important that we use DAOFactoryTest in our tests and not DAOFactory
		DAOFactoryTest.getInstance();
	}
	
	@AfterClass
	public static void tearDown() {
		// drop all tables in the dummy database
		runScript("testDatabaseTearDown.sql");
	}
	
	private static void runScript(String script) {
		try {
			
			Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /C\"psql -U postgres -d test_grimsby -f "+script+"\"");

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while (stdOut.readLine() != null) {
				// Wait for script to complete...
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not run script, terminating tests");
			System.exit(0);
		}
	}
	
}
