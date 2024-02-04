package com.chat.usecases.message;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.File.FileType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.message.SendingMessage.InputValues;
import com.chat.usecases.message.SendingMessage.SendingMessageResult;

class SendingMessageTest {
	private byte[] video;
	private byte[] image;
	private byte[] audio;

	@BeforeEach
	public void setUp() throws FileNotFoundException, IOException {
		User user1 = new User("Tuan", "", "", "", false, null);
		User user2 = new User("Nhan", "", "", "", false, null);
		User user3 = new User("Nghia", "", "", "", false, null);

		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.getUserRepository().add(user1);
		storage.getUserRepository().add(user2);
		storage.getUserRepository().add(user3);

		FileService fileService = new FileService();
		audio = fileService.readFile("file_example_MP3_700KB.mp3");
		image = fileService.readFile("file_example_PNG_500kB.png");
		video = fileService.readFile("file_example_MP4_480_1_5MG.mp4");
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void test() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = new User("Tuan", "", "", "", false, null);
		User receiver = new User("Nhan", "", "", "", false, null);

		Map<byte[], FileType> files = new HashMap<>();
		files.put(audio, FileType.Audio);
		files.put(video, FileType.Video);
		files.put(image, FileType.Image);

		SendingMessage.InputValues input = new InputValues(sender.getId(), receiver.getId(), "Hello", files);
		SendingMessage sendingMessage = new SendingMessage(storage);
		SendingMessage.OutputValues output = sendingMessage.execute(input);

		assertEquals(SendingMessageResult.Successed, output.getResult());
	}

}
