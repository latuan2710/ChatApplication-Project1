package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;

public class PrivateGroupCreation extends UseCase<PrivateGroupCreation.InputValues, PrivateGroupCreation.OutputValues> {
	private DataStorage _dataStorage;

	public PrivateGroupCreation(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private User _user;

		public InputValues(User _user) {
			this._user = _user;
		}
	}

	public static class OutputValues {
		private GroupCreationResult _result;
		private String _message;
		private PrivateGroup _group;

		public OutputValues(GroupCreationResult result, String message, PrivateGroup group) {
			this._result = result;
			this._message = message;
			this._group = group;
		}

		public GroupCreationResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}

		public PrivateGroup getGroup() {
			return _group;
		}

	}

	public static enum GroupCreationResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository repository = (GroupRepository) _dataStorage.getGroupRepository();
		List<User> users = new ArrayList<>();
		PrivateGroup group = new PrivateGroup(users, GroupType.Private, input._user);

		users.add(input._user);
		repository.add(group);

		return new OutputValues(GroupCreationResult.Successed, "", group);
	}

}
