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
				buildResponse();
			}

		} catch (Exception e) {
			this.response.setInternalServerError(request.GetHttpVer());
		} finally {

			try {
				sendResponse();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			this.queue.Dequeue();

			try {
				this.socket.close();
			} catch (IOException e) {
			}
		}
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
		// TODO send response
		// if length > 0 cheack chankes
		// true - send in chankes
		// else - send all

	}

	private void buildResponse() throws IOException {

		switch (request.getMethod()) {
		case "GET":
			buildGETResponse();
			break;
		case "POST":
			buildPOSTResponse();
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
		if (!(request.getURI().equalsIgnoreCase("/"))) {
			response.setBadRequest(request.GetHttpVer());
		} else {
			response.setTRACE(request);
		}

	}

	private void buildHEADResponse() {

		if (filePathOK()) {

			File requestFile = new File(request.getPath());

			String fileExtention = request.getURI().substring(request.getURI().lastIndexOf(".") + 1);
			response.setHEAD((int) requestFile.length(), fileExtention);
		}
	}

	private boolean filePathOK() {

		boolean ret = false;

		File requestFile = new File(request.getPath());

		if (requestFile.exists()) {
			ret = true;
		}

		if (request.getURI().contains("..")) {
			ret = false;
		}

		return ret;
	}

	private void buildOPTIONSResponse() {
		if (request.getURI().equals("*")) {
			response.setOPTIONS(request.GetHttpVer());
		} else {
			response.setBadRequest(request.GetHttpVer());
		}
	}

	private void buildPOSTResponse() throws IOException {
		if (filePathOK()) {

			File requestFile = new File(request.getPath());
			String fileExtention = request.getURI().substring(request.getURI().lastIndexOf(".") + 1);
			int fileLength = (int) requestFile.length();

			response.setGETandPOST(fileLength, fileExtention, requestFile);
		} else {
			this.response.setBadRequest(request.GetHttpVer());
		}

	}

	private void buildGETResponse() throws IOException {
		if (filePathOK()) {

			File requestFile = new File(request.getPath());
			String fileExtention = request.getURI().substring(request.getURI().lastIndexOf(".") + 1);
			int fileLength = (int) requestFile.length();

			response.setGETandPOST(fileLength, fileExtention, requestFile);
		} else {
			this.response.setBadRequest(request.GetHttpVer());
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

		if (request.getURI().equals("/")) {
			request.setPath("/index.html");
		}

		if (uriContainParam()) {
			this.request.addParams(this.request.getURI().split("\\?")[1]);
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
		return this.request.getURI().contains("?");
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
