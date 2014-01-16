
import java.util.Calendar;


public class SMTPNotifictionThread implements Runnable {

	public SMTPNotifictionThread() {

	}

	@Override
	public void run() {
		// while (true) {
		Calendar cal = Calendar.getInstance();

		checkReminder(cal);
		checkTask(cal);
		// checkPolls(cal);

		// TODO Make Delay
		// }

	}

	/*
	 * private void checkPolls(Calendar cal) { Polls polls; Properties pollsProp
	 * = new Properties();
	 * 
	 * try { pollsProp.load(pollsFile); } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); } for (Entry<Object,
	 * Object> entry : pollsProp.entrySet()) { polls = new Polls((String)
	 * entry.getKey(),(String) entry.getValue());
	 * 
	 * if (!polls.isWas_handled()){ if (cal.after(polls.getDue_date())) { //TODO
	 * something
	 * 
	 * } else if (polls.isCompleted()) { //TODO sendToCreatorPollComple(polls);
	 * polls.setWas_handled(true);
	 * 
	 * } else if (checkIfComplet(polls.getRecipient())) { //TODO
	 * sendToCreatorPollComple(polls); polls.setWas_handled(true);
	 * polls.setCompleted(true);
	 * 
	 * } } } }
	 */
 /*	private boolean checkIfComplet(Recipient[] recipient) {

		for (Recipient recipient2 : recipient) {
			if (!recipient2.isDidReply()) {
				return false;
			}

		}
		return true;
	}*/

	private void checkTask(Calendar cal) {

		Task[] tasks = DBHandler.getAllTasks();

		for (Task task : tasks) {

			if (!task.isWas_handled()) {

				if (!(task.isTasKFirstSent())) {
					// TODO sendTaskToRecipient(task);
					task.setFirstsent(true);
				}

				if (task.isTaskDone()) {
					// TODO sendTaskCompleted(task);
					task.setWas_handled(true);

				} else if (cal.after(task.getDue_date())) {
					// TODO
					// sendTaskTimeIsDue(task.getCreator(),task.getRecipient()[0]);
					task.setWas_handled(true);
				}
			}
		}
	}

	private void checkReminder(Calendar cal) {

		Reminder[] reminders = DBHandler.getAllRiminders();

		for (Reminder reminder : reminders) {

			if (!reminder.isWas_handled()) {

				if (cal.after((reminder.getDue_date()))) {
					// TODO! sendReminder(reminder.getCreator());
					reminder.setWas_handled(true);
					DBHandler.addReminder(reminder);
				}
			}
		}
	}
}
