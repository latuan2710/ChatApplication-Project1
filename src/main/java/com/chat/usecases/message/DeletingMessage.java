package com.chat.usecases.message;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chat.domains.ChatEntity;
import com.chat.domains.File;
import com.chat.domains.File.FileType;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.user.UserRegistration.RegisterResult;

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

		User user = userRepository.getById(input._senderId);
		Message message = messageRepository.getById(input._messageId);

		if (user.getId().equals(message.getSender().getId())) {
			for (File file : message.getAttachments()) {
				fileService.deleteFile(file.getPath());
			}
			messageRepository.de
			return new OutputValues(DeletingMessageResult.Successed, "");
		} else {
			return new OutputValues(DeletingMessageResult.Failed, "");
		}

	}
}
