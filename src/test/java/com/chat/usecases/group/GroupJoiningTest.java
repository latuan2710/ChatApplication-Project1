package com.chat.usecases.group;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupJoining.GroupJoiningResult;

class GroupJoiningTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		GroupCreation groupCreation = new GroupCreation(storage);

		User user = new User("dasdas", "", "", "", false, null);

		storage.getUserRepository().add(user);

		GroupCreation.InputValues inputPublicGroup = new GroupCreation.InputValues(user, GroupType.Public);
		GroupCreation.InputValues inputPrivateGroup = new GroupCreation.InputValues(user, GroupType.Private);

		groupCreation.execute(inputPublicGroup);
		groupCreation.execute(inputPrivateGroup);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testJoiningPublicGroupByInvitationSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);
		User invitor = storage.getUserRepository().getAll().get(0);
		PublicGroup group = storage.getGroupRepository().getAllPublicGroup().get(0);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, invitor, group);
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Successed, output.getResult());
	}

	@Test
	void testJoiningPublicGroupByInvitationFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);
		User invitor = new User("dasdas", "", "", "", false, null);
		PublicGroup group = storage.getGroupRepository().getAllPublicGroup().get(0);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, invitor, group);
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Failed, output.getResult());
	}

	@Test
	void testJoiningPublicGroupByCodeSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);
		PublicGroup group = storage.getGroupRepository().getAllPublicGroup().get(0);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, group.getJOINING_CODE());
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Successed, output.getResult());
	}

	@Test
	void testJoiningPublicGroupByCodeFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, "VGVVVVV");
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Failed, output.getResult());
	}

	@Test
	void testJoiningPrivateGroupByAdminSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);
		PrivateGroup group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		User admin = group.getAdmins().get(0);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, admin, group);
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Successed, output.getResult());
	}

	@Test
	void testJoiningPrivateGroupByAdminFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("dasdas", "", "", "", false, null);
		PrivateGroup group = storage.getGroupRepository().getAllPrivateGroup().get(0);
		User admin = new User("dasdas", "", "", "", false, null);

		GroupJoining.InputValues input = new GroupJoining.InputValues(user, admin, group);
		GroupJoining groupJoining = new GroupJoining(storage);
		GroupJoining.OutputValues output = groupJoining.execute(input);

		Assert.assertEquals(GroupJoiningResult.Failed, output.getResult());
	}
}
