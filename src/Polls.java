public class Polls extends Task {

	private Answer[] answer;
	private boolean completed;

	public Polls() {
		super();
		answer = new Answer[0];
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void setAnswers(Answer[] answerArray) throws CloneNotSupportedException {
		if (answerArray != null) {

			this.answer = new Answer[answerArray.length];

			for (int i = 0; i < answerArray.length; i++) {
				this.answer[i] = answerArray[i].clone();
			}
		}
	}

	public void setRecipient(Recipient[] recipientArray) throws CloneNotSupportedException {
		if (recipientArray != null) {

			this.recipient = new Recipient[recipientArray.length];

			for (int i = 0; i < recipientArray.length; i++) {
				this.recipient[i] = recipientArray[i].clone();
			}
		}
	}
	
	public Recipient[] getRecipientsArray(){
		return recipient;
	}

	public Answer[] getAnswers(){
		return answer;
	}
	
	public String getRecipients() {
		StringBuilder retVal = new StringBuilder();
		
		for (Recipient recipient : this.recipient) {
			retVal.append(recipient.getMail());
			retVal.append(", ");
		}
		
		retVal.deleteCharAt(retVal.length()-2);
		
		return retVal.toString();
	}
	
	public String[] getAnswersIDforURL(){
		String[] retVal = new String[answer.length];
		
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = "&answerid=" + answer[i].getId();
		}
		
		return retVal;
	}

}