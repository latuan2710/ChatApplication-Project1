package com.chat.usecases.group;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.File;
import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.domains.Group.GroupType;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;

public class GroupFileViewerTest {

	@BeforeEach
	public void setUp() {
		User user = new User("dasdas", "", "", "", false, null);
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user);
		
		GroupFileViewer.InputValues input = new GroupFileViewer.InputValues("senderId", "groupId");
		GroupCreation pb = new GroupCreation(storage);
		List<Message> messages = new ArrayList<>();
        Message message = new Message("messageId", "content", "senderId", messages);
        message.getAttachments();
        messages.add(message);
		pb.execute(input);

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
		GettingUserGroups.InputValues input = new GettingUserGroups.InputValues(user.getId());

		GettingUserGroups getUserGroups = new GettingUserGroups(storage);
		GettingUserGroups.OutputValues output = getUserGroups.execute(input);
		Assert.assertEquals(2, output.getGroups().size());
	}

}
