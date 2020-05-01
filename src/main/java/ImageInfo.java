
public class ImageInfo {
	
	String name;
	String value;
	
	
	// Constructor with parameters
	public ImageInfo(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	// Constructor without parameters
	public ImageInfo() {
		this(null, null);
	}
	
	// getters & setters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	

}
