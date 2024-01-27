package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;

public class GroupCreation extends UseCase<GroupCreation.InputValues, GroupCreation.OutputValues> {
	private DataStorage _dataStorage;

	public GroupCreation(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private User _user;
		private GroupType _type;

		public InputValues(User user, GroupType type) {
			this._user = user;
			this._type = type;
		}
	}

	public static class OutputValues {
		private GroupCreationResult _result;
		private String _message;
		private Group _group;

		public OutputValues(GroupCreationResult result, String message, Group group) {
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

		public Group getGroup() {
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
		User user = input._user;
		Group group = null;

		users.add(user);

		if (input._type == GroupType.Public) {
			group = createPublicGroup(repository, users);
		} else {
			group = createPrivateGroup(repository, users, user);
		}

		return new OutputValues(GroupCreationResult.Successed, "", group);
	}

	private Group createPrivateGroup(GroupRepository repository, List<User> users, User user) {
		PrivateGroup group = new PrivateGroup(users, GroupType.Private, user);

		repository.add(group);
		return group;
	}

	private Group createPublicGroup(GroupRepository repository, List<User> users) {
		String joiningCode = repository.generateJoiningCode();
		PublicGroup group = new PublicGroup(users, GroupType.Public, joiningCode);

		repository.add(group);
		return group;
	}
}
