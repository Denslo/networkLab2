import java.util.Map;

public class SMTPRequest {

	private static String COOKIE_NAME = "usermail=";

	private static String COOKE_PARAM = "user";
	private static String LOG_OUT_PARAM = "logout";
	private static String DELETE_PARAM = "delete";
	private static String EDIT_PARAM = "edit";

	public static void handler(Request request, Response response) throws Exception {

		if (!isCookeOK(request) && !isUriIndex(request) && !isAnswerURL(request)) {

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

				activateSubmitReminderHTML(request, response);
				break;

			case "/smtp/tasks.html":

				activateTasksHTML(request, response);
				break;

			case "/smtp/task_editor.html":

				// TODO clean this msg
				// do nothing this is here just for future use and code
				// flexability
				// but for now we do not need here any code
				break;

			case "/smtp/submit_task.html":

				activateSubmitTaskHTML(request, response);
				break;

			case "/smtp/task_reply.html":

				activateTaskReplayHTML(request, response);
				break;

			case "/smtp/polls.html":

				activatePollsHTML(request, response);
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

	private static void activatePollsHTML(Request request, Response response) {
		// if delet was activated then delet and load this page again
		if (request.getParams().get(DELETE_PARAM) != null) {
			DBHandler.deletePoll(request.getParams().get(DELETE_PARAM), getMailFromCookie(request));
			response.setRedirect(request.getURI(true), request.GetHttpVer());
			return;
		}

		byte[] byteReader = Helper.readFullFileToByteArray(request.getPath());

		String origSite = new String(byteReader);

		String userMail = getMailFromCookie(request);
		String siteWithUserMail = origSite.replaceAll("%mail%", userMail);

		Polls[] userData = DBHandler.getPollssByUserMail(userMail);
		String siteWithUserMailAndData = siteWithUserMail.replaceAll("%table%", parsPollsData(userData));

		response.setData(siteWithUserMailAndData.getBytes());

	}

	private static String parsPollsData(Polls[] userData) {
		StringBuilder retVal = new StringBuilder();
		retVal.append("<table border=\"1\">");
		retVal.append("<tr><th>Title</th><th>Created On</th><th>Recipients</th></tr>");

		for (Polls poll : userData) {
			retVal.append("<tr>");

			retVal.append("<td>" + poll.getSubject() + "</td>");
			retVal.append("<td>" + poll.getStringWhen_created() + "</td>");
			retVal.append("<td>" + poll.getRecipients() + "</td>");

			retVal.append("<td><a href=polls.html?delete=" + poll.getId() + ">Delete</a></td>");

			retVal.append("</tr>");
		}

		retVal.append("</table>");
		return retVal.toString();
	}

	private static void activateTaskReplayHTML(Request request, Response response) {
		Map<String, String> param = request.getParams();
		DBHandler.updateTask(param.get("taskid"), param.get("recipientid"));
	}

	private static void activateSubmitTaskHTML(Request request, Response response) {
		boolean submitResult = false;
		Task newTask = new Task();

		if (parsAndSetNewTask(newTask, request)) {
			if (DBHandler.addTask(newTask)) {
				submitResult = true;
			}
		}

		if (submitResult) {
			response.setRedirect("/smtp/tasks.html", request.GetHttpVer());
		}

	}

	private static boolean parsAndSetNewTask(Task task, Request request) {
		boolean retVal = false;
		Map<String, String> reqParams = request.getParams();

		try {
			task.setCreator(getMailFromCookie(request));
			task.setSubject(reqParams.get("subject"));
			task.setData(reqParams.get("content"));
			task.setDue_dateDate(reqParams.get("date"));
			task.setDue_dateTime(reqParams.get("time"));
			task.setId(reqParams.get("id"));
			task.setRecipient(reqParams.get("recipient"));
			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}

		return retVal;
	}

	private static void activateTasksHTML(Request request, Response response) {
		// if delet was activated then delet and load this page again
		if (request.getParams().get(DELETE_PARAM) != null) {
			DBHandler.deleteTesk(request.getParams().get(DELETE_PARAM), getMailFromCookie(request));
			response.setRedirect(request.getURI(true), request.GetHttpVer());
			return;
		}

		byte[] byteReader = Helper.readFullFileToByteArray(request.getPath());

		String origSite = new String(byteReader);

		String userMail = getMailFromCookie(request);
		String siteWithUserMail = origSite.replaceAll("%mail%", userMail);

		Task[] userData = DBHandler.getTasksByUserMail(userMail);
		String siteWithUserMailAndData = siteWithUserMail.replaceAll("%table%", parsTaskData(userData));

		response.setData(siteWithUserMailAndData.getBytes());

	}

	private static String parsTaskData(Task[] userData) {

		StringBuilder retVal = new StringBuilder();
		retVal.append("<table border=\"1\">");
		retVal.append("<tr><th>Title</th><th>Created On</th><th>Due Date</th><th>Status</th></tr>");

		for (Task task : userData) {
			retVal.append("<tr>");

			retVal.append("<td>" + task.getSubject() + "</td>");
			retVal.append("<td>" + task.getStringWhen_created() + "</td>");
			retVal.append("<td>" + task.getStringDue_date() + "</td>");
			retVal.append("<td>" + task.getStatus() + "</td>");

			if (task.getStatus().equals("in progress")) {
				retVal.append("<td><a href=tasks.html?delete=" + task.getId() + ">Delete</a></td>");
			}

			retVal.append("</tr>");
		}

		retVal.append("</table>");
		return retVal.toString();
	}

	private static void activateSubmitReminderHTML(Request request, Response response) {

		boolean submitResult = false;
		Reminder newReminder = new Reminder();

		if (parsAndSetNewReminder(newReminder, request)) {
			if (DBHandler.addReminder(newReminder)) {
				submitResult = true;
			}
		}

		if (submitResult) {
			response.setRedirect("/smtp/reminders.html", request.GetHttpVer());
		}

	}

	private static boolean parsAndSetNewReminder(Reminder reminder, Request request) {
		boolean retVal = false;
		Map<String, String> reqParams = request.getParams();

		try {
			reminder.setCreator(getMailFromCookie(request));
			reminder.setSubject(reqParams.get("subject"));
			reminder.setData(reqParams.get("content"));
			reminder.setDue_dateDate(reqParams.get("date"));
			reminder.setDue_dateTime(reqParams.get("time"));
			reminder.setId(reqParams.get("id"));
			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}

		return retVal;
	}

	private static void activateRemindersEditorHTML(Request request, Response response) {

		// if request have edit param on then then load this page with values
		if (request.getParams().get(EDIT_PARAM) != null) {
			if (DBHandler.getReminder(request.getParams().get(EDIT_PARAM)) != null) {

				String tempPage = new String(Helper.readFullFileToByteArray(request.getPath()));
				String frist = tempPage.substring(0, tempPage.indexOf("<form"));
				String last = tempPage.substring(tempPage.indexOf("</form>") + "</form>".length());
				String dataPage = frist + "%data%" + last;

				String reminderForHTML = parsReminderForHTML(DBHandler.getReminder(request.getParams().get(EDIT_PARAM)));
				response.setData(dataPage.replaceAll("%data%", reminderForHTML).getBytes());
			}
		}
	}

	private static String parsReminderForHTML(Reminder reminder) {

		StringBuilder retVal = new StringBuilder();
		retVal.append("<form action=\"submit_reminder.html\"  method=\"post\">");

		retVal.append("Subject: <input type=\"text\" name=\"subject\" value=\"" + reminder.getSubject() + "\"><br>");
		retVal.append("Content: <input type=\"text\" name=\"content\" value=\"" + reminder.getData() + "\"><br>");
		retVal.append("Date: <input type=\"date\" name=\"date\" value=\"" + reminder.getStringDue_dateDate() + "\"><br>");
		retVal.append("Time: <input type=\"time\" name=\"time\" value=\"" + reminder.getStringDue_dateTime() + "\"><br>");
		retVal.append("<input type=\"hidden\" name=\"id\" value=\"" + reminder.getId() + "\"><br>");

		retVal.append("<input type=\"submit\" value=\"Save\"></form>");
		return retVal.toString();
	}

	private static void activateRemindersHTML(Request request, Response response) {

		// if delet was activated then delet and load this page again
		if (request.getParams().get(DELETE_PARAM) != null) {
			DBHandler.deleteReminder(request.getParams().get(DELETE_PARAM), getMailFromCookie(request));
			response.setRedirect(request.getURI(true), request.GetHttpVer());
			return;
		}

		byte[] byteReader = Helper.readFullFileToByteArray(request.getPath());

		String origSite = new String(byteReader);

		String userMail = getMailFromCookie(request);
		String siteWithUserMail = origSite.replaceAll("%mail%", userMail);

		Reminder[] userData = DBHandler.getRimindersByUserMail(userMail);
		String siteWithUserMailAndData = siteWithUserMail.replaceAll("%table%", parsReminderData(userData));

		response.setData(siteWithUserMailAndData.getBytes());

	}

	private static String parsReminderData(Reminder[] userData) {

		StringBuilder retVal = new StringBuilder();
		retVal.append("<table border=\"1\">");
		retVal.append("<tr><th>Title</th><th>Created On</th><th>Reminding Date</th></tr>");

		for (Reminder reminder : userData) {
			retVal.append("<tr>");

			retVal.append("<td>" + reminder.getSubject() + "</td>");
			retVal.append("<td>" + reminder.getStringWhen_created() + "</td>");
			retVal.append("<td>" + reminder.getStringDue_date() + "</td>");

			retVal.append("<td><a href=reminder_editor.html?edit=" + reminder.getId() + ">Edit</a></td>");
			retVal.append("<td><a href=reminders.html?delete=" + reminder.getId() + ">Delete</a></td>");

			retVal.append("</tr>");
		}

		retVal.append("</table>");
		return retVal.toString();
	}

	private static void activateMainHTML(Request request, Response response) throws Exception {

		if (request.getParams().get(LOG_OUT_PARAM) != null) {
			if (request.getParams().get(LOG_OUT_PARAM).equals("true")) {
				response.addHeader("Set-Cookie", COOKIE_NAME);
				response.setRedirect(Server.prop.getProperty("defaultPage"), request.GetHttpVer());
				return;
			}
		}

	}

	private static void activateIndexHTML(Request request, Response response) throws Exception {

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

	private static boolean isAnswerURL(Request request) {
		return request.getURI(true).toLowerCase().equals("/smtp/task_reply.html") || request.getURI(true).toLowerCase().equals("/smtp/poll_reply.html");
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
