package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group.GroupType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupCreation.GroupCreationResult;

class GroupCreationTest {

	@BeforeEach
	public void setUp() {
		User user = new User("dasdas", "", "", "", false, null);
		GroupCreation.InputValues inputPublicGroup = new GroupCreation.InputValues(user, GroupType.Public);
		GroupCreation.InputValues inputPrivateGroup = new GroupCreation.InputValues(user, GroupType.Private);

		DataStorage storage = InMemoryDataStorage.getInstance();
		GroupCreation groupCreation = new GroupCreation(storage);
		
		groupCreation.execute(inputPublicGroup);
		groupCreation.execute(inputPrivateGroup);
	}
	
	@Test
	void testPrivateGroupCreation() {
		User user = new User("dasdas", "", "", "", false, null);
		GroupCreation.InputValues input = new GroupCreation.InputValues(user, GroupType.Private);

		DataStorage storage = InMemoryDataStorage.getInstance();
		GroupCreation groupCreation = new GroupCreation(storage);
		GroupCreation.OutputValues output = groupCreation.execute(input);

		Assert.assertEquals(output.getResult(), GroupCreationResult.Successed);
	}

	@Test
	void testPublicGroupCreation() {
		User user = new User("dasdas", "", "", "", false, null);
		GroupCreation.InputValues input = new GroupCreation.InputValues(user, GroupType.Public);

		DataStorage storage = InMemoryDataStorage.getInstance();
		GroupCreation groupCreation = new GroupCreation(storage);
		GroupCreation.OutputValues output = groupCreation.execute(input);

		Assert.assertEquals(output.getResult(), GroupCreationResult.Successed);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}
}
