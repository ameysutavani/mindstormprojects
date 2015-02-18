package mypackage;

import java.io.IOException;

public class StreamandWebControl {

	public static void main(String[] args) throws IOException {
		Stream obstream = new Stream();
		WebControl obwc = new WebControl();
		obstream.start();
		obwc.start();
		
	}

}
