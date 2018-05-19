package view;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class EmojiManage. Creates a map of emojis. Used to add the emoji in the
 * messages field.
 */
public class EmojiManage {

	/** The Emoji map. */
	Map<String, String> EmojiMap;

	/**
	 * Instantiates a new emoji manage.
	 */
	public EmojiManage() {
		EmojiMap = new HashMap<>();

		EmojiMap.put("Crying_Small", setEmojiPath("Crying_Small"));
		EmojiMap.put("Flushed_Emoji_Icon", setEmojiPath("Flushed_Emoji_Icon"));
		EmojiMap.put("Thinking", setEmojiPath("Thinking"));
		EmojiMap.put("Crying_Large", setEmojiPath("Crying_Large"));
		EmojiMap.put("Devil_Emoji", setEmojiPath("Devil_Emoji"));
		EmojiMap.put("Smiling", setEmojiPath("Smiling"));
		EmojiMap.put("Smirk_face", setEmojiPath("Smirk_face"));
		EmojiMap.put("Sunglasses_cool", setEmojiPath("Sunglasses_cool"));
		EmojiMap.put("Thumbs_Up", setEmojiPath("Thumbs_Up"));
		EmojiMap.put("Victory_Hand", setEmojiPath("Victory_Hand"));
		EmojiMap.put("Waving_Hand", setEmojiPath("Waving_Hand"));
		EmojiMap.put("Woman_Saying_Yes", setEmojiPath("Woman_Saying_Yes"));
		EmojiMap.put("Ghost", setEmojiPath("Ghost"));
		EmojiMap.put("Heart_Eyes", setEmojiPath("Heart_Eyes"));
		EmojiMap.put("High_Five", setEmojiPath("High_Five"));
		EmojiMap.put("Kiss", setEmojiPath("Kiss"));
		EmojiMap.put("Man_With_Turban", setEmojiPath("Man_With_Turban"));
		EmojiMap.put("Middle_Finger", setEmojiPath("Middle_Finger"));
		EmojiMap.put("Money_Face", setEmojiPath("Money_Face"));
		EmojiMap.put("LOL", setEmojiPath("LOL"));
		EmojiMap.put("OMG", setEmojiPath("OMG"));
		EmojiMap.put("Pinocchio", setEmojiPath("Pinocchio"));
		EmojiMap.put("Poop", setEmojiPath("Poop"));
		EmojiMap.put("Raised_Fist", setEmojiPath("Raised_Fist"));
		EmojiMap.put("Robot", setEmojiPath("Robot"));
		EmojiMap.put("Sad", setEmojiPath("Sad"));
		EmojiMap.put("Sick", setEmojiPath("Sick"));
		EmojiMap.put("Sleeping", setEmojiPath("Sleeping"));
		EmojiMap.put("Slightly_Smiling", setEmojiPath("Slightly_Smiling"));
		EmojiMap.put("Super_Angry_Face", setEmojiPath("Super_Angry_Face"));
		EmojiMap.put("Zipper_mouth", setEmojiPath("Zipper_mouth"));
		EmojiMap.put("Chenglong_TBH", setEmojiPath("Chenglong_TBH"));

	}

	/**
	 * Sets the emoji path.
	 *
	 * @param name the name
	 * @return the string
	 */
	public String setEmojiPath(String name) {
		String wholePath = "emoji/" + name + ".png";
		return wholePath;
	}

	/**
	 * Checks if the given worf is an emoji.
	 *
	 * @param emojiName the emoji name
	 * @return true, if is emoji
	 */
	public boolean isEmoji(String emojiName) {
		return EmojiMap.containsKey(emojiName);
	}

	/**
	 * Gets the emoji path.
	 *
	 * @param emojiName the emoji name
	 * @return the emoji path
	 */
	public String getEmojiPath(String emojiName) {
		return EmojiMap.get(emojiName);
	}

}
