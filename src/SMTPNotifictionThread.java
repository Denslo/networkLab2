import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Calendar;
import java.util.Properties;

public class SMTPNotifictionThread implements Runnable {

	private static final String REMINDER_FILE = "reminderFilePath";
	private static final String TASK_FILE = "taskFilePath";
	private static final String POLLS_FILE = "pollFilePath";

	private FileInputStream reminderFile;
	private FileInputStream taskFile;
	private FileInputStream pollsFile;
	
	private FileOutputStream reminderFileToSave;
	private FileOutputStream taskFileToSave;
	private FileOutputStream pollsFileToSave;

	public SMTPNotifictionThread() {
		try {
			reminderFile = new FileInputStream((String) Server.prop.getProperty(REMINDER_FILE));
			//taskFile = new FileInputStream((String) Server.prop.getProperty(TASK_FILE));
			//pollsFile = new FileInputStream((String) Server.prop.getProperty(POLLS_FILE));
			
	//		reminderFileToSave = new FileOutputStream("C:\\School\\Networks\\shai.txt");
//			taskFileToSave = new FileOutputStream((String) Server.prop.getProperty(TASK_FILE));
	//		pollsFileToSave = new FileOutputStream((String) Server.prop.getProperty(POLLS_FILE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		//while (true) {
			Calendar cal = Calendar.getInstance();

			checkReminder(cal);
			//checkTask(cal);
			//checkPolls(cal);

		//	 TODO Make Delay
		//}

	}

	private void checkPolls(Calendar cal) {
		Polls polls;
		Properties pollsProp = new Properties();

		try {
			pollsProp.load(pollsFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Entry<Object, Object> entry : pollsProp.entrySet()) {
			polls = new Polls((String) entry.getKey(),(String) entry.getValue());
			
			if (!polls.isWas_handled()){
				if (cal.after(polls.getDue_date())) {
					//TODO something
					
				} else if (polls.isCompleted()) {
					//TODO sendToCreatorPollComple(polls);
					polls.setWas_handled(true);
					
				} else if (checkIfComplet(polls.getRecipient())) {
					//TODO sendToCreatorPollComple(polls);
					polls.setWas_handled(true);
					polls.setCompleted(true);
					
				}	
			}
		}
	}

	private boolean checkIfComplet(Recipient[] recipient) {
		
		for (Recipient recipient2 : recipient) {
			if(!recipient2.isDidReply()){
				return false;
			}
			
		}
		return true;
	}

	private void checkTask(Calendar cal) {
		Task task;
		Properties taskProp = new Properties();

		try {
			taskProp.load(taskFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Entry<Object, Object> entry : taskProp.entrySet()) {

			task = new Task((String) entry.getKey(), (String) entry.getValue());

			if (!(task.getRecipient()[0].isWasFirstSent())) {
				// TODO sendTaskToRecipient(task);
				task.getRecipient()[0].setWasFirstSent(true);
			}

			if (!task.isWas_handled()) {

				if (cal.after(task.getDue_date())) {
					// TODO sendTaskTimeIsDue(task.getCreator(),
					// task.getRecipient()[0]);
					task.setWas_handled(true);
				}
			}
		}
	}

	private void checkReminder(Calendar cal) {

		Reminder reminder;
		Properties remiderProp = new Properties();
		try {
			remiderProp.load(reminderFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Entry<Object, Object> entry : remiderProp.entrySet()) {

			reminder = new Reminder((String) entry.getKey(),(String) entry.getValue());

			if (!reminder.isWas_handled()) {

				//cal.after(reminder.getDue_date())
				if (cal.after((reminder.getDue_date()))) {
					// TODO! sendReminder(reminder.getCreator());
					System.out.println(reminder.getDue_date());

					reminder.setWas_handled(true);
					//TODO store
					//remiderProp.setProperty(reminder.getId(), reminder.toString());
					remiderProp.setProperty(reminder.getId(), reminder.toString());
					try {
						remiderProp.store(reminderFileToSave, null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			}
		}
	}
	public static void main(String[] args)  {
		SMTPNotifictionThread smtp = new SMTPNotifictionThread();
		smtp.run();
	}
}
