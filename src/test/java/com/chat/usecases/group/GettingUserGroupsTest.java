package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group.GroupType;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.infrastructure.services.SHA256Hasher;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GettingUserGroups.GroupResult;
import com.chat.usecases.user.UserRegistration;

class GettingUserGroupsTest {
	@BeforeEach
	public void setUp() {
		User user = new User("dasdas", "", "", "", false, null);
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user);

		GroupCreation.InputValues in = new GroupCreation.InputValues(user, GroupType.Public);
		GroupCreation.InputValues in1 = new GroupCreation.InputValues(user, GroupType.Private);

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
	void testGetGroupsSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = storage.getUserRepository().getFirst(x -> x.getUsername().equals("dasdas"));
		GettingUserGroups.InputValues input = new GettingUserGroups.InputValues(user);

		GettingUserGroups getUserGroups = new GettingUserGroups(storage);
		GettingUserGroups.OutputValues output = getUserGroups.execute(input);
		Assert.assertEquals(2, output.getGroups().size());

	}

	@Test
	void testGetGroupsFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = storage.getUserRepository().getFirst(x -> x.getUsername().equals("asd"));
		GettingUserGroups.InputValues input = new GettingUserGroups.InputValues(user);

		GettingUserGroups getUserGroups = new GettingUserGroups(storage);
		GettingUserGroups.OutputValues output = getUserGroups.execute(input);
		Assert.assertEquals(0, output.getGroups().size());

	}

}
