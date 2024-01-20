package com.chat.usecases.adapters;

import com.chat.domains.User;

public interface DataStorage {
	UserRepository<User> getUserRepository();

	void cleanAll();
}
