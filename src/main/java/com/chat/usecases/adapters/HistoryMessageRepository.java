package com.chat.usecases.adapters;

import com.chat.domains.MessageHistory;

public interface HistoryMessageRepository extends Repository<MessageHistory> {
	MessageHistory findHistoryByMessageId(String id);

}
