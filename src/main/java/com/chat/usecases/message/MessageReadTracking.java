package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Conversation;
import com.chat.domains.Message;
import com.chat.domains.MessageRecord;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class MessageReadTracking extends UseCase<MessageReadTracking.InputValues, MessageReadTracking.OutputValues> {
	private DataStorage _dataStorage;

	public MessageReadTracking(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _conversationId;
		private String _messageId;

		public InputValues(String userId, String conversationId, String messageId) {
			this._userId = userId;
			this._conversationId = conversationId;
			this._messageId = messageId;
		}
	}

	public static class OutputValues {
		private MessageReadTrackingResult _result;
		private List<User> _users;

		public OutputValues(MessageReadTrackingResult result, List<User> user) {
			_result = result;
			_users = user;
		}

		public MessageReadTrackingResult getResult() {
			return _result;
		}

		public List<User> getUsers() {
			return _users;
		}

	}

	public enum MessageReadTrackingResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<User> userRead = new ArrayList<>();

		User user = _dataStorage.getUserRepository().findById(input._userId);
		Message message = _dataStorage.getMessageRepository().findById(input._messageId);
		List<MessageRecord> records = _dataStorage.getMessageRecordRepository().getAll();
		Conversation conversation = getConversation(input);

		if (user == null || records == null || conversation == null || message == null) {
			return new OutputValues(MessageReadTrackingResult.Failed, null);
		}

		if (!conversation.haveMessage(message) || !message.getSender().equals(user)) {
			return new OutputValues(MessageReadTrackingResult.Failed, null);
		}

		for (MessageRecord record : records) {
			if (message.equals(record.getMessage()) && !record.getUser().equals(user)) {
				userRead.add(record.getUser());
			}
		}

		return new OutputValues(MessageReadTrackingResult.Successed, userRead);
	}

	private Conversation getConversation(InputValues input) {
		GettingConversationById.InputValues gettingConversationInput = new GettingConversationById.InputValues(
				input._userId, input._conversationId);
		GettingConversationById gettingConversation = new GettingConversationById(_dataStorage);
		GettingConversationById.OutputValues gettingConversationOutput = gettingConversation
				.execute(gettingConversationInput);

		return gettingConversationOutput.getConversations();
	}

}
