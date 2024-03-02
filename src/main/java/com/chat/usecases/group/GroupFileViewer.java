package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.File;
import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.message.GettingMessages;

public class GroupFileViewer extends UseCase<GroupFileViewer.InputValues, GroupFileViewer.OutputValues> {
	private DataStorage _dataStorage;

	public GroupFileViewer(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _groupId;

		public InputValues(String senderId, String groupId) {
			this._userId = senderId;
			this._groupId = groupId;
		}
	}

	public static class OutputValues {
		private GettingAllFileResult _result;
		private List<File> _files;

		public OutputValues(GettingAllFileResult result, List<File> files) {
			_files = files;
			_result = result;
		}

		public GettingAllFileResult getResult() {
			return _result;
		}

		public List<File> getFiles() {
			return _files;
		}

	}

	public static enum GettingAllFileResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository groupRepository = _dataStorage.getGroupRepository();

		GettingMessages.InputValues gettingMessageInput = new GettingMessages.InputValues(input._userId);
		GettingMessages gettingMessage = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingMessageOutput = gettingMessage.execute(gettingMessageInput);

		List<Message> messages = gettingMessageOutput.getMessages();

		Group group = groupRepository.findById(input._groupId);
		Predicate<Message> groupMessagePredicate = m -> m.getReceiver().getId().equals(group.getId());
		messages = messages.stream().filter(groupMessagePredicate).toList();

		if (messages.isEmpty()) {
			return new OutputValues(GettingAllFileResult.Successed, null);
		}

		List<File> result = new ArrayList<>();
		for (Message message : messages) {
			result.addAll(message.getAttachments());
		}

		return new OutputValues(GettingAllFileResult.Successed, result.isEmpty() ? null : result);

	}
}
