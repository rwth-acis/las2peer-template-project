package i5.las2peer.services.servicePackage;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import i5.las2peer.api.Service;
import i5.las2peer.logging.L2pLogger;
import i5.las2peer.logging.NodeObserver.Event;
import i5.las2peer.restMapper.HttpResponse;
import i5.las2peer.restMapper.MediaType;
import i5.las2peer.restMapper.RESTMapper;
import i5.las2peer.restMapper.annotations.Version;
import i5.las2peer.restMapper.tools.ValidationResult;
import i5.las2peer.restMapper.tools.XMLCheck;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.servicePackage.database.DatabaseManager;
import i5.las2peer.services.servicePackage.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 * LAS2peer Service
 * 
 * This is a template for a very basic LAS2peer service
 * that uses the LAS2peer Web-Connector for RESTful access to it.
 * 
 * Note:
 * If you plan on using Swagger you should adapt the information below
 * in the ApiInfo annotation to suit your project.
 * If you do not intend to provide a Swagger documentation of your service API,
 * the entire ApiInfo annotation should be removed.
 * 
 */
@Path("/example")
@Version("0.1") // this annotation is used by the XML mapper
@Api
@SwaggerDefinition(
		info = @Info(
				title = "LAS2peer Template Service",
				version = "0.1",
				description = "A LAS2peer Template Service for demonstration purposes.",
				termsOfService = "http://your-terms-of-service-url.com",
				contact = @Contact(
						name = "John Doe",
						url = "provider.com",
						email = "john.doe@provider.com"
				),
				license = @License(
						name = "your software license name",
						url = "http://your-software-license-url.com"
				)
		))
public class TemplateService extends Service {

	// instantiate the logger class
	private final L2pLogger logger = L2pLogger.getInstance(StorageService.class.getName());

	/*
	 * Database configuration
	 */
	private String jdbcDriverClassName;
	private String jdbcLogin;
	private String jdbcPass;
	private String jdbcUrl;
	private String jdbcSchema;
	private DatabaseManager dbm;

