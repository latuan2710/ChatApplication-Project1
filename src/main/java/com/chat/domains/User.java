package com.chat.domains;

import java.util.Date;

public class User extends BaseEntity {
	private String _username;
	private String _lastName;
	private String _firstName;
	private String _hashedPassword;
	private boolean _isMale;
	private Date _dateOfBirth;

	public User(String username, String lastName, String firstName, String hashedPassword, boolean isMale,
			Date dateOfBirth) {
		super();
		this._username = username;
		this._lastName = lastName;
		this._firstName = firstName;
		this._hashedPassword = hashedPassword;
		this._isMale = isMale;
		this._dateOfBirth = dateOfBirth;
	}

	public String getUsername() {
		return _username;
	}

	public String getLastName() {
		return _lastName;
	}

	public String getFirstName() {
		return _firstName;
	}

	public String getFullName() {
		return _firstName + " " + _lastName;
	}

	public String getHashedPassword() {
		return _hashedPassword;
	}

	public boolean isMale() {
		return _isMale;
	}

	public Date getDateOfBirth() {
		return _dateOfBirth;
	}

	public static class UserBuilder {
		private String _lastName;
		private String _firstName;
		private String _username;
		private String _password;
		private boolean _isMale;
		private Date _dateOfBirth;

		public UserBuilder(String username, String password) {
			this._username = username;
			this._password = password;
		}

		public UserBuilder setLastName(String lastName) {
			this._lastName = lastName;
			return this;
		}

		public UserBuilder setFirstName(String firstName) {
			this._firstName = firstName;
			return this;
		}

		public UserBuilder isMale(boolean isMale) {
			this._isMale = isMale;
			return this;
		}

		public UserBuilder dateOfBirth(Date dateOfBirth) {
			this._dateOfBirth = dateOfBirth;
			return this;
		}

		public User build() {
			User user = new User(_username, _lastName, _firstName, _password, _isMale, _dateOfBirth);
			return user;
		}
	}
}
