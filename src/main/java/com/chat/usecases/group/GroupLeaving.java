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
		private User _user;
		private Group _group;

		public InputValues(User _user, Group _group) {
			super();
			this._user = _user;
			this._group = _group;
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

	public static enum leavingGroupResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Group groupInput = input._group;
		User userInput = input._user;

		for (User user : groupInput.getUsers()) {
			if (user.equals(userInput)) {
				groupInput.getUsers().remove(user);
				return new OutputValues(leavingGroupResult.Successed);
			}
		}
		return new OutputValues(leavingGroupResult.Failed);
	}
}
