package com.chat.usecases.group;

import com.chat.domains.GroupRequest;
import com.chat.domains.PrivateGroup;
import com.chat.domains.User;
import com.chat.domains.GroupRequest.GroupRequestStatus;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class GroupRequestHandler extends UseCase<GroupRequestHandler.InputValues, GroupRequestHandler.OutputValues> {
	private DataStorage _dataStorage;

	public GroupRequestHandler(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _adminId;
		private boolean _isAccept;
		private String _groupId;
		
		public InputValues(String adminId, boolean isAccept, String groupId) {
			this._adminId = adminId;
			this._isAccept = isAccept;
			this._groupId = groupId;
		}

	}

	public static class OutputValues {
		private RequestResult _result;

		public OutputValues(RequestResult result) {
			this._result = result;
		}

		public RequestResult getResult() {
			return _result;
		}
	}

	public static enum RequestResult {
		Approve, Reject
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<GroupRequest> groupRequestRepository = _dataStorage.getGroupRequestRepository();
		GroupRepository groupRepository = _dataStorage.getGroupRepository();
		
		//get group and adminId
		PrivateGroup inputGroup = (PrivateGroup) groupRepository.findById(input._groupId);
		User admin = userRepository.findById(input._adminId);
		
		//get request
		GroupRequest userRequest = (GroupRequest) groupRequestRepository.getAll();
		
		User userReqUserId = userRequest.get_user();
		
		Boolean isAccept = input._isAccept;
		
		if (inputGroup.getId()==userRequest.get_group().getId() && isAccept == true) {
			addNewMember(inputGroup, admin, userReqUserId);
			userRequest.set_status(GroupRequestStatus.Accepted);
			return new OutputValues(RequestResult.Approve);
		}
		else {
			userRequest.set_status(GroupRequestStatus.Rejected);
			return new OutputValues(RequestResult.Reject);
		}
		
	}

	public boolean addNewMember(PrivateGroup inputGroup, User admin, User userRequest) {
		
		if (inputGroup.hasAdmin(admin)) {
			inputGroup.addMember(userRequest);
			return true;
		}
		return false;

	}

}
