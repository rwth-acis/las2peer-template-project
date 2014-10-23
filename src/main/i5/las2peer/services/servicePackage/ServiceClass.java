package i5.las2peer.services.servicePackage;

import i5.las2peer.api.Service;
import i5.las2peer.restMapper.RESTMapper;
import i5.las2peer.restMapper.annotations.GET;
import i5.las2peer.restMapper.annotations.POST;
import i5.las2peer.restMapper.annotations.Path;
import i5.las2peer.restMapper.annotations.PathParam;
import i5.las2peer.restMapper.annotations.Version;
import i5.las2peer.restMapper.tools.ValidationResult;
import i5.las2peer.restMapper.tools.XMLCheck;
import i5.las2peer.security.Context;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.servicePackage.database.DatabaseManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * LAS2peer Service
 * 
 * This is a template for a very basic LAS2peer service
 * that uses the LAS2peer Web-Connector for RESTful access to it.
 * 
 */
@Path("example")
@Version("0.1")
public class ServiceClass extends Service {

	private String jdbcDriverClassName;
	private String jdbcLogin;
	private String jdbcPass;
	private String jdbcUrl;
	private String jdbcSchema;
	private DatabaseManager dbm;

	public ServiceClass() {
		// read and set properties values
		// IF THE SERVICE CLASS NAME IS CHANGED, THE PROPERTIES FILE NAME NEED TO BE CHANGED TOO!
		setFieldValues();
		// instantiate a database manager to handle database connection pooling and credentials
		dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl, jdbcSchema);
	}

	/**
	 * Simple function to validate a user login.
	 * Basically it only serves as a "calling point" and does not really validate a user
	 * (since this is done previously by LAS2peer itself, the user does not reach this method
	 * if he or she is not authenticated).
	 * 
	 */
	@GET
	@Path("validate")
	public String validateLogin() {
		String returnString = "";
		returnString += "You are " + ((UserAgent) getActiveAgent()).getLoginName() + " and your login is valid!";
		return returnString;
	}

	/**
	 * Another example method.
	 * 
	 * @param myInput
	 * 
	 */
	@POST
	@Path("myMethodPath/{input}")
	public String exampleMethod(@PathParam("input") String myInput) {
		String returnString = "";
		returnString += "You have entered " + myInput + "!";
		return returnString;
	}

	/**
	 * Example method that shows how to retrieve a user email address from a database.
	 * 
	 * @param username
	 * @return The email address for the given username or an error message
	 */
	@GET
	@Path("getUserEmail/{username}")
	public String getUserEmail(@PathParam("username") String username) {
		String result = "";
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn = dbm.getConnection();
			stmnt = conn.prepareStatement("SELECT email FROM users WHERE username = ?;");
			stmnt.setString(1, username);
			rs = stmnt.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			} else {
				result = "No result for username " + username;
			}
		} catch (Exception e) {
			result = "Error occurred: " + e;
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * Example method that shows how to change a user email address in a database.
	 * 
	 * @param username
	 * @param email
	 * @return Returns a message with the number of rows that were updated or an error message.
	 */
	@POST
	@Path("setUserEmail/{username}/{email}")
	public String setUserEmail(@PathParam("username") String username, @PathParam("email") String email) {
		String result = "";
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn = dbm.getConnection();
			stmnt = conn.prepareStatement("UPDATE users SET email = ? WHERE username = ?;");
			stmnt.setString(1, email);
			stmnt.setString(2, username);
			int rows = stmnt.executeUpdate(); // same works for insert
			result = "Database updated. " + rows + " rows affected";
		} catch (Exception e) {
			result = "Error occurred: " + e;
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					Context.logError(this, e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * Method for debugging purposes.
	 * Here the concept of restMapping validation is shown.
	 * It is important to check, if all annotations are correct and consistent.
	 * Otherwise the service will not be accessible by the WebConnector.
	 * Best to do it in the unit tests.
	 * To avoid being overlooked/ignored the method is implemented here and not in the test section.
	 * @return  true, if mapping correct
	 */
	public boolean debugMapping() {
		String XML_LOCATION = "./restMapping.xml";
		String xml = getRESTMapping();

		try {
			RESTMapper.writeFile(XML_LOCATION, xml);
		} catch (IOException e) {
			e.printStackTrace();
		}

		XMLCheck validator = new XMLCheck();
		ValidationResult result = validator.validate(xml);

		if (result.isValid())
			return true;
		return false;
	}

	/**
	 * This method is needed for every RESTful application in LAS2peer. There is no need to change!
	 * 
	 * @return the mapping
	 */
	public String getRESTMapping() {
		String result = "";
		try {
			result = RESTMapper.getMethodsAsXML(this.getClass());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

}
