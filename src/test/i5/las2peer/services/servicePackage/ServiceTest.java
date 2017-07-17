package i5.las2peer.services.servicePackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import i5.las2peer.api.p2p.ServiceNameVersion;
import i5.las2peer.connectors.webConnector.WebConnector;
import i5.las2peer.connectors.webConnector.client.ClientResponse;
import i5.las2peer.connectors.webConnector.client.MiniClient;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.LocalNodeManager;
import i5.las2peer.security.ServiceAgentImpl;
import i5.las2peer.security.UserAgentImpl;
import i5.las2peer.testing.MockAgentFactory;

/**
 * Example Test Class demonstrating a basic JUnit test structure.
 *
 */
public class ServiceTest {

	private static final String HTTP_ADDRESS = "http://127.0.0.1";
	private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

	private static LocalNode node;
	private static WebConnector connector;
	private static ByteArrayOutputStream logStream;

	private static UserAgentImpl testAgent;
	private static final String testPass = "adamspass";

	private static final String mainPath = "template/";

	/**
	 * Called before the tests start.
	 * 
	 * Sets up the node and initializes connector and users that can be used throughout the tests.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void startServer() throws Exception {

		// start node
		node = new LocalNodeManager().newNode();
		testAgent = MockAgentFactory.getAdam();
		testAgent.unlock(testPass); // agent must be unlocked in order to be stored
		node.storeAgent(testAgent);
		node.launch();

		// during testing, the specified service version does not matter
		ServiceAgentImpl testService = ServiceAgentImpl
				.createServiceAgent(ServiceNameVersion.fromString(TemplateService.class.getName() + "@1.0"), "a pass");
		testService.unlock("a pass");

		node.registerReceiver(testService);

		// start connector
		logStream = new ByteArrayOutputStream();

		connector = new WebConnector(true, HTTP_PORT, false, 1000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
		Thread.sleep(1000); // wait a second for the connector to become ready
		testAgent = MockAgentFactory.getAdam(); // get a locked agent

	}

	/**
	 * Called after the tests have finished. Shuts down the server and prints out the connector log file for reference.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void shutDownServer() throws Exception {

		connector.stop();
		node.shutDown();

		connector = null;
		node = null;

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());

	}

	/**
	 * 
	 * Tests the validation method.
	 * 
	 */
	@Test
	public void testGet() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);

		try {
			c.setLogin(testAgent.getIdentifier(), testPass);
			ClientResponse result = c.sendRequest("GET", mainPath + "get", "");
			assertEquals(200, result.getHttpCode());
			assertTrue(result.getResponse().trim().contains("result")); // YOUR RESULT VALUE HERE
			System.out.println("Result of 'testGet': " + result.getResponse().trim());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}

	}

	/**
	 * 
	 * Test the example method that consumes one path parameter which we give the value "testInput" in this test.
	 * 
	 */
	@Test
	public void testPost() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);

		try {
			c.setLogin(testAgent.getIdentifier(), testPass);
			ClientResponse result = c.sendRequest("POST", mainPath + "post/testInput", ""); // testInput is
																							// the pathParam
			assertEquals(200, result.getHttpCode());
			assertTrue(result.getResponse().trim().contains("testInput")); // "testInput" name is part of response
			System.out.println("Result of 'testPost': " + result.getResponse().trim());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}
	}

}
