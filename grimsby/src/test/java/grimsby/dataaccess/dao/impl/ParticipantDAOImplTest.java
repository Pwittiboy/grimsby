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
import grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import grimsby.libraries.entities.ParticipantEntity;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 4 Mar 2018
 */
public class ParticipantDAOImplTest {

	DAOFactoryTest daoFactory;
	ParticipantDAOImpl pdao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		daoFactory = DAOFactoryTest.getInstance();
		pdao = daoFactory.getParticipantDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		//testing first and last column
		List<ParticipantEntity> messages;
		try {
			messages = pdao.findAll();
			assertEquals(messages.get(0).getAccount().getUsername(), "dummy1");
			assertEquals(messages.get(0).getConversation().getAccount().getUsername(), "dummy1");
			assertEquals(messages.get(3).getAccount().getUsername(), "dummy3");
			assertEquals(messages.get(3).getConversation().getAccount().getUsername(), "dummy2");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		//testing first and last column
		try {
		assertEquals(pdao.findById(1).getAccount().getUsername(), "dummy1");
		assertEquals(pdao.findById(1).getConversation().getAccount().getUsername(), "dummy1");
		assertEquals(pdao.findById(4).getAccount().getUsername(), "dummy3");
		assertEquals(pdao.findById(4).getConversation().getAccount().getUsername(), "dummy2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#findByAccount(grimsby.libraries.entities.Account)}.
	 */
	@Test
	public void testFindByAccount() {
		AccountEntity account = new AccountEntity(2,null, "dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", "none", null, false, false, "bio", false);
		try {
			List<ParticipantEntity> messages = pdao.findByAccount(account);
			assertEquals(messages.get(0).getAccount().getUsername(), "dummy2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#findByConversation(grimsby.libraries.entities.Conversation)}.
	 */
	@Test
	public void testFindByConversation() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(1, account, null, null);
		try {
			
			List<ParticipantEntity> messages = pdao.findByConversation(convo);
			assertEquals(messages.get(0).getAccount().getUsername(), "dummy1");
			assertEquals(messages.get(1).getAccount().getUsername(), "dummy2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
		
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#create(grimsby.libraries.entities.Participant)}.
	 */
	@Test
	public void testCreate() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(1,account, null, null);
		ParticipantEntity participant = new ParticipantEntity(5,account, convo, null);
		try {
			Assert.assertTrue(pdao.create(participant));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#delete(grimsby.libraries.entities.Participant)}.
	 */
	@Test
	public void testDelete() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(account, null);
		ParticipantEntity participant = new ParticipantEntity(account, convo);
		try {
			Assert.assertTrue(pdao.delete(participant));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
		
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ParticipantDAOImpl#deleteById(int)}.
	 */
	@Test
	public void testDeleteById() {
		try {
			Assert.assertTrue(pdao.deleteById(1));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

}
