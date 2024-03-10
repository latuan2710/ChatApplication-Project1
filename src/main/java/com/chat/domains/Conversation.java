package com.chat.domains;

import java.util.List;

public class Conversation {
	private String _id;
	private List<Message> _messages;
	private List<ChatEntity> _participants;

	public Conversation(String id, List<Message> messages, List<ChatEntity> participants) {
		this._id = id;
		this._messages = messages;
		this._participants = participants;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
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

	public boolean haveMessage(Message message) {
		return _messages.contains(message);
	}

	public List<ChatEntity> getParticipants() {
		return _participants;
	}

	public void setParticipants(List<ChatEntity> participants) {
		this._participants = participants;
	}
}
