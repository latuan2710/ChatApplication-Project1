package com.chat.usecases.group;

import com.chat.domains.PrivateGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class DeletingGroupMembers extends UseCase<DeletingGroupMembers.InputValues, DeletingGroupMembers.OutputValues> {
	private DataStorage _dataStorage;

	public DeletingGroupMembers(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _adminId;
		private String _memberId;
		private String _groupId;

		public InputValues(String adminId, String memberId, String groupId) {
			this._adminId = adminId;
			this._memberId = memberId;
			this._groupId = groupId;
		}

	}

	public static class OutputValues {
		private DeletingGroupMembersResult _result;

		public OutputValues(DeletingGroupMembersResult result) {
			this._result = result;
		}

		public DeletingGroupMembersResult getResult() {
			return _result;
		}
	}

	public enum DeletingGroupMembersResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository groupRepository = _dataStorage.getGroupRepository();
		Repository<User> userRepository = _dataStorage.getUserRepository();

		PrivateGroup group = (PrivateGroup) groupRepository.findById(input._groupId);
		User admin = userRepository.findById(input._adminId);
		User member = userRepository.findById(input._memberId);

		if (group.hasAdmin(admin)) {
			group.removeMember(member);

			return new OutputValues(DeletingGroupMembersResult.Successed);
		}

		return new OutputValues(DeletingGroupMembersResult.Failed);
	}
}
