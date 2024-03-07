package com.chat.usecases.user;

import java.util.List;

import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.adapters.Hasher;

public class UserLogin extends UseCase<UserLogin.InputValues, UserLogin.OutputValues> {
	private DataStorage _dataStorage;
	private Hasher _hasher;

	public UserLogin(DataStorage dataStorage, Hasher hasher) {
		this._dataStorage = dataStorage;
		this._hasher = hasher;
	}

	public static class InputValues {
		private String _username;
		private String _password;

		public InputValues(String username, String password) {
			this._username = username;
			this._password = password;
		}
	}

	public static class OutputValues {
		private LoginResult _result;
		private String _message;

		public OutputValues(LoginResult result, String message) {
			this._result = result;
			this._message = message;
		}

		public LoginResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}
	}

	public enum LoginResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<User> users = _dataStorage.getUserRepository().getAll();
		String username = input._username;
		String password = input._password;
		String hashedPasword = _hasher.hash(password);

		for (User user : users) {
			if (user.getUsername().equals(username) && user.getHashedPassword().equals(hashedPasword)) {
				return new OutputValues(LoginResult.Successed, "");
			}
		}
		return new OutputValues(LoginResult.Failed, "");
	}
}
