package com.chat.usecases.group;

import com.chat.domains.GroupRequest;
import com.chat.domains.GroupRequest.GroupRequestStatus;
import com.chat.domains.PrivateGroup;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class GroupRequestHandler extends UseCase<GroupRequestHandler.InputValues, GroupRequestHandler.OutputValues> {
	private DataStorage _dataStorage;

	public GroupRequestHandler(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _adminId;
		private String _requestId;
		private boolean _isAccept;

		public InputValues(String adminId, String requestId, boolean isAccept) {
			this._adminId = adminId;
			this._isAccept = isAccept;
			this._requestId = requestId;
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

	public enum RequestResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<User> userRepository = _dataStorage.getUserRepository();
		Repository<GroupRequest> groupRequestRepository = _dataStorage.getGroupRequestRepository();

		User admin = userRepository.findById(input._adminId);
		GroupRequest request = groupRequestRepository.findById(input._requestId);

		if (admin == null || request == null) {
			return new OutputValues(RequestResult.Failed);
		}

		PrivateGroup group = request.getGroup();

		if (group.hasAdmin(admin)) {
			if (input._isAccept) {
				request.setStatus(GroupRequestStatus.Accepted);
				group.addMember(request.getUser());
			} else {
				request.setStatus(GroupRequestStatus.Rejected);
			}
			
			return new OutputValues(RequestResult.Successed);
		}

		return new OutputValues(RequestResult.Failed);

	}
}
