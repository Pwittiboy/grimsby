/**
 * 
 */
package grimsby.dataaccess.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import grimsby.dataaccess.dao.factory.DAOFactoryTest;
import grimsby.libraries.dataaccess.dao.MessageDAO;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.MessageEntity;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 4 Mar 2018
 */
public class MessageDAOImplTest {
	
	DAOFactoryTest daoFactory;
	MessageDAO mdao;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		daoFactory = DAOFactoryTest.getInstance();
		mdao = daoFactory.getMessageDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		try {
			//testing first 3 columns of first row
			List<MessageEntity> messages = mdao.findAll();
			assertEquals(messages.get(0).getSender().getUsername(), "dummy1");
			assertEquals(messages.get(0).getReceiver().getUsername(), "dummy2");
			assertEquals(messages.get(0).getConversation().getAccount().getUsername(), "dummy1");

		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
		
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#findByAccount(grimsby.libraries.entities.Account)}.
	 */
	@Test
	public void testFindByAccount() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		try {
			//all 3 rows have conversation id 1 which belongs to account 1
			List<MessageEntity> messages = mdao.findByAccount(account);
			assertEquals(messages.get(0).getSender().getUsername(), "dummy1");
			assertEquals(messages.get(0).getReceiver().getUsername(), "dummy2");
			assertEquals(messages.get(0).getConversation().getAccount().getUsername(), "dummy1");
//			assertEquals(messages.get(1).getSender().getUsername(), "dummy2");
//			assertEquals(messages.get(2).getSender().getUsername(), "dummy1");

		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#findByConversation(grimsby.libraries.entities.Conversation)}.
	 */
	@Test
	public void testFindByConversation() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(1, account, null, null);
		try {
			List<MessageEntity> messages = mdao.findByConversation(convo);
			assertEquals(messages.get(0).getSender().getUsername(), "dummy1");
			assertEquals(messages.get(0).getReceiver().getUsername(), "dummy2");
			assertEquals(messages.get(1).getSender().getUsername(), "dummy2");
			assertEquals(messages.get(2).getSender().getUsername(), "dummy1");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		try {
			//testing first and last group member entries
			assertEquals(mdao.findById(1).getMessage(), "Getting tired now");
			assertEquals(mdao.findById(2).getMessage(), "Why are you still awake? ");
			assertEquals(mdao.findById(5).getMessage(), "Going bed :\\ ");	
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#create(grimsby.libraries.entities.MessageEntity)}.
	 */
	@Test
	public void testCreate() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		AccountEntity account2 = new AccountEntity(2,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(1,account, null, null);
		MessageEntity message = new MessageEntity(account, account2, convo, "this is the created message");
		try {
			Assert.assertTrue(mdao.create(message));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	

	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.MessageDAOImpl#deleteById(int)}.
	 */
	@Test
	public void testDeleteById() {
		try {
			Assert.assertTrue(mdao.deleteById(1));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

}
