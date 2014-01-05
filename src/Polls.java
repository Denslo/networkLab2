public class Polls extends Task {

	private final String DELIMITER = ";;";
	private Answer[] answer;
	private boolean completed;

	public Polls(String key, String value) {
		super(key, value);
		
		try {

			String[] temp = value.split(";;;");
			String[] answers = temp[7].split(DELIMITER);
			this.setCompleted(Boolean.parseBoolean(temp[8]));
			this.answer = new Answer[answers.length];

			for (int i = 0; i < answers.length; i++) {
				this.answer[i] = new Answer(answers[i]);
			}
		} catch (Exception e) {
			answer = new Answer[1];
			answer[0] = new Answer("");
			this.setWasParsedOk(false);
		}
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {

		StringBuilder stringForReturn = new StringBuilder(super.toString());

		for (Answer ansewer : answer) {
			stringForReturn.append(ansewer.toString());
			stringForReturn.append(DELIMITER);
		}
		stringForReturn.append(";");
		
		stringForReturn.append(completed);
		

		return stringForReturn.toString();
	}

}
