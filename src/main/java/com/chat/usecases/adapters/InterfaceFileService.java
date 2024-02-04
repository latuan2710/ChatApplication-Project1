package com.chat.usecases.adapters;

public interface InterfaceFileService {
	public void saveFile(byte[] content, String path);

	public byte[] readFile(String path);

	public void deleteFile(String path);
}
