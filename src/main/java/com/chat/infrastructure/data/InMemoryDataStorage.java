package com.chat.infrastructure.data;

import com.chat.domains.Message;
import com.chat.domains.MessageHistory;
import com.chat.domains.User;
import com.chat.infrastructure.repositories.InMemoryGroupRepository;
import com.chat.infrastructure.repositories.InMemoryRepository;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class InMemoryDataStorage implements DataStorage {
	private Repository<User> _users;
	private Repository<Message> _messages;
	private Repository<MessageHistory> _messageHistory;
	private GroupRepository _groups;

	private static InMemoryDataStorage _storage;

	private InMemoryDataStorage() {
		_users = new InMemoryRepository<User>();
		_groups = new InMemoryGroupRepository();
		_messages = new InMemoryRepository<Message>();
		_messageHistory = new InMemoryRepository<MessageHistory>();
	}

	public static InMemoryDataStorage getInstance() {
		if (_storage == null) {
			_storage = new InMemoryDataStorage();
		}
		return _storage;
	}

	@Override
	public Repository<User> getUserRepository() {
		return _users;
	}

	@Override
	public Repository<Message> getMessageRepository() {
		return _messages;
	}

	@Override
	public Repository<MessageHistory> getMessageHistoryRepository() {
		return _messageHistory;
	}

	@Override
	public GroupRepository getGroupRepository() {
		return _groups;
	}

	@Override
	public void cleanAll() {
		_users.deleteAll();
		_groups.deleteAll();
		_messages.deleteAll();
		_messageHistory.deleteAll();
	}

}
