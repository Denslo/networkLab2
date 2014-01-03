import java.io.IOException;
import java.net.Socket;

public class HttpRequestQueue {

	private int maxSize;
	private int counter;

	public HttpRequestQueue(int max) {
		this.maxSize = max;
		this.counter = 0;
	}

	public synchronized void Enqueue(Socket socket) throws InterruptedException, IOException {

		while (maxSize == counter) {
			this.wait();
		}

		this.counter++;

		(new Thread(new HttpRequest(this, socket))).start();

	}

	public synchronized void Dequeue() {
		this.counter--;
		notifyAll();
	}
}
