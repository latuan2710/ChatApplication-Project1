package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group.GroupType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GettingUserGroups.GettingGroupResult;

class GettingUserGroupsTest {
	@BeforeEach
	public void setUp() {
		User user = new User("dasdas", "", "", "", false, null);
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user);

		GroupCreation.InputValues in = new GroupCreation.InputValues(user.getId(), GroupType.Public, "public");
		GroupCreation.InputValues in1 = new GroupCreation.InputValues(user.getId(), GroupType.Private, "private");

		GroupCreation pb = new GroupCreation(storage);
		pb.execute(in);
		pb.execute(in1);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testGetGroupsSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = storage.getUserRepository().getFirst(x -> x.getUsername().equals("dasdas"));
		GettingUserGroups.InputValues input = new GettingUserGroups.InputValues(user.getId());

		GettingUserGroups getUserGroups = new GettingUserGroups(storage);
		GettingUserGroups.OutputValues output = getUserGroups.execute(input);
		Assert.assertEquals(2, output.getGroups().size());
	}

	@Test
	public void testGetGroupsFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User userTest = new User("dasdas", "", "", "", false, null);

		GettingUserGroups.InputValues input = new GettingUserGroups.InputValues(userTest.getId());

		GettingUserGroups getUserGroups = new GettingUserGroups(storage);
		GettingUserGroups.OutputValues output = getUserGroups.execute(input);
		Assert.assertEquals(GettingGroupResult.Failed, output.getResult());
	}
}
