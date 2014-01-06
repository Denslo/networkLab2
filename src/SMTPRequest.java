import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SMTPRequest {

	private static String COOKIE_NAME = "usermail=";
	private static String COOKE_PARAM = "user";
	private static String LOG_OUT_PARAM = "logout";
	private static String DELETE_PARAM = "delete";

	public static void handler(Request request, Response response) throws Exception {

		if (!isCookeOK(request) && !isUriIndex(request)) {

			response.setRedirect(Server.prop.getProperty("defaultPage"), request.GetHttpVer());

		} else {

			switch (request.getURI(true).toLowerCase()) {
			case "/smtp/index.html":

				activateIndexHTML(request, response);

				break;

			case "/smtp/main.html":

				activateMainHTML(request, response);

				break;

			case "/smtp/reminders.html":

				activateRemindersHTML(request, response);

				break;

			case "/smtp/reminder_editor.html":

				activateRemindersEditorHTML(request, response);
				
				break;

			case "/smtp/submit_reminder.html":

				break;

			case "/smtp/tasks.html":

				break;

			case "/smtp/task_editor.html":

				break;

			case "/smtp/submit_task.html":

				break;

			case "/smtp/task_reply.html":

				break;

			case "/smtp/polls.html":

				break;

			case "/smtp/poll_editor.html":

				break;

			case "/smtp/submit_poll.html":

				break;

			case "/smtp/poll_reply.html":

				break;

			default:

				Helper.buildGETorPostResponse(request, response);

				break;
			}

			Helper.buildGETorPostResponse(request, response);
		}

	}

	private static void activateRemindersEditorHTML(Request request, Response response) {
		// TODO Auto-generated method stub
		
	}

	private static void activateRemindersHTML(Request request, Response response) throws Exception {

		if (request.getParams().get(DELETE_PARAM) != null) {
			DBHandler.deleteReminder(request.getParams().get(DELETE_PARAM), getMailFromCookie(request));
			response.setRedirect(request.getURI(true), request.GetHttpVer());
			return;
		}

		File file = new File(request.getPath());
		FileInputStream fis = new FileInputStream(file);
		byte[] byteReader = new byte[(int) file.length()];
		fis.read(byteReader);
		fis.close();

		String origSite = new String(byteReader);

		String userMail = getMailFromCookie(request);
		String siteWithUserMail = origSite.replaceAll("%mail%", userMail);

		Reminder[] userData = DBHandler.getRimindersByUserMail(userMail);
		String siteWithUserMailAndData = siteWithUserMail.replaceAll("%table%", parsUserData(userData));

		response.setData(siteWithUserMailAndData.getBytes());

	}

	private static String parsUserData(Reminder[] userData) {

		StringBuilder retVal = new StringBuilder();
		retVal.append("<table border=\"1\">");
		retVal.append("<tr><th>Title</th><th>Created On</th><th>Reminding Date</th></tr>");

		for (Reminder reminder : userData) {
			retVal.append("<tr>");

			retVal.append("<td>" + reminder.getSubject() + "</td>");
			retVal.append("<td>" + reminder.getWhen_created() + "</td>");
			retVal.append("<td>" + reminder.getWhen_created() + "</td>");

			retVal.append("<td><a href=reminder_editor.html?edit=" + reminder.getId() + ">Edit</a></td>");
			retVal.append("<td><a href=reminder.html?delete=" + reminder.getId() + ">Delete</a></td>");

			retVal.append("</tr>");
		}

		retVal.append("</table>");
		return retVal.toString();
	}

	private static void activateMainHTML(Request request, Response response) throws IOException {

		if (request.getParams().get(LOG_OUT_PARAM) != null) {
			if (request.getParams().get(LOG_OUT_PARAM).equals("true")) {
				response.addHeader("Set-Cookie", COOKIE_NAME);
				response.setRedirect(Server.prop.getProperty("defaultPage"), request.GetHttpVer());
				return;
			}
		}

	}

	private static void activateIndexHTML(Request request, Response response) throws IOException {

		if (isCookeOK(request)) {
			response.setRedirect("/smtp/main.html", request.GetHttpVer());
			return;
		}

		if (request.getParams().get(COOKE_PARAM) != null) {

			if (Helper.EmailValidator(request.getParams().get(COOKE_PARAM))) {
				response.addHeader("Set-Cookie", COOKIE_NAME + request.getParams().get(COOKE_PARAM));
			}
			response.setRedirect(Server.prop.getProperty("defaultPage"), request.GetHttpVer());
			return;
		}

	}

	private static boolean isUriIndex(Request request) {

		return request.getURI(true).toLowerCase().equals("/smtp/index.html");
	}

	private static boolean isCookeOK(Request request) {

		boolean retVal = false;

		if (request.getHeaderValue("Cookie") != null) {

			String cooke = request.getHeaderValue("Cookie");

			if (validateCooke(cooke)) {
				retVal = true;
			}
		}

		return retVal;
	}

	private static boolean validateCooke(String cooke) {

		boolean retVal = false;

		if (cooke.toLowerCase().startsWith(COOKIE_NAME)) {

			String eMail = cooke.substring(COOKIE_NAME.length());

			if (Helper.EmailValidator(eMail)) {
				retVal = true;
			}
		}

		return retVal;
	}

	private static String getMailFromCookie(Request request) {
		return request.getHeaderValue("Cookie").substring(COOKIE_NAME.length());
	}

}
