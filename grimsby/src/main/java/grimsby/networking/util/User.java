package grimsby.networking.util;

import java.io.Serializable;

/**
 * The Class User. Used for storing the most important data about the client:
 * one's photo and username
 */
public class User implements Serializable {

	/** The photo. */
	private byte[] photo;

	/** The username. */
	private String username;

	/**
	 * Instantiates a new user.
	 *
	 * @param photo the photo
	 * @param name the name
	 */
	public User(byte[] photo, String name) {
		super();
		this.photo = photo;
		this.username = name;
	}

	/**
	 * Gets the photo.
	 *
	 * @return the photo
	 */
	public byte[] getPhoto() {
		return photo;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return username;
	}
}
