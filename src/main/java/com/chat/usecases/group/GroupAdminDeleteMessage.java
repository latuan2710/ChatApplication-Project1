package com.chat.usecases.group;

import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.PrivateGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;
import com.chat.usecases.message.DeletingMessage;
import com.chat.usecases.message.DeletingMessage.DeletingMessageResult;

public class GroupAdminDeleteMessage
		extends UseCase<GroupAdminDeleteMessage.InputValues, GroupAdminDeleteMessage.OutputValues> {
	private DataStorage _dataStorage;

	public GroupAdminDeleteMessage(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _messageId;
		private String _groupId;

		public InputValues(String userId, String messageId, String groupId) {
			this._userId = userId;
			this._messageId = messageId;
			this._groupId = groupId;
		}

	}

	public static class OutputValues {
		private GroupAdminDeleteMessageResult _result;

		public OutputValues(GroupAdminDeleteMessageResult result) {
			this._result = result;
		}

		public GroupAdminDeleteMessageResult getResult() {
			return _result;
		}
	}

	public enum GroupAdminDeleteMessageResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository groupRepository = _dataStorage.getGroupRepository();
		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<Message> messageRepository = _dataStorage.getMessageRepository();

		Group group = groupRepository.findById(input._groupId);
		User user = userRepository.findById(input._userId);
		Message message = messageRepository.findById(input._messageId);

		if (group == null || user == null || message == null) {
			return new OutputValues(GroupAdminDeleteMessageResult.Failed);
		}

		boolean isAdminGroup = group instanceof PrivateGroup && ((PrivateGroup) group).hasAdmin(user);

		if (isAdminGroup) {
			DeletingMessage.InputValues deletingMessageInput = new DeletingMessage.InputValues(
					message.getSender().getId(), message.getId());
			DeletingMessage deletingMessage = new DeletingMessage(_dataStorage);
			DeletingMessage.OutputValues deletingMessageOutput = deletingMessage.execute(deletingMessageInput);
			
			if (deletingMessageOutput.getResult() == DeletingMessageResult.Successed) {
				return new OutputValues(GroupAdminDeleteMessageResult.Successed);
			} else {
				return new OutputValues(GroupAdminDeleteMessageResult.Failed);
			}

		}

		return new OutputValues(GroupAdminDeleteMessageResult.Failed);

	}
}
