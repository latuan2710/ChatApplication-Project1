package com.chat.usecases.user;

import com.chat.domains.User;
import com.chat.domains.User.UserBuilder;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Hasher;

public class UserRegistration extends UseCase<UserRegistration.InputValues, UserRegistration.OutputValues> {
	private DataStorage _dataStorage;
	private Hasher _hasher;

	public UserRegistration(DataStorage dataStorage, Hasher hasher) {
		_dataStorage = dataStorage;
		_hasher = hasher;
	}

	@Override
	public OutputValues execute(InputValues input) {
		String username = input._username;
		String password = input._password;

		if (checkInputValid(username, password)) {
			return new OutputValues(RegisterResult.Failed, "");
		}

		User user = new UserBuilder(username, _hasher.hash(password)).build();
		_dataStorage.getUserRepository().add(user);

		return new OutputValues(RegisterResult.Successed, "");
	}

	private boolean checkInputValid(String username, String password) {
		return username == null || password == null || username.isBlank() || password.isBlank() || username.isEmpty()
				|| password.isEmpty();
	}

	public static class InputValues {
		private String _username;
		private String _password;

		public InputValues(String username, String password) {
			_username = username;
			_password = password;
		}
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

}
