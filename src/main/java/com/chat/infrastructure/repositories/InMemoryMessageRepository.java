package com.chat.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.File;
import com.chat.domains.Message;
import com.chat.usecases.adapters.MessageRepository;

public class InMemoryMessageRepository extends InMemoryRepository<Message> implements MessageRepository {

}
