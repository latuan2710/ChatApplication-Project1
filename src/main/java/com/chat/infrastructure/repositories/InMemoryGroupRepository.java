package com.chat.infrastructure.repositories;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PublicGroup;
import com.chat.usecases.adapters.GroupRepository;

public class InMemoryGroupRepository extends InMemoryRepository<Group> implements GroupRepository {

	@Override
	public String generateJoiningCode() {
		List<PublicGroup>groups=this.getAll().stream().filter(g->g.);
		return null;
	}

}
