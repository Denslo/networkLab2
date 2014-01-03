import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class Recipient {
	
	private final String DELIMITER = ";";
	private int id;
	private String mail;
	private boolean didReply;
	
	public Recipient(String value){
		String[] temp = value.split(DELIMITER);
		
		this.id = Integer.parseInt(temp[0]);
		this.mail = temp[1];
		this.didReply = Boolean.parseBoolean(temp[2]);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isDidReply() {
		return didReply;
	}

	public void setDidReply(boolean didReply) {
		this.didReply = didReply;
	}
	
	public String toString(){
		StringBuilder stringForReturn = new StringBuilder();
		
		stringForReturn.append(mail);
		stringForReturn.append(DELIMITER);
		
		stringForReturn.append(didReply);
		
		return stringForReturn.toString();
	}
	
	//TODO DELETE !!!!!!!!!!!!!!!!!!!!!!!!!!!!1
		public static void main(String[] args) throws IOException {
			//Recipient rem = new Recipient("123;shaiambar;true");
			 Properties a = new Properties();
			 FileInputStream shai = new FileInputStream("C:\\School\\Networks\\ran.txt");
			 File file = new File("C:\\School\\Networks\\ran.txt");
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			
			a.load(shai);
			
			System.out.println(a.get("123"));
			Reminder rem = new Reminder("123", (String) a.get("123"));
			System.out.println(rem.toString());
			
			/*
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a.put(rem.getId() + "", rem.toString());
			 try {
				a.store(fos, null);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 //create a new reminder
			 //crette a now propet
			 //save rem*/
		}
		

}
