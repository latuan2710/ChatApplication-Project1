package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.Group.GroupType;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.group.PrivateGroupCreation.GroupCreationResult;

public class PublicGroupCreation extends UseCase<PublicGroupCreation.InputValues, PublicGroupCreation.OutputValues> {
	private DataStorage _dataStorage;

	public PublicGroupCreation(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private User _user;

		public InputValues(User user) {
			this._user = user;
		}
	}

	public static class OutputValues {
		private GroupCreationResult _result;
		private String _message;
		private PublicGroup _group;

		public OutputValues(GroupCreationResult result, String message, PublicGroup group) {
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

		public PublicGroup getGroup() {
			return _group;
		}

	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository repository = (GroupRepository) _dataStorage.getGroupRepository();
		List<User> users = new ArrayList<>();
		String joiningCode = repository.generateJoiningCode();
		PublicGroup group = new PublicGroup(users, GroupType.Public, joiningCode);

		users.add(input._user);
		repository.add(group);

		return new OutputValues(GroupCreationResult.Successed, "", group);
	}

}
