package com.chat.domains;

import java.util.LinkedList;
import java.util.Queue;

public class MessageHistory extends BaseEntity {
	private final String MESSAGE_ID;
	private Queue<String> _historyTexts;

	public MessageHistory(String messageId) {
		super();
		MESSAGE_ID = messageId;
		this._historyTexts = new LinkedList<>();
	}

	public Queue<String> getHistoryTexts() {
		return _historyTexts;
	}

	public void addText(String text) {
		this._historyTexts.add(text);
	}

	public String getMessageId() {
		return MESSAGE_ID;
	}

}
