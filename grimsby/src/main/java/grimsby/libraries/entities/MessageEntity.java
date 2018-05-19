/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity Class for the messages table.
 *
 * @author Christopher Friis (cxf798)
 * @version 21 Feb 2018
 */
public class MessageEntity extends AbstractEntity {
	
	/**
	 * Represents the sender_id column.
	 */
	private AccountEntity sender;
	
	/**
	 * Represents the receiver_id column.
	 */
	private AccountEntity receiver;
	
	/**
	 * Represents the conversation_id column.
	 */
	private ConversationEntity conversation;
	
	/**
	 * Represents the message column.
	 */
	private String message;
	
	/**
	 * Convenience constructor for creating a message entity.
	 * @param sender The account that sends the message.
	 * @param receiver The account that received the message.
	 * @param conversation The conversation that the message was sent in.
	 * @param message The contents of the message.
	 */
	public MessageEntity(AccountEntity sender, AccountEntity receiver, ConversationEntity conversation, String message) {
		this(0, sender, receiver, conversation, null, message);
	}
	
	/**
	 * Main constructor for a message entity. 
	 * @param id Message's unique id.
	 * @param sender Creation date timestamp for the account.
	 * @param receiver The account that received the message.
	 * @param conversation The conversation that the message was sent in.
	 * @param message The contents of the message.
	 * @param timestamp The creation date timestamp of the message.
	 */
	public MessageEntity(int id, AccountEntity sender, AccountEntity receiver, ConversationEntity conversation, Timestamp timestamp, String message) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.conversation = conversation;
		this.timestamp = timestamp;
		this.message = message;
	}

	/**
	 * Getter method for the message's sender account.
	 * @return Message's sender account.
	 */
	public AccountEntity getSender() {
		return sender;
	}

	/**
	 * Getter method for the message's receiver account.
	 * @return Message's receiver account.
	 */
	public AccountEntity getReceiver() {
		return receiver;
	}

	/**
	 * Getter method for the conversation that the message was sent in.
	 * @return Conversation the message was sent in.
	 */
	public ConversationEntity getConversation() {
		return conversation;
	}

	/**
	 * Getter method for the message contents.
	 * @return Message contents.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Setter method for the message contents.
	 * @param message Message contents.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.sender.getUsername()+": "+getMessage()+"\n";
	}
	
	@Override
	public boolean equals(Object message) {
		return this.sender == ((MessageEntity)message).getSender() &&
				this.conversation == ((MessageEntity)message).getConversation() &&
				this.message == ((MessageEntity)message).getMessage();
	}
}
