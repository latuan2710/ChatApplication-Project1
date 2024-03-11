package com.chat.usecases.message;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.group.GettingUserGroups;

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

	public enum GettingMessagesResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<Message> messageMepository = _dataStorage.getMessageRepository();

		List<Group> groups = getUserGroups(input);
		
		if(groups==null) {
			return new OutputValues(GettingMessagesResult.Failed, null);
		}

		Predicate<Message> predicate = m -> (m.getSender().getId().equals(input._senderId))
				|| m.getReceiver().getId().equals(input._senderId) || groups.contains(m.getReceiver());
		List<Message> messages = messageMepository.getAll();
		List<Message> result = messages.stream().filter(predicate).toList();

		return new OutputValues(GettingMessagesResult.Successed, result);
	}

	private List<Group> getUserGroups(InputValues input) {
		GettingUserGroups.InputValues gettingUserGroupsInput = new GettingUserGroups.InputValues(input._senderId);
		GettingUserGroups gettingUserGroups = new GettingUserGroups(_dataStorage);
		GettingUserGroups.OutputValues gettingUserGroupsOutput = gettingUserGroups.execute(gettingUserGroupsInput);

		List<Group> groups = gettingUserGroupsOutput.getGroups();
		return groups;
	}
}
