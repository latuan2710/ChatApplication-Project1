package com.chat.usecases.group;

import com.chat.domains.Group;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class GroupLeaving extends UseCase<GroupLeaving.InputValues, GroupLeaving.OutputValues> {
	private DataStorage _dataStorage;

	public GroupLeaving(DataStorage _dataStorage) {
		super();
		this._dataStorage = _dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _groupId;

		public InputValues(String userId, String groupId) {
			this._userId = userId;
			this._groupId = groupId;
		}
	}

	public static class OutputValues {
		private leavingGroupResult _result;

		public OutputValues(leavingGroupResult result) {
			this._result = result;
		}

		public leavingGroupResult getResult() {
			return _result;
		}
	}

	public enum leavingGroupResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Group groupInput = _dataStorage.getGroupRepository().findById(input._groupId);
		User userInput = _dataStorage.getUserRepository().findById(input._userId);

		for (User user : groupInput.getUsers()) {
			if (user.equals(userInput)) {
				groupInput.getUsers().remove(user);
				return new OutputValues(leavingGroupResult.Successed);
			}
		}
		return new OutputValues(leavingGroupResult.Failed);
	}
}
