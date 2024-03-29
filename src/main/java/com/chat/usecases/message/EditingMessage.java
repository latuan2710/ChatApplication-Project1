package com.chat.usecases.message;

import com.chat.domains.Message;
import com.chat.domains.MessageHistory;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class EditingMessage extends UseCase<EditingMessage.InputValues, EditingMessage.OutputValues> {
	private DataStorage _dataStorage;

	public EditingMessage(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;
		private String _messageId;
		private String _newText;

		public InputValues(String senderId, String messageId, String newText) {
			this._senderId = senderId;
			this._messageId = messageId;
			this._newText = newText;
		}

	}

	public static class OutputValues {
		private EditingMessageResult _result;
		private Message _newMessage;

		public OutputValues(EditingMessageResult result, Message newMessage) {
			_result = result;
			_newMessage = newMessage;
		}

		public EditingMessageResult getResult() {
			return _result;
		}

		public Message getNewMessage() {
			return _newMessage;
		}

	}

	public enum EditingMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<Message> messageRepository = _dataStorage.getMessageRepository();
		Repository<MessageHistory> messageHistoryRepository = _dataStorage.getMessageHistoryRepository();

		Message message = messageRepository.findById(input._messageId);

		if (message != null && message.getSender().getId().equals(input._senderId)) {
			MessageHistory history = getMessageHistory(input);

			if (history == null) {
				history = new MessageHistory(message.getId());
				messageHistoryRepository.add(history);
			}

			history.addText(message.getContent());
			message.setContent(input._newText);

			return new OutputValues(EditingMessageResult.Successed, message);
		}

		return new OutputValues(EditingMessageResult.Failed, message);

	}

	private MessageHistory getMessageHistory(InputValues input) {
		GettingMessageHistory gettingMessageHistory = new GettingMessageHistory(_dataStorage);
		GettingMessageHistory.InputValues gettingMessageHistoryInput = new GettingMessageHistory.InputValues(
				input._messageId);
		GettingMessageHistory.OutputValues gettingMessageHistoryOutput = gettingMessageHistory
				.execute(gettingMessageHistoryInput);

		MessageHistory history = gettingMessageHistoryOutput.getMessageHistory();
		return history;
	}
}
