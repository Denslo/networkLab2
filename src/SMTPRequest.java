import java.io.IOException;


public class SMTPRequest {

	public static void handler(Request request, Response response) throws IOException {

		//if index no params no cooke => get index
		//if index and params => if set cooke => redyrect to main else return to index
		//if index and cooke => redyrect to main

		if (uriIsIndexAndNoParams(request)) {
			
			Helper.buildGETResponse(request, response);
			
		} else if (isResponseToTaskOrPoll(request)){
		
			//send to the right class for handaling
			
		} else if (isNoCooke(request)){
			
			//redyrect to index
			
		} else if (uriIsIndexAndCookeOK(request)){
			
		}
		
		
		
	}

	private static boolean uriIsIndexAndNoParams(Request request) {

		return request.getURI().toLowerCase().equals("/index.html") && request.getParams().isEmpty();
	}

}
