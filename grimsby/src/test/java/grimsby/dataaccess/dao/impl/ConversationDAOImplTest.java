/**
 * 
 */
package grimsby.dataaccess.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import grimsby.dataaccess.dao.factory.DAOFactoryTest;
import grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.ConversationEntity;
import junit.framework.TestCase;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 4 Mar 2018
 */
public class ConversationDAOImplTest extends TestCase {

	DAOFactoryTest daoFactory;
	ConversationDAOImpl cdao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		daoFactory = DAOFactoryTest.getInstance();
		cdao = daoFactory.getConversationDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		try {
			//id starts from 1
			List<ConversationEntity> convos = cdao.findAll();
			assertEquals(convos.get(0).getAccount().getUsername(), "dummy1");
			assertEquals(convos.get(1).getAccount().getUsername(), "dummy2");
			assertEquals(convos.get(2).getAccount().getUsername(), "dummy1");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		try {
			//id starts from 1
			assertEquals(cdao.findById(1).getAccount().getUsername(), "dummy1");
			assertEquals(cdao.findById(2).getAccount().getUsername(), "dummy2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl#findByOwner(grimsby.libraries.entities.AccountEntity)}.
	 */
	@Test
	public void testFindByOwner() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		try {
			List<ConversationEntity> convos = cdao.findByOwner(account);
			assertEquals(convos.get(0).getAccount().getUsername(), "dummy1");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl#create(grimsby.libraries.entities.ConversationEntity)}.
	 */
	@Test
	public void testCreate() {
		AccountEntity account = new AccountEntity(1,null, "dummy20", "password20", "name20", "surname20", "dummy20@dummy.ac.uk", "none", null, false, false, "bio", false);
		ConversationEntity convo = new ConversationEntity(20, account, null, null);
		try {
			Assert.assertTrue(cdao.create(convo));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.ConversationDAOImpl#deleteById(int)}.
	 */
	@Test
	public void testDeleteById() {
		try {
			Assert.assertTrue(cdao.deleteById(5));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

}
