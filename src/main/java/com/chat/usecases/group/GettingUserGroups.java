package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class GettingUserGroups extends UseCase<GettingUserGroups.InputValues, GettingUserGroups.OutputValues> {
	private DataStorage _dataStorage;

	public GettingUserGroups(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;

		public InputValues(String userId) {
			this._userId = userId;
		}
	}

	public static class OutputValues {
		private GettingGroupResult _result;
		private String _message;
		private List<Group> _group;

		public OutputValues(GettingGroupResult result, String message, List<Group> group) {
			this._result = result;
			this._message = message;
			this._group = group;
		}

		public GettingGroupResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}

		public List<Group> getGroups() {
			return _group;
		}
	}

	public static enum GettingGroupResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<Group> result = new ArrayList<>();
		List<Group> groups = _dataStorage.getGroupRepository().getAll();

		if (groups.isEmpty()) {
			return new OutputValues(GettingGroupResult.Successed, "", result);
		}

		User userInput =  _dataStorage.getUserRepository().findById(input._userId);
		for (Group group : groups) {
			for (User u : group.getUsers()) {
				if (u.equals(userInput)) {
					result.add(group);
				}
			}
		}
		
		if (result.isEmpty()) {
			return new OutputValues(GettingGroupResult.Failed, "", result);
		} else {
			return new OutputValues(GettingGroupResult.Successed, "", result);
		}
	}
}
