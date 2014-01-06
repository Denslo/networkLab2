import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Request {

	private String type = null;
	private Map<String, String> hedders = null;
	private Map<String, String> params = null;

	public Request() {
		hedders = new HashMap<String, String>();
		params = new HashMap<String, String>();
	}

	public void addHeader(String str) throws Exception {

		String key = str.substring(0, str.indexOf(":"));
		String value = str.substring(str.indexOf(":") + 1);
		hedders.put(key.trim(), value.trim());
	}

	public void setType(String str) throws Exception {

		if (str.split(" ").length != 3)
			throw new Exception("pars error");

		this.type = str;
	}

	public Map<String, String> getHedders() {
		return new HashMap<>(hedders);
	}

	public Map<String, String> getParams() {
		return new HashMap<>(params);
	}

	public String getHeaderValue(String hedder) {
		return hedders.get(hedder);
	}

	public String getType() {
		return this.type;
	}

	public void addParams(String str) throws UnsupportedEncodingException {

		if (str != null && !str.equals("")) {
			String[] parameters = str.split("&");
			for (String subStr : parameters) {
				String[] param = subStr.split("=");
				if (param.length == 2) {
					if (this.params.get(param[0]) == null) {
						this.params.put(param[0], URLDecoder.decode(param[1],"UTF-8"));
					}
				}
			}
		}
	}

	public String GetHttpVer() {
		String retVal = "HTTP/1.0";
		
		if (this.getType() != null) {
			retVal = this.getType().split(" ")[2];
		}
		
		return retVal;
	}

	public String getMethod() {
		return this.getType().split(" ")[0];
	}

	public String getURI(boolean removeParamsFromURI) {
		
		String retVal = this.getType().split(" ")[1];
		
		if (removeParamsFromURI) {
			if (retVal.contains("?")) {
				retVal = retVal.split("\\?")[0];
			}
		}
		
		return retVal ;
	}

	public String getPath() {
		String[] temp = this.getURI(true).substring(1).split("/");
		StringBuilder temp2 = new StringBuilder();
		
		for (String string : temp) {
			temp2.append(string);
			temp2.append("\\");
		}
		
		temp2.deleteCharAt(temp2.length()-1);
		
		return Server.prop.getProperty("root") + temp2.toString();
	}

	public void setPath(String string) {
		this.type = this.getMethod() + " " + string + " " + this.GetHttpVer();

	}
}
