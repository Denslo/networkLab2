import java.util.UUID;

public class Recipient implements Cloneable {

	private String id;
	private String mail;
	private boolean didReply;
	private boolean wasFirstSent;
	private boolean wasSendWhenAnswer;

	public Recipient() {

		this.id = UUID.randomUUID().toString();
		this.mail = "";
		this.setDidReply(false);
		this.setWasFirstSent(false);
		this.setWasSendWhenAnswer(false);
	}

	public Recipient(String mail) {
		this();
		this.setMail(mail);
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
		if (Helper.EmailValidator(mail)) {
			this.mail = mail;
		}
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
	
	@Override
	public Recipient clone() throws CloneNotSupportedException{
		return (Recipient) super.clone();
	}

	public boolean isWasSendWhenAnswer() {
		return wasSendWhenAnswer;
	}

	public void setWasSendWhenAnswer(boolean wasSendWhenAnswer) {
		this.wasSendWhenAnswer = wasSendWhenAnswer;
	}

}
