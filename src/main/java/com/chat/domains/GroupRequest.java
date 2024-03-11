package com.chat.domains;

public class GroupRequest extends BaseEntity {
	private User _user;
	private PrivateGroup _group;
	private GroupRequestStatus _status;

	public GroupRequest(User user, PrivateGroup group, GroupRequestStatus status) {
		super();
		this._user = user;
		this._group = group;
		this._status = status;
	}

	public User getUser() {
		return _user;
	}

	public PrivateGroup getGroup() {
		return _group;
	}

	public GroupRequestStatus getStatus() {
		return _status;
	}

	public GroupRequestStatus setStatus(GroupRequestStatus status) {
		return status;
	}

	public enum GroupRequestStatus {
		Waiting, Rejected, Accepted
	}
}
