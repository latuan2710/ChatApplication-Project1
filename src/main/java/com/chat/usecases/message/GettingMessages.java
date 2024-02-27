package com.chat.usecases.message;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.Message;
import com.chat.infrastructure.repositories.InMemoryMessageRepository;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.MessageRepository;

public class GettingMessages extends UseCase<GettingMessages.InputValues, GettingMessages.OutputValues> {
	private DataStorage _dataStorage;

	public GettingMessages(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;

		public InputValues(String senderId) {
			this._senderId = senderId;
		}

	}

	public static class OutputValues {
		private GettingMessagesResult _result;
		private List<Message> _messages;

		public OutputValues(GettingMessagesResult result, List<Message> messages) {
			_messages = messages;
			_result = result;
		}

		public GettingMessagesResult getResult() {
			return _result;
		}

		public List<Message> getMessages() {
			return _messages;
		}
	}

	public static enum GettingMessagesResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		MessageRepository messageMepository = _dataStorage.getMessageRepository();

		Predicate<Message> predicate = m -> (m.getSender().getId().equals(input._senderId))
				|| m.getReceiver().getId().equals(input._senderId);
		List<Message> messages = messageMepository.getAll();
		List<Message> result = messages.stream().filter(predicate).toList();

		result = result.isEmpty() ? null : result;

		return new OutputValues(GettingMessagesResult.Successed, result);
	}
}
