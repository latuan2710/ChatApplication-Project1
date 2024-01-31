package com.chat.usecases.message;

import java.util.List;

import com.chat.domains.ChatEntity;
import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.user.UserRegistration.RegisterResult;

public class SendingMessage extends UseCase<SendingMessage.InputValues, SendingMessage.OutputValues> {
	private DataStorage _dataStorage;

	public static class InputValues {
		private User _sender;
		private ChatEntity _receiver;
		private String _text;
		private List<byte[]> _files;

	}

	public static class OutputValues {
		private RegisterResult _result;
		private String _message;

		public OutputValues(RegisterResult result, String message) {
			_message = message;
			_result = result;
		}

		public RegisterResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}
	}

	public static enum RegisterResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		return null;
	}
}
