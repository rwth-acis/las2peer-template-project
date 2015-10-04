package i5.las2peer.services.servicePackage.storage;

import java.io.Serializable;

/**
 * This is an example object used to persist some data (in this case a simple String) to the network storage. It can be
 * replaced with any type of Serializable or even with a plain String object.
 * 
 */
public class MyStorageObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msg;

	public MyStorageObject(String msg) {
		this.setMsg(msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
