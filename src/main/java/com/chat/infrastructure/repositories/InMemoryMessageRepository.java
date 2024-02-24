package com.chat.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.File;
import com.chat.domains.Message;
import com.chat.usecases.adapters.MessageRepository;

public class InMemoryMessageRepository extends InMemoryRepository<Message> implements MessageRepository {

	@Override
	public List<Message> getAllMessageByUserId(String userId) {
		Predicate<Message> predicate = m -> (m.getSender().getId().equals(userId))
				|| m.getReceiver().getId().equals(userId);
		List<Message> messages = this.getAll();
		List<Message> result = messages.stream().filter(predicate).toList();

		return result.isEmpty() ? null : result;
	}

}
