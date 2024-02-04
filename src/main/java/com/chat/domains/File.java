package com.chat.domains;

public class File extends ChatEntity {
	private String _path;
	private FileType _type;

	public File(FileType type) {
		super();
		this._type = type;
	}

	public FileType getType() {
		return _type;
	}

	public void setType(FileType type) {
		this._type = type;
	}

	public String getPath() {
		return _path;
	}

	public void setPath() {
		switch (_type) {
		case Image:
			this._path = "images/" + this.getId() + ".png";
			break;

		case Video:
			this._path = "videos/" + this.getId() + ".mp4";
			break;
		case Audio:
			this._path = "audios/" + this.getId() + ".mp3";
			break;
		default:
			break;
		}

	}

	public static enum FileType {
		Image, Video, Audio
	}
}
