package com.chat.usecases.message;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.chat.domains.MessageHistory;
import com.chat.domains.MessageRecord;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Repository;

public class GettingMessageRecord extends UseCase<GettingMessageRecord.InputValues, GettingMessageRecord.OutputValues> {
	private DataStorage _dataStorage;

	public GettingMessageRecord(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;

		public InputValues(String userId) {
			this._userId = userId;
		}

	}

	public static class OutputValues {
		private GettingMessageRecordResult _result;
		private List<MessageRecord> _messageRecords;

		public OutputValues(GettingMessageRecordResult result, List<MessageRecord> messageRecords) {
			_messageRecords = messageRecords;
			_result = result;
		}

		public GettingMessageRecordResult getResult() {
			return _result;
		}

		public List<MessageRecord> getMessageRecords() {
			return _messageRecords;
		}
	}

	public enum GettingMessageRecordResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		Repository<MessageRecord> repository = _dataStorage.getMessageRecordRepository();
		List<MessageRecord> records = repository.getAll().stream()
				.filter(r -> r.getUser().getId().equals(input._userId)).collect(Collectors.toList());
		
		if (records == null || records.isEmpty()) {
			return new OutputValues(GettingMessageRecordResult.Failed, null);
		}
		
		return new OutputValues(GettingMessageRecordResult.Successed, records);
	}
}
