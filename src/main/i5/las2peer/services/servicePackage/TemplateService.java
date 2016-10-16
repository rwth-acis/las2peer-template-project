package i5.las2peer.services.servicePackage;

import i5.las2peer.api.Context;
import i5.las2peer.logging.L2pLogger;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

import java.net.HttpURLConnection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// TODO Describe your own service
/**
 * las2peer Service
 * 
 * This is a template for a very basic las2peer service that uses the las2peer Web-Connector for RESTful access to it.
 * 
 * Note: If you plan on using Swagger you should adapt the information below in the ApiInfo annotation to suit your
 * project. If you do not intend to provide a Swagger documentation of your service API, the entire ApiInfo annotation
 * should be removed.
 * 
 */
// TODO Adjust the following configuration
@ServicePath("template")
// TODO Your own Serviceclass
public class TemplateService extends RESTService {

	@Override
	protected void initResources() {
		getResourceConfig().register(RootResource.class);
	}

	public TemplateService() {
		// read and set properties values
		// IF THE SERVICE CLASS NAME IS CHANGED, THE PROPERTIES FILE NAME NEED TO BE CHANGED TOO!
		setFieldValues();
	}

	@Api
	@SwaggerDefinition(
			info = @Info(
					title = "las2peer Template Service",
					version = "0.1",
					description = "A las2peer Template Service for demonstration purposes.",
					termsOfService = "http://your-terms-of-service-url.com",
					contact = @Contact(
							name = "John Doe",
							url = "provider.com",
							email = "john.doe@provider.com"),
					license = @License(
							name = "your software license name",
							url = "http://your-software-license-url.com")))
	@Path("/")
	public static class RootResource {
		// instantiate the logger class
		private final L2pLogger logger = L2pLogger.getInstance(TemplateService.class.getName());

		// get access to the service class
		private final TemplateService service = (TemplateService) Context.getCurrent().getService();

		/**
		 * Template of a get function.
		 * 
		 * @return HttpResponse with the returnString
		 */
		@GET
		@Path("/get")
		@Produces(MediaType.TEXT_PLAIN)
		@ApiOperation(
				value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
				notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
		@ApiResponses(
				value = { @ApiResponse(
						code = HttpURLConnection.HTTP_OK,
						message = "REPLACE THIS WITH YOUR OK MESSAGE"), @ApiResponse(
						code = HttpURLConnection.HTTP_UNAUTHORIZED,
						message = "Unauthorized") })
		public Response getTemplate() {
			String returnString = "result";
			return Response.ok().entity(returnString).build();
		}

		/**
		 * Template of a post function.
		 * 
		 * @param myInput The post input the user will provide.
		 * @return HttpResponse with the returnString
		 */
		@POST
		@Path("/post/{input}")
		@Produces(MediaType.TEXT_PLAIN)
		@ApiResponses(
				value = { @ApiResponse(
						code = HttpURLConnection.HTTP_OK,
						message = "REPLACE THIS WITH YOUR OK MESSAGE"), @ApiResponse(
						code = HttpURLConnection.HTTP_UNAUTHORIZED,
						message = "Unauthorized") })
		@ApiOperation(
				value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
				notes = "Example method that returns a phrase containing the received input.")
		public Response postTemplate(@PathParam("input") String myInput) {
			String returnString = "";
			returnString += "Input " + myInput;
			return Response.ok().entity(returnString).build();
		}
	}

	// TODO OWN METHODS

}
