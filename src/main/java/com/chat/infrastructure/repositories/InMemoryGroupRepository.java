package com.chat.infrastructure.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.domains.User;
import com.chat.usecases.adapters.GroupRepository;

public class InMemoryGroupRepository extends InMemoryRepository<Group> implements GroupRepository {

	@Override
	public String generateJoiningCode() {
		List<PublicGroup> groups = this.getAllPublicGroup();
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

	public String getRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder randomString = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			randomString.append(characters.charAt(index));
		}

		return randomString.toString();
	}

	@Override
	public List<PublicGroup> getAllPublicGroup() {
		return (List<PublicGroup>) this.getAll().stream().filter(g -> g.getType().equals(GroupType.Public));
	}

	@Override
	public List<PrivateGroup> getAllPrivateGroup() {
		return (List<PrivateGroup>) this.getAll().stream().filter(g -> g.getType().equals(GroupType.Private));
	}

	@Override
	public void addUser(String id, User u) {
		Group group = this.getById(id);
		List<User> users = group.getUsers();
		for (User user : users) {
			if (user != u) {
				group.getUsers().add(user);
			}
		}
	}

}
