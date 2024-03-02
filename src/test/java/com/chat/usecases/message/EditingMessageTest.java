package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.message.EditingMessage.EditingMessageResult;

class EditingMessageTest {
	@BeforeEach
	public void setUp() throws FileNotFoundException, IOException {
		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(sender);
		storage.getUserRepository().add(receiver);

		SendingMessage sendingMessage = new SendingMessage(storage);
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "Hello"));
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testEditMessageSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = storage.getUserRepository().getAll().get(0);
		Message message = storage.getMessageRepository().getAll().get(0);

		EditingMessage.InputValues inputValues = new EditingMessage.InputValues(sender.getId(), message.getId(),
				"After Editing");
		EditingMessage editingMessage = new EditingMessage(storage);
		EditingMessage.OutputValues output = editingMessage.execute(inputValues);

		GettingMessageHistory gettingMessageHistory = new GettingMessageHistory(storage);
		GettingMessageHistory.InputValues gettingMessageHistoryInput = new GettingMessageHistory.InputValues(
				message.getId());
		GettingMessageHistory.OutputValues gettingMessageHistoryOutput = gettingMessageHistory
				.execute(gettingMessageHistoryInput);

		List<String> historyText = gettingMessageHistoryOutput.getMessageHistory().getHistoryTexts();

		assertEquals("After Editing", output.getNewMessage().getContent());
		assertEquals("Hello", historyText.get(0));
	}

	@Test
	void testEditMessageFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User receiver = storage.getUserRepository().getAll().get(1);
		Message message = storage.getMessageRepository().getAll().get(0);

		EditingMessage.InputValues inputValues = new EditingMessage.InputValues(receiver.getId(), message.getId(),
				"After Editing");
		EditingMessage editingMessage = new EditingMessage(storage);
		EditingMessage.OutputValues output = editingMessage.execute(inputValues);

		assertEquals(EditingMessageResult.Failed, output.getResult());
	}

}
