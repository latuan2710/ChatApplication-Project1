package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

class TimeBoundMessageRetrieverTest {

	@BeforeEach
	public void setUp() throws FileNotFoundException, IOException {
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

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testTimeBoundMessageRetriever() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		User sender = userRepository.getAll().get(0);
//		User receiver = userRepository.getAll().get(1);

		Date time = new Date();

		TimeBoundMessageRetriever.InputValues input = new TimeBoundMessageRetriever.InputValues(sender.getId(), 3,
				time);

		TimeBoundMessageRetriever timeBoundMessageRetriever = new TimeBoundMessageRetriever(storage);
		TimeBoundMessageRetriever.OutputValues output = timeBoundMessageRetriever.execute(input);

		List<Message> messages = output.getMessages();
	
		assertEquals(3, messages.size());

	}
}
