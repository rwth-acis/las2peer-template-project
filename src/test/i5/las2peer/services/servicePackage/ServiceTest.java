package i5.las2peer.services.servicePackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import i5.las2peer.httpConnector.HttpConnector;
import i5.las2peer.httpConnector.client.Client;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.testing.MockAgentFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceTest {
	private static final String HTTP_ADDRESS = "localhost";
	private static final int HTTP_PORT = 8080;

	private LocalNode node;
	private HttpConnector connector;
	private ByteArrayOutputStream logStream;

	private static final String testPass = "adamspass";

	private static final String testServiceClass = "i5.las2peer.services.servicePackage.ServiceClass";

	@Before
	public void startServer() throws Exception {
		// start Node
		node = LocalNode.newNode();
		node.storeAgent(MockAgentFactory.getAdam());
		node.launch();

		ServiceAgent testService = ServiceAgent.generateNewAgent(
				testServiceClass, "a pass");
		testService.unlockPrivateKey("a pass");

		node.registerReceiver(testService);

		// start connector
		logStream = new ByteArrayOutputStream();
		connector = new HttpConnector();
		connector.setSocketTimeout(10000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
	}

	@After
	public void shutDownServer() throws Exception {
		connector.stop();
		node.shutDown();

		connector = null;
		node = null;

		LocalNode.reset();

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());
	}
	
	@Test
	public void testSimpleCall() {
		Client c = new Client(HTTP_ADDRESS, HTTP_PORT, "adam", testPass);

		try {
			c.connect();

			Object result = c.invoke(testServiceClass, "testMethod");
			assertEquals(true, result);
			
			result = c.invoke(testServiceClass, "testMethod2", "Hello!");
			assertEquals("adam", result);
			
			c.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e);
		}
	}
}
