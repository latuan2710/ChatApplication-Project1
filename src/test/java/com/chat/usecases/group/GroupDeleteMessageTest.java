package com.chat.usecases.group;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupDeleteMessage.GroupDeleteMessageResult;
import com.chat.usecases.message.GettingMessages;
import com.chat.usecases.message.SendingMessage;

class GroupDeleteMessageTest {

	@BeforeEach
	public void setUp() throws FileNotFoundException, IOException {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = new User("Tuan", "", "", "", false, null);
		User member = new User("Nghia", "", "", "", false, null);

		storage.getUserRepository().add(sender);
		storage.getUserRepository().add(member);

		GroupCreation groupCreation = new GroupCreation(storage);
		Group publicGroup = groupCreation.execute(new GroupCreation.InputValues(sender.getId(), GroupType.Public))
				.getGroup();
		Group privateGroup = groupCreation.execute(new GroupCreation.InputValues(sender.getId(), GroupType.Private))
				.getGroup();

		GroupJoining groupJoining = new GroupJoining(storage);
		groupJoining.execute(new GroupJoining.InputValues(member.getId(), sender.getId(), publicGroup.getId()));
		groupJoining.execute(new GroupJoining.InputValues(member.getId(), sender.getId(), privateGroup.getId()));

		SendingMessage sendingMessage = new SendingMessage(storage);
		sendingMessage
				.execute(new SendingMessage.InputValues(sender.getId(), publicGroup.getId(), "Hello Public Group"));
		sendingMessage
				.execute(new SendingMessage.InputValues(sender.getId(), privateGroup.getId(), "Hello Private Group"));

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testDeleteMessageInPrivateGroupSuccess() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = storage.getUserRepository().getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(1);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);

		GroupDeleteMessage.InputValues input = new GroupDeleteMessage.InputValues(sender.getId(), message.getId(),
				group.getId());
		GroupDeleteMessage deletingMessage = new GroupDeleteMessage(storage);
		GroupDeleteMessage.OutputValues output = deletingMessage.execute(input);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(sender.getId());
		GettingMessages gettingAllMessages = new GettingMessages(storage);
		List<Message> messages = gettingAllMessages.execute(gettingAllMessagesInput).getMessages();

		assertEquals(GroupDeleteMessageResult.Successed, output.getResult());
		assertEquals(1, messages.size());
		assertEquals("Hello Public Group", messages.get(0).getContent());
	}

	@Test
	void testDeleteMessageInPublicGroupSuccess() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = storage.getUserRepository().getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(0);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);

		GroupDeleteMessage.InputValues input = new GroupDeleteMessage.InputValues(sender.getId(), message.getId(),
				group.getId());
		GroupDeleteMessage deletingMessage = new GroupDeleteMessage(storage);
		GroupDeleteMessage.OutputValues output = deletingMessage.execute(input);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(sender.getId());
		GettingMessages gettingAllMessages = new GettingMessages(storage);
		List<Message> messages = gettingAllMessages.execute(gettingAllMessagesInput).getMessages();

		assertEquals(GroupDeleteMessageResult.Successed, output.getResult());
		assertEquals(1, messages.size());
		assertEquals("Hello Private Group", messages.get(0).getContent());
	}

	@Test
	void testDeleteMessageInPrivateGroupFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User member = storage.getUserRepository().getAll().get(1);
		Message message = storage.getMessageRepository().getAll().get(1);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);

		GroupDeleteMessage.InputValues input = new GroupDeleteMessage.InputValues(member.getId(), message.getId(),
				group.getId());
		GroupDeleteMessage deletingMessage = new GroupDeleteMessage(storage);
		GroupDeleteMessage.OutputValues output = deletingMessage.execute(input);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(member.getId());
		GettingMessages gettingAllMessages = new GettingMessages(storage);
		List<Message> messages = gettingAllMessages.execute(gettingAllMessagesInput).getMessages();

		assertEquals(GroupDeleteMessageResult.Failed, output.getResult());
		assertEquals(2, messages.size());
	}

	@Test
	void testDeleteMessageInPublicGroupFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User member = storage.getUserRepository().getAll().get(1);
		Message message = storage.getMessageRepository().getAll().get(0);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);

		GroupDeleteMessage.InputValues input = new GroupDeleteMessage.InputValues(member.getId(), message.getId(),
				group.getId());
		GroupDeleteMessage deletingMessage = new GroupDeleteMessage(storage);
		GroupDeleteMessage.OutputValues output = deletingMessage.execute(input);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(member.getId());
		GettingMessages gettingAllMessages = new GettingMessages(storage);
		List<Message> messages = gettingAllMessages.execute(gettingAllMessagesInput).getMessages();

		assertEquals(GroupDeleteMessageResult.Failed, output.getResult());
		assertEquals(2, messages.size());
	}

}
