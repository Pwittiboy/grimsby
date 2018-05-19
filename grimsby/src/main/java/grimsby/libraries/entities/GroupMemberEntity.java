/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for group_members table.
 *
 * @author Christopher Friis (cxf798)
 * @version 1 Mar 2018
 */
public class GroupMemberEntity extends AbstractEntity {
	
	/**
	 * Represents group_id column.
	 */
	private GroupEntity group;
	
	/**
	 * Represents account_id column.
	 */
	private AccountEntity account;
	
	public GroupMemberEntity(GroupEntity group, AccountEntity account) {
		this(0, group, account, null);
	}
	
	/**
	 * Constructor for group member entity.
	 * @param id Unique id of the group member.
	 * @param group The group that the group member belongs to.
	 * @param account The account that the group member represents/
	 * @param timestamp The creation date of the group member.
	 */
	public GroupMemberEntity(int id, GroupEntity group, AccountEntity account, Timestamp timestamp) {
		this.id = id;
		this.group = group;
		this.account = account;
		this.timestamp = timestamp;
	}

	/**
	 * Getter method for the group member's group.
	 * @return The group that the group member belongs to.
	 */
	public GroupEntity getGroup() {
		return group;
	}

	/**
	 * Setter method for the group member's group.
	 * @param group The group that the group member belongs to.
	 */
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
	/**
	 * Getter method for the account that the group member represents.
	 * @return The account that the group member represents.
	 */
	public AccountEntity getAccount() {
		return account;
	}

	/**
	 * Setter method for the account that the group member represents.
	 * @param account The account that the group member represents.
	 */
	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	
	@Override
	public boolean equals(Object groupMember) {
		return this.group == ((GroupMemberEntity)groupMember).getGroup() &&
				this.account == ((GroupMemberEntity)groupMember).getAccount();
	}

}
