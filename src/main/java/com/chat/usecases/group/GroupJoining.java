package com.chat.usecases.group;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class GroupJoining extends UseCase<GroupJoining.InputValues, GroupJoining.OutputValues> {
	private DataStorage _dataStorage;

	public GroupJoining(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _invitorId;
		private String _groupId;
		private String _inviteCode;

		public InputValues(String userId, String invitorId, String group) {
			this._userId = userId;
			this._invitorId = invitorId;
			this._groupId = group;
		}

		public InputValues(String userId, String inviteCode) {
			super();
			this._userId = userId;
			this._inviteCode = inviteCode;
		}

	}

	public static class OutputValues {
		private GroupJoiningResult _result;
		private String _message;

		public OutputValues(GroupJoiningResult result, String message) {
			this._result = result;
			this._message = message;
		}

		public GroupJoiningResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}

	}

	public static enum GroupJoiningResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		GroupRepository groupRepository = _dataStorage.getGroupRepository();

		String inviteCode = input._inviteCode;
		Group inputGroup = groupRepository.findById(input._groupId);
		User user = userRepository.findById(input._userId);
		User invitor = userRepository.findById(input._invitorId);

		boolean isJoinedByCode = false;
		boolean isJoinedByMember = false;
		boolean isJoinByAdmin = false;

		if (inviteCode != null) {
			isJoinedByCode = joinByCode(inviteCode, user);
		} else if (inputGroup instanceof PublicGroup) {
			isJoinedByMember = joinByMember((PublicGroup) inputGroup, invitor, user);
		} else if (inputGroup instanceof PrivateGroup) {
			isJoinByAdmin = joinByAdmin((PrivateGroup) inputGroup, invitor, user);
		}

		return (isJoinedByCode || isJoinedByMember || isJoinByAdmin)
				? new OutputValues(GroupJoiningResult.Successed, "")
				: new OutputValues(GroupJoiningResult.Failed, "");
	}

	private boolean joinByCode(String inviteCode, User user) {
		FindingByGroupCode findingByGroupCode = new FindingByGroupCode(_dataStorage);
		FindingByGroupCode.InputValues input = new FindingByGroupCode.InputValues(inviteCode);
		FindingByGroupCode.OutputValues output = findingByGroupCode.execute(input);

		PublicGroup group = output.getGroup();

		if (group != null) {
			List<User> members = group.getUsers();
			members.add(user);
			group.setUsers(members);
			return true;
		} else {
			return false;
		}

	}

	private boolean joinByMember(PublicGroup inputGroup, User invitor, User user) {
		List<User> members = inputGroup.getUsers();

		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).equals(invitor)) {
				members.add(user);
				inputGroup.setUsers(members);
				return true;
			}
		}

		return false;
	}

	private boolean joinByAdmin(PrivateGroup inputGroup, User invitor, User user) {
		if (invitor != null && inputGroup.isAdmin(invitor)) {
			inputGroup.addMember(user);
			return true;
		}

		return false;
	}
}
