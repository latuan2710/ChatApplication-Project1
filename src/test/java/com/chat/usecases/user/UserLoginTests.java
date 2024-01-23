package com.chat.usecases.user;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.infrastructure.services.SHA256Hasher;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.user.UserLogin.LoginResult;

class UserLoginTests {
	@BeforeEach
	public void setUp() {
		UserRegistration.InputValues input = new UserRegistration.InputValues("ntn", "123");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserRegistration registration = new UserRegistration(storage, new SHA256Hasher());
		UserRegistration.OutputValues output = registration.execute(input);
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void loginSuccessfully() {
		UserLogin.InputValues input = new UserLogin.InputValues("ntn", "123");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserLogin login = new UserLogin(storage, new SHA256Hasher());
		UserLogin.OutputValues output = login.execute(input);

		Assert.assertEquals(output.getResult(), LoginResult.Successed);
	}

	@Test
	void loginFailed() {
		UserLogin.InputValues input = new UserLogin.InputValues("ntn", "1234");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserLogin login = new UserLogin(storage, new SHA256Hasher());
		UserLogin.OutputValues output = login.execute(input);

		Assert.assertEquals(output.getResult(), LoginResult.Failed);
	}

}
