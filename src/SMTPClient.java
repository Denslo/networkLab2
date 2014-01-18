import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;

public class SMTPClient {

	private final static String CRLF = "\r\n";
	private final static String CLIENT_NAME = "ShaiAmbar RanKlein";
	private final static String AUTH_LOGIN = "AUTH LOGIN";
	private final static String MailFrom = "MAIL FROM: ";
	private final static String MailTo = "RCPT TO: ";
	private final static String SubjectHeader = "Subject: ";
	private final static String From_Name = "Mr.Tasker";
	private final static String SenderHeader = "Sender: ";
	private final static String FromHeader = "From: ";
	private final static String QUIT = "QUIT";
	private final static String DATA = "DATA";

	private final static String WELCOME_CODE = "220";
	private final static String OK_CODE = "250";
	private final static String AUTH_CODE = "334";
	private final static String FINAL_AUTH_CODE = "235";
	private final static String DATA_OK_CODE = "354";
	private final static String SESSION_OK_CODE = "221";

	private String mailTo;
	private String data;
	private String sender;
	private String subject;

	private String name;
	private int port;
	private Socket socket;
	private BufferedReader inputStream;
	private OutputStreamWriter outPutStream;

	public SMTPClient(String mailTo, String data, String sender, String subject) {

		this.mailTo = mailTo;
		this.name = Server.prop.getProperty("SMTPName");
		this.port = Integer.parseInt(Server.prop.getProperty("SMTPPort"));
		this.data = data;
		this.subject = subject;
		this.sender = sender;

	}

	private String getResponse() {
		String response = null;
		try {
			response = inputStream.readLine();
		} catch (IOException e) {
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
				e.printStackTrace();
			}

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(WELCOME_CODE)) {
				throw new IOException("Could not connect to the server");
			}

			if (Boolean.parseBoolean(Server.prop.getProperty("SMTPIsAuthLogin"))) {

				outPutStream.write("EHLO " + CLIENT_NAME + CRLF);
				System.out.println("EHLO " + CLIENT_NAME);
				outPutStream.flush();

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OK_CODE)) {
					throw new IOException("SMTP Error - SMTP Server ERROR");
				}

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OK_CODE)) {
					throw new IOException("SMTP Error - SMTP Server ERROR");
				}

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OK_CODE)) {
					throw new IOException("SMTP Error - SMTP Server ERROR");
				}
			}

			else {

				outPutStream.write("HELO " + CLIENT_NAME + CRLF);
				System.out.println("HELO " + CLIENT_NAME);
				outPutStream.flush();

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OK_CODE)) {
					throw new IOException("SMTP Error - SMTP Server ERROR");
				}

			}
			outPutStream.write(AUTH_LOGIN + CRLF);
			outPutStream.flush();
			System.out.println(AUTH_LOGIN);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AUTH_CODE)) {
				throw new IOException("SMTP Error - SMTP Server ERROR");
			}
			String SMTPUser = (String) Server.prop.get("SMTPUsername");
			userNameEncoded = Base64.encodeBase64String(SMTPUser.getBytes());
			outPutStream.write(userNameEncoded + CRLF);
			outPutStream.flush();
			System.out.println(userNameEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AUTH_CODE)) {
				throw new IOException("SMTP Error - SMTP Server ERROR");
			}
			String SMTPUserPassword = (String) Server.prop.get("SMTPPassword");
			passwordEncoded = Base64.encodeBase64String(SMTPUserPassword.getBytes());
			outPutStream.write(passwordEncoded + CRLF);
			outPutStream.flush();
			System.out.println(passwordEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(FINAL_AUTH_CODE)) {
				throw new IOException();
			}

			sendEmail();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				inputStream.close();
				outPutStream.close();
				socket.close();
			} catch (Exception e1) {

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
			if (!response.startsWith(OK_CODE)) {
				throw new IOException();
			}

			outPutStream.write(MailTo + this.mailTo + CRLF);
			outPutStream.flush();
			System.out.println(MailTo + this.mailTo + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OK_CODE)) {
				throw new IOException();
			}

			outPutStream.write(DATA + CRLF);
			outPutStream.flush();
			System.out.println(DATA + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(DATA_OK_CODE)) {
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
			mailContent.append(".");
			mailContent.append(CRLF);

			outPutStream.write(mailContent.toString());
			outPutStream.flush();
			System.out.println(mailContent.toString());

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OK_CODE)) {
				throw new IOException();
			}

			outPutStream.write(QUIT + CRLF);
			outPutStream.flush();
			System.out.println(QUIT + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(SESSION_OK_CODE)) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}