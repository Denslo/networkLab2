import java.io.IOException;

public class SMTPRequest {

	public static void handler(Request request, Response response) throws IOException {

		if (uriIsIndexAndNoParams(request)) {

			Helper.buildGETorPostResponse(request, response);

		} else if (isResponseToTaskOrPoll(request)) {

			// send to the right class for handaling

		} else if (isNoCooke(request)) {

			// redyrect to index

		} else if (uriIsIndexAndCookeOK(request)) {

			// redyrect to main

		} else {

			switch (request.getURI().toLowerCase()) {
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

	private static boolean isNoCooke(Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isResponseToTaskOrPoll(Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean uriIsIndexAndCookeOK(Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean uriIsIndexAndNoParams(Request request) {

		return request.getURI().toLowerCase().equals("/index.html") && request.getParams().isEmpty();
	}

}
