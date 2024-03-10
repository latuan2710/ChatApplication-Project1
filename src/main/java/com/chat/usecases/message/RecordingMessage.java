package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Conversation;
import com.chat.domains.Message;
import com.chat.domains.MessageRecord;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.GettingAllConversation.GettingAllConversationResult;
import com.chat.usecases.message.GettingMessageRecorded.GettingMessageRecordResult;

public class RecordingMessage extends UseCase<RecordingMessage.InputValues, RecordingMessage.OutputValues> {
	private DataStorage _dataStorage;

	public RecordingMessage(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _messageId;

		public InputValues(String userId, String messageId) {
			this._userId = userId;
			this._messageId = messageId;
		}
	}

	public static class OutputValues {
		private RecordingMessageResult _result;

		public OutputValues(RecordingMessageResult result) {
			_result = result;
		}

		public RecordingMessageResult getResult() {
			return _result;
		}

	}

	public enum RecordingMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		User user = _dataStorage.getUserRepository().findById(input._userId);
		Message message = _dataStorage.getMessageRepository().findById(input._messageId);

		if (user == null || message == null) {
			return new OutputValues(RecordingMessageResult.Failed);
		}

		GettingMessageRecorded gettingRecord = new GettingMessageRecorded(_dataStorage);
		GettingMessageRecorded.InputValues gettingRecordInput = new GettingMessageRecorded.InputValues(user.getId());
		GettingMessageRecorded.OutputValues gettingRecordOutput = gettingRecord.execute(gettingRecordInput);

		GettingAllConversation gettingConversation = new GettingAllConversation(_dataStorage);
		GettingAllConversation.InputValues gettingConversationInput = new GettingAllConversation.InputValues(
				user.getId());
		GettingAllConversation.OutputValues gettingConversationOutput = gettingConversation
				.execute(gettingConversationInput);

		List<Conversation> conversations;
		if (gettingConversationOutput.getResult() == GettingAllConversationResult.Successed) {
			conversations = new ArrayList<>(gettingConversationOutput.getConversations());
		} else {
			return new OutputValues(RecordingMessageResult.Failed);
		}

		if (gettingRecordOutput.getResult() == GettingMessageRecordResult.Successed) {
			List<MessageRecord> records = gettingRecordOutput.getMessageRecords();

			for (MessageRecord record : records) {
				Message messageRecorded = record.getMessage();

				if (isSameConversation(conversations, message, messageRecorded)) {
					record.setMessage(message);
					return new OutputValues(RecordingMessageResult.Successed);
				}
			}
		}

		Repository<MessageRecord> repository = _dataStorage.getMessageRecordRepository();
		repository.add(new MessageRecord(message, user));

		return new OutputValues(RecordingMessageResult.Successed);
	}

	private boolean isSameConversation(List<Conversation> conversations, Message message, Message messageRecorded) {
		for (Conversation conversation : conversations) {
			List<Message> messages = conversation.getMessages();
			boolean isSameConversation = messages.contains(message) && messages.contains(messageRecorded);
			if (isSameConversation) {
				return true;
			}
		}
		return false;
	}
}
