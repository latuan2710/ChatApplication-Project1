package com.chat.usecases.message;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.File;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.repositories.InMemoryMessageRepository;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.MessageRepository;
import com.chat.usecases.adapters.Repository;

public class GettingAllMessageByUserId
		extends UseCase<GettingAllMessageByUserId.InputValues, GettingAllMessageByUserId.OutputValues> {
	private DataStorage _dataStorage;

	public GettingAllMessageByUserId(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;

		public InputValues(String senderId) {
			this._senderId = senderId;
		}

	}

	public static class OutputValues {
		private GettingAllMessageByUserIdResult _result;
		private List<Message> _messages;

		public OutputValues(GettingAllMessageByUserIdResult result, List<Message> messages) {
			_messages = messages;
			_result = result;
		}

		public GettingAllMessageByUserIdResult getResult() {
			return _result;
		}

		public List<Message> getMessages() {
			return _messages;
		}
	}

	public static enum GettingAllMessageByUserIdResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		MessageRepository messageMepository = new InMemoryMessageRepository();

		Predicate<Message> predicate = m -> (m.getSender().getId().equals(input._senderId))
				|| m.getReceiver().getId().equals(input._senderId);
		List<Message> messages = messageMepository.getAll();
		List<Message> result = messages.stream().filter(predicate).toList();

		result = result.isEmpty() ? null : result;

		return new OutputValues(GettingAllMessageByUserIdResult.Successed, result);
	}
}
