package com.chat.usecases.message;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.RecordingMessage.RecordingMessageResult;

class MessageRecordTest {

	@BeforeEach
	public void setUp() {
		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(sender);
		storage.getUserRepository().add(receiver);

		SendingMessage sendingMessage = new SendingMessage(storage);

		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "96"));
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "97"));
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "98"));
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "99"));
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "100"));
	}

	@Test
	public void testSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		User sender = userRepository.getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(0);

		RecordingMessage.InputValues input = new RecordingMessage.InputValues(sender.getId(), message.getId());
		RecordingMessage recordingMessage = new RecordingMessage(storage);
		RecordingMessage.OutputValues output = recordingMessage.execute(input);

		assertEquals(RecordingMessageResult.Successed, output.getResult());

	}

	@Test
	public void testFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = new User("Tuan", "", "", "", false, null);

		Message message = storage.getMessageRepository().getAll().get(0);

		RecordingMessage.InputValues input = new RecordingMessage.InputValues(user.getId(), message.getId());
		RecordingMessage recordingMessage = new RecordingMessage(storage);
		RecordingMessage.OutputValues output = recordingMessage.execute(input);

		assertEquals(RecordingMessageResult.Failed, output.getResult());

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

}
