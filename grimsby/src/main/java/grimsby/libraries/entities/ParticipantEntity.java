/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for participants table.
 *
 * @author Christopher Friis (cxf798)
 * @version 27 Feb 2018
 */
public class ParticipantEntity extends AbstractEntity {

	/**
	 * Represents account_id column.
	 */
	private AccountEntity account;
	
	/**
	 * Represents conversation_id column.
	 */
	private ConversationEntity conversation;
	
	/**
	 * Convenience constructor for the participant entity.
	 * @param account The account that participant represents.
	 * @param conversation The conversation that the participant is binded to.
	 */
	public ParticipantEntity(AccountEntity account, ConversationEntity conversation) {
		this(0, account, conversation, null);
	}
	
	/**
	 * Main constructor for the participant entity.
	 * @param id Participant's unique id.
	 * @param account The account that participant represents.
	 * @param conversation The conversation that the participant is binded to.
	 * @param timestamp Creation date timestamp.
	 */
	public ParticipantEntity(int id, AccountEntity account, ConversationEntity conversation, Timestamp timestamp) {
		this.id = id;
		this.account = account;
		this.conversation = conversation;
		this.timestamp = timestamp;
	}

	/**
	 * Getter method for the participant's unique id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for the account that the participant represents.
	 * @return Account that the participant represents.
	 */
	public AccountEntity getAccount() {
		return account;
	}

	/**
	 * Getter method for the conversation that the participant is binded to.
	 * @return Conversation that the participant is binded to.
	 */
	public ConversationEntity getConversation() {
		return conversation;
	}

	@Override
	public String toString() {
		return "Participant ID: "+id+"; Account ID: "+account.getId()+"; Conversation ID: "+conversation.getId();
	}
	
	@Override
	public boolean equals(Object participant) {
		return this.account == ((ParticipantEntity)participant).getAccount() &&
				this.conversation == ((ParticipantEntity)participant).getConversation();
	}
	
}
