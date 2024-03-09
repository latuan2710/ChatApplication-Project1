package com.chat.usecases.user;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.user.UserSearching.SearchingResult;

class UserSearchingTest {

	@BeforeEach
	public void setUp() {
		User user1 = new User("dasdas", "", "", "", false, null);
		User user2 = new User("dasdks", "", "", "", false, null);
		User user3 = new User("", "dasdas", "", null, false, null);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user1);
		storage.getUserRepository().add(user2);
		storage.getUserRepository().add(user3);
	}

	@Test
	public void searchSuccessfully() {
		UserSearching.InputValues input = new UserSearching.InputValues("sda");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserSearching finder = new UserSearching(storage);
		UserSearching.OutputValues output = finder.execute(input);

		Assert.assertEquals(SearchingResult.Successed, output.getResult());
		Assert.assertEquals(output.getUsers().size(), 2);
	}

	@Test
	public void searchFailed() {
		UserSearching.InputValues input = new UserSearching.InputValues("sdak");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserSearching finder = new UserSearching(storage);
		UserSearching.OutputValues output = finder.execute(input);

		Assert.assertEquals(SearchingResult.Failed, output.getResult());
		Assert.assertEquals(output.getUsers(), null);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}
}
