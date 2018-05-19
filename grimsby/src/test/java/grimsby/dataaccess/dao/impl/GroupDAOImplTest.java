/**
 * 
 */
package grimsby.dataaccess.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import grimsby.dataaccess.dao.factory.DAOFactoryTest;
import grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 4 Mar 2018
 */
public class GroupDAOImplTest {

	DAOFactoryTest daoFactory;
	GroupDAOImpl gdao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		daoFactory = DAOFactoryTest.getInstance();
		gdao = daoFactory.getGroupDAOImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		try {
			List<GroupEntity> groups = gdao.findAll();
			assertEquals(groups.get(0).getOwner().getUsername(), "dummy1");
			assertEquals(groups.get(0).getGroupName(), "dummyGroup1");
			assertEquals(groups.get(1).getOwner().getUsername(), "dummy2");	
			assertEquals(groups.get(1).getGroupName(), "dummyGroup2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		try {
			//id starts from 1
			assertEquals(gdao.findById("dummyGroup1").getOwner().getUsername(), "dummy1");
			assertEquals(gdao.findById("dummyGroup1").getGroupName(), "dummyGroup1");
			assertEquals(gdao.findById("dummyGroup2").getOwner().getUsername(), "dummy2");
			assertEquals(gdao.findById("dummyGroup2").getGroupName(), "dummyGroup2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#create(grimsby.libraries.entities.Group)}.
	 */
	@Test
	public void testCreate() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		GroupEntity group = new GroupEntity("5", account, "GroupName", new Timestamp(0));
		try {
			Assert.assertTrue(gdao.create(group));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#deleteById(int)}.
	 */
	@Test
	public void testDeleteById() {
		try {
			Assert.assertTrue(gdao.deleteById("dummyGroup3"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#findByOwner(grimsby.libraries.entities.Account)}.
	 */
	@Test
	public void testFindByOwner() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		try {
			List<GroupEntity> groups = gdao.findByOwner(account);
			assertEquals(groups.get(0).getOwner().getUsername(), "dummy1");
			assertEquals(groups.get(0).getGroupName(), "dummyGroup1");
//			assertEquals(groups.get(1).getOwner().getUsername(), "dummy3");
//			assertEquals(groups.get(1).getGroupName(), "dummyGroup2");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupDAOImpl#findByGroupName(java.lang.String)}.
	 */
	@Test
	public void testFindByGroupName() {
		List<GroupEntity> groups;
		try {
			groups = gdao.findByGroupName("dummyGroup1");
			assertEquals(groups.get(0).getOwner().getUsername(), "dummy1");
		} catch (SQLException e) {
			fail();
		}
	}

}
