import java.io.IOException;

public class SMTPRequest {

	private static String COOKE_START_WITH = "usermail-";
	private static String COOKE_PARAM = "user";

	public static void handler(Request request, Response response) throws IOException {
		
		if (!isCookeOK(request) && !isUriIndex(request)) {
	
			response.setRedirect("/index.html" , request.GetHttpVer());
	
		} else {

			switch (request.getURI(true).toLowerCase()) {
			case "/index.html":
				
				activateIndexHTML(request, response);
				
				break;
			
			case "/smtp/main.html":
				
				activateMainHTML(request,response);

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
	
	private static void activateMainHTML(Request request, Response response) {
		// TODO Auto-generated method stub
		
	}

	private static void activateIndexHTML(Request request, Response response) throws IOException {
		
		if (isCookeOK(request)) {
			response.setRedirect("/smtp/main.html" , request.GetHttpVer());
			return;
		} 
		
		if (!request.getParams().isEmpty() && request.getParams().get(COOKE_PARAM) != null) {
			
			if (Helper.EmailValidator(request.getParams().get(COOKE_PARAM))) {
				response.addHeader("Set-Cookie", COOKE_START_WITH + request.getParams().get(COOKE_PARAM));
			}
			
			response.setRedirect("/index.html" , request.GetHttpVer());
			return;
		}
		
		Helper.buildGETorPostResponse(request, response);
		
	}
	
	private static boolean isUriIndex(Request request) {

		return request.getURI(true).toLowerCase().equals("/index.html");
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

		String eMail = cooke.substring(COOKE_START_WITH.length() - 1);

		if (Helper.EmailValidator(eMail)) {
			retVal = true;
		}
	}

	return retVal;
}







}
