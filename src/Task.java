import java.util.Date;

public class Task extends Reminder {
	
	protected Recipient[] recipient;
	
	public Task(){
		super();
		recipient = new Recipient[1];
		recipient[0] = new Recipient();
	}
	
	public void setRecipient(Recipient recipient) throws CloneNotSupportedException{
		this.recipient[0] = recipient.clone();
	}
	
	public Recipient getRecipient(){
		return recipient[0];
	}
	
	public String getStatus(){
		String retVal = "in progress";
		
		if (getDue_date().before(new Date())) {
			retVal = "time is due";
		}
		
		if (getRecipient().isDidReply()) {
			retVal = "completed";
		}
		return retVal;
	}

	public void setRecipient(String mail) throws CloneNotSupportedException {
		Recipient newRecip = new Recipient(mail);
		setRecipient(newRecip);
	}
	
	public String[] getUserURL(){
		String[] retVal = new String[recipient.length];
		
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = "http://" + Server.prop.getProperty("ServerName") + ":" + Server.prop.getProperty("port") + "/smtp/task_reply.html?taskid=" + this.getId() + "&recipientid=" + recipient[i].getId();
		}
		
		return retVal;
	}
	
	public boolean isTaskDone(){
		return recipient[0].isDidReply();
	}
	
	public boolean isTasKFirstSent(){
		return recipient[0].isWasFirstSent();
	}
	public void setFirstsent(boolean wasSent){
		recipient[0].setWasFirstSent(wasSent);
	}
}
