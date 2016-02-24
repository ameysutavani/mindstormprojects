import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Sound;

public class SimpleWebServer {
	public static final int PORT = 80;
	private ServerSocket ss;
	private Socket sock;

	public SimpleWebServer() throws IOException {
		ss = new ServerSocket(PORT);
	}

	public void run() throws IOException {
		for (;;) {
			sock = ss.accept();
			InputStream is = sock.getInputStream();
			OutputStream os = sock.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			PrintStream ps = new PrintStream(os);

			for (;;) {
				String cmd = br.readLine();
				if (cmd == null)
					break;
				String reply = execute(cmd);
				if (reply != null)
					ps.println(reply);
				else {
					br.close();
					ps.close();
					break;
				}
			}

		}
	}

	public String execute(String cmd) {
		System.out.println("cmd: " + cmd);
		String[] tokens = cmd.split(" ");

		if (tokens.length > 1 && tokens[0].equals("GET")) {
			if (tokens[1].equals("/Hello")) {
				Sound.beepSequenceUp();
			} else if (tokens[1].equals("/Goodbye")) {
				Sound.beepSequence();
			} else if (tokens[1].equals("/Text")) {
				Sound.beep();
			}
			return "HTTP/1.1 200 OK\r\n\r\nOK\r\n";
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		new SimpleWebServer().run();
	}
}