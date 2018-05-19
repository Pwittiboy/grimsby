/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for conversations table.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public class ConversationEntity extends AbstractEntity {
	
	/**
	 * Represents owner_id column.
	 */
	private AccountEntity account;
	
	/**
	 * Represents group_id column.
	 */
	private GroupEntity group;
	
	/**
	 * Convenience constructor for creating conversations.
	 * @param account Account that created, and therefore, owns the conversation.
	 * @param group Group that this conversation may be attached to.
	 */
	public ConversationEntity(AccountEntity account, GroupEntity group) {
		this(0, account, group, null);
	}
	
	/**
	 * Constructor for creating conversations entities.
	 * @param id Unique id of the conversation being created.
	 * @param account Account that created, and therefore, owns the conversation.
	 * @param group Group that this conversation may be attached to.
	 * @param timestamp Creation date timestamp for the conversation. 
	 */
	public ConversationEntity(int id, AccountEntity account, GroupEntity group, Timestamp timestamp) {
		this.id = id;
		this.account = account;
		this.group = group;
		this.timestamp = timestamp;
	}
	
	/**
	 * Convenience getter method for conversation id.
	 * @return Id of conversation as a String object.
	 */
	public String getIdAsString() {
		return String.valueOf(id);
	}

	/**
	 * Getter method for the conversation's owner account.
	 * @return Conversation's owner account.
	 */
	public AccountEntity getAccount() {
		return account;
	}

	/**
	 * Setter method for the conversation's owner account.
	 * @param account Conversation's owner account.
	 */
	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	/**
	 * Getter method for the conversation's group.
	 * @return Conversation's group.
	 */
	public GroupEntity getGroup() {
		return group;
	}

	/**
	 * Setter method for the conversation's group.
	 * @param group Conversation's group.
	 */
	public void setGroup(GroupEntity group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Conversation ID: "+id+"; Owner ID: "+account.getId();
	}
	
	@Override
	public boolean equals(Object conversation) {
		return this.account == ((ConversationEntity)conversation).getAccount();
	}

}
