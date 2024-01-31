package com.chat.domains;

public class File extends ChatEntity {
	private Byte[] _content;
	private FileType _type;

	public File(FileType type, Byte[] content) {
		super();
		this._type = type;
		this._content = content;
	}

	public FileType getType() {
		return _type;
	}

	public void setType(FileType type) {
		this._type = type;
	}

	public Byte[] getContent() {
		return _content;
	}

	public void setContent(Byte[] content) {
		this._content = content;
	}

	public static enum FileType {
		Image, Video, Audio
	}
}
