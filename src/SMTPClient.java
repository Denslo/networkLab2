import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;

public class SMTPClient {

	private final String CRLF = "\r\n";
	private final String CLIENT_NAME = "ShaiAmbar RanK";
	private final String AUTH_LOGIN = "AUTH LOGIN";
	private final static String MailFrom = "MAIL FROM: ";
	private final static String MailTo = "RCPT TO: ";
	private final static String SubjectHeader = "Subject: ";
	static String From_Name = "Mr.Tasker";
	private final static String SenderHeader = "Sender: ";
	private final static String FromHeader = "From: ";
	private final static String Dot = ".";
	private final static String QUIT = "QUIT";
	private static String DATA = "DATA";
	//private ConfigFileManager config = null;

	private final static String welcomeCode = "220";
	private final static String OKCode = "250";
	private final static String AuthCode = "334";
	private final static String FinalAuthCode = "235";
	private final static String DataOKCode = "354";
	private final static String sessionOKTerminationCode = "221";

	private String eTo;
	//TODO private String elFrom;
	private String data;
	private String sender;
	private String subject;
	
	private String name;
	private int port;
	private Socket socket;
	private BufferedReader inputStream;
	private OutputStreamWriter outPutStream;

	
	public SMTPClient(String eMailTo, String data, String sender, String subject) {

		this.eTo = eMailTo;
		//TODO this.elFrom = emailFrom;
		this.name =  Server.prop.getProperty("SMTPName"); 
		this.port = Integer.parseInt(Server.prop.getProperty("SMTPPort"));
		this.data = data;
		this.subject = subject;
		this.sender = sender;
		
	}
/*TODO
	private void getConnection() {
		try {
			socket = new Socket(this.name, this.port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new OutputStreamWriter(socket.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
/*TODO
	private void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (Exception ex) {
			
		}
	}*/

	private String getResponse() {
		String response = null;
		try {
			response = inputStream.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public void connect() {
		String response;
		String userNameEncoded;
		String passwordEncoded;

		try {
			try {
				socket = new Socket(this.name, this.port);
				inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outPutStream = new OutputStreamWriter(socket.getOutputStream());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			response = getResponse(); 
			System.out.println(response);
			if (!response.startsWith(welcomeCode)) {
				throw new IOException("Could not connect to the server");
			}

			if (Server.prop.getProperty(AUTH_LOGIN).equals("true")) {

				outPutStream.write("EHLO " + CLIENT_NAME + CRLF);
				System.out.println("EHLO " + CLIENT_NAME);
				outPutStream.flush();

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OKCode)) {
					throw new IOException("Error with the SMTP server");
				}

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OKCode)) {
					throw new IOException("Error with the SMTP server");
				}

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OKCode)) {
					throw new IOException("Error with the SMTP server");
				}

			}

			else {

				outPutStream.write("HELO " + CLIENT_NAME + CRLF);
				System.out.println("HELO " + CLIENT_NAME);
				outPutStream.flush();

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OKCode)) {
					throw new IOException("Error with the SMTP server");
				}

			}
			outPutStream.write(AUTH_LOGIN + CRLF);
			outPutStream.flush();
			System.out.println(AUTH_LOGIN);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AuthCode)) {
				throw new IOException("Error with the SMTP server");
			}

			userNameEncoded = Base64.encodeBase64String("tasker@cscidc.ac.il".getBytes());
			// System.out.println(Base64.decodeBase64(userNameEncoded));
			outPutStream.write(userNameEncoded + CRLF);
			outPutStream.flush();
			System.out.println(userNameEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AuthCode)) {
				throw new IOException("Error with the SMTP server");
			}

			passwordEncoded = Base64.encodeBase64String("password".getBytes());
			outPutStream.write(passwordEncoded + CRLF);
			outPutStream.flush();
			System.out.println(passwordEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(FinalAuthCode)) {
				throw new IOException();
			}

			sendEmail();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				inputStream.close();
				outPutStream.close();
				socket.close();
			} catch (Exception ex) {
				
			}
		}

	}

	private void sendEmail() {

		String response;

		try {
			outPutStream.write(MailFrom + sender + CRLF);
			outPutStream.flush();
			System.out.println(MailFrom + sender + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			outPutStream.write(MailTo + this.eTo + CRLF);
			outPutStream.flush();
			System.out.println(MailTo + this.eTo + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			outPutStream.write(DATA + CRLF);
			outPutStream.flush();
			System.out.println(DATA + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(DataOKCode)) {
				throw new IOException();
			}

			// Mail context part.
			StringBuilder mailContent = new StringBuilder();
			mailContent.append(SubjectHeader);
			mailContent.append(this.subject);
			mailContent.append(CRLF);
			mailContent.append(FromHeader);
			mailContent.append(From_Name);
			mailContent.append(CRLF);
			mailContent.append(SenderHeader);
			mailContent.append(sender);
			mailContent.append(CRLF);
			mailContent.append(CRLF);
			mailContent.append(this.data);
			mailContent.append(CRLF);
			mailContent.append(Dot);
			mailContent.append(CRLF);

			outPutStream.write(mailContent.toString());
			outPutStream.flush();
			System.out.println(mailContent.toString());

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			outPutStream.write(QUIT + CRLF);
			outPutStream.flush();
			System.out.println(QUIT + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(sessionOKTerminationCode)) {
				throw new IOException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}