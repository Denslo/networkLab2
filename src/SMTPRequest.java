import java.io.IOException;

public class SMTPRequest {
	
	private static String COOKE_START_WITH = "usermail-";
	private static String COOKE_PARAM = "user";
	private static String ANSWER_PARAM = "answer";

	public static void handler(Request request, Response response) throws IOException {

		if (uriIsIndexAndNoParamsNoCooke(request)) {

			Helper.buildGETorPostResponse(request, response);

		} else if (uriIsIndexAndParams(request)) {
			
			if (Helper.EmailValidator(request.getParams().get(COOKE_PARAM))) {
	
				response.addHeader("Set-Cookie", COOKE_START_WITH + request.getParams().get(COOKE_PARAM));
			}
			
			//TODO redyrect to main


		} else if (isResponseToTaskOrPoll(request)) {
 
			//TODO send to the right class for handaling

		} else if (!isCookeOK(request)) {

			//TODO redyrect to index

		} else if (uriIsIndexAndCookeOK(request)) {

			//TODO redyrect to main

		} else {

			switch (request.getURI(true).toLowerCase()) {
			case "/smtp/main.html":
				
				break;

			case "/smtp/reminders.html":
				
				break;

			case "/smtp/reminder_editor.html":

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

		}

	}

	private static boolean uriIsIndexAndParams(Request request) {

		return isUriIndex(request) && !request.getParams().isEmpty();
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
		
		if (cooke.toLowerCase().startsWith(COOKE_START_WITH)) {
			
			String eMail = cooke.substring(COOKE_START_WITH.length() -1);
			
			if (Helper.EmailValidator(eMail)) {
				retVal = true;
			}
		}
		
		return retVal;
	}

	private static boolean isResponseToTaskOrPoll(Request request) {

		boolean retVal = false;
		
		if (request.getParams().get(ANSWER_PARAM) != null) {
			if (request.getParams().get(ANSWER_PARAM).toLowerCase().equals("true")) {
				retVal = true;
			}
		}

		return retVal;
	}

	private static boolean uriIsIndexAndCookeOK(Request request) {

		return isUriIndex(request) && isCookeOK(request);
	}

	private static boolean uriIsIndexAndNoParamsNoCooke(Request request) {

		return isUriIndex(request) && request.getParams().isEmpty() && !isCookeOK(request);
	}

	private static boolean isUriIndex(Request request) {

		return request.getURI(true).toLowerCase().equals("/index.html");
	}

}
