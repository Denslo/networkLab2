public class Answer {

	private final String DELIMITER = ";";
	private int id;
	private String data;
	private int count;

	public Answer(String value) {
			String[] temp = value.split(DELIMITER);

			this.id = Integer.parseInt(temp[0]);
			this.data = temp[1];
			this.count = Integer.parseInt(temp[2]);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
		stringForReturn.append(String.valueOf(id));
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(data);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(String.valueOf(count));
		
		return stringForReturn.toString();
	}
}
