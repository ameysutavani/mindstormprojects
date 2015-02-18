import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class FeatureAvoider {

	static final float MAX_DISTANCE = 0.5f;
	static final int DETECTOR_DELAY = 1000;

	public static void main(String[] args) {
		final DifferentialPilot robot = new DifferentialPilot(5.6, 12.2,
				Motor.B, Motor.C);
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
		RangeFeatureDetector detector = new RangeFeatureDetector(
				new RangeFinderAdapter(us.getDistanceMode()), MAX_DISTANCE,
				DETECTOR_DELAY);

		detector.enableDetection(true);
		robot.forward();

		detector.addListener(new FeatureListener() {
			public void featureDetected(Feature feature,
					FeatureDetector detector) {
				detector.enableDetection(false);
				robot.travel(-30);
				robot.rotate(30);
				detector.enableDetection(true);
				robot.forward();
			}
		});

		while (Button.ESCAPE.isUp())
			Thread.yield();
	}
}