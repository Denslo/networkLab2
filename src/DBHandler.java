
public class DBHandler {

	public static Reminder[] getRimindersByUserMail(String userMail) {
		//TODO DO NOT RETURN NULL
		return new Reminder[0];
	}

	public synchronized static void deleteReminder(String id, String mail) {
		// TODO get the reminder from the db by id. if the reminder creator ==
		// mail delete this record
	}

	public synchronized static Reminder getReminder(String id) {
		// TODO get Reminder by id or return null if dosnt exsist
		return null;
	}

	public static boolean addReminder(Reminder newReminder) {
		// TODO Auto-generated method stub
		return false;
	}

	public synchronized static void deleteTesk(String id, String mail) {
		// TODO Auto-generated method stub
		// IMPORTANT!!!!! Only “in progress…” tasks can be deleted	
	}

	public static Task[] getTasksByUserMail(String userMail) {
		// TODO Auto-generated method stub DO NOT RETURN NULL
		return new Task[0];
	}

	public synchronized static boolean addTask(Task newTask) {
		// TODO Auto-generated method stub
		return false;
	}

	public synchronized static void updateTask(String taskID, String recipientID) {
		// TODO Auto-generated method stub
		// this method will update the DB so it will note that this task is complited	
	}

}
