/**
 * 
 */
package grimsby.libraries.entities;

import java.sql.Timestamp;

/**
 * Entity class for contacts table.
 *
 * @author Christopher Friis (cxf798)
 * @version 1 Mar 2018
 */
public class Contact extends AbstractEntity {

	/**
	 * Represents owner_id column.
	 */
	private AccountEntity owner;
	
	/**
	 * Represents contact_account_id column.
	 */
	private AccountEntity contactAccount;
	
	/**
	 * Represents block column.
	 */
	private boolean blocked;
	
	/**
	 * Constructor for creating a contact object. 
	 * @param owner
	 * @param contactAccount
	 */
	public Contact(AccountEntity owner, AccountEntity contactAccount, boolean blocked) {
		this(0, owner, contactAccount, blocked, null);
	}
	
	/**
	 * Constructor for instantiating a contact from the database.
	 * @param id
	 * @param owner
	 * @param contactAccount
	 * @param timestamp
	 */
	public Contact(int id, AccountEntity owner, AccountEntity contactAccount, boolean blocked, Timestamp createdAt) {
		this.id = id;
		this.owner = owner;
		this.contactAccount = contactAccount;
		this.blocked = blocked;
		this.timestamp = createdAt;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public AccountEntity getOwner() {
		return owner;
	}

	public AccountEntity getContactAccount() {
		return contactAccount;
	}
	
	@Override
	public boolean equals(Object contact) {
		return this.owner == ((Contact)contact).getOwner() &&
				this.contactAccount == ((Contact)contact).getContactAccount();
	}

}
