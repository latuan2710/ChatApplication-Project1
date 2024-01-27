package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.PrivateGroupCreation.GroupCreationResult;

class PrivateGroupCreationTest {

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testGroupCreation() {
		User user = new User("dasdas", "", "", "", false, null);
		PrivateGroupCreation.InputValues input = new PrivateGroupCreation.InputValues(user);

		DataStorage storage = InMemoryDataStorage.getInstance();
		PrivateGroupCreation groupCreation = new PrivateGroupCreation(storage);
		PrivateGroupCreation.OutputValues output = groupCreation.execute(input);

		Assert.assertEquals(output.getResult(), GroupCreationResult.Successed);
	}

}
