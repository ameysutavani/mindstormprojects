package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.PilotProps;

public class WebControl extends Thread {
	public static final int WCPORT = 12345;
	private ServerSocket ss;
	private Socket sock;
	private DifferentialPilot robot;

	public WebControl() throws IOException {
		PilotProps pp = new PilotProps();
		pp.loadPersistentValues();
		float wheelDiameter = Float.parseFloat(pp.getProperty(
				PilotProps.KEY_WHEELDIAMETER, "4.0"));
		float trackWidth = Float.parseFloat(pp.getProperty(
				PilotProps.KEY_TRACKWIDTH, "18.0"));

		System.out.println("Wheel diameter is " + wheelDiameter);
		System.out.println("Track width is " + trackWidth);

		RegulatedMotor leftMotor = PilotProps.getMotor(pp.getProperty(
				PilotProps.KEY_LEFTMOTOR, "B"));
		RegulatedMotor rightMotor = PilotProps.getMotor(pp.getProperty(
				PilotProps.KEY_RIGHTMOTOR, "C"));
		boolean reverse = Boolean.parseBoolean(pp.getProperty(
				PilotProps.KEY_REVERSE, "false"));

		robot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor,
				rightMotor, reverse);
		ss = new ServerSocket(WCPORT);
	}

	public void run() {
		try {
			while (Button.ESCAPE.isUp()) {

				sock = ss.accept();
				InputStream is = sock.getInputStream();
				OutputStream os = sock.getOutputStream();

				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				PrintStream ps = new PrintStream(os);

				while (Button.ESCAPE.isUp()) {
					String cmd = br.readLine();
					if (cmd == null && Button.ESCAPE.isUp())
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
				is.close();
				os.close();
				ss.close();
			}
		} catch (Exception e) {
		}
	}

	public String execute(String cmd) {
		String[] tokens = cmd.split(" ");

		if (tokens.length >= 1 && tokens[0].equals("GET") && Button.ESCAPE.isUp()) {
			if (tokens[1].equals("/stop")) {
				robot.stop();
			} else if (tokens[1].equals("/forward")) {
				robot.forward();
			} else if (tokens[1].equals("/backward")) {
				robot.backward();
			} else if (tokens[1].equals("/left")) {
				robot.rotateLeft();
			} else if (tokens[1].equals("/right")) {
				robot.rotateRight();
			}
			return "HTTP/1.1 200 OK\r\n\r\nOK\r\n";
		}
		return null;
	}

}