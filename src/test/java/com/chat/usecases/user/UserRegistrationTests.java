package com.chat.usecases.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.infrastructure.services.SHA256Hasher;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.user.UserRegistration.RegisterResult;

class UserRegistrationTests {

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void RegistrationSuccesfully() {
		UserRegistration.InputValues input = new UserRegistration.InputValues("ntn", "123");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserRegistration registration = new UserRegistration(storage, new SHA256Hasher());
		UserRegistration.OutputValues output = registration.execute(input);

		assertEquals(output.getResult(), RegisterResult.Successed);
	}

	@Test
	void RegistrationFailed() {
		UserRegistration.InputValues input = new UserRegistration.InputValues("ntn", "");
		DataStorage storage = InMemoryDataStorage.getInstance();

		UserRegistration registration = new UserRegistration(storage, new SHA256Hasher());
		UserRegistration.OutputValues output = registration.execute(input);

		assertEquals(output.getResult(), RegisterResult.Failed);
	}
}
