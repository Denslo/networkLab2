import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {

	private static final String CONFIG_FILE = "config.ini";

	public static Properties prop = new Properties();

	public static void main(String[] args) {

		try {
			prop.load(new FileInputStream(CONFIG_FILE));
			startLisning();
		} catch (NumberFormatException e) {
			System.out.println("Plz check 'port' value in config.ini");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("config fle not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("could not read config file - or port 8080 could not be obtained - or could not start a new thread");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("we had a problem in the Requst queue");
			e.printStackTrace();
		}
	}

	private static void startLisning() throws NumberFormatException, IOException, InterruptedException {

		HttpRequestQueue queue = new HttpRequestQueue(Integer.parseInt(prop.getProperty("maxThreads")));
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(Integer.parseInt(prop.getProperty("port")));

		while (true) {
			// Listen for a TCP connection request.
			Socket connection = socket.accept();
			queue.Enqueue(connection);
		}

	}
}
