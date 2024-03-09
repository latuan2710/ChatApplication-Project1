package com.chat.usecases.group;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.DeletingGroupMembers.DeletingGroupMembersResult;

class DeletingGroupMembersTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User admin = new User("Tuan", "", "", "", false, null);
		User member1 = new User("Nhan", "", "", "", false, null);
		User member2 = new User("Nghia", "", "", "", false, null);

		storage.getUserRepository().add(admin);
		storage.getUserRepository().add(member1);
		storage.getUserRepository().add(member2);

		GroupCreation groupCreation = new GroupCreation(storage);
		GroupCreation.OutputValues output = groupCreation
				.execute(new GroupCreation.InputValues(admin.getId(), GroupType.Private,"private"));

		PrivateGroup group = (PrivateGroup) output.getGroup();

		GroupJoining groupJoining = new GroupJoining(storage);
		groupJoining.execute(new GroupJoining.InputValues(member1.getId(), admin.getId(), group.getId()));
		groupJoining.execute(new GroupJoining.InputValues(member2.getId(), admin.getId(), group.getId()));

	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testRemoveMemberSuccessfully() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		GroupRepository groupRepository = storage.getGroupRepository();

		User admin = userRepository.getAll().get(0);
		User member = userRepository.getAll().get(1);
		PrivateGroup group = (PrivateGroup) groupRepository.getAll().get(0);

		DeletingGroupMembers.InputValues input = new DeletingGroupMembers.InputValues(admin.getId(), member.getId(),
				group.getId());
		DeletingGroupMembers deletingGroupMembers = new DeletingGroupMembers(storage);
		DeletingGroupMembers.OutputValues ouput = deletingGroupMembers.execute(input);

		assertEquals(DeletingGroupMembersResult.Successed, ouput.getResult());
		assertEquals(1, group.getUsers().size());
	}

	@Test
	public void testRemoveMemberFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		Repository<User> userRepository = storage.getUserRepository();
		GroupRepository groupRepository = storage.getGroupRepository();

		User member1 = userRepository.getAll().get(1);
		User member2 = userRepository.getAll().get(2);
		PrivateGroup group = (PrivateGroup) groupRepository.getAll().get(0);

		DeletingGroupMembers.InputValues input = new DeletingGroupMembers.InputValues(member1.getId(), member2.getId(),
				group.getId());
		DeletingGroupMembers deletingGroupMembers = new DeletingGroupMembers(storage);
		DeletingGroupMembers.OutputValues ouput = deletingGroupMembers.execute(input);

		assertEquals(DeletingGroupMembersResult.Failed, ouput.getResult());
		assertEquals(2, group.getUsers().size());
	}

}
