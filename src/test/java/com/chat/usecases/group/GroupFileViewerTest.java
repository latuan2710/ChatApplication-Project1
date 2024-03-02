package com.chat.usecases.group;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.domains.File.FileType;
import com.chat.domains.Group;
import com.chat.domains.Group.GroupType;
import com.chat.domains.User;
import com.chat.infrastructure.data.InMemoryDataStorage;
import com.chat.infrastructure.services.FileService;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.message.SendingMessage;
import com.chat.usecases.message.SendingMessage.InputValues;

public class GroupFileViewerTest {

	@BeforeEach
	public void setUp() {
		DataStorage storage = InMemoryDataStorage.getInstance();

		User sender = new User("Tuan", "", "", "", false, null);
		User member = new User("Nghia", "", "", "", false, null);

		storage.getUserRepository().add(sender);
		storage.getUserRepository().add(member);

		GroupCreation.InputValues in = new GroupCreation.InputValues(sender.getId(), GroupType.Private);
		GroupCreation groupCreation = new GroupCreation(storage);
		GroupCreation.OutputValues out = groupCreation.execute(in);

		Group group = out.getGroup();
		storage.getGroupRepository().add(group);

		FileService fileService = new FileService();
		byte[] audio = fileService.readFile("file_example_MP3_700KB.mp3");
		byte[] image = fileService.readFile("file_example_PNG_500kB.png");
		byte[] video = fileService.readFile("file_example_MP4_480_1_5MG.mp4");

		Map<byte[], FileType> files = new HashMap<>();
		files.put(audio, FileType.Audio);
		files.put(video, FileType.Video);
		files.put(image, FileType.Image);

		SendingMessage.InputValues input = new InputValues(sender.getId(), group.getId(), "Hello", files);
		SendingMessage sendingMessage = new SendingMessage(storage);
		sendingMessage.execute(input);
		sendingMessage.execute(input);

		GroupJoining groupJoining = new GroupJoining(storage);
		groupJoining.execute(new GroupJoining.InputValues(member.getId(), sender.getId(), group.getId()));
	}

	@AfterEach
	public void tearDown() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		storage.cleanAll();
	}

	@Test
	void testGetBySender() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User sender = storage.getUserRepository().getAll().get(0);
		Group group = storage.getGroupRepository().getAll().get(0);

		GroupFileViewer groupFileViewer = new GroupFileViewer(storage);
		GroupFileViewer.InputValues input = new GroupFileViewer.InputValues(sender.getId(), group.getId());
		GroupFileViewer.OutputValues output = groupFileViewer.execute(input);

		assertEquals(6, output.getFiles().size());
	}
	
	@Test
	void testGetByMember() {
		DataStorage storage = InMemoryDataStorage.getInstance();
		User member = storage.getUserRepository().getAll().get(1);
		Group group = storage.getGroupRepository().getAll().get(0);

		GroupFileViewer groupFileViewer = new GroupFileViewer(storage);
		GroupFileViewer.InputValues input = new GroupFileViewer.InputValues(member.getId(), group.getId());
		GroupFileViewer.OutputValues output = groupFileViewer.execute(input);

		assertEquals(6, output.getFiles().size());
	}

}
