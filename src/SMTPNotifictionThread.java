import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SMTPNotifictionThread implements Runnable {

	public SMTPNotifictionThread() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				Calendar cal = Calendar.getInstance();
				checkReminder(cal);
				checkTask(cal);
				checkPolls(cal);				
				Thread.sleep(60000);
			} catch (Exception e) {	}
		}

	}

	private void checkPolls(Calendar cal) {
		Polls[] polls = DBHandler.getAllPolls();
		boolean wasChange;

		for (Polls poll : polls) {
			wasChange = false;
			if (!poll.isWas_handled()) {

				for (Recipient recipient : poll.getRecipientsArray()) {

					if (!recipient.isWasFirstSent()) {
						sendRecipientPoll(poll,recipient);
						recipient.setWasFirstSent(true);
						wasChange = true;
					}
				}

				if (checkIfComplet(poll.getRecipientsArray())) {
					sendCreatorCompleted(poll);
					wasChange = true;
					poll.setCompleted(true);
					poll.setWas_handled(true);
				} else {
					for (Recipient recipient : poll.getRecipientsArray()) {
						if((!recipient.isWasSendWhenAnswer()) && recipient.isDidReply()){
							sendSomeOneAnswer(poll, recipient);
							recipient.setWasSendWhenAnswer(true);
							wasChange = true;
						}
						
					}
				}
			}
			if (wasChange) {
				DBHandler.addPoll(poll);
			}
		}
	}





	private boolean checkIfComplet(Recipient[] recipient) {

		for (Recipient currentRecipient : recipient) {

			if (!currentRecipient.isDidReply()) {

				return false;
			}
		}
		return true;
	}

	private void checkTask(Calendar cal) {
		Task[] tasks = DBHandler.getAllTasks();
		boolean wasChange;

		for (Task task : tasks) {

			wasChange = false;

			if (!task.isWas_handled()) {

				if (!(task.isTasKFirstSent())) {
					sendTaskToRecipient(task);
					task.setFirstsent(true);
					wasChange = true;
				}

				if (task.isTaskDone()) {
					sendTaskCompleted(task);
					task.setWas_handled(true);
					wasChange = true;
					System.out.println(task.isWas_handled());

				} else if (cal.getTime().after(task.getDue_date())) {
					 
					sendTaskTimeIsDue(task);
					task.setWas_handled(true);
					wasChange = true;
				}
			}
			if (wasChange) {
				DBHandler.addTask(task);
			}
		}
	}




	private void checkReminder(Calendar cal) {
		

		Reminder[] reminders = DBHandler.getAllRiminders();

		for (Reminder reminder : reminders) {
			
			if (!reminder.isWas_handled()) {

				if (cal.getTime().after((reminder.getDue_date()))) {
					sendReminder(reminder);
					reminder.setWas_handled(true);
					DBHandler.addReminder(reminder);
				}
			}
		}
	}
	private void sendSomeOneAnswer(Polls poll, Recipient recipient) {
		String mailTo = poll.getCreator();
		String sender = poll.getCreator();
		String subject = poll.getSubject();
		Answer[] answers = poll.getAnswers();
		int count;
		StringBuilder data = new StringBuilder();
		data.append(recipient.getMail() + " voted and the current status for the poll is: \r\n");
		
		for (Answer answer : answers) {
			count = answer.getCount();
			if (count < 0){
				count = 0;
			}
			data.append("for " + answer.getData() + " was voted " + String.valueOf(count) + " times \r\n" );
		}
		SMTPClient smtpClient = new SMTPClient(mailTo, data.toString(), sender, subject);
		  smtpClient.connect();
		
		
	}
	private void sendCreatorCompleted(Polls poll) {
		StringBuilder data = new StringBuilder();
		String mailTo = poll.getCreator();
		String sender = poll.getCreator();
		String subject = poll.getSubject();
		Answer[] answers = poll.getAnswers();
		data.append("Poll Completed And the result are:....\r\n");
		int count;
		
		for (Answer answer : answers) {
			count = answer.getCount();
			if (count < 0){
				count = 0;
			}
			data.append("for " + answer.getData() + " was voted " + String.valueOf(count) + " times \r\n" );
		}
		SMTPClient smtpClient = new SMTPClient(mailTo, data.toString(), sender, subject);
		  smtpClient.connect();
		
		
	}
	private void sendRecipientPoll(Polls poll, Recipient recipient) {
		
		String[] links = poll.getUserURL(recipient.getId());
		Answer[] answer = poll.getAnswers();
		StringBuilder data = new StringBuilder();
		
	    String mailTo = recipient.getMail();
	    String sender = poll.getCreator();
	    String subject = poll.getSubject();
		data.append("The Question is:" + poll.getData() + "\r\n");
		data.append("Please Choose An Answer:" + "\r\n");
		
		int index = 0;
		for (String link : links) {
			
			data.append("To Choose - " + " " + answer[index].getData()+ " " + "Press-->" + link + "\r\n");
			data.append("\r\n");
			index++;
		}
		
		SMTPClient smtpClient = new SMTPClient(mailTo, data.toString(), sender, subject);
		  smtpClient.connect();
		
		/*
		for (Answer answer : poll.getAnswers()) {
			System.out.println(answer.getId());
			System.out.println(answer.getData());
		}*/
	}
	
	  private void sendReminder(Reminder reminder) { 
		  String mailTo = reminder.getCreator();
		  String data = reminder.getData();
		  String sender = reminder.getCreator();
		  String subject = reminder.getSubject();
		  SMTPClient smtpClient = new SMTPClient(mailTo, data, sender, subject);
		  smtpClient.connect();
	  
	  }
	  
	  private void sendTaskCompleted(Task task) {
		  String mailTo = task.getCreator();
		  String data = "The Task Is Done congratulation";
		  String sender = task.getCreator();
		  String subject = task.getSubject();
		  
		  SMTPClient smtpClient = new SMTPClient(mailTo, data, sender, subject);
		  smtpClient.connect();
		  
	  }
	  private void sendTaskTimeIsDue(Task task) {
		  String mailTo = task.getRecipient().getMail();
		  String data = "The time is due and the task was not completed";
		  String sender = task.getCreator();
		  String subject = task.getSubject();
		  
		  SMTPClient smtpClient = new SMTPClient(sender, data, sender, subject);
		  smtpClient.connect();
		  
		  SMTPClient MailToRecipient = new SMTPClient(mailTo, data, sender, subject);
		  MailToRecipient.connect();
		  
		  
		  
	  }	 
	  private void sendTaskToRecipient(Task task) {
		  StringBuilder taskToSend = new StringBuilder();
		  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		  
		  String mailTo = task.getRecipient().getMail();
		  taskToSend.append(task.getData() + "\r\n");
		  String sender = task.getCreator();
		  String subject = task.getSubject();
		  taskToSend.append("The due date is: " +  dateFormat.format(task.getDue_date()) + "\r\n");
		  taskToSend.append("Press the link:" + task.getUserURL()[0]);
		  
		  SMTPClient smtpClient = new SMTPClient(mailTo, taskToSend.toString(), sender, subject);
		  smtpClient.connect();
		  
	  }
}
