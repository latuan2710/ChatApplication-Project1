package com.chat.infrastructure.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;
import com.chat.usecases.adapters.GroupRepository;

public class InMemoryGroupRepository extends InMemoryRepository<Group> implements GroupRepository {

	@Override
	public List<PublicGroup> getAllPublicGroup() {
		List<Group> allGroups = this.getAll();
		return allGroups.stream().filter(g -> g instanceof PublicGroup).map(g -> (PublicGroup) g)
				.collect(Collectors.toList());
	}

	@Override
	public List<PrivateGroup> getAllPrivateGroup() {
		List<Group> allGroups = this.getAll();
		return allGroups.stream().filter(g -> g instanceof PrivateGroup).map(g -> (PrivateGroup) g)
				.collect(Collectors.toList());
	}
}
