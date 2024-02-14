package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.message.DeletingMessage.DeletingMessageResult;

class DeletingMessageTest {
	@BeforeEach
	public void setUp() throws FileNotFoundException, IOException {
		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(sender);
		storage.getUserRepository().add(receiver);

		SendingMessage.InputValues input = new SendingMessage.InputValues(sender.getId(), receiver.getId(), "Hello",
				new HashMap<>());
		SendingMessage sendingMessage = new SendingMessage(storage);
		sendingMessage.execute(input);

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testDeleteMessageSuccess() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Message message = storage.getMessageRepository().getAll().get(0);
		User sender = storage.getUserRepository().getAll().get(0);

		DeletingMessage.InputValues input = new DeletingMessage.InputValues(sender.getId(), message.getId());
		DeletingMessage deletingMessage = new DeletingMessage(storage);
		DeletingMessage.OutputValues output = deletingMessage.execute(input);

		assertEquals(DeletingMessageResult.Successed, output.getResult());
	}
	
	@Test
	void testDeleteMessageFail() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Message message = storage.getMessageRepository().getAll().get(0);
		User sender = new User("Tuan", "", "", "", false, null);

		DeletingMessage.InputValues input = new DeletingMessage.InputValues(sender.getId(), message.getId());
		DeletingMessage deletingMessage = new DeletingMessage(storage);
		DeletingMessage.OutputValues output = deletingMessage.execute(input);

		assertEquals(DeletingMessageResult.Failed, output.getResult());
	}

}
