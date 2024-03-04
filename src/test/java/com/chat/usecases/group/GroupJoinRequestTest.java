package com.chat.usecases.group;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupJoining.GroupJoiningResult;

class GroupJoinRequestTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		GroupCreation groupCreation = new GroupCreation(storage);

		User user = new User("dasdas", "", "", "", false, null);

		storage.getUserRepository().add(user);

		GroupCreation.InputValues inputPrivateGroup = new GroupCreation.InputValues(user.getId(), GroupType.Private);

		groupCreation.execute(inputPrivateGroup);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}
	
	@Test
	void testSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User userSendRequestId = new User("dasdas", "", "", "", false, null);
		storage.getUserRepository().add(userSendRequestId);
		PrivateGroup group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		User admin = group.getAdmins().get(0);
		
		
		
		GroupJoinRequest.InputValues input = new GroupJoinRequest.InputValues( admin.getId(),group.getId(),userSendRequestId.getId());
		GroupJoinRequest groupJoinRequest = new GroupJoinRequest(storage);
		GroupJoinRequest.OutputValues output = groupJoinRequest.execute(input);

		Assert.assertEquals(com.chat.usecases.group.GroupJoinRequest.GroupJoinRequestResult.Successed, output.getResult());
	}

//	@Test
//	void testJoiningPrivateGroupByAdminFailed() {
//		DataStorage storage = InMemoryDataStorage.getInstance();
//
//		User user = new User("dasdas", "", "", "", false, null);
//		PrivateGroup group = storage.getGroupRepository().getAllPrivateGroup().get(0);
//		User admin = new User("dasdas", "", "", "", false, null);
//
//		GroupJoining.InputValues input = new GroupJoining.InputValues(user.getId(), admin.getId(), group.getId());
//		GroupJoining groupJoining = new GroupJoining(storage);
//		GroupJoining.OutputValues output = groupJoining.execute(input);
//
//		Assert.assertEquals(GroupJoiningResult.Failed, output.getResult());
//	}
}
