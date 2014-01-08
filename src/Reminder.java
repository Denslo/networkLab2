import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Reminder {

	private static final String DELIMITER = ";;;";
	private static final DateFormat DATE_FORMAT= new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String id;
	private String creator;
	private String data;
	private String subject;
	private Calendar when_created;
	private Calendar due_date;
	private boolean was_handled;
	private boolean wasParsedOk;
	
	public Reminder(){
		this.id = "";
		this.creator = "";
		this.data = "";
		this.subject = "";
		this.when_created = null;
		this.due_date = null;
		this.was_handled = Boolean.parseBoolean("");
		this.setWasParsedOk(true);
	}

	public Reminder(String key, String value) {
		
		this();
		
		try {
			String[] temp = value.split(DELIMITER);
			
			 when_created = Calendar.getInstance();
			 due_date = Calendar.getInstance();

			this.id = key;
			this.creator = temp[0];
			this.data = temp[1];
			this.subject = temp[2];
			when_created.setTime(DATE_FORMAT.parse(temp[3]));
			due_date.setTime(DATE_FORMAT.parse(temp[4]));
			this.was_handled = Boolean.parseBoolean(temp[5]);
			this.setWasParsedOk(true);
			
		} catch (Exception e) {
			this.setWasParsedOk(false);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Calendar getWhen_created() {
		return when_created;
	}
	public String getTimeWhen_created(){
		StringBuilder stringForReturn = new StringBuilder();
		
		stringForReturn.append(Integer.toString(when_created.get(Calendar.HOUR_OF_DAY)));
		stringForReturn.append(":");
		stringForReturn.append(Integer.toString(when_created.get(Calendar.MINUTE)));
		
		return stringForReturn.toString();
	}
	
	public String getStringWhen_created() {
		
		StringBuilder stringForReturn = new StringBuilder();
		stringForReturn.append(when_created.get(Calendar.DATE));
		
		stringForReturn.append("/");
		stringForReturn.append(when_created.get(Calendar.MONTH));
		
		stringForReturn.append("/");
		stringForReturn.append(when_created.get(Calendar.YEAR));
		
		return stringForReturn.toString();
		
	}

	public void setWhen_created(Calendar when_created) {
		this.when_created = when_created;
	}

	public Calendar getDue_date() {
		return due_date;
	}
	public String getStringDue_date(){
		StringBuilder stringForReturn = new StringBuilder();
		stringForReturn.append(due_date.get(Calendar.DATE));
		
		stringForReturn.append("/");
		stringForReturn.append(due_date.get(Calendar.MONTH));
		
		stringForReturn.append("/");
		stringForReturn.append(due_date.get(Calendar.YEAR));
		
		return stringForReturn.toString();
		
	}
	
	public String getTimeDue_date(){
		StringBuilder stringForReturn = new StringBuilder();
		
		stringForReturn.append(Integer.toString(due_date.get(Calendar.HOUR_OF_DAY)));
		stringForReturn.append(":");
		stringForReturn.append(Integer.toString(due_date.get(Calendar.MINUTE)));
		
		return stringForReturn.toString();
		
	}
	

	public void setDue_date(Calendar due_date) {
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
		
		stringForReturn.append(creator);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(data);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(subject);
		stringForReturn.append(DELIMITER);

		stringForReturn.append(DATE_FORMAT.format(when_created.getTime()));
		stringForReturn.append(DELIMITER);

		stringForReturn.append(DATE_FORMAT.format(due_date.getTime()));
		stringForReturn.append(DELIMITER);

		stringForReturn.append(was_handled);
		stringForReturn.append(DELIMITER);

		return stringForReturn.toString();

	}
	//TODO?"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""'
	public static void main(String[] args)  {
		//Properties prop = new Properties();
		 //prop.load(new FileInputStream("config.ini"));
		// prop.load(new FileInputStream("C:\\School\\Networks\\shai.txt"));
		Reminder r = new Reminder("123", "sbar;;;this is data;;;sub;;;22/12/2098 12:23:43;;;6/6/2098 22:43:43;;;false");
		 System.out.println(r.getStringWhen_created());
		 System.out.println(r.getTimeWhen_created());
		 System.out.println(r.getStringDue_date());
		 System.out.println(r.getTimeDue_date());
	}

	public void setDue_date(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setDue_time(String string) {
		// TODO Auto-generated method stub
		
	}
}
