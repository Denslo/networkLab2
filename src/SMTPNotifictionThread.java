import java.util.Calendar;

public class SMTPNotifictionThread implements Runnable {

	public SMTPNotifictionThread() {

	}

	@Override
	public void run() {
		while (true) {
			try{
			Calendar cal = Calendar.getInstance();

			checkReminder(cal);
			checkTask(cal);
			checkPolls(cal);

			Thread.sleep(60000);
			}catch(Exception e){
				
			}
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
						// TODO sendRecipientPoll(poll,recipient);
						recipient.setWasFirstSent(true);
						wasChange = true;
					}
				}

				if (checkIfComplet(poll.getRecipientsArray())) {
					// TODO sendCreatorCompleted(poll)
					wasChange = true;
					poll.setWas_handled(true);
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
					// TODO sendTaskToRecipient(task);
					task.setFirstsent(true);
					wasChange = true;
				}

				if (task.isTaskDone()) {
					// TODO sendTaskCompleted(task);
					task.setWas_handled(true);
					wasChange = true;

				} else if (cal.after(task.getDue_date())) {
					// TODO
					// sendTaskTimeIsDue(task.getCreator(),task.getRecipient()[0]);
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

				if (cal.after((reminder.getDue_date()))) {
		//			sendReminder(reminder);
					reminder.setWas_handled(true);
					DBHandler.addReminder(reminder);
				}
			}
		}
	}
/*
	private void sendReminder(Reminder reminder) {
		String mailTo = reminder.g
		SMTPClient reminderMail = new SMTPClient(reminder, data, sender, subject);
		
	}*/
}
