public class Task extends Reminder {
	private final String DELIMITER = ";;";
	private Recipient[] recipient;

	public Task(String key, String value) {
		super(key, value);
		
		try {
			
			String[] temp = value.split(";;;");
			String[] recipients = temp[6].split(DELIMITER);
			this.recipient = new Recipient[recipients.length];
			
			for (int i = 0; i < recipients.length; i++) {
				this.recipient[i] = new Recipient(recipients[i]);
			}
		} catch (Exception e) {
			this.recipient = new Recipient[1];
			this.recipient[0] = new Recipient("");
			this.setWasParsedOk(false);
		}
	}

	@Override
	public String toString() {
		StringBuilder stringForReturn = new StringBuilder(super.toString());
		
		for (Recipient res : recipient) {
			stringForReturn.append(res.toString());
			stringForReturn.append(DELIMITER);
		}
		stringForReturn.append(";");
		return stringForReturn.toString();

	}
}
