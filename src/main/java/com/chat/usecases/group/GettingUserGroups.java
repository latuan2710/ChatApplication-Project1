package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
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
		private List<Group> _group;

		public OutputValues(GettingGroupResult result, List<Group> group) {
			this._result = result;
			this._group = group;
		}

		public GettingGroupResult getResult() {
			return _result;
		}

		public List<Group> getGroups() {
			return _group;
		}
	}

	public enum GettingGroupResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<Group> result = new ArrayList<>();
		List<Group> groups = _dataStorage.getGroupRepository().getAll();
		User userInput = _dataStorage.getUserRepository().findById(input._userId);

		if (userInput == null) {
			return new OutputValues(GettingGroupResult.Failed, null);
		}

		for (Group group : groups) {
			if (group instanceof PublicGroup && group.getUsers().contains(userInput)) {
				result.add(group);
			} else if (group instanceof PrivateGroup) {
				PrivateGroup privateGroup = (PrivateGroup) group;
				if (privateGroup.getUsers().contains(userInput) || privateGroup.getAdmins().contains(userInput)) {
					result.add(group);
				}
			}
		}

		return new OutputValues(GettingGroupResult.Successed, result);
	}
}
