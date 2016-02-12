package i5.las2peer.services.servicePackage;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import i5.las2peer.p2p.PastryNodeImpl;
import i5.las2peer.p2p.PastryNodeImpl.STORAGE_MODE;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.services.servicePackage.storage.MyStorageObject;
import i5.las2peer.services.servicePackage.storage.StorageService;
import i5.las2peer.tools.ColoredOutput;

/**
 * This class checks the functionality of the StorageService example.
 *
 */
public class StorageTest {

	private static final int STORAGE_SERVICE_NODE_PORT = 30200;
	private static final String TEST_STORAGE_ID = "test-storage";

	private static PastryNodeImpl storageServiceNode;
	private static ServiceAgent storageService;

	@BeforeClass
	public static void init() {
		try {
			System.out.println("starting test network...");
			ColoredOutput.allOff();
			// start storage service node as standalone network
			storageServiceNode = new PastryNodeImpl(STORAGE_SERVICE_NODE_PORT, null, STORAGE_MODE.memory, false, null,
					null);
			storageServiceNode.launch();
			// start storage service
			storageService = ServiceAgent.createServiceAgent(new ServiceNameVersion(StorageService.class.getName(),"1.0"), "test-service-pass");
			storageService.unlockPrivateKey("test-service-pass");
			storageServiceNode.registerReceiver(storageService);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@AfterClass
	public static void stopNetwork() {
		System.out.println("stopping test network...");
		storageServiceNode.shutDown();
	}

	@Test
	public void testStorage() throws Exception {
		// generate an unique identifier, use a better/safer algorithm for your own service!
		String identifier = TEST_STORAGE_ID + new Random().nextInt();
		// this is the test object that will be persisted
		MyStorageObject exampleObj = new MyStorageObject("Hello world!");
		storageServiceNode.invokeLocally(storageService.getId(), new ServiceNameVersion(StorageService.class.getCanonicalName(),"1.0"),
				"persistObject", new Serializable[] { identifier, exampleObj });
		// retrieve test object again from network
		MyStorageObject result = (MyStorageObject) storageServiceNode.invokeLocally(storageService.getId(),
				new ServiceNameVersion(StorageService.class.getCanonicalName(),"1.0"), "fetchObject", new Serializable[] { identifier });
		System.out.println("Success! Received test object with message: " + result.getMsg());
		assertEquals(exampleObj.getMsg(), result.getMsg());
	}

}
