package com.chat.usecases.adapters;

import java.util.List;

import com.chat.domains.Group;
import com.chat.domains.PrivateGroup;
import com.chat.domains.PublicGroup;

public interface GroupRepository extends Repository<Group> {
	List<PublicGroup> getAllPublicGroup();

	List<PrivateGroup> getAllPrivateGroup();
}
