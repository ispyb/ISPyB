package file;

import java.io.InputStream;

import javax.ws.rs.FormParam;

public class FileUploadForm {

	@FormParam("file")
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@FormParam("description")
	private String description;

	@FormParam("fileName")
	private String fileName;
	
	@FormParam("groupName")
	private String groupName;

	@FormParam("type")
	private String type;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}


	



