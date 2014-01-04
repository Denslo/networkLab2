import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Reminder {

	private final String DELIMITER = ";;;";
	private int id;
	private String creator;
	private String data;
	private String subject;
	private DateFormat when_created;
	private DateFormat due_date;
	private boolean was_handled;
	private boolean wasParsedOk;

	public Reminder(String key, String value) {
		try {
			String[] temp = value.split(DELIMITER);

			this.id = Integer.parseInt(key);
			this.creator = temp[0];
			this.data = temp[1];
			this.subject = temp[2];
			this.when_created = new SimpleDateFormat(temp[3]);
			this.due_date = new SimpleDateFormat(temp[4]);
			this.was_handled = Boolean.parseBoolean(temp[5]);
			this.setWasParsedOk(true);
			
		} catch (Exception e) {

			this.id = -1;
			this.creator = "";
			this.data = "";
			this.subject = "";
			this.when_created = new SimpleDateFormat("");
			this.due_date = new SimpleDateFormat("");
			this.was_handled = Boolean.parseBoolean("");
			this.setWasParsedOk(false);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public DateFormat getWhen_created() {
		return when_created;
	}

	public void setWhen_created(DateFormat when_created) {
		this.when_created = when_created;
	}

	public DateFormat getDue_date() {
		return due_date;
	}

	public void setDue_date(DateFormat due_date) {
		this.due_date = due_date;
	}

	public boolean isWas_handled() {
		return was_handled;
	}

	public void setWas_handled(boolean was_handled) {
		this.was_handled = was_handled;
	}

	public boolean isWasParsedOk() {
		return wasParsedOk;
	}
	
	public void setWasParsedOk(boolean wasParsedOk) {
		this.wasParsedOk = wasParsedOk;
	}
	
	public String toString() {
		StringBuilder stringForReturn = new StringBuilder();
		Calendar calWhen = Calendar.getInstance();
		Calendar calDue = Calendar.getInstance();

		stringForReturn.append(creator);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(data);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(subject);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(when_created.format(calWhen.getTime()));
		stringForReturn.append(DELIMITER);

		stringForReturn.append(due_date.format(calDue.getTime()));
		stringForReturn.append(DELIMITER);

		stringForReturn.append(was_handled);
		stringForReturn.append(DELIMITER);

		return stringForReturn.toString();

	}
	//TODO?"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""'
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		 prop.load(new FileInputStream("config.ini"));
		 prop.load(new FileInputStream("C:\\School\\Networks\\shai.txt"));
		 System.out.println("fooo");
	}
}
