package grimsby.libraries.dataaccess.jdbc;

/**
 * Class that generates temporary passwords for users who have forgotten their password.
 * 
 * @author Christopher Friis (cxf798)
 * @version 7 Mar 2018
 */
public class PwGenerator {
	
	/**
	 * Method for generating passwords.
	 * Characters A-Z are 65 to 90.
	 * Characters a-z are 97 to 122.
	 * @param n How many characters.
	 */
	public static String generate(int n) {
		StringBuilder s = new StringBuilder();
		int count = 0;
		
		while(!test(s.toString())) {
			s.delete(0, s.length());
			for (int i = 0; i < n; i++) {
				
				if(isNumber()){
					s.append((char)(Math.random()*(57-48) + 48));
				} else {
					if(isUpper()) {
						s.append((char)(Math.random()*(90-65)+65));
					} else {
						s.append((char)(Math.random()*(122-97)+97));
					}

				}
				
			}
			
			test(s.toString());
			count++;
			if (count>20) {
				System.out.println("Password generation failed.");
				System.exit(0);
			}
		}
		
		return s.toString();
	}
	
	/**
	 * Private method to determine whether the next character will be a number.
	 * @return True if the next character is a number.
	 */
	private static boolean isNumber() {
		if (Math.random()<0.2) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Private method to determine whether the next character will be an upper case letter.
	 * @return True if the next character is an upper case letter.
	 */
	private static boolean isUpper() {
		if (Math.random()<0.5) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Test method to ensure generated password has at least one lowercase, one uppercase, and one number
	 * @param s The password being tested.
	 * @return
	 */
	private static boolean test(String s) {
		if (!s.matches(".*[A-Z].*")) return false;
		if (!s.matches(".*[a-z].*")) return false;
		if (!s.matches(".*\\d.*")) return false;
		return true;
	}
	
}
