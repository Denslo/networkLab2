import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;

public class SmTpAmit {

	private final String CRLF = "\r\n";
	private final String CLIENT_NAME = "NoaKochav AmitGilat";
	private final String AUTH_LOGIN = "AUTH LOGIN";
	private final String MAIL_FROM = "MAIL FROM: ";
	private final String MAIL_TO = "RCPT TO: ";
	private final String SUBJECT = "Subject: ";
	private final String FROM_NAME = "Mr.Tasker";
	private final String SENDER = "Sender: ";
	private final String FROM = "From: ";
	private final String QUIT = "QUIT";
	private final String DATA = "DATA";
	private final String DOT = ".";
	private final String welcomeCode = "220";
	private final String OKCode = "250";
	private final String AuthCode = "334";
	private final String FinalAuthCode = "235";
	private final String DataOKCode = "354";
	private final String sessionOKTerminationCode = "221";

	private int port;
	private String name;
	private Socket socket;
	private BufferedReader in;
	private OutputStreamWriter out;
	//private ConfigFileManager config = null;

	public SmTpAmit() {

		this.name = "compnet.idc.ac.il";
		this.port = 25;
		//this.config = new ConfigFileManager();
	}

	private void getConnection() {
		try {
			System.setProperty("java.net.preferIPv4Stack" , "true");
			socket = new Socket(this.name, this.port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new OutputStreamWriter(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (Exception ex) {
			// Ignore the exception. Probably the socket is not open.
		}
	}

	private String getResponse() {
		String response = null;
		try {
			response = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public void connect(String emailTo, String emailFrom, String data,
			String subject, String sender) {

		String response;
		String userNameEncoded;
		String passwordEncoded;

		try {
			getConnection();
			
			// After connecting, the SMTP server will send a response string.
			response = getResponse(); 
			System.out.println(response);
			if (!response.startsWith(welcomeCode)) {
				throw new IOException("Could not connect to the server");
			}
			//Server.prop.getProperty(AUTH_LOGIN).equals("TRUE")
			if (true) {

				out.write("EHLO " + CLIENT_NAME + CRLF);
				System.out.println("EHLO " + CLIENT_NAME);
				out.flush();

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
			}else {

				out.write("HELO " + CLIENT_NAME + CRLF);
				System.out.println("HELO " + CLIENT_NAME);
				out.flush();

				response = getResponse();
				System.out.println(response);
				if (!response.startsWith(OKCode)) {
					throw new IOException("Error with the SMTP server");
				}
			}
			
			out.write(AUTH_LOGIN + CRLF);
			out.flush();
			System.out.println(AUTH_LOGIN);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AuthCode)) {
				throw new IOException("Error with the SMTP server");
			}

			userNameEncoded = Base64.encodeBase64String("tasker@cscidc.ac.il"
					.getBytes());
			out.write(userNameEncoded + CRLF);
			out.flush();
			System.out.println(userNameEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(AuthCode)) {
				throw new IOException("Error with the SMTP server");
			}

			passwordEncoded = Base64.encodeBase64String("password".getBytes());
			out.write(passwordEncoded + CRLF);
			out.flush();
			System.out.println(passwordEncoded);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(FinalAuthCode)) {
				throw new IOException();
			}

			sendEmail(emailTo, emailFrom, data, subject, sender);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void sendEmail(String emailTo, String emailFrom, String data,
			String subject, String sender) {

		String response;

		try {
			out.write(MAIL_FROM + emailFrom +CRLF);
			out.flush();
			System.out.println(MAIL_FROM + emailFrom + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			out.write(MAIL_TO +emailTo+ CRLF);
			out.flush();
			System.out.println(MAIL_TO + emailTo + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			out.write(DATA + CRLF);
			out.flush();
			System.out.println(DATA + CRLF);

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(DataOKCode)) {
				throw new IOException();
			}

			// Mail context part.
			StringBuilder mailContent = new StringBuilder();
			mailContent.append(SUBJECT + subject);
			mailContent.append(CRLF);
			mailContent.append(FROM +  FROM_NAME);
			mailContent.append(CRLF);
			mailContent.append(SENDER + sender);
			mailContent.append(CRLF + CRLF);
			mailContent.append(data);
			mailContent.append(CRLF);
			mailContent.append(DOT);
			mailContent.append(CRLF);

			out.write(mailContent.toString());
			out.flush();
			System.out.println(mailContent.toString());

			response = getResponse();
			System.out.println(response);
			if (!response.startsWith(OKCode)) {
				throw new IOException();
			}

			// Quit part.
			out.write(QUIT + CRLF);
			out.flush();
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