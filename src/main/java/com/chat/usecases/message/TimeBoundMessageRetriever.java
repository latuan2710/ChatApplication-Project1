package com.chat.usecases.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.chat.domains.Message;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class TimeBoundMessageRetriever
		extends UseCase<TimeBoundMessageRetriever.InputValues, TimeBoundMessageRetriever.OutputValues> {
	private DataStorage _dataStorage;

	public TimeBoundMessageRetriever(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _senderId;
		private int _k;
		private Date _time;

		public InputValues(String senderId, int k, Date time) {
			this._senderId = senderId;
			this._k = k;
			this._time = time;
		}

	}

	public static class OutputValues {
		private TimeBoundMessageRetrieverResult _result;
		private List<Message> _messages;

		public OutputValues(TimeBoundMessageRetrieverResult result, List<Message> messages) {
			_messages = messages;
			_result = result;
		}

		public TimeBoundMessageRetrieverResult getResult() {
			return _result;
		}

		public List<Message> getMessages() {
			return _messages;
		}
	}

	public static enum TimeBoundMessageRetrieverResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GettingMessages.InputValues gettingMessageInput = new GettingMessages.InputValues(input._senderId);
		GettingMessages gettingMessage = new GettingMessages(_dataStorage);
		GettingMessages.OutputValues gettingMessageOutput = gettingMessage.execute(gettingMessageInput);

		List<Message> messages = gettingMessageOutput.getMessages();
		messages = messages.stream().filter(m -> m.getTime().compareTo(input._time) < 0).collect(Collectors.toList());
		
		if (messages.isEmpty()) {
			return new OutputValues(TimeBoundMessageRetrieverResult.Successed, null);
		}

		messages.sort((m1, m2) -> Long.compare(m1.getTime().getTime(), m2.getTime().getTime()));

		if (messages.size() <= input._k) {
			return new OutputValues(TimeBoundMessageRetrieverResult.Successed, messages);
		}

		messages = this.getKLastestMessage(input._k, messages);

		return new OutputValues(TimeBoundMessageRetrieverResult.Successed, messages);
	}

	private List<Message> getKLastestMessage(int k, List<Message> messages) {
		List<Message> result = new ArrayList<>();

		for (int i = messages.size() - k; i < messages.size(); i++) {
			result.add(messages.get(i));
		}
		return result;

	}
}
