package com.chat.infrastructure.repositories;

import java.util.HashSet;
import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.PublicGroup;
import com.chat.usecases.adapters.GroupRepository;

public class InMemoryGroupRepository extends InMemoryRepository<Group> implements GroupRepository {

	@Override
	public String generateJoiningCode() {
		List<PublicGroup> groups = (List<PublicGroup>) this.getAll().stream()
				.filter(g -> g.getType().equals(GroupType.Public));

		HashSet<String> existCodes = new HashSet<>();

		for (PublicGroup group : groups) {
			existCodes.add(group.getJOINING_CODE());
		}

		String joining_code;
		boolean flag = true;
//		while()

		return null;
	}

}
