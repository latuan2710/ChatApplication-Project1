package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.PrivateGroupCreation.GroupCreationResult;

class PublicGroupCreationTest {

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testGroupCreation() {
		User user = new User("dasdas", "", "", "", false, null);
		PublicGroupCreation.InputValues input = new PublicGroupCreation.InputValues(user);

		DataStorage storage = InMemoryDataStorage.getInstance();
		PublicGroupCreation groupCreation = new PublicGroupCreation(storage);
		PublicGroupCreation.OutputValues output = groupCreation.execute(input);

		Assert.assertEquals(output.getResult(), GroupCreationResult.Successed);
	}

}
