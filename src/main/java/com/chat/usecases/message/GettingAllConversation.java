package com.chat.usecases.message;

import java.util.HashMap;
import java.util.List;

import com.chat.domains.ChatEntity;
import com.chat.domains.Conversation;
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
		private String _senderId;

		public InputValues(String senderId) {
			this._senderId = senderId;
		}

	}

	public static class OutputValues {
		private GettingAllConversationResult _result;
		private HashMap<String, Conversation> _conversations;

		public OutputValues(GettingAllConversationResult result, HashMap<String, Conversation> conversations) {
			this._result = result;
			this._conversations = conversations;
		}

		public GettingAllConversationResult getResult() {
			return _result;
		}

		public HashMap<String, Conversation> getConversations() {
			return _conversations;
		}

	}

	public static enum GettingAllConversationResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		HashMap<String, Conversation> conversations = new HashMap<>();

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(input._senderId);
		GettingMessages gettingAllMessages = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingAllMessagesOutput = gettingAllMessages.execute(gettingAllMessagesInput);

		List<Message> messages = gettingAllMessagesOutput.getMessages();

		if (messages == null) {
			return new OutputValues(GettingAllConversationResult.Failed, null);
		}

		for (Message message : messages) {
			User sender = message.getSender();
			ChatEntity receiver = message.getReceiver();
			String receiverId = receiver.getId();
			String receiverName = "";

			if (receiver instanceof User) {
				receiverName = ((User) receiver).getFullName();
			} else {

			}

			String conversionName = receiverId + receiverName;
			if (conversations.containsKey(conversionName)) {
				Conversation conversation = conversations.get(conversionName);
				conversation.addMessage(message);
				conversations.put(conversionName, conversation);
			} else {
				conversations.put(conversionName, new Conversation(List.of(message), List.of(sender, receiver)));
			}
		}

		return new OutputValues(GettingAllConversationResult.Successed, conversations);
	}
}
