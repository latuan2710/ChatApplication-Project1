package com.chat.usecases.group;

import java.util.List;

import com.chat.domains.PublicGroup;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class FindingByGroupCode extends UseCase<FindingByGroupCode.InputValues, FindingByGroupCode.OutputValues> {
	private DataStorage _dataStorage;

	public FindingByGroupCode(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private final String JOINING_CODE;

		public InputValues(String jOINING_CODE) {
			JOINING_CODE = jOINING_CODE;
		}
	}

	public static class OutputValues {
		private FindingByGroupCodeResult _result;
		private PublicGroup _group;

		public OutputValues(FindingByGroupCodeResult result, PublicGroup group) {
			this._result = result;
			this._group = group;
		}

		public FindingByGroupCodeResult getResult() {
			return _result;
		}

		public PublicGroup getGroup() {
			return _group;
		}
	}

	public enum FindingByGroupCodeResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<PublicGroup> groups = _dataStorage.getGroupRepository().getAllPublicGroup();
		PublicGroup result = null;

		for (PublicGroup group : groups) {
			if (group.getJOINING_CODE().equals(input.JOINING_CODE)) {
				result = group;
				break;
			}
		}

		if (result == null) {
			return new OutputValues(FindingByGroupCodeResult.Failed, null);
		}

		return new OutputValues(FindingByGroupCodeResult.Successed, result);
	}
}
