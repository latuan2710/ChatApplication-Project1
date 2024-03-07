package com.chat.usecases.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
		private String _userId;
		private GroupType _type;

		public InputValues(String userId, GroupType type) {
			this._userId = userId;
			this._type = type;
		}
	}

	public static class OutputValues {
		private GroupCreationResult _result;
		private String _message;
		private Group _group;

		public OutputValues(GroupCreationResult result, String messageId, Group group) {
			this._result = result;
			this._message = messageId;
			this._group = group;
		}

		public GroupCreationResult getResult() {
			return _result;
		}

		public String getMessageId() {
			return _message;
		}

		public Group getGroup() {
			return _group;
		}

	}

	public enum GroupCreationResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GroupRepository repository = (GroupRepository) _dataStorage.getGroupRepository();
		List<User> users = new ArrayList<>();
		User user = _dataStorage.getUserRepository().findById(input._userId);
		Group group = null;

		if (user == null) {
			return new OutputValues(GroupCreationResult.Failed, "", group);
		}

		users.add(user);

		if (input._type == GroupType.Public) {
			group = createPublicGroup(repository, users);
		} else {
			group = createPrivateGroup(repository, new ArrayList<>(), user);
		}

		return new OutputValues(GroupCreationResult.Successed, "", group);
	}

	private Group createPrivateGroup(GroupRepository repository, List<User> users, User user) {
		List<User> admins = new ArrayList<>();
		admins.add(user);

		PrivateGroup group = new PrivateGroup(users, GroupType.Private, admins);

		repository.add(group);
		return group;
	}

	private Group createPublicGroup(GroupRepository repository, List<User> users) {
		String joiningCode = this.generateJoiningCode(repository);
		PublicGroup group = new PublicGroup(users, GroupType.Public, joiningCode);

		repository.add(group);
		return group;
	}

	private String generateJoiningCode(GroupRepository groupRepository) {
		List<PublicGroup> groups = groupRepository.getAllPublicGroup();
		HashSet<String> existCodes = new HashSet<>();

		for (PublicGroup group : groups) {
			existCodes.add(group.getJOINING_CODE());
		}

		String joining_code = null;
		boolean flag = true;

		while (flag) {
			joining_code = this.getRandomString(6);

			if (!existCodes.contains(joining_code)) {
				flag = false;
			}
		}

		return joining_code;
	}

	private String getRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		StringBuilder randomString = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			randomString.append(characters.charAt(index));
		}

		return randomString.toString();
	}
}
