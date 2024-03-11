package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.chat.domains.ChatEntity;
import com.chat.domains.Conversation;
import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class GettingAllConversation
		extends UseCase<GettingAllConversation.InputValues, GettingAllConversation.OutputValues> {
	private DataStorage _dataStorage;

	public GettingAllConversation(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;

		public InputValues(String userId) {
			this._userId = userId;
		}
	}

	public static class OutputValues {
		private GettingAllConversationResult _result;
		private List<Conversation> _conversations;

		public OutputValues(GettingAllConversationResult result, List<Conversation> conversations) {
			this._result = result;
			this._conversations = conversations;
		}

		public GettingAllConversationResult getResult() {
			return _result;
		}

		public List<Conversation> getConversations() {
			return _conversations;
		}

	}

	public enum GettingAllConversationResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		HashMap<String, Conversation> conversations = new HashMap<>();

		List<Message> messages = getMessagesByUserId(input._userId);

		if (messages == null) {
			return new OutputValues(GettingAllConversationResult.Failed, null);
		}

		for (Message message : messages) {
			User sender = message.getSender();
			ChatEntity receiver = message.getReceiver();
			String receiverId = receiver.getId();
			String receiverName = "";

			if (receiver instanceof User) {
				if (receiver.getId().equals(input._userId)) {
					receiverName = sender.getFullName();
					receiverId = sender.getId();
				} else {
					receiverName = ((User) receiver).getFullName();
				}
			} else if (receiver instanceof Group) {
				receiverName = ((Group) receiver).getName();
			}

			String conversionId = receiverId + receiverName;
			if (conversations.containsKey(conversionId)) {
				Conversation conversation = conversations.get(conversionId);
				conversation.addMessage(message);
				conversations.put(conversionId, conversation);
			} else {
				conversations.put(conversionId,
						new Conversation(conversionId, new ArrayList<>(List.of(message)), List.of(sender, receiver)));
			}
		}

		return new OutputValues(GettingAllConversationResult.Successed, new ArrayList<>(conversations.values()));
	}

	private List<Message> getMessagesByUserId(String userId) {
		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(userId);
		GettingMessages gettingAllMessages = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingAllMessagesOutput = gettingAllMessages.execute(gettingAllMessagesInput);

		List<Message> messages = gettingAllMessagesOutput.getMessages();
		return messages;
	}
}
