/**
 * 
 */
package grimsby.dataaccess.dao.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import grimsby.dataaccess.dao.factory.DAOFactoryTest;
import grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import junit.framework.TestCase;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 2 Mar 2018
 */
public class AccountDAOImplTest extends TestCase {
	
	DAOFactoryTest daoFactory;
	AccountDAOImpl adao;
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		daoFactory = DAOFactoryTest.getInstance();
		adao = daoFactory.getAccountDAO();
		
		Field daoField = adao.getClass().getDeclaredField("gdao");
		daoField.setAccessible(true);
		daoField.set(adao, DAOFactoryTest.getInstance().getGroupDAOImpl());
	}
	
	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {		
		try {
			//ids start from 1 with serial
			List<AccountEntity> accounts = adao.findAll();
			assertEquals("dummy1", accounts.get(0).getUsername());
			assertEquals("dummy2", accounts.get(1).getUsername());
			assertEquals("dummy3", accounts.get(2).getUsername());
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}
	
	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		try {
			//id 1 is 1st in list
			assertEquals("dummy1", adao.findById(1).getUsername());
			assertEquals("dummy2", adao.findById(2).getUsername());
			assertEquals("dummy3", adao.findById(3).getUsername());
			assertEquals("cxf798", adao.findById(5).getUsername());
			assertEquals("axz503", adao.findById(6).getUsername());
			assertEquals("Muhammad", adao.findById(7).getUsername());
			assertEquals("lcltentacle", adao.findById(8).getUsername());
			assertEquals("master", adao.findById(9).getUsername());
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}


	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#findByUsername(java.lang.String)}.
	 */
	@Test
	public void testFindByUsername() {
		try {
			//id 1 is 1st in list
			assertEquals("password1", adao.findByUsername("dummy1").getPassword());
			assertEquals("password2", adao.findByUsername("dummy2").getPassword());
			assertEquals("password3", adao.findByUsername("dummy3").getPassword());
			assertEquals("meow", adao.findByUsername("cxf798").getPassword());
			assertEquals("krabas", adao.findByUsername("axz503").getPassword());
			assertEquals("abc123", adao.findByUsername("Muhammad").getPassword());
			assertEquals("cjb1219lcl", adao.findByUsername("lcltentacle").getPassword());
			assertEquals("password", adao.findByUsername("master").getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#create(grimsby.libraries.entities.Account)}.
	 * @throws SQLException 
	 */
	@Test
	public void testCreate() throws SQLException {
		AccountEntity account = new AccountEntity("createTestx", "aaaaaaaax", "Samx", "Carrx", "testx@gmail.com", false, false, "empty bio");
		
		Assert.assertTrue(adao.create(account));
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#deleteById(int)}.
	 * @throws SQLException 
	 */
	@Test
	public void testDelete() throws SQLException {
		AccountEntity account = new AccountEntity("dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", false, false, "empty bio");
		Assert.assertTrue(adao.delete(account));
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.AccountDAOImpl#verifyUser(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testVerifyUser() {
		Assert.assertTrue(adao.verifyUser("dummy1", "password1"));
	}

	@Test
	public void testGenerateTempPassword() {
		AccountEntity account = new AccountEntity("dummy4", "password4", "name4", "surname4", "dummy4@dummy.ac.uk", false, false, "empty bio");
		try {
			account = adao.findByUsername("dummy4");
			assertFalse(adao.generateTempPassword(account).equals("password4"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindByEmail() {
		try {
			//id 1 is 1st in list
			assertEquals("password1", adao.findByEmail("dummy1@dummy.ac.uk").getPassword());
			assertEquals("password2", adao.findByEmail("dummy2@dummy.ac.uk").getPassword());
			assertEquals("password3", adao.findByEmail("dummy3@dummy.ac.uk").getPassword());
			assertEquals("meow", adao.findByEmail("cxf798@student.bham.ac.uk").getPassword());
			assertEquals("krabas", adao.findByEmail("axz503@student.bham.ac.uk").getPassword());
			assertEquals("abc123", adao.findByEmail("mxr474dstudent.bham.ac.uk").getPassword());
			assertEquals("cjb1219lcl", adao.findByEmail("1210035194@qq.com").getPassword());
			assertEquals("password", adao.findByEmail("master@master.com").getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	@Test
	public void testIsFirstLogin() {
		AccountEntity account = new AccountEntity("dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(!adao.isFirstLogin(account));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSetFirstLogin() {
		AccountEntity account = new AccountEntity("dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(adao.setFirstLogin(account, true));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSetName() {
		AccountEntity account = new AccountEntity("dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(adao.setName(account, "name2Changed"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSetSurname() {
		AccountEntity account = new AccountEntity("dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(adao.setName(account, "surname2Changed"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testUpdateBio() {
		AccountEntity account = new AccountEntity("dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(adao.setName(account, "updated Bio"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testUpdateEmail() {
		AccountEntity account = new AccountEntity("dummy2", "password2", "name2", "surname2", "dummy2@dummy.ac.uk", false, false, "empty bio");
		try {
			Assert.assertTrue(adao.setName(account, "dummy2Changed@dummy.ac.uk"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetTopContacts() {
		try {

			AccountEntity account3 = adao.findById(7); // method parameter
			List<AccountEntity> topContacts = adao.getTopContacts(account3, 2);
			assertEquals(topContacts.get(0).getUsername(), "axz503");

		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

}
