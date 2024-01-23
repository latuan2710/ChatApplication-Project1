package com.chat.domains;

import java.util.Date;
import java.util.List;

public class Message extends ChatEntity {
	private User _sender;
	private ChatEntity _receiver;
	private Date _timestamp;
	private String _content;
	private List<File> _attachments;

	public Message(User sender, ChatEntity receiver, Date timestamp, String content, List<File> attachments) {
		super();
		this._sender = sender;
		this._receiver = receiver;
		this._timestamp = timestamp;
		this._content = content;
		this._attachments = attachments;
	}

	public User getSender() {
		return _sender;
	}

	public void setSender(User sender) {
		this._sender = sender;
	}

	public ChatEntity getReceiver() {
		return _receiver;
	}

	public void setReceiver(ChatEntity receiver) {
		this._receiver = receiver;
	}

	public Date getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this._timestamp = timestamp;
	}

	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		this._content = content;
	}

	public List<File> getAttachments() {
		return _attachments;
	}

	public void setAttachments(List<File> attachments) {
		this._attachments = attachments;
	}

}
