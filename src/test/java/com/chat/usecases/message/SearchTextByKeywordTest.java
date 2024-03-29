package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.SearchTextByKeyword.SearchTextByKeywordResult;

class SearchTextByKeywordTest {

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
	public void searchTextByKeywordTestSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		User sender = userRepository.getAll().get(0);
		User receiver = userRepository.getAll().get(1);

		SearchTextByKeyword.InputValues input = new SearchTextByKeyword.InputValues("0", sender.getId(),
				receiver.getId());
		SearchTextByKeyword searchTextByKeyword = new SearchTextByKeyword(storage);
		SearchTextByKeyword.OutputValues output = searchTextByKeyword.execute(input);
		
		assertEquals(SearchTextByKeywordResult.Successed, output.getResult());

	}
	
	@Test
	public void searchTextByKeywordTestFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		
		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);
		
		SendingMessage sendingMessage = new SendingMessage(storage);
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "96"));
		sendingMessage.execute(new SendingMessage.InputValues(sender.getId(), receiver.getId(), "97"));
		
		SearchTextByKeyword.InputValues input = new SearchTextByKeyword.InputValues("0", sender.getId(),
				receiver.getId());
		SearchTextByKeyword searchTextByKeyword = new SearchTextByKeyword(storage);
		SearchTextByKeyword.OutputValues output = searchTextByKeyword.execute(input);
		
		assertEquals(SearchTextByKeywordResult.Failed, output.getResult());

	}
}
