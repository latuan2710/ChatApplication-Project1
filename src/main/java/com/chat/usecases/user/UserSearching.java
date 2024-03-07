package com.chat.usecases.user;

import java.util.ArrayList;
import java.util.List;

import com.chat.domains.User;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;

public class UserSearching extends UseCase<UserSearching.InputValues, UserSearching.OutputValues> {
	private DataStorage _dataStorage;

	public UserSearching(DataStorage dataStorage) {
		super();
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _name;

		public InputValues(String name) {
			this._name = name;
		}
	}

	public static class OutputValues {
		private SearchingResult _result;
		private String _message;
		private List<User> _users;

		public OutputValues(SearchingResult result, String message, List<User> users) {
			this._result = result;
			this._message = message;
			this._users = users;
		}

		public SearchingResult getResult() {
			return _result;
		}

		public String getMessage() {
			return _message;
		}

		public List<User> getUsers() {
			return _users;
		}

	}

	public enum SearchingResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		List<User> users = _dataStorage.getUserRepository().getAll();
		List<User> result = new ArrayList<>();
		String nameInput = input._name;

		for (User user : users) {
			String username = user.getUsername();
			String fullName = user.getFullName();

			if (username.contains(nameInput) || fullName.contains(nameInput)) {
				result.add(user);
			}
		}

		if (result.isEmpty()) {
			return new OutputValues(SearchingResult.Failed, "", null);
		} else {
			return new OutputValues(SearchingResult.Successed, "", result);
		}
	}
}
