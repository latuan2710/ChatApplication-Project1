package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class SearchTextByKeyword extends UseCase<SearchTextByKeyword.InputValues, SearchTextByKeyword.OutputValues> {
	DataStorage _dataStorage;

	public SearchTextByKeyword(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _keyword;
		private String _senderId;
		private String _receiverId;

		public InputValues(String keyword, String _senderId, String _receiverId) {
			this._keyword = keyword;
			this._senderId = _senderId;
			this._receiverId = _receiverId;
		}

	}

	public static class OutputValues {
		private SearchTextByKeywordTestResult _result;
		private List<Message> _messages;

		public OutputValues(SearchTextByKeywordTestResult result, List<Message> messages) {
			_messages = messages;
			_result = result;
		}

		public SearchTextByKeywordTestResult getResult() {
			return _result;
		}

		public List<Message> getMessage() {
			return _messages;
		}
	}

	public static enum SearchTextByKeywordTestResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();

		User sender = userRepository.findById(input._senderId);
		User receiver = userRepository.findById(input._receiverId);

		GettingMessages.InputValues gettingAllMessagesInput = new GettingMessages.InputValues(
				input._senderId);
		GettingMessages gettingAllMessages = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingAllMessagesOutput = gettingAllMessages
				.execute(gettingAllMessagesInput);

		List<Message> messages = gettingAllMessagesOutput.getMessages();
		messages = messages.stream().filter(m -> m.getSender().equals(sender) && m.getReceiver().equals(receiver))
				.toList();
//		messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));

		messages = searchTextByKeyword(input._keyword, messages);

		return new OutputValues(SearchTextByKeywordTestResult.Successed, messages);
	}

	private List<Message> searchTextByKeyword(String keyword, List<Message> messages) {
		List<Message> result = new ArrayList<>();
		for (Message item : messages) {
			if (item.getContent().contains(keyword)) {
				result.add(item);
			}
		}
		return result;
	}

}
