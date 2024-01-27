package com.chat.domains;

import java.util.List;

public class PublicGroup extends Group {
	private final String JOINING_CODE;

	public PublicGroup(List<User> users, GroupType type, String jOINING_CODE) {
		super(users, type);
		JOINING_CODE = jOINING_CODE;
	}

	public String getJOINING_CODE() {
		return JOINING_CODE;
	}

}
