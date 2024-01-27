package com.chat.usecases.group;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupLeaving.leavingGroupResult;

class GroupLeavingTest {

	@BeforeEach
	public void setUp() {
		User user = new User("dasdas", "", "", "", false, null);
		GroupCreation.InputValues inputPublicGroup = new GroupCreation.InputValues(user, GroupType.Public);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user);
		GroupCreation groupCreation = new GroupCreation(storage);

		groupCreation.execute(inputPublicGroup);

	}
                                                                                                                                
	@Test
	void successLeavingGroup() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = storage.getUserRepository().getFirst(x -> x.getUsername().equals("dasdas"));
		Group group = storage.getGroupRepository().getAll().get(0);
		
		GroupLeaving groupLeaving = new GroupLeaving(storage);
		GroupLeaving.InputValues input = new GroupLeaving.InputValues(user, group);
		GroupLeaving.OutputValues output = groupLeaving.execute(input);
		
		assertEquals(leavingGroupResult.Successed, output.getResult());
	}
	
	@Test
	void flailLeavingGroup() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User user = storage.getUserRepository().getFirst(x -> x.getUsername().equals("dasas"));
		Group group = storage.getGroupRepository().getAll().get(0);
		
		GroupLeaving groupLeaving = new GroupLeaving(storage);
		GroupLeaving.InputValues input = new GroupLeaving.InputValues(user, group);
		GroupLeaving.OutputValues output = groupLeaving.execute(input);
		
		assertEquals(leavingGroupResult.Failed, output.getResult());
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}
}
