package i5.las2peer.services.servicePackage.rmi;

import i5.las2peer.api.Service;

/**
 * This is the foreign service used for the RMI call example. It represents any other LAS2peer service that should be
 * called via RMI.
 *
 */
public class RMIForeignService extends Service {

	// this constant is used to validate test results
	public static final String TEST_STRING = "This is LAS2peer!";

	// simple value return function with no arguments
	public String serviceMethodOne() {
		return TEST_STRING;
	}

	// simple addition method to show how to transfer variables and results
	public int serviceMethodTwo(int val1, int val2) {
		return val1 + val2;
	}

}
