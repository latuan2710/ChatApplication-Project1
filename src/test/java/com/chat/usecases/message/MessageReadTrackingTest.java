package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.GroupCreation;
import com.chat.usecases.group.GroupJoining;
import com.chat.usecases.message.MessageReadTracking.MessageReadTrackingResult;

class MessageReadTrackingTest {
	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User user1 = new User("Tuan", "", "", "", false, null);
		User user2 = new User("Nhan", "", "", "", false, null);
		User user3 = new User("Nghia", "", "", "", false, null);

		userRepository.add(user1);
		userRepository.add(user2);
		userRepository.add(user3);

		GroupCreation pb = new GroupCreation(storage);
		Group group = pb.execute(new GroupCreation.InputValues(user1.getId(), GroupType.Public, "public")).getGroup();

		GroupJoining groupJoining = new GroupJoining(storage);
		groupJoining.execute(new GroupJoining.InputValues(user2.getId(), user1.getId(), group.getId()));
		groupJoining.execute(new GroupJoining.InputValues(user3.getId(), user1.getId(), group.getId()));

		SendingMessage.InputValues sendingMessageInput1 = new SendingMessage.InputValues(user1.getId(), group.getId(),
				"from user1 to group");
		SendingMessage.InputValues sendingMessageInput2 = new SendingMessage.InputValues(user2.getId(), group.getId(),
				"from user2 to group");
		SendingMessage sendingMessage = new SendingMessage(storage);
		Message message1 = sendingMessage.execute(sendingMessageInput1).getMessage();
		Message message2 = sendingMessage.execute(sendingMessageInput2).getMessage();

		RecordingMessage recordingMessage = new RecordingMessage(storage);
		recordingMessage.execute(new RecordingMessage.InputValues(user2.getId(), message1.getId()));
		recordingMessage.execute(new RecordingMessage.InputValues(user2.getId(), message2.getId()));
		recordingMessage.execute(new RecordingMessage.InputValues(user1.getId(), message1.getId()));
		recordingMessage.execute(new RecordingMessage.InputValues(user1.getId(), message2.getId()));
		recordingMessage.execute(new RecordingMessage.InputValues(user3.getId(), message1.getId()));
		recordingMessage.execute(new RecordingMessage.InputValues(user3.getId(), message2.getId()));
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testRunSuccessful1() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = storage.getUserRepository().getAll().get(0);
		Group group = storage.getGroupRepository().getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(0);

		String groupConversationId = group.getId() + group.getName();

		MessageReadTracking messageReadTracking = new MessageReadTracking(storage);
		MessageReadTracking.InputValues input = new MessageReadTracking.InputValues(user.getId(), groupConversationId,
				message.getId());
		MessageReadTracking.OutputValues output = messageReadTracking.execute(input);

		assertEquals(MessageReadTrackingResult.Successed, output.getResult());
		assertEquals(0, output.getUsers().size());
	}

	@Test
	public void testRunSuccessful2() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = storage.getUserRepository().getAll().get(1);
		Group group = storage.getGroupRepository().getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(1);

		String groupConversationId = group.getId() + group.getName();

		MessageReadTracking messageReadTracking = new MessageReadTracking(storage);
		MessageReadTracking.InputValues input = new MessageReadTracking.InputValues(user.getId(), groupConversationId,
				message.getId());
		MessageReadTracking.OutputValues output = messageReadTracking.execute(input);

		assertEquals(MessageReadTrackingResult.Successed, output.getResult());
		assertEquals(2, output.getUsers().size());
	}

}
