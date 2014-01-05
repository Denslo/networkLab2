import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Calendar;
import java.util.Properties;


public class SMTPNotifictionThread implements Runnable  {
	
	private static final String CONFIG_FILE = "config.ini";
	private static final String REMINDER_FILE = "reminderFilePath";
	private static final String TASK_FILE = "taskFilePath";
	private static final String POLLS_FILE = "pollFilePath";

	private FileInputStream reminderFile;
	private FileInputStream taskFile;
	private FileInputStream pollsFile;
	
	public SMTPNotifictionThread(){
		try {
			DateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			reminderFile = new FileInputStream((String) Server.prop.get(REMINDER_FILE));
			taskFile = new FileInputStream((String)Server.prop.getProperty(TASK_FILE));
			pollsFile = new FileInputStream((String)Server.prop.getProperty(POLLS_FILE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		while(true){
			checkReminder();
			checkTask();
			checkPolls();
		}
		
	}

	private void checkPolls() {
		// TODO Auto-generated method stub
		
		
	}

	private void checkTask() {
		// TODO Auto-generated method stub
		
	}

	
	private void checkReminder() {
		Date currentDate = new Date();

		Reminder reminder;
		Properties remiderProp = new Properties();
		try {
			remiderProp.load(reminderFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Entry<Object, Object> entry : remiderProp.entrySet()) {
			reminder = new Reminder((String)entry.getKey(),(String)entry.getValue());
			//TODO find how to check the date!!!!!!!!!
			//if (reminder.getDue_date()) {
				
				
		//	}
		}
		
	}

}
