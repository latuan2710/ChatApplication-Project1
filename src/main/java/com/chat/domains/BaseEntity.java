package com.chat.domains;

import java.util.UUID;

public abstract class BaseEntity {
	private String _id;

	public BaseEntity() {
		this._id = UUID.randomUUID().toString();
	}

	public String getId() {
		return _id;
	}
}
