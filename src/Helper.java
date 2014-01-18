import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Helper {
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static void buildGETorPostResponse(Request request, Response response) throws IOException {

		if (filePathOK(request)) {

			File requestFile = new File(request.getPath());
			String fileExtention = request.getURI(true).substring(request.getURI(true).lastIndexOf(".") + 1);
			
			int fileLength;
			
			if (response.getData().length == 0) {
				fileLength = (int) requestFile.length();
			} else {
				fileLength = response.getData().length;
				requestFile =null;
			}
			
			response.setGETandPOST(fileLength, fileExtention, requestFile);
			
		} else {
			response.setNotFound(request.GetHttpVer());
		}
		
	}

	
	public static boolean filePathOK(Request request) {

		boolean ret = false;

		File requestFile = new File(request.getPath());

		if (requestFile.exists()) {
			ret = true;
		}

		if (request.getURI(true).contains("..")) {
			ret = false;
		}

		return ret;
	}

	public static boolean EmailValidator(String eMail) {
		
		boolean retVal = false;
		
		if (eMail == null) {
			retVal = false;
		} else {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(eMail);
			retVal = matcher.matches();
		}
		
		
		return retVal;
	}
	
	public static byte[] readFullFileToByteArray(String filePath){
		
		byte[] byteReader = new byte[0];
		
		File file = new File(filePath);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byteReader = new byte[(int) file.length()];
			fis.read(byteReader);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		return byteReader;
	}

}
