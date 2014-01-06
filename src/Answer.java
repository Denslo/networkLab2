public class Answer {

	private final String DELIMITER = ";";
	private String id;
	private String data;
	private int count;
	
	public Answer(){
		this.id = "";
		this.data = "";
		this.count = -1;
	}

	public Answer(String value) {
			String[] temp = value.split(DELIMITER);

			this.id = temp[0];
			this.data = temp[1];
			this.count = Integer.parseInt(temp[2]);
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

	public void setCount(int count) {
		this.count = count;
	}

	public String toString() {
		StringBuilder stringForReturn = new StringBuilder();
		stringForReturn.append(id);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(data);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(String.valueOf(count));
		
		return stringForReturn.toString();
	}
}
