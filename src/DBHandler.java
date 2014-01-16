import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DBHandler {

	public static Reminder[] getRimindersByUserMail(String userMail) {

		XStream xstream = new XStream(new DomDriver());

		Reminder[] allReminders = new Reminder[0];

		try {
			File file = new File((String) Server.prop.get("reminderFilePath"));
			allReminders = (Reminder[]) xstream.fromXML(file);
		} catch (Exception e) {
		}

		List<Reminder> retList = new ArrayList<Reminder>();

		for (Reminder reminder : allReminders) {
			if (reminder.getCreator().equals(userMail)) {
				retList.add(reminder);
			}
		}

		return retList.toArray(new Reminder[0]);
	}
	
	public static Reminder[] getAllRiminders(){
		XStream xstream = new XStream(new DomDriver());

		Reminder[] allReminders = new Reminder[0];

		try {
			File file = new File((String) Server.prop.get("reminderFilePath"));
			allReminders = (Reminder[]) xstream.fromXML(file);
		} catch (Exception e) {
		}
		
		return allReminders;
	}

	public synchronized static void deleteReminder(String id, String mail) {

		try {

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("reminderFilePath"));
			Reminder[] allReminders = (Reminder[]) xstream.fromXML(remFile);

			List<Reminder> retList = new ArrayList<Reminder>(Arrays.asList(allReminders));

			for (int i = 0; i < allReminders.length; i++) {
				if (allReminders[i].getId().equals(id) && allReminders[i].getCreator().equals(mail)) {
					retList.remove(i);
				}
			}

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Reminder[0]), fos);
			fos.close();

		} catch (Exception e) {}

	}

	public static Reminder getReminder(String id) {
	
		Reminder retVal = null;

		try {
			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("reminderFilePath"));
			Reminder[] allReminders = (Reminder[]) xstream.fromXML(remFile);

			for (Reminder reminder : allReminders) {
				if (reminder.getId().equals(id)) {
					retVal = reminder;
				}
			}
		} catch (Exception e) {}

		return retVal;
	}

	public synchronized static boolean addReminder(Reminder newReminder) {

		boolean retVal = false;

		try {
			if (getReminder(newReminder.getId()) != null) {
				deleteReminder(newReminder.getId(), newReminder.getCreator());
			}

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("reminderFilePath"));
			Reminder[] allReminders = (Reminder[]) xstream.fromXML(remFile);
			List<Reminder> retList = new ArrayList<Reminder>(Arrays.asList(allReminders));
			retList.add(newReminder);

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Reminder[0]), fos);
			fos.close();

			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public synchronized static void deleteTesk(String id, String mail) {

		try {

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("taskFilePath"));
			Task[] allTask = (Task[]) xstream.fromXML(remFile);

			List<Task> retList = new ArrayList<Task>(Arrays.asList(allTask));

			for (int i = 0; i < allTask.length; i++) {
				if (allTask[i].getId().equals(id) && allTask[i].getCreator().equals(mail) && allTask[i].getStatus().equals("in progress")) {
					retList.remove(i);
				}
			}

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Task[0]), fos);
			fos.close();

		} catch (Exception e) {}
	}

	public static Task[] getTasksByUserMail(String userMail) {

		XStream xstream = new XStream(new DomDriver());

		Task[] allTasks = new Task[0];

		try {
			File file = new File((String) Server.prop.get("taskFilePath"));
			allTasks = (Task[]) xstream.fromXML(file);
		} catch (Exception e) {
		}

		List<Task> retList = new ArrayList<Task>();

		for (Task task : allTasks) {
			if (task.getCreator().equals(userMail)) {
				retList.add(task);
			}
		}

		return retList.toArray(new Task[0]);
	}

	public synchronized static boolean addTask(Task newTask) {

		boolean retVal = false;

		try {

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("taskFilePath"));
			Task[] allTasks = (Task[]) xstream.fromXML(remFile);
			List<Task> retList = new ArrayList<Task>(Arrays.asList(allTasks));
			retList.add(newTask);

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Task[0]), fos);
			fos.close();

			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public synchronized static void updateTaskCompleted(String taskID, String recipientID) {

		XStream xstream = new XStream(new DomDriver());

		Task[] allTasks = new Task[0];

		try {
			File file = new File((String) Server.prop.get("taskFilePath"));
			allTasks = (Task[]) xstream.fromXML(file);

			for (Task task : allTasks) {
				if (task.getId().equals(taskID)) {
					if (task.getRecipient().getId().equals(recipientID)) {
						task.getRecipient().setDidReply(true);
						FileOutputStream fos = new FileOutputStream(file);
						xstream.toXML(allTasks, fos);
						fos.close();
					}
				}
			}
		} catch (Exception e) {
		}

	}

	public synchronized static void deletePoll(String id, String mail) {
		try {

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("pollFilePath"));
			Polls[] allPolls = (Polls[]) xstream.fromXML(remFile);

			List<Polls> retList = new ArrayList<Polls>(Arrays.asList(allPolls));

			for (int i = 0; i < allPolls.length; i++) {
				if (allPolls[i].getId().equals(id) && allPolls[i].getCreator().equals(mail)) {
					retList.remove(i);
				}
			}

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Polls[0]), fos);
			fos.close();

		} catch (Exception e) {}

	}

	public static Polls[] getPollssByUserMail(String userMail) {
		XStream xstream = new XStream(new DomDriver());

		Polls[] allPolls = new Polls[0];

		try {
			File file = new File((String) Server.prop.get("pollFilePath"));
			allPolls = (Polls[]) xstream.fromXML(file);
		} catch (Exception e) {
		}

		List<Polls> retList = new ArrayList<Polls>();

		for (Polls poll : allPolls) {
			if (poll.getCreator().equals(userMail)) {
				retList.add(poll);
			}
		}

		return retList.toArray(new Polls[0]);
	}

	public synchronized static boolean addPoll(Polls newPoll) {
		boolean retVal = false;

		try {

			XStream xstream = new XStream(new DomDriver());

			File remFile = new File((String) Server.prop.get("pollFilePath"));
			Polls[] allPolls = (Polls[]) xstream.fromXML(remFile);
			List<Polls> retList = new ArrayList<Polls>(Arrays.asList(allPolls));
			retList.add(newPoll);

			FileOutputStream fos = new FileOutputStream(remFile);

			xstream.toXML(retList.toArray(new Polls[0]), fos);
			fos.close();

			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public synchronized static boolean updatePollAnswer(String pollID, String recipientID, String answerID) {
		boolean retValu = false;

		XStream xstream = new XStream(new DomDriver());

		Polls[] allPolls = new Polls[0];

		try {
			File file = new File((String) Server.prop.get("pollFilePath"));
			allPolls = (Polls[]) xstream.fromXML(file);

			for (Polls polls : allPolls) {
				if (polls.getId().equals(pollID)) {
					for (Recipient recipient : polls.getRecipientsArray()) {
						if (recipient.getId().equals(recipientID) && !recipient.isDidReply()) {
							for (Answer answer : polls.getAnswers()) {
								if (answer.getId().equals(answerID)) {
									recipient.setDidReply(true);
									answer.addToCount();
									FileOutputStream fos = new FileOutputStream(file);
									xstream.toXML(allPolls, fos);
									fos.close();
									retValu = true;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			retValu = false;
		}

		return retValu;
	}
}
