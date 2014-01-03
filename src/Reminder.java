import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;

public class Reminder {

	private final String DELIMITER = ";;;";
	private int id;
	private String creator;
	private String data;
	private String subject;
	private DateFormat when_created;
	private DateFormat due_date;
	private boolean was_handled;

	
	public Reminder(String key, String value) {
		String[] temp = value.split(DELIMITER);
		
		this.id = Integer.parseInt(key);
		this.creator = temp[0];
		this.data = temp[1];
		this.subject = temp[2];
		this.when_created =  new SimpleDateFormat(temp[3]);
		this.due_date =  new SimpleDateFormat(temp[4]);
		this.was_handled = Boolean.parseBoolean(temp[5]);

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
	
	public String toString(){
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
		
		return stringForReturn.toString();
		
		
	}
	//TODO DELETE !!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	/*public static void main(String[] args) throws IOException {
		//Reminder rem = new Reminder("123", "shaiambar;this is data;sub;1987/23/12 12:23:43;1999/23/14 22:43:43;true");
		 Properties a = new Properties();
		 FileInputStream shai = new FileInputStream("C:\\School\\Networks\\shai.txt");
		 File file = new File("C:\\School\\Networks\\ran.txt");
		FileOutputStream fos = null;
		fos = new FileOutputStream(file);
		
		a.load(shai);
		
		System.out.println(a.get("123"));
		Reminder rem = new Reminder("123", (String) a.get("123"));
		System.out.println(rem.toString());
		
		
		/*try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.put(rem.getId() + "", rem.toString());
		 try {
			a.store(fos, null);
			a.load(file)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //create a new reminder
		 //crette a now propet
		 //save rem
	}*/
	
}
