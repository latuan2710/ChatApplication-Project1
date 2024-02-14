package com.chat.domains;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory extends BaseEntity {
	private final String MESSAGE_ID;
	private List<String> _historyTexts;

	public MessageHistory(String messageId) {
		super();
		MESSAGE_ID = messageId;
		this._historyTexts = new ArrayList<>();
	}

	public List<String> getHistoryTexts() {
		return _historyTexts;
	}

	public void addText(String text) {
		this._historyTexts.add(text);
	}

	public String getMessageId() {
		return MESSAGE_ID;
	}

}
