package com.chat.usecases.adapters;

import com.chat.domains.Group;

public interface GroupRepository extends Repository<Group> {
	String generateJoiningCode();
}
