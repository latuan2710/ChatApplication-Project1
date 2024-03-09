package com.chat.usecases.message;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.MessageRecord;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.GettingUserGroups;
import com.chat.usecases.message.DeletingMessage.DeletingMessageResult;
import com.chat.usecases.message.DeletingMessage.OutputValues;

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
		
		Repository<MessageRecord> repository = _dataStorage.getMessageRecordRepository();
		repository.add(new MessageRecord(message, user));

		return new OutputValues(RecordingMessageResult.Successed);
	}
}
