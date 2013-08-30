package i5.las2peer.service;

import i5.las2peer.api.Service;
import i5.las2peer.security.UserAgent;

public class SampleService extends Service {

    public boolean testMethod() {
        return true;
    }

    //Does not matter which message is sent, just for demonstrating purpose 
    //how to call a method with parameters from unit test
    public String testMethod2(String message) {
        UserAgent sendingAgent = (UserAgent) this.getActiveAgent();
        return sendingAgent.getLoginName();
    }
}
