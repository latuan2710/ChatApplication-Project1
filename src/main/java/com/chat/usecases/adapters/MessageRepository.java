package com.chat.usecases.adapters;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;

public interface MessageRepository extends Repository<Message> {
	void saveConversation();
	
}
