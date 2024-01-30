package com.chat.usecases.group;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class GroupJoining extends UseCase<GroupJoining.InputValues, GroupJoining.OutputValues> {
	private DataStorage _dataStorage;

	public GroupJoining(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private User _user;
		private User _invitor;
		private Group _group;
		private String _inviteCode;

		public InputValues(User user, User invitor, Group group) {
			this._user = user;
			this._invitor = invitor;
			this._group = group;
		}

		public InputValues(User user, String inviteCode) {
			super();
			this._user = user;
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
		String inviteCode = input._inviteCode;
		Group inputGroup = input._group;

		boolean isJoinedByCode = false;
		boolean isJoinedByMember = false;
		boolean isJoinByAdmin = false;

		if (inviteCode != null) {
			isJoinedByCode = joinByCode(inviteCode, input._user);
		} else if (inputGroup instanceof PublicGroup) {
			isJoinedByMember = joinByMember((PublicGroup) inputGroup, input._invitor, input._user);
		} else if (inputGroup instanceof PrivateGroup) {
			isJoinByAdmin = joinByAdmin((PrivateGroup) inputGroup, input._invitor, input._user);
		}

		return (isJoinedByCode || isJoinedByMember || isJoinByAdmin)
				? new OutputValues(GroupJoiningResult.Successed, "")
				: new OutputValues(GroupJoiningResult.Failed, "");
	}

	private boolean joinByCode(String inviteCode, User user) {
		PublicGroup group = _dataStorage.getGroupRepository().findByJoiningCode(inviteCode);

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
		List<User> admins = inputGroup.getAdmins();

		for (int i = 0; i < admins.size(); i++) {
			if (admins.get(i).equals(invitor)) {
				admins.add(user);
				inputGroup.setUsers(admins);
				return true;
			}
		}

		return false;
	}
}