	public TemplateService() {
		// read and set properties values
		// IF THE SERVICE CLASS NAME IS CHANGED, THE PROPERTIES FILE NAME NEED TO BE CHANGED TOO!
		setFieldValues();
		// instantiate a database manager to handle database connection pooling and credentials
		dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl, jdbcSchema);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// Service methods.
	// //////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Simple function to validate a user login.
	 * Basically it only serves as a "calling point" and does not really validate a user
	 * (since this is done previously by LAS2peer itself, the user does not reach this method
	 * if he or she is not authenticated).
	 * 
	 */
	@GET
	@Path("/validation")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "User Validation",
			notes = "Simple function to validate a user login.")
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Validation Confirmation"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized")
	})
	public HttpResponse validateLogin() {
		UserAgent userAgent = (UserAgent) getContext().getMainAgent();
		// take username as default name
		String name = userAgent.getLoginName();
		// try to fetch firstname/lastname from userdata received from OpenID
		Serializable userData = userAgent.getUserData();
		if (userData != null) {
			Object jsonUserData = JSONValue.parse(userData.toString());
			if (jsonUserData instanceof JSONObject) {
				JSONObject obj = (JSONObject) jsonUserData;
				Object firstname = obj.get("given_name");
				Object lastname = obj.get("family_name");
				if (firstname != null && lastname != null) {
					name = ((String) firstname) + " " + ((String) lastname) + " (" + name + ")";
				} else if (firstname != null) {
					name = ((String) firstname) + " (" + name + ")";
				} else if (lastname != null) {
					name = ((String) lastname) + " (" + name + ")";
				}
			} else {
				logger.warning("Parsing user data failed! Got '" + jsonUserData.getClass().getName() + "' instead of "
						+ JSONObject.class.getName() + " expected!");
			}
		}
		String returnString = "You are " + name + " and your login is valid!";
		return new HttpResponse(returnString, HttpURLConnection.HTTP_OK);
	}

	/**
	 * Example method that returns a phrase containing the received input.
	 * 
	 * @param myInput
	 * 
	 */
	@POST
	@Path("/myResourcePath/{input}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Input Phrase"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized")
	})
	@ApiOperation(value = "Sample Resource",
			notes = "Example method that returns a phrase containing the received input.")
	public HttpResponse exampleMethod(@PathParam("input") String myInput) {
		String returnString = "";
		returnString += "You have entered " + myInput + "!";

		return new HttpResponse(returnString, HttpURLConnection.HTTP_OK);
	}

	/**
	 * Example method that shows how to retrieve a user email address from a database 
	 * and return an HTTP response including a JSON object.
	 * 
	 * WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! 
	 * IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE TEMPLATE.
	 * 
	 */
	@GET
	@Path("/userEmail/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "User Email"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "User not found"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal Server Error")
	})
	@ApiOperation(value = "Email Address Administration",
			notes = "Example method that retrieves a user email address from a database."
					+ " WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! "
					+ "IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE TEMPLATE.")
	public HttpResponse getUserEmail(@PathParam("username") String username) {
		String result = "";
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			// get connection from connection pool
			conn = dbm.getConnection();

			// prepare statement
			stmnt = conn.prepareStatement("SELECT email FROM users WHERE username = ?;");
			stmnt.setString(1, username);

			// retrieve result set
			rs = stmnt.executeQuery();

			// process result set
			if (rs.next()) {
				result = rs.getString(1);

				// setup resulting JSON Object
				JSONObject ro = new JSONObject();
				ro.put("email", result);

				// return HTTP Response on success
				return new HttpResponse(ro.toJSONString(), HttpURLConnection.HTTP_OK);
			} else {
				result = "No result for username " + username;

				// return HTTP Response on error
				return new HttpResponse(result, HttpURLConnection.HTTP_NOT_FOUND);
			}
		} catch (Exception e) {
			// return HTTP Response on error
			return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
		} finally {
			// free resources
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
		}
	}

	/**
	 * Example method that shows how to change a user email address in a database.
	 * 
	 * WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! 
	 * IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE TEMPLATE.
	 * 
	 */
	@POST
	@Path("/userEmail/{username}/{email}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiResponses(value = {
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Update Confirmation"),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized"),
			@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal Server Error")
	})
	@ApiOperation(value = "setUserEmail",
			notes = "Example method that changes a user email address in a database."
					+ " WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! "
					+ "IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE TEMPLATE.")
	public HttpResponse setUserEmail(@PathParam("username") String username, @PathParam("email") String email) {

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

			// return
			return new HttpResponse(result, HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			// return HTTP Response on error
			return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return new HttpResponse("Internal error: " + e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// Methods required by the LAS2peer framework.
	// //////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Method for debugging purposes.
	 * Here the concept of restMapping validation is shown.
	 * It is important to check, if all annotations are correct and consistent.
	 * Otherwise the service will not be accessible by the WebConnector.
	 * Best to do it in the unit tests.
	 * To avoid being overlooked/ignored the method is implemented here and not in the test section.
	 * @return true, if mapping correct
	 */
	public boolean debugMapping() {
		String XML_LOCATION = "./restMapping.xml";
		String xml = getRESTMapping();

		try {
			RESTMapper.writeFile(XML_LOCATION, xml);
		} catch (IOException e) {
			// write error to logfile and console
			logger.log(Level.SEVERE, e.toString(), e);
			// create and publish a monitoring message
			L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());
		}

		XMLCheck validator = new XMLCheck();
		ValidationResult result = validator.validate(xml);

		if (result.isValid()) {
			return true;
		}
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
			// write error to logfile and console
			logger.log(Level.SEVERE, e.toString(), e);
			// create and publish a monitoring message
			L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());
		}
		return result;
	}

}
