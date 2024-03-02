package com.chat.usecases.adapters;

import com.chat.domains.Message;
import com.chat.domains.MessageHistory;
import com.chat.domains.User;

public interface DataStorage {
	Repository<User> getUserRepository();

	Repository<Message> getMessageRepository();

	Repository<MessageHistory> getMessageHistoryRepository();

	GroupRepository getGroupRepository();

	void cleanAll();

}
