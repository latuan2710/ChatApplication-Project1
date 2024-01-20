package com.chat.infrastructure.data;

import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.UserRepository;
import com.chat.domains.User;
import com.chat.infrastructure.repositories.InMemoryRepository;

public class InMemoryDataStorage implements DataStorage {
    private UserRepository<User> _users;

    private static InMemoryDataStorage _storage;

    private InMemoryDataStorage() {
        _users = new InMemoryRepository<User>();
    }

    public static InMemoryDataStorage getInstance() {
        if (_storage == null) {
            _storage = new InMemoryDataStorage();
        }
        return _storage;
    }

    public UserRepository<User> getUserRepository(){
        return _users;
    }

    @Override
    public void cleanAll() {
        _users.deleteAll();
    }
}
