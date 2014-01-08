
public class DBHandler {

	public static Reminder[] getRimindersByUserMail(String userMail) {
		//TODO
		return new Reminder[0];
	}

	public synchronized static void deleteReminder(String id, String mail) throws Exception {
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

}
