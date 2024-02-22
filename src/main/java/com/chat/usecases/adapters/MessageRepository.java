package com.chat.usecases.adapters;

import java.util.List;

import com.chat.domains.File;
import com.chat.domains.Message;

public interface MessageRepository extends Repository<Message> {
	List<Message> getAllMessageByUserId(String userId);

	List<File> getAllFileByUserId(String userId);

}
