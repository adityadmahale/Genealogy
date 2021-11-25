
public class FileIdentifier {
	private int fileId;
	private String fileLocation;
	
	FileIdentifier(int fileId, String fileLocation) {
		this.fileId = fileId;
		this.fileLocation = fileLocation;
	}

	int getFileId() {
		return fileId;
	}

	String getFileLocation() {
		return fileLocation;
	}
}
