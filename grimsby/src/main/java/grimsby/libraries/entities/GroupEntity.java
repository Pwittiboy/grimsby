/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for groups table.
 *
 * @author Christopher Friis (cxf798)
 * @version 1 Mar 2018
 */
public class GroupEntity {
	
	/**
	 * Represents group_id column.
	 */
	private String id;
	
	/**
	 * Represents owner_id column.
	 */
	private AccountEntity owner;
	
	/**
	 * Represents group_name column.
	 */
	private String groupName;
	
	/**
	 * Represents created_at column.
	 */
	private Timestamp timestamp;

	/**
	 * Constructor for a group entity.
	 * @param id Group's unique id.
	 * @param owner The group's owner account.
	 * @param groupName The group's name.
	 * @param timestamp The creation date timestamp for the group.
	 */
	public GroupEntity(String id, AccountEntity owner, String groupName, Timestamp timestamp) {
		this.id = id;
		this.owner = owner;
		this.groupName = groupName;
		this.timestamp = timestamp;
	}

	/**
	 * Getter method for the group's owner account.
	 * @return The group's owner account.
	 */
	public AccountEntity getOwner() {
		return owner;
	}

	/**
	 * Getter method for the group's name.
	 * @return The group's name.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Setter method for the group's name.
	 * @param groupName The group's name.
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * Geter method for the unique id of the group.
	 * @return The group's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter method for the group's id.
	 * @param id The group's id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter method for the group's creation date timestamp.
	 * @return Timestamp of the group's creation date.
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * setter method for the group's creation date timestamp.
	 * @param timestamp Creation date of the group.
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object group) {
		return this.owner == ((GroupEntity)group).getOwner() &&
				this.groupName == ((GroupEntity)group).getGroupName();
	}

}
