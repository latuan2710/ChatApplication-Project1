package com.chat.usecases.group;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class GroupJoinRequest extends UseCase<GroupJoinRequest.InputValues, GroupJoinRequest.OutputValues> {
	private DataStorage _dataStorage;

	public GroupJoinRequest(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _groupId;
		private String _userSendRequestId;

		public InputValues(String groupId, String userSendRequestId) {
			super();
			this._groupId = groupId;
			this._userSendRequestId = userSendRequestId;
		}
	}

	public static class OutputValues {
		private GroupJoinRequestResult _result;

		public OutputValues(GroupJoinRequestResult result, String message) {
			this._result = result;
		}

		public GroupJoinRequestResult getResult() {
			return _result;
		}
	}

	public static enum GroupJoinRequestResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		GroupRepository groupRepository = _dataStorage.getGroupRepository();

		User userSendRequestId = userRepository.findById(input._userSendRequestId);
		Group inputGroup = groupRepository.findById(input._groupId);

		if (userSendRequestId == null || inputGroup == null) {
			return new OutputValues(GroupJoinRequestResult.Failed, "");
		} else {
			return new OutputValues(GroupJoinRequestResult.Successed, "");
		}
	}

}
