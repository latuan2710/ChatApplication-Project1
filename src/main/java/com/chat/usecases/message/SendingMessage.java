package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chat.domains.ChatEntity;
import com.chat.domains.File;
import com.chat.domains.File.FileType;
import com.chat.domains.Group;
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
		private Message _message;

		public OutputValues(SendingMessageResult result, Message message) {
			_message = message;
			_result = result;
		}

		public SendingMessageResult getResult() {
			return _result;
		}

		public Message getMessage() {
			return _message;
		}
	}

	public enum SendingMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		if (input._text.isBlank() || input._text.isEmpty()) {
			return new OutputValues(SendingMessageResult.Failed, null);
		}

		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<Group> groupRepository = _dataStorage.getGroupRepository();
		Repository<Message> messageRepository = _dataStorage.getMessageRepository();
		FileService fileService = new FileService();

		User sender = userRepository.findById(input._senderId);
		ChatEntity receiver = userRepository.findById(input._receiverId);
		if (receiver == null) {
			receiver = groupRepository.findById(input._receiverId);
			if (receiver == null) {
				return new OutputValues(SendingMessageResult.Failed, null);
			}
		}

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

		return new OutputValues(SendingMessageResult.Successed, message);
	}
}
