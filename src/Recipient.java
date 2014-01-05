public class Recipient {
	
	private final String DELIMITER = ";";
	private String id;
	private String mail;
	private boolean didReply;
	private boolean wasFirstSent;
	
	public Recipient(String value){
		String[] temp = value.split(DELIMITER);
		
		this.id = temp[0];
		this.mail = temp[1];
		this.didReply = Boolean.parseBoolean(temp[2]);
		this.setWasFirstSent(Boolean.parseBoolean(temp[3]));
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isDidReply() {
		return didReply;
	}

	public void setDidReply(boolean didReply) {
		this.didReply = didReply;
	}
	
	public boolean isWasFirstSent() {
		return wasFirstSent;
	}
	
	public void setWasFirstSent(boolean wasSent) {
		this.wasFirstSent = wasSent;
	}
	
	public String toString(){
		StringBuilder stringForReturn = new StringBuilder();
		stringForReturn.append(id);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(mail);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(didReply);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(wasFirstSent);
		
		
		return stringForReturn.toString();
	}

}
