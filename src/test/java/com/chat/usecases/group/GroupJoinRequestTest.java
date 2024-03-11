package com.chat.usecases.group;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.GroupJoinRequest.GroupJoinRequestResult;

class GroupJoinRequestTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User user1 = new User("Tuan", "", "", "", false, null);
		User user2 = new User("Nhan", "", "", "", false, null);

		userRepository.add(user1);
		userRepository.add(user2);

		GroupCreation pb = new GroupCreation(storage);
		pb.execute(new GroupCreation.InputValues(user1.getId(), GroupType.Private, "private"));
		pb.execute(new GroupCreation.InputValues(user1.getId(), GroupType.Public, "public"));

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testSuccessful() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User user2 = userRepository.getAll().get(1);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);

		GroupJoinRequest groupJoinRequest = new GroupJoinRequest(storage);
		GroupJoinRequest.InputValues input = new GroupJoinRequest.InputValues(group.getId(), user2.getId());
		GroupJoinRequest.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(GroupJoinRequestResult.Successed, output.getResult());
	}

	@Test
	public void testFail1() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User user2 = userRepository.getAll().get(1);
		Group group = storage.getGroupRepository().getAllPublicGroup().get(0);

		GroupJoinRequest groupJoinRequest = new GroupJoinRequest(storage);
		GroupJoinRequest.InputValues input = new GroupJoinRequest.InputValues(group.getId(), user2.getId());
		GroupJoinRequest.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(GroupJoinRequestResult.Failed, output.getResult());
	}

	@Test
	public void testFail2() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user2 = new User("Nghia", "", "", "", false, null);
		Group group = storage.getGroupRepository().getAllPublicGroup().get(0);

		GroupJoinRequest groupJoinRequest = new GroupJoinRequest(storage);
		GroupJoinRequest.InputValues input = new GroupJoinRequest.InputValues(group.getId(), user2.getId());
		GroupJoinRequest.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(GroupJoinRequestResult.Failed, output.getResult());
	}
}
