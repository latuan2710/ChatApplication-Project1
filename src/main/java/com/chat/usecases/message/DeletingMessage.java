package com.chat.usecases.message;

import com.chat.domains.File;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class DeletingMessage extends UseCase<DeletingMessage.InputValues, DeletingMessage.OutputValues> {
	private DataStorage _dataStorage;

	public DeletingMessage(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;
		private String _messageId;

		public InputValues(String senderId, String messageId) {
			this._senderId = senderId;
			this._messageId = messageId;
		}

	}

	public static class OutputValues {
		private DeletingMessageResult _result;
		private String _message;

		public OutputValues(DeletingMessageResult result, String message) {
			_message = message;
			_result = result;
		}

		public DeletingMessageResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}
	}

	public static enum DeletingMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<Message> messageRepository = _dataStorage.getMessageRepository();
		FileService fileService = new FileService();

		User user = userRepository.findById(input._senderId);
		Message message = messageRepository.findById(input._messageId);

		if (user == null || message == null)
			return new OutputValues(DeletingMessageResult.Failed, "");

		if (user.getId().equals(message.getSender().getId())) {
			for (File file : message.getAttachments()) {
				fileService.deleteFile(file.getPath());
			}
			messageRepository.deleteById(input._messageId);
			return new OutputValues(DeletingMessageResult.Successed, "");
		} else {
			return new OutputValues(DeletingMessageResult.Failed, "");
		}

	}
}
