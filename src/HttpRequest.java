import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map.Entry;

public class HttpRequest implements Runnable {

	private Socket socket = null;
	private HttpRequestQueue queue = null;
	private Request request = null;
	private Response response = null;

	public HttpRequest(HttpRequestQueue httpRequestQueue, Socket socket) throws IOException {
		this.socket = socket;
		this.queue = httpRequestQueue;
		this.request = new Request();
		this.response = new Response();
	}

	@Override
	public void run() {

		try {

			parsRequest();

			if (response.isOK()) {

				if (isSMTPorIndexRequest()) {
					SMTPRequest.handler(request, response);
				} else {
					buildResponse();
				}

			}

		} catch (Exception e) {
			this.response.setInternalServerError(request.GetHttpVer());
		} finally {

			try {
				sendResponse();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			this.queue.Dequeue();

			try {
				this.socket.close();
			} catch (IOException e) {
			}
		}
	}

	private boolean isSMTPorIndexRequest() {

		boolean retVal = false;

		if (request.getURI(true).toLowerCase().startsWith("/index.html") || request.getURI(true).toLowerCase().startsWith("/smtp/")) {
			retVal = true;
		}

		return retVal;
	}

	private void sendResponse() throws IOException {

		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		out.println(response.getType());
		System.out.println(response.getType());

		for (Entry<String, String> header : response.getHeaders().entrySet()) {

			String outStr = header.getKey() + ": " + header.getValue();

			out.println(outStr);
			System.out.println(outStr);
		}
		out.println("");
		java.io.OutputStream out2 = socket.getOutputStream();
		DataOutputStream dos = new DataOutputStream(out2);
		dos.write(response.getData());

		this.socket.close();
		this.queue.Dequeue();

	}

	private void buildResponse() throws IOException {

		switch (request.getMethod()) {
		case "GET":
		case "POST":
			Helper.buildGETorPostResponse(request, response);
			break;
		case "OPTIONS":
			buildOPTIONSResponse();
			break;
		case "HEAD":
			buildHEADResponse();
			break;
		case "TRACE":
			buildTRACEResponse();
			break;

		default:
			this.response.setNotImplemented(request.GetHttpVer());
			break;
		}

	}

	private void buildTRACEResponse() {
		if (!(request.getURI(false).equalsIgnoreCase("/"))) {
			response.setBadRequest(request.GetHttpVer());
		} else {
			response.setTRACE(request);
		}

	}

	private void buildHEADResponse() {

		if (Helper.filePathOK(request)) {

			File requestFile = new File(request.getPath());

			String fileExtention = request.getURI(true).substring(request.getURI(true).lastIndexOf(".") + 1);
			response.setHEAD((int) requestFile.length(), fileExtention);
		}
	}

	private void buildOPTIONSResponse() {
		if (request.getURI(false).equals("*")) {
			response.setOPTIONS(request.GetHttpVer());
		} else {
			response.setBadRequest(request.GetHttpVer());
		}
	}

	private void parsRequest() throws IOException, Exception {
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// pars body
		while (!(line = input.readLine()).equals("")) {

			System.out.println(line);

			try {
				if (request.getType() == null) {
					request.setType(line);
					this.response.setType(request.GetHttpVer() + " 200 OK");
				} else {
					request.addHeader(line);
				}
			} catch (Exception e) {
				this.response.setBadRequest("HTTP/1.0");
				return;
			}
		}

		// validate request
		if (!isMethodImplemented()) {
			// set response as 501 not implemented
			this.response.setNotImplemented(request.GetHttpVer());
			return;
		}

		if (isVersionSuported()) {
			if (!isVerOK()) {
				// 400 bad request
				this.response.setBadRequest(request.GetHttpVer());
				return;
			}
		} else {
			// set as 505 not request ver
			this.response.setNotSupported(request.GetHttpVer());
			return;
		}

		if (request.getURI(true).equals("/") || request.getURI(true).equals("/index.html")) {
			response.setRedirect(Server.prop.getProperty("defaultPage"), request.GetHttpVer());
		}

		if (uriContainParam()) {
			String[] paramsinURI = this.request.getURI(false).split("\\?");
			if (paramsinURI.length >= 2) {
				this.request.addParams(paramsinURI[1]);
			}
		}

		if (request.getMethod().equals("POST") && request.getHeaderValue("Content-Length") != null) {
			char[] body = new char[Integer.parseInt(request.getHeaderValue("Content-Length"))];
			input.read(body);
			if (body.length > 0) {
				this.request.addParams(new String(body));
			}
		}
	}

	private boolean uriContainParam() {
		return this.request.getURI(false).contains("?");
	}

	private boolean isVersionSuported() {
		String version = request.GetHttpVer();
		if (version.equals("HTTP/1.0") || version.equals("HTTP/1.1")) {
			return true;
		}
		return false;
	}

	private boolean isVerOK() {
		boolean retVal = false;

		if (this.request.GetHttpVer().equals("HTTP/1.1") && this.request.getHeaderValue("Host") != null) {
			retVal = true;
		}

		if (this.request.GetHttpVer().equals("HTTP/1.0")) {
			retVal = true;
		}
		return retVal;
	}

	private boolean isMethodImplemented() {
		boolean retVal = false;
		if (this.request.getMethod().equals("GET")) {
			retVal = true;
		}
		if (this.request.getMethod().equals("POST")) {
			retVal = true;
		}
		if (this.request.getMethod().equals("OPTIONS")) {
			retVal = true;
		}
		if (this.request.getMethod().equals("HEAD")) {
			retVal = true;
		}
		if (this.request.getMethod().equals("TRACE")) {
			retVal = true;
		}
		return retVal;
	}
}
