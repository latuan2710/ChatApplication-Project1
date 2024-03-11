package com.chat.domains;

public class GroupRequest extends BaseEntity {
	private User _user;
	private PrivateGroup _group;
	public GroupRequestStatus _status;

	public enum GroupRequestStatus {
		Waiting, Rejected, Accepted
	}

	public GroupRequest(User _user, PrivateGroup _group, GroupRequestStatus _status) {
		super();
		this._user = _user;
		this._group = _group;
		this._status = _status;
	}

	public User get_user() {
		return _user;
	}

	public PrivateGroup get_group() {
		return _group;
	}

	public GroupRequestStatus get_status() {
		return _status;
	}

	public GroupRequestStatus set_status(GroupRequestStatus _status) {
		return _status;
	}

	
	
	

}
