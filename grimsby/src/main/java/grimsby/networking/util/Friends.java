package grimsby.networking.util;

import java.io.File;

/**
 * The Class Friends.
 */
public class Friends {

	/** The friends photo. */
	private File friendsPhoto;

	/** The name of the frend. */
	private String name;

	/**
	 * Instantiates a new friends.
	 *
	 * @param name the name of the friend
	 * @param friendsPhoto the photo of the frend
	 */
	public Friends(String name, File friendsPhoto) {
		this.name = name;
		this.friendsPhoto = friendsPhoto;
	}

	/**
	 * Gets the friend;s photo.
	 *
	 * @return the friends photo
	 */
	public File getFriendsPhoto() {
		return this.friendsPhoto;
	}

	/**
	 * Gets the friend's name.
	 *
	 * @return the friends name
	 */
	public String getFriendsName() {
		return this.name;
	}
}
