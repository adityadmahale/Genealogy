
public class FileIdentifier {
	private int fileId;
	private String fileLocation;
	
	public FileIdentifier(int fileId, String fileLocation) {
		this.fileId = fileId;
		this.fileLocation = fileLocation;
	}

	public int getFileId() {
		return fileId;
	}

	public String getFileLocation() {
		return fileLocation;
	}
}
