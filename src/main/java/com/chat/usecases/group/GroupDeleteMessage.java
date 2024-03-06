package com.chat.usecases.group;

import com.chat.domains.File;
import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class GroupDeleteMessage extends UseCase<GroupDeleteMessage.InputValues, GroupDeleteMessage.OutputValues> {
	private DataStorage _dataStorage;

	public GroupDeleteMessage(DataStorage dataStorage) {
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
		private GroupDeleteMessageResult _result;

		public OutputValues(GroupDeleteMessageResult result) {
			this._result = result;
		}

		public GroupDeleteMessageResult getResult() {
			return _result;
		}
	}

	public static enum GroupDeleteMessageResult {
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
			return new OutputValues(GroupDeleteMessageResult.Failed);
		}

		boolean isAdminGroup = group instanceof PrivateGroup && ((PrivateGroup) group).hasAdmin(user);
		boolean isSender = group instanceof PublicGroup && message.getSender().equals(user);

//		if (isAdminGroup || isSender) {
//			DeletingMessage.InputValues deletingMessageInput = new DeletingMessage.InputValues(
//					message.getSender().getId(), message.getId());
//			DeletingMessage deletingMessage = new DeletingMessage(_dataStorage);
//			deletingMessage.execute(deletingMessageInput);
//			return new OutputValues(GroupDeleteMessageResult.Successed);
//		}

		if (isAdminGroup || isSender) {
			FileService fileService = new FileService();
			for (File file : message.getAttachments()) {
				fileService.deleteFile(file.getPath());
			}
			messageRepository.deleteById(input._messageId);
			return new OutputValues(GroupDeleteMessageResult.Successed);
		}

		return new OutputValues(GroupDeleteMessageResult.Failed);

	}
}
