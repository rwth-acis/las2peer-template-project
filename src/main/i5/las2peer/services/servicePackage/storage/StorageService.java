package i5.las2peer.services.servicePackage.storage;

import java.io.Serializable;
import java.util.logging.Level;

import i5.las2peer.api.Service;
import i5.las2peer.logging.L2pLogger;
import i5.las2peer.logging.NodeObserver.Event;
import i5.las2peer.persistency.Envelope;

/**
 * This class is a LAS2peer service example. It shows how a service should store and fetch objects in network storage.
 *
 */
public class StorageService extends Service {

	// instantiate the logger class
	private final L2pLogger logger = L2pLogger.getInstance(StorageService.class.getName());

	/**
	 * This method stores an object inside the LAS2peer network storage. The type of the object is not limited, any
	 * class that implements the {@link Serializable} interface can be used.
	 * 
	 * @param identifier This identifier is used to identify the stored object inside the network.
	 * @param object The object that should actually be stored in network storage.
	 */
	public void persistObject(String identifier, MyStorageObject object) {
		try {
			Envelope env = null;
			try {
				// fetch existing container object from network storage
				env = getContext().getStoredObject(MyStorageObject.class, identifier);
			} catch (Exception e) {
				// write info message to logfile and console
				logger.log(Level.INFO, "Network storage container not found. Creating new one. " + e.toString());
				// create new container object with current ServiceAgent as owner
				env = Envelope.createClassIdEnvelope(object, identifier, getAgent());
			}
			// decrypt envelope with owner instance
			env.open(getAgent());
			// place the new object inside container
			env.updateContent(object);
			// sign content with current ServiceAgent to grant exclusive writing access (see wiki for details)
			env.addSignature(getAgent());
			// upload the updated storage container back to the network
			env.store();
			// close local instance to prevent unauthorized reading while waiting for garbage collection
			env.close();
		} catch (Exception e) {
			// write error to logfile and console
			logger.log(Level.SEVERE, "Can't persist to network storage!", e);
			// create and publish a monitoring message
			L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());
		}
	}

	/**
	 * This method fetches an object from the LAS2peer network storage. The return type is not limited, any class that
	 * implements the {@link Serializable} interface can be used.
	 * 
	 * @param identifier This identifier is used to identify the storage object inside the network.
	 * @return Returns the fetched object or null if an error occurred.
	 */
	public MyStorageObject fetchObject(String identifier) {
		try {
			// fetch existing container object from network storage
			Envelope env = getContext().getStoredObject(MyStorageObject.class, identifier);
			// decrypt envelope with owner instance
			env.open(getAgent());
			// deserialize content from envelope
			MyStorageObject retrieved = env.getContent(MyStorageObject.class);
			// close local instance to prevent unauthorized reading while waiting for garbage collection
			env.close();
			return retrieved;
		} catch (Exception e) {
			// write error to logfile and console
			logger.log(Level.SEVERE, "Can't fetch from network storage!", e);
			// create and publish a monitoring message
			L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());
		}
		return null;
	}

}
