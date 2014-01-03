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

	public void addParams(String str) {

		if (str != null && !str.equals("")) {
			String[] parameters = str.split("&");
			for (String subStr : parameters) {
				String[] param = subStr.split("=");
				if (param.length == 2) {
					if (this.params.get(param[0]) == null) {
						this.params.put(param[0], param[1]);
					}
				}
			}
		}
	}

	public String GetHttpVer() {
		return this.getType().split(" ")[2];
	}

	public String getMethod() {
		return this.getType().split(" ")[0];
	}

	public String getURI() {
		return this.getType().split(" ")[1];
	}

	public String getPath() {
		return Server.prop.getProperty("root") + this.getURI().substring(1).replaceAll("/", "\\");
	}

	public void setPath(String string) {
		this.type = this.getMethod() + " " + string + " " + this.GetHttpVer();

	}
}
