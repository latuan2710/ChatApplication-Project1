package com.chat.usecases.group;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.GroupRequest;
import com.chat.domains.GroupRequest.GroupRequestStatus;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.GroupRequestHandler.RequestResult;

class GroupRequestHandlerTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User admin = new User("user1", "", "", "", false, null);
		User user2 = new User("user2", "", "", "", false, null);
		User user3 = new User("user3", "", "", "", false, null);
		User user4 = new User("user4", "", "", "", false, null);
		User user5 = new User("user5", "", "", "", false, null);

		userRepository.add(admin);
		userRepository.add(user2);
		userRepository.add(user3);
		userRepository.add(user4);
		userRepository.add(user5);

		GroupCreation pb = new GroupCreation(storage);
		Group group = pb.execute(new GroupCreation.InputValues(admin.getId(), GroupType.Private, "private")).getGroup();

		GroupJoinRequest groupJoinRequest = new GroupJoinRequest(storage);
		groupJoinRequest.execute(new GroupJoinRequest.InputValues(group.getId(), user2.getId()));
		groupJoinRequest.execute(new GroupJoinRequest.InputValues(group.getId(), user3.getId()));
		groupJoinRequest.execute(new GroupJoinRequest.InputValues(group.getId(), user4.getId()));
		groupJoinRequest.execute(new GroupJoinRequest.InputValues(group.getId(), user5.getId()));

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testAcceptSuccessful() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User admin = userRepository.getAll().get(0);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		GroupRequest request = this.getRequests(storage, group.getId()).get(0);

		GroupRequestHandler groupJoinRequest = new GroupRequestHandler(storage);
		GroupRequestHandler.InputValues input = new GroupRequestHandler.InputValues(admin.getId(), request.getId(),
				true);
		GroupRequestHandler.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(RequestResult.Successed, output.getResult());
		assertEquals(GroupRequestStatus.Accepted, request.getStatus());
	}
	
	@Test
	public void testRejectSuccessful() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User admin = userRepository.getAll().get(0);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		GroupRequest request = this.getRequests(storage, group.getId()).get(0);

		GroupRequestHandler groupJoinRequest = new GroupRequestHandler(storage);
		GroupRequestHandler.InputValues input = new GroupRequestHandler.InputValues(admin.getId(), request.getId(),
				false);
		GroupRequestHandler.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(RequestResult.Successed, output.getResult());
		assertEquals(GroupRequestStatus.Rejected, request.getStatus());
	}

	@Test
	public void testFail1() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user2 = new User("fakeAdmin", "", "", "", false, null);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		GroupRequest request = this.getRequests(storage, group.getId()).get(0);

		GroupRequestHandler groupJoinRequest = new GroupRequestHandler(storage);
		GroupRequestHandler.InputValues input = new GroupRequestHandler.InputValues(user2.getId(), request.getId(),
				true);
		GroupRequestHandler.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(RequestResult.Failed, output.getResult());
	}

	@Test
	public void testFail2() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();

		User user2 = userRepository.getAll().get(1);
		Group group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		GroupRequest request = this.getRequests(storage, group.getId()).get(0);

		GroupRequestHandler groupJoinRequest = new GroupRequestHandler(storage);
		GroupRequestHandler.InputValues input = new GroupRequestHandler.InputValues(user2.getId(), request.getId(),
				true);
		GroupRequestHandler.OutputValues output = groupJoinRequest.execute(input);

		assertEquals(RequestResult.Failed, output.getResult());
	}

	private List<GroupRequest> getRequests(DataStorage storage, String groupId) {
		GettingGroupRequest gettingGroupRequest = new GettingGroupRequest(storage);
		GettingGroupRequest.InputValues input = new GettingGroupRequest.InputValues(groupId);
		GettingGroupRequest.OutputValues output = gettingGroupRequest.execute(input);

		return output.getRequests();
	}

}
