public class Recipient {
	
	private final String DELIMITER = ";";
	private int id;
	private String mail;
	private boolean didReply;
	
	public Recipient(String value){
		String[] temp = value.split(DELIMITER);
		
		this.id = Integer.parseInt(temp[0]);
		this.mail = temp[1];
		this.didReply = Boolean.parseBoolean(temp[2]);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
	
	public String toString(){
		StringBuilder stringForReturn = new StringBuilder();
		stringForReturn.append(String.valueOf(id));
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(mail);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(didReply);
		
		
		return stringForReturn.toString();
	}
}
