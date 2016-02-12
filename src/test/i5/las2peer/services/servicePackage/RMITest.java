package i5.las2peer.services.servicePackage;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mpisws.p2p.transport.multiaddress.MultiInetSocketAddress;

import i5.las2peer.p2p.PastryNodeImpl;
import i5.las2peer.p2p.PastryNodeImpl.STORAGE_MODE;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.services.servicePackage.rmi.RMIForeignService;
import i5.las2peer.services.servicePackage.rmi.RMIMyService;
import i5.las2peer.tools.ColoredOutput;
import rice.pastry.socket.SocketPastryNodeFactory;

public class RMITest {

	private static final int FOREIGN_SERVICE_NODE_PORT = 30100;
	private static final int MY_SERVICE_NODE_PORT = 30101;

	private static PastryNodeImpl foreignServiceNode;
	private static PastryNodeImpl myServiceNode;
	private static ServiceAgent foreignService;
	private static ServiceAgent myService;

	@BeforeClass
	public static void init() {
		try {
			System.out.println("starting test network...");
			ColoredOutput.allOff();
			// start foreign service node as standalone network
			foreignServiceNode = new PastryNodeImpl(FOREIGN_SERVICE_NODE_PORT, null, STORAGE_MODE.memory, false, null,
					null);
			foreignServiceNode.launch();
			// start foreign service
			foreignService = ServiceAgent.createServiceAgent(new ServiceNameVersion(RMIForeignService.class.getName(),"1.0"), "test-service-pass");
			foreignService.unlockPrivateKey("test-service-pass");
			foreignServiceNode.registerReceiver(foreignService);
			// to link both nodes get the address the foreign service node listens to
			// this is kind of hacky and usually should be part of your bootstrap configuration
			MultiInetSocketAddress addr = (MultiInetSocketAddress) foreignServiceNode.getPastryNode().getVars()
					.get(SocketPastryNodeFactory.PROXY_ADDRESS);
			String strAddr = addr.getAddress(0).getHostString();
			// start developer defined service that uses the foreign service for RMI calls
			myServiceNode = new PastryNodeImpl(MY_SERVICE_NODE_PORT,
					strAddr + ":" + FOREIGN_SERVICE_NODE_PORT, STORAGE_MODE.memory, false, null, null);
			myServiceNode.launch();
			// start developer defined service
			myService = ServiceAgent.createServiceAgent(new ServiceNameVersion(RMIMyService.class.getName(),"1.0"), "test-service-pass");
			myService.unlockPrivateKey("test-service-pass");
			myServiceNode.registerReceiver(myService);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@AfterClass
	public static void stopNetwork() {
		System.out.println("stopping test network...");
		foreignServiceNode.shutDown();
		myServiceNode.shutDown();
	}

	@Test
	public void testRMIOne() {
		// trigger method call in developer defined service
		try {
			String result = (String) myServiceNode.invokeLocally(myService.getId(),
					new ServiceNameVersion(RMIMyService.class.getName(),"1.0"), "callRMIOne", new Serializable[] {});
			System.out.println("The RMI call returned: " + result);
			assertEquals(result, RMIForeignService.TEST_STRING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRMITwo() {
		// trigger method call in developer defined service
		try {
			int result = (int) myServiceNode.invokeLocally(myService.getId(),
					new ServiceNameVersion(RMIMyService.class.getName(),"1.0"), "callRMITwo", new Serializable[] {});
			System.out.println("The RMI call returned: " + result);
			assertEquals(result, RMIMyService.TEST_RESULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
