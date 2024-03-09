package com.chat.usecases.message;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.group.GroupCreation;
import com.chat.usecases.group.GroupJoining;
import com.chat.usecases.message.GettingAllConversation.GettingAllConversationResult;

class GettingAllConversationTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user1 = new User("Tuan", "La", "Tuan", "", false, null);
		User user2 = new User("Nhan", "Nguyen", "Nhan", "", false, null);

		storage.getUserRepository().add(user1);
		storage.getUserRepository().add(user2);

		GroupCreation.InputValues in = new GroupCreation.InputValues(user1.getId(), GroupType.Public, "CSE422");
		GroupCreation pb = new GroupCreation(storage);
		Group group = pb.execute(in).getGroup();

		GroupJoining groupJoining = new GroupJoining(storage);
		groupJoining.execute(new GroupJoining.InputValues(user2.getId(), user1.getId(), group.getId()));

		SendingMessage sendingMessage = new SendingMessage(storage);
		
		sendingMessage.execute(new SendingMessage.InputValues(user1.getId(), group.getId(), "message1"));
		sendingMessage.execute(new SendingMessage.InputValues(user1.getId(), group.getId(), "message2"));
		sendingMessage.execute(new SendingMessage.InputValues(user2.getId(), group.getId(), "message3"));
		sendingMessage.execute(new SendingMessage.InputValues(user2.getId(), group.getId(), "message4"));
		
		sendingMessage.execute(new SendingMessage.InputValues(user1.getId(), user2.getId(), "message5"));
		sendingMessage.execute(new SendingMessage.InputValues(user2.getId(), user1.getId(), "message6"));
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	public void testGetConversationSuccess() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = storage.getUserRepository().getAll().get(0);

		GettingAllConversation.InputValues input = new GettingAllConversation.InputValues(user.getId());
		GettingAllConversation gettingAllConversation = new GettingAllConversation(storage);
		GettingAllConversation.OutputValues output = gettingAllConversation.execute(input);
		
		assertEquals(GettingAllConversationResult.Successed, output.getResult());
		assertEquals(2, output.getConversations().keySet().size());
	}
	
	@Test
	public void testGetConversationFailed() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User user = new User("Nghia", "Nguyen", "Nghia", "", false, null);

		GettingAllConversation.InputValues input = new GettingAllConversation.InputValues(user.getId());
		GettingAllConversation gettingAllConversation = new GettingAllConversation(storage);
		GettingAllConversation.OutputValues output = gettingAllConversation.execute(input);
		
		assertEquals(GettingAllConversationResult.Failed, output.getResult());
		assertEquals(null, output.getConversations());
	}

}
