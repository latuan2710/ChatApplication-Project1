package com.chat.usecases.message;

import com.chat.domains.MessageHistory;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class GettingMessageHistory
		extends UseCase<GettingMessageHistory.InputValues, GettingMessageHistory.OutputValues> {
	private DataStorage _dataStorage;

	public GettingMessageHistory(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _messageId;

		public InputValues(String messageId) {
			this._messageId = messageId;
		}

	}

	public static class OutputValues {
		private GettingMessageHistoryResult _result;
		private MessageHistory _messageHistory;

		public OutputValues(GettingMessageHistoryResult result, MessageHistory messageHistory) {
			_messageHistory = messageHistory;
			_result = result;
		}

		public GettingMessageHistoryResult getResult() {
			return _result;
		}

		public MessageHistory getMessageHistory() {
			return _messageHistory;
		}
	}

	public enum GettingMessageHistoryResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<MessageHistory> repository = _dataStorage.getMessageHistoryRepository();
		MessageHistory history = repository.getFirst(m -> m.getMessageId().equals(input._messageId));
		return new OutputValues(GettingMessageHistoryResult.Successed, history);
	}
}
