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
import grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl;
import grimsby.libraries.entities.AccountEntity;
import grimsby.libraries.entities.GroupEntity;
import grimsby.libraries.entities.GroupMemberEntity;

/**
 * 
 *
 * @author Christopher Friis (cxf798)
 * @version 4 Mar 2018
 */
public class GroupMemberDAOImplTest {

	DAOFactoryTest daoFactory;
	GroupMemberDAOImpl gmdao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		daoFactory = DAOFactoryTest.getInstance();
		gmdao = daoFactory.getGroupMemberDAOImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		try {
			//testing first and last group member entries
			List<GroupMemberEntity> groupMembers = gmdao.findAll();
			assertEquals(groupMembers.get(0).getGroup().getGroupName(), "dummyGroup1");
			assertEquals(groupMembers.get(0).getAccount().getUsername(), "dummy1");
			assertEquals(groupMembers.get(4).getGroup().getGroupName(), "dummyGroup2");	
			assertEquals(groupMembers.get(4).getAccount().getUsername(), "dummy3");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#findById(int)}.
	 */
	@Test
	public void testFindById() {
		try {
			//testing first and last group member entries
			assertEquals(gmdao.findById(1).getGroup().getGroupName(), "dummyGroup1");
			assertEquals(gmdao.findById(1).getAccount().getUsername(), "dummy1");
			assertEquals(gmdao.findById(5).getGroup().getGroupName(), "dummyGroup2");	
			assertEquals(gmdao.findById(5).getAccount().getUsername(), "dummy3");
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#create(grimsby.libraries.entities.GroupMember)}.
	 */
	@Test
	public void testCreate() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		GroupEntity group = new GroupEntity("dummyGroup1", account, "GroupName", new Timestamp(0));
		GroupMemberEntity groupMember = new GroupMemberEntity(group, account);

		try {
			Assert.assertTrue(gmdao.create(groupMember));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}	
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#deleteById(int)}.
	 */
	@Test
	public void testDeleteById() {
		try {
			Assert.assertTrue(gmdao.deleteById(1));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();

		}
	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#findByGroup(grimsby.libraries.entities.Group)}.
	 */
	@Test
	public void testFindByGroup() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		//hopefully finds despite different timestamp
		GroupEntity group = new GroupEntity("dummyGroup1", account, "dummyGroup1", new Timestamp(0));
		try {
			List<GroupMemberEntity> groupMembers = gmdao.findByGroup(group);
			assertEquals(groupMembers.get(0).getGroup().getGroupName(), "dummyGroup1");
			assertEquals(groupMembers.get(0).getAccount().getUsername(), "dummy1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for {@link grimsby.libraries.dataaccess.dao.impl.GroupMemberDAOImpl#findByAccount(grimsby.libraries.entities.Account)}.
	 */
	@Test
	public void testFindByAccount() {
		AccountEntity account = new AccountEntity(1,null, "dummy1", "password1", "name1", "surname1", "dummy1@dummy.ac.uk", "none", null, false, false, "bio", false);
		try {
			List<GroupMemberEntity> groupMembers = gmdao.findByAccount(account);
			assertEquals(groupMembers.get(0).getGroup().getGroupName(), "dummyGroup2");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
