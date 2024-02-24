package com.chat.usecases.adapters;

import com.chat.domains.User;

public interface DataStorage {
	Repository<User> getUserRepository();

	MessageRepository getMessageRepository();
	
	HistoryMessageRepository getMessageHistoryRepository();

	GroupRepository getGroupRepository();

	void cleanAll();
}
