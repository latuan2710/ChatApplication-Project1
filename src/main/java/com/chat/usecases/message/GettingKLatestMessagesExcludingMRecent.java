package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.GettingMessages.GettingMessagesResult;

public class GettingKLatestMessagesExcludingMRecent extends
		UseCase<GettingKLatestMessagesExcludingMRecent.InputValues, GettingKLatestMessagesExcludingMRecent.OutputValues> {
	DataStorage _dataStorage;

	public GettingKLatestMessagesExcludingMRecent(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private int _k;
		private int _m;
		private String _senderId;
		private String _receiverId;

		public InputValues(int k, int m, String senderId, String receiverId) {
			this._k = k;
			this._m = m;
			this._senderId = senderId;
			this._receiverId = receiverId;
		}

	}

	public static class OutputValues {
		private GettingKLatestMessagesExcludingMRecentResult _result;
		private List<Message> _messages;

		public OutputValues(GettingKLatestMessagesExcludingMRecentResult result, List<Message> messages) {
			_messages = messages;
			_result = result;
		}

		public GettingKLatestMessagesExcludingMRecentResult getResult() {
			return _result;
		}

		public List<Message> getmessages() {
			return _messages;
		}
	}

	public enum GettingKLatestMessagesExcludingMRecentResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();

		User sender = userRepository.findById(input._senderId);
		User receiver = userRepository.findById(input._receiverId);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(input._senderId);
		GettingMessages gettingAllMessages = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingAllMessagesOutput = gettingAllMessages.execute(gettingAllMessagesInput);

		if (gettingAllMessagesOutput.getResult() == GettingMessagesResult.Failed) {
			return new OutputValues(GettingKLatestMessagesExcludingMRecentResult.Failed, null);
		}

		List<Message> messages = gettingAllMessagesOutput.getMessages();
		messages = messages.stream().filter(m -> m.getSender().equals(sender) && m.getReceiver().equals(receiver))
				.toList();

		messages = getTopLatestMessages(input._k, input._m, messages);

		if (messages.isEmpty()) {
			return new OutputValues(GettingKLatestMessagesExcludingMRecentResult.Failed, null);
		}

		return new OutputValues(GettingKLatestMessagesExcludingMRecentResult.Successed, messages);
	}

	private List<Message> getTopLatestMessages(int k, int m, List<Message> messages) {
		List<Message> result = new ArrayList<>();
		for (int i = Math.max(0, messages.size() - k - m); i < Math.max(0, messages.size() - m); i++) {
			result.add(messages.get(i));
		}
		return result;
	}

}
