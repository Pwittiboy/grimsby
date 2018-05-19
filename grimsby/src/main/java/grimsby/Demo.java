/**
 * 
 */
package grimsby;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl;
import grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl;
import grimsby.libraries.dataaccess.jdbc.DbConnect;


/**
 * Admin tool for quick testing of new DAO methods.
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public class Demo {
	
	private static final Logger LOGGER = Logger.getLogger(Demo.class);
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SQLException, IOException {
		Connection connection = DbConnect.createConnection();
		AccountDAOImpl adao = new AccountDAOImpl(connection);
		ConversationDAOImpl cdao = new ConversationDAOImpl(connection);
		GroupDAOImpl gdao = new GroupDAOImpl(connection);
		MessageDAOImpl mdao = new MessageDAOImpl(connection);
		
		// SQL injection tests
		String sql = "SELECT * FROM groups WHERE group_name LIKE ''; DELETE FROM groups WHERE group_name LIKE 'qer';--";
		gdao.findByGroupName(sql);
		System.out.println(gdao.findByGroupName("qer"));
		
//		Account account = adao.findByUsername("axz503");
//		adao.setDarkTheme(account, false);
//		System.out.println(adao.findByUsername("axz503").isDarkTheme());
//		
//		adao.setDarkTheme(adao.findByUsername("axz503"), true);
		
		
//		gdao.findByTimestamp(timestamp)
		
//		// best friends test
//		System.out.println(adao.getTopContacts(adao.findByUsername("dummy5"), 5));
		
//		// particular group history test
//		Group group = cdao.findById(322).getGroup();
//		String[][] history = adao.getParticularGroupHistory(group.getTimestamp());
//		for (String[] row : history) {
//			for (String entry : row) {
//				System.out.println(entry);
//			}
//		}
		
		
//		// particular history test
//		Account sender = adao.findById(1);
//		Account receiver = adao.findById(4);
//		
//		String[][] history = adao.getParticularHistory(sender, receiver);
//		for (String[] row : history) {
//			for (String entry : row) {
//				System.out.println(entry);
//			}
//		}
		
		
//		// All history test
//		List<String> list = adao.getAllHistory(adao.findById(10));
//		for (String s: list) {
//			System.out.println(s);
//		}
		
	}

}
