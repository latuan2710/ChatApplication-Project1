package com.chat.infrastructure.repositories;

import com.chat.domains.Message;
import com.chat.usecases.adapters.MessageRepository;

public class InMemoryMessageRepository extends InMemoryRepository<Message> implements MessageRepository {

}
