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
import i5.las2peer.security.UserAgent;

import java.io.IOException;

/**
 * 
 * LAS2peer Service
 * 
 * This is a template for a very basic LAS2peer service
 * that uses the LAS2peer Web-Connector for RESTful access to it.
 * 
 * 
 *
 */
@Path("example")
@Version("0.1")
public class ServiceClass extends Service {



	/**
	 * This method is needed for every RESTful application in LAS2peer.
	 * 
	 * @return the mapping
	 */
    public String getRESTMapping()
    {
        String result="";
        try {
            result= RESTMapper.getMethodsAsXML(this.getClass());
        } catch (Exception e) {

            e.printStackTrace();
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
    public boolean debugMapping()
	{
		String XML_LOCATION = "./restMapping.xml";
		String xml= getRESTMapping();

		try{
			RESTMapper.writeFile(XML_LOCATION,xml);
		}catch (IOException e){
			e.printStackTrace();
		}

		XMLCheck validator= new XMLCheck();
		ValidationResult result = validator.validate(xml);

		if(result.isValid())
			return true;
		return false;
	}
    
    /**
     * 
     * Simple function to validate a user login.
     * Basically it only serves as a "calling point" and does not really validate a user
     * (since this is done previously by LAS2peer itself, the user does not reach this method
     * if he or she is not authenticated).
     * 
     */
    @GET
    @Path("validate")
    public String validateLogin()
    {
    	String returnString = "";
    	returnString += "You are " + ((UserAgent) getActiveAgent()).getLoginName() + " and your login is valid!";
    	return returnString;
    }
    
    /**
     * 
     * Another example method.
     * 
     * @param myInput
     * 
     */
    @POST
    @Path("myMethodPath/{input}")
    public String exampleMethod( @PathParam("input") String myInput)
    {
    	String returnString = "";
    	returnString += "You have entered " + myInput + "!";
    	return returnString;
    }
}
