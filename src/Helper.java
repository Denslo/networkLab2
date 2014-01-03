import java.io.File;
import java.io.IOException;


public class Helper {

	public static void buildGETorPostResponse(Request request, Response response) throws IOException {

		if (filePathOK(request)) {

			File requestFile = new File(request.getPath());
			String fileExtention = request.getURI().substring(request.getURI().lastIndexOf(".") + 1);
			int fileLength = (int) requestFile.length();

			response.setGETandPOST(fileLength, fileExtention, requestFile);
		} else {
			response.setBadRequest(request.GetHttpVer());
		}
		
	}

	
	public static boolean filePathOK(Request request) {

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
}
