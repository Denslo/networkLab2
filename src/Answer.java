import java.util.UUID;

public class Answer implements Cloneable {

	private String id;
	private String data;
	private int count;
	
	public Answer(){
		this.id = UUID.randomUUID().toString();
		this.data = "";
		this.count = -1;
	}

	public Answer(String value) {
		this();
		setData(value);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void addToCount() {
		if (count == -1) {
			count = 1;
		} else {
			count++;
		}
	}
	
	public Answer clone() throws CloneNotSupportedException{
		return (Answer) super.clone();
	}
}
