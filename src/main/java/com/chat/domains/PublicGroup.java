package com.chat.domains;

import java.util.List;

public class PublicGroup extends Group {
	private final String JOINING_CODE;

	public PublicGroup(List<User> users, String joiningCode) {
		super(users);
		this.JOINING_CODE = joiningCode;
	}

	public String getJOINING_CODE() {
		return JOINING_CODE;
	}

}
