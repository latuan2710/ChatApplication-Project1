package com.chat.domains;

public class MessageRecorder extends BaseEntity {
	private Message _message;
	private User _user;

	public MessageRecorder(Message message, User user) {
		super();
		this._message = message;
		this._user = user;
	}

	public Message getMessage() {
		return _message;
	}

	public void setMessage(Message message) {
		this._message = message;
	}

	public User getUser() {
		return _user;
	}

	public void setUser(User user) {
		this._user = user;
	}
}
