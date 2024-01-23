package com.chat.domains;

import java.util.List;

public class Conversation extends ChatEntity {
	private List<Message> _messages;
	private List<User> _participants;

	public Conversation(List<Message> messages, List<User> participants) {
		super();
		this._messages = messages;
		this._participants = participants;
	}

	public List<Message> get_messages() {
		return _messages;
	}

	public void set_messages(List<Message> _messages) {
		this._messages = _messages;
	}

	public List<User> getParticipants() {
		return _participants;
	}

	public void setParticipants(List<User> participants) {
		this._participants = participants;
	}

}
