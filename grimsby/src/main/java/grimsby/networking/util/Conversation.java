package grimsby.networking.util;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * The Class Conversation. Used to store information about the particular
 * conversation between two people or group.
 */
public class Conversation extends Observable {

	/** The conversation id. */
	private String conversationId;

	/** The people in Conversation. */

	private Set<String> peopleInConversation;

	/**
	 * Instantiates a new private conversation.
	 *
	 * @param conversationId the conversation id
	 */
	public Conversation(String conversationId) {
		this.conversationId = conversationId;
		this.peopleInConversation = new HashSet<>();
	}

	/**
	 * Instantiates a new group conversation.
	 *
	 * @param conversationId the conversation id
	 * @param peopleInConversation the people in conversation
	 */
	public Conversation(String conversationId, Set<String> peopleInConversation) {
		this(conversationId);
		this.peopleInConversation = peopleInConversation;
	}

	/**
	 * New message.
	 *
	 * @param m the m
	 */
	public void newMessage(MessageUS m) {
		setChanged();
		notifyObservers(m);
	}

	/**
	 * Declined message.
	 *
	 * @param m the m
	 */
	public void declinedMessage(MessageUS m) {
		setChanged();
		notifyObservers(m);
	}

	/**
	 * Gets the conversation id.
	 *
	 * @return the conversation id
	 */
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * Notifies that this chat has ended.
	 *
	 */
	public void chatEnded() {
		setChanged();
		notifyObservers(Constants.CHAT_ENDED);
	}

	/**
	 * Adds the person to the conversation
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean addPerson(MessageUS message) {
		boolean successful = peopleInConversation.add(message.getLogin());
		setChanged();
		notifyObservers(message);
		return successful;
	}

	/**
	 * Gets the people in conversation if it is a group chat
	 *
	 * @return the people in conversation
	 */
	public Set<String> getPeopleInConversation() {
		return peopleInConversation;
	}
}
