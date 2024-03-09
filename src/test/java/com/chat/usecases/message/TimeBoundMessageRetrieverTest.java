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
import com.chat.usecases.message.TimeBoundMessageRetriever.TimeBoundMessageRetrieverResult;

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
	public void testTimeBoundMessageRetrieverSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		User sender = userRepository.getAll().get(0);

		Date time = new Date();

		TimeBoundMessageRetriever.InputValues input = new TimeBoundMessageRetriever.InputValues(sender.getId(), 3,
				time);

		TimeBoundMessageRetriever timeBoundMessageRetriever = new TimeBoundMessageRetriever(storage);
		TimeBoundMessageRetriever.OutputValues output = timeBoundMessageRetriever.execute(input);

		assertEquals(TimeBoundMessageRetrieverResult.Successed, output.getResult());

	}
	
	@Test
	public void testTimeBoundMessageRetrieverFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
	
		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);
		
		Date time = new Date();

		TimeBoundMessageRetriever.InputValues input = new TimeBoundMessageRetriever.InputValues(sender.getId(), 3,
				time);

		TimeBoundMessageRetriever timeBoundMessageRetriever = new TimeBoundMessageRetriever(storage);
		TimeBoundMessageRetriever.OutputValues output = timeBoundMessageRetriever.execute(input);
		assertEquals(TimeBoundMessageRetrieverResult.Failed, output.getResult());

	}
}
