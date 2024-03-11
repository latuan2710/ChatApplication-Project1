package com.chat.usecases.group;

import java.util.List;
import java.util.stream.Collectors;

import com.chat.domains.Group;
import com.chat.domains.GroupRequest;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.GroupRepository;
import com.chat.usecases.adapters.Repository;

public class GettingGroupRequest extends UseCase<GettingGroupRequest.InputValues, GettingGroupRequest.OutputValues> {
	private DataStorage _dataStorage;

	public GettingGroupRequest(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _groupId;

		public InputValues(String groupId) {
			this._groupId = groupId;
		}

	}

	public static class OutputValues {
		private GettingGroupRequestResult _result;
		private List<GroupRequest> _requests;

		public OutputValues(GettingGroupRequestResult result, List<GroupRequest> requests) {
			this._result = result;
			this._requests = requests;
		}

		public GettingGroupRequestResult getResult() {
			return _result;
		}

		public List<GroupRequest> getRequests() {
			return _requests;
		}

	}

	public enum GettingGroupRequestResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<GroupRequest> groupRequestRepository = _dataStorage.getGroupRequestRepository();
		GroupRepository groupRepository = _dataStorage.getGroupRepository();

		List<GroupRequest> requests = groupRequestRepository.getAll();
		Group group = groupRepository.findById(input._groupId);

		if (group == null) {
			return new OutputValues(GettingGroupRequestResult.Failed, null);
		}

		requests = requests.stream().filter(r -> r.getGroup().equals(group)).collect(Collectors.toList());

		return new OutputValues(GettingGroupRequestResult.Successed, requests);
	}
}
