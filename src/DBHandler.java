import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Map.Entry;

public class DBHandler {
	// "reminderFilePath";

	public static Reminder[] getRimindersByUserMail(String userMail) throws IOException {

		Properties remiderProp = new Properties();
		ArrayList<Reminder> arrayOfReminders = new ArrayList<>();
		Reminder[] arrayToReturn;

		FileInputStream reminderFile = new FileInputStream((String) Server.prop.getProperty("reminderFilePath"));
		remiderProp.load(reminderFile);

		for (Entry<Object, Object> entry : remiderProp.entrySet()) {
			Reminder reminder = new Reminder((String) entry.getKey(), (String) entry.getValue());

			if (reminder.getCreator().equals(userMail)) {
				arrayOfReminders.add(reminder);
			}
		}
		arrayToReturn = new Reminder[arrayOfReminders.toArray().length];
		arrayOfReminders.toArray(arrayToReturn);

		return arrayToReturn;
	}

	public synchronized static void deleteReminder(String id, String mail) throws Exception {
		// TODO get the reminder from the db by id. if the reminder creator ==
		// mail delete this record
		Properties remiderProp = new Properties();
		FileInputStream reminderFile = new FileInputStream("C:\\School\\Networks\\shai.txt");
		remiderProp.load(reminderFile);

		Reminder reminder = new Reminder(id, (String) remiderProp.getProperty(id));
		if (reminder.getCreator().equals(mail)) {
			remiderProp.remove(id);
		}
		reminderFile.close();
	}

	public synchronized static Reminder getReminder(String id) {
		// TODO get Reminder by id or return null if dosnt exsist
		return null;
	}

	public static boolean addReminder(Reminder newReminder) {
		// TODO Auto-generated method stub
		return false;
	}

}
