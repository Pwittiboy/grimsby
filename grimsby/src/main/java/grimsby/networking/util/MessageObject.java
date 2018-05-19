package grimsby.networking.util;

/**
 * The Class MessageObject.
 */
public class MessageObject {

	/** The message text. */
	private String message;

	/** The is emoji flag. */
	private boolean isEmoji;

	/**
	 * Instantiates a new message object.
	 *
	 * @param message the message text
	 * @param isEmoji the emoji flag
	 */
	public MessageObject(String message, boolean isEmoji) {
		this.message = message;
		this.isEmoji = isEmoji;
	}

	/**
	 * Return whether the message contains the emoji.
	 *
	 * @return true, if contains, false otherwise
	 */
	public boolean isEmoji() {
		return this.isEmoji;
	}

	/**
	 * Gets the message text.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

}
