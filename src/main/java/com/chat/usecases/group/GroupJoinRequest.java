package com.chat.usecases.group;

import com.chat.domains.Group;
import com.chat.domains.GroupRequest;
import com.chat.domains.GroupRequest.GroupRequestStatus;
import com.chat.domains.PrivateGroup;
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
		private String _userId;

		public InputValues(String groupId, String userId) {
			this._groupId = groupId;
			this._userId = userId;
		}
	}

	public static class OutputValues {
		private GroupJoinRequestResult _result;

		public OutputValues(GroupJoinRequestResult result) {
			this._result = result;
		}

		public GroupJoinRequestResult getResult() {
			return _result;
		}
	}

	public enum GroupJoinRequestResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<GroupRequest> groupRequestRepository = _dataStorage.getGroupRequestRepository();
		GroupRepository groupRepository = _dataStorage.getGroupRepository();

		User user = userRepository.findById(input._userId);
		Group group = groupRepository.findById(input._groupId);

		if (user == null || group == null || !(group instanceof PrivateGroup)) {
			return new OutputValues(GroupJoinRequestResult.Failed);
		}

		GroupRequest groupRequest = new GroupRequest(user, (PrivateGroup) group, GroupRequestStatus.Waiting);
		groupRequestRepository.add(groupRequest);

		return new OutputValues(GroupJoinRequestResult.Successed);
	}

}
