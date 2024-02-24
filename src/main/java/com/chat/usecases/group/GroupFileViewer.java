package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.chat.domains.File;
import com.chat.domains.Group;
import com.chat.domains.Message;
import com.chat.infrastructure.repositories.InMemoryMessageRepository;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.MessageRepository;
import com.chat.usecases.message.GettingAllMessageByUserId;
import com.chat.usecases.message.GettingAllMessageByUserId.GettingAllMessageByUserIdResult;

public class GroupFileViewer extends UseCase<GroupFileViewer.InputValues, GroupFileViewer.OutputValues> {
	private DataStorage _dataStorage;

	public GroupFileViewer(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;
		private String _groupId;

		public InputValues(String senderId, String groupId) {
			this._senderId = senderId;
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

		GettingAllMessageByUserId.InputValues gettingMessageInput = new GettingAllMessageByUserId.InputValues(
				input._senderId);
		GettingAllMessageByUserId gettingMessage = new GettingAllMessageByUserId();
		GettingAllMessageByUserId.OutputValues gettingMessageOutput = gettingMessage.execute(gettingMessageInput);

		List<Message> messages = gettingMessageOutput.getMessages();
		Group group = groupRepository.findById(input._groupId);

		Predicate<Message> groupMessagePredicate = m -> m.getReceiver().equals(group);
		messages = messages.stream().filter(groupMessagePredicate).collect(Collectors.toList());

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
