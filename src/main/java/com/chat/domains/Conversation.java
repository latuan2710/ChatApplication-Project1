package com.chat.domains;

import java.util.List;

public class Conversation extends ChatEntity {
	private List<Message> _messages;
	private List<ChatEntity> _participants;

	public Conversation(List<Message> messages, List<ChatEntity> participants) {
		super();
		this._messages = messages;
		this._participants = participants;
	}

	public List<Message> getMessages() {
		return _messages;
	}

	public void setMessages(List<Message> messages) {
		this._messages = messages;
	}

	public void addMessage(Message message) {
		if (!_messages.contains(message)) {
			this._messages.add(message);
		}
	}

	public List<ChatEntity> getParticipants() {
		return _participants;
	}

	public void setParticipants(List<ChatEntity> participants) {
		this._participants = participants;
	}
}
