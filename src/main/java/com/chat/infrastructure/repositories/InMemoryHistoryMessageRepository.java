package com.chat.infrastructure.repositories;

import com.chat.domains.MessageHistory;
import com.chat.usecases.adapters.HistoryMessageRepository;

public class InMemoryHistoryMessageRepository extends InMemoryRepository<MessageHistory>
		implements HistoryMessageRepository {

	@Override
	public MessageHistory findHistoryByMessageId(String id) {
		return super.getFirst(m -> m.getMessageId().equals(id));
	}

}
