import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MoveController;

/**
 * Robot travels to obstacle and back again
 */
public class TravelTest {
	MoveController pilot;
	EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
	SampleProvider bump = us.getDistanceMode();
	float[] sample = new float[1];

	public void go() {
		pilot.forward();
		;
		while (pilot.isMoving()) {
			bump.fetchSample(sample, 0);
			if (sample[0] < 0.25)
				pilot.stop();
		}
		float dist = pilot.getMovement().getDistanceTraveled();
		System.out.println("Distance = " + dist);
		pilot.travel(-dist);
	}

	public static void main(String[] args) {
		TravelTest traveler = new TravelTest();
		traveler.pilot = new DifferentialPilot(2.25f, 5.5f, Motor.B, Motor.C);
		traveler.go();
	}
}