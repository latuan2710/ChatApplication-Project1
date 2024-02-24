package com.chat.usecases.message;

import static org.hamcrest.CoreMatchers.containsString;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Conversation;
import com.chat.domains.Message;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.MessageRepository;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.GettingAllMessageByUserId.GettingAllMessageByUserIdResult;
import com.chat.usecases.user.UserRegistration;

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
			super();
			this._keyword = keyword;
			this._senderId = _senderId;
			this._receiverId = _receiverId;
		}

	}

	public static class OutputValues {
		private SearchTextByKeywordTestResult _result;
		private Conversation _conversation;

		public OutputValues(SearchTextByKeywordTestResult result, Conversation conversation) {
			_conversation = conversation;
			_result = result;
		}

		public SearchTextByKeywordTestResult getResult() {
			return _result;
		}

		public Conversation getConversation() {
			return _conversation;
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

		GettingAllMessageByUserId.InputValues gettingAllMessagesInput = new GettingAllMessageByUserId.InputValues(
				input._senderId);
		GettingAllMessageByUserId gettingAllMessages = new GettingAllMessageByUserId(_dataStorage);
		GettingAllMessageByUserId.OutputValues gettingAllMessagesOutput = gettingAllMessages
				.execute(gettingAllMessagesInput);

		List<Message> messages = gettingAllMessagesOutput.getMessages();
		messages = messages.stream().filter(m -> m.getSender().equals(sender) && m.getReceiver().equals(receiver))
				.toList();
//		messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));

		messages = searchTextByKeyword(input._keyword, messages);

		Conversation conversation = new Conversation(messages, List.of(sender, receiver));

		return new OutputValues(SearchTextByKeywordTestResult.Successed, conversation);
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
