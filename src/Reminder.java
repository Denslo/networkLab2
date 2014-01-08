import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


public class Reminder {

	private static final DateFormat DATE_FORMAT= new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private String id;
	private String creator;
	private String data;
	private String subject;
	private Calendar when_created;
	private Calendar due_date;
	private boolean was_handled;
	
	public Reminder(){
		
		this.id = UUID.randomUUID().toString();
		this.creator = "";
		this.data = "";
		this.subject = "";
		this.when_created = Calendar.getInstance();
		this.due_date = Calendar.getInstance();
		this.was_handled = Boolean.parseBoolean("");
	}
	
	public Reminder(String id){
		this();
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (isParamOK(id)) {
			this.id = id;
		}
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		if (isParamOK(creator)) {
			this.creator = creator;
		}
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		if (isParamOK(data)) {
			this.data = data;
		}
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		if(isParamOK(subject)){
			this.subject = subject;
		}
	}

	public Calendar getWhen_created() {
		return when_created;
	}
	
	public String getStringWhen_created() {
		return DATE_FORMAT.format(when_created.getTime());
	}
	
	public boolean isWas_handled() {
		return was_handled;
	}

	public void setWas_handled(boolean was_handled) {
		this.was_handled = was_handled;
	}

	public void setDue_dateDate(String date) {

		try {
			String[] dateArray = date.split("-");
			int year = Integer.parseInt(dateArray[0]);
			int month = Integer.parseInt(dateArray[1]);
			int day = Integer.parseInt(dateArray[2]);
			
			due_date.set(Calendar.YEAR, year);
			due_date.set(Calendar.MONTH, month);
			due_date.set(Calendar.DAY_OF_MONTH, day);
		} catch (Exception e) {}
		
	}

	public void setDue_dateTime(String time) {
		try {
			String[] timeArray = time.split(":");
			int hour = Integer.parseInt(timeArray[0]);
			int minute = Integer.parseInt(timeArray[1]);
			
			due_date.set(Calendar.HOUR_OF_DAY, hour);
			due_date.set(Calendar.MINUTE, minute);
		} catch (Exception e) {}
		
	}

	
	public String getStringDue_dateDate() {
		DateFormat tempDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return tempDateFormat.format(due_date.getTime());
	}

	public String getStringDue_dateTime() {
		DateFormat tempDateFormat = new SimpleDateFormat("HH:mm");
		return tempDateFormat.format(due_date.getTime());
	}


	public String getStringDue_date() {
		return DATE_FORMAT.format(due_date.getTime());
	}
	
	private boolean isParamOK(String parm){
		return parm != null && parm != "";
	}
}
