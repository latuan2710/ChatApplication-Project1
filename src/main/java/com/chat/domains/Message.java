package com.chat.domains;

import java.util.Date;
import java.util.List;

public class Message extends ChatEntity {
	private User _sender;
	private ChatEntity _receiver;
	private Date _time;
	private String _text;
	private List<File> _attachments;

	public Message(User sender, ChatEntity receiver, String text, List<File> attachments) {
		super();
		this._sender = sender;
		this._receiver = receiver;
		this._time = new Date();
		this._text = text;
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
		return _time;
	}

	public void setTimestamp(Date timestamp) {
		this._time = timestamp;
	}

	public String getContent() {
		return _text;
	}

	public void setContent(String content) {
		this._text = content;
	}

	public List<File> getAttachments() {
		return _attachments;
	}

	public void setAttachments(List<File> attachments) {
		this._attachments = attachments;
	}

}
