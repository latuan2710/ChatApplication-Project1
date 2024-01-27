package com.chat.infrastructure.data;

import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;
import com.chat.domains.Group;
import com.chat.domains.User;
import com.chat.infrastructure.repositories.InMemoryGroupRepository;
import com.chat.infrastructure.repositories.InMemoryRepository;

public class InMemoryDataStorage implements DataStorage {
	private Repository<User> _users;
	private GroupRepository _groups;

	private static InMemoryDataStorage _storage;

	private InMemoryDataStorage() {
		_users = new InMemoryRepository<User>();
		_groups = new InMemoryGroupRepository();
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
	public Repository<Group> getGroupRepository() {
		return _groups;
	}

	@Override
	public void cleanAll() {
		_users.deleteAll();
		_groups.deleteAll();
	}
}
