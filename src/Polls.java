public class Polls extends Task {

	private final String DELIMITER = ";;";
	private Answer[] answer;

	public Polls(String key, String value) {
		super(key, value);
		
		try {

			String[] temp = value.split(";;;");
			String[] answers = temp[7].split(DELIMITER);
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

	@Override
	public String toString() {

		StringBuilder stringForReturn = new StringBuilder(super.toString());

		for (Answer ansewer : answer) {
			stringForReturn.append(ansewer.toString());
			stringForReturn.append(DELIMITER);
		}
		stringForReturn.append(";");

		return stringForReturn.toString();
	}
}
