package com.chat.infrastructure.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import com.chat.usecases.adapters.InterfaceFileService;

public class FileService implements InterfaceFileService {

	@Override
	public void saveFile(byte[] content, String path) {
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write(content);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	@Override
	public byte[] readFile(String path) {
		File file = new File(path);
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteFile(String path) {
		File myObj = new File(path);
		if (myObj.delete()) {
			System.out.println("Deleted the file: " + myObj.getName());
		} else {
			System.out.println("Failed to delete the file.");
		}

	}

}
