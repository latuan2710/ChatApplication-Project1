package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chat.domains.File;
import com.chat.domains.File.FileType;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class SendingMessage extends UseCase<SendingMessage.InputValues, SendingMessage.OutputValues> {
	private DataStorage _dataStorage;

	public SendingMessage(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;
		private String _receiverId;
		private String _text;
		private Map<byte[], FileType> _files;

		public InputValues(String senderId, String receiverId, String text, Map<byte[], FileType> files) {
			this._senderId = senderId;
			this._receiverId = receiverId;
			this._text = text;
			this._files = files;
		}

		public InputValues(String senderId, String receiverId, String text) {
			this._senderId = senderId;
			this._receiverId = receiverId;
			this._text = text;
			this._files = new HashMap<>();
		}

	}

	public static class OutputValues {
		private SendingMessageResult _result;
		private String _message;

		public OutputValues(SendingMessageResult result, String message) {
			_message = message;
			_result = result;
		}

		public SendingMessageResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}
	}

	public static enum SendingMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		if (input._text.isBlank() || input._text.isEmpty()) {
			return new OutputValues(SendingMessageResult.Failed, "");
		}

		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<Message> messageRepository = _dataStorage.getMessageRepository();
		FileService fileService = new FileService();

		User sender = userRepository.getById(input._senderId);
		User receiver = userRepository.getById(input._receiverId);

		List<File> files = new ArrayList<>();
		for (byte[] content : input._files.keySet()) {
			if (content == null || content.length == 0) {
				continue;
			}

			FileType fileType = input._files.get(content);
			File file = new File(fileType);
			file.setPath();
			fileService.saveFile(content, file.getPath());
			files.add(file);
		}

		Message message = new Message(sender, receiver, input._text, files);

		messageRepository.add(message);

		return new OutputValues(SendingMessageResult.Successed, "");
	}
}
