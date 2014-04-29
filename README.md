LAS2peer-Template-Project
=======================

This project can be used as a starting point for your LAS2peer service development.
It contains everything needed to start LAS2peer service development, you do not need to add any dependencies manually.
For documentation on the LAS2peer service API, please refer to the [LAS2peer Project](https://github.com/rwth-acis/las2peer/).

Please follow the instructions of this ReadMe to setup your basic service development environment.


1. Enable Strong Encryption
-----------------------

If you use an Oracle Java version, you have to enable strong encryption for this software.

Please put the files to [...]/lib/security/ of your java runtime installation (replacing the existing files).

The policy files can be downloaded via Oracle:

[JCE for Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html "JCE-7")


2. Setup your Service Development Environment
-------------------------------------

1. If you use Eclipse, import this project or just create a new project in the same folder.  
2. Change "etc/ant_configuration/service.properties" and "etc/ant_configuration/user.properties" according to the service you want to build.  
3. Run "ant get_deps" once to pull all dependencies (You can skip this but Eclipse will complain about missing libraries until you build the first time).  
4. The service source code can be found at "i5.las2peer.services.servicePackage.ServiceClass".  
(Optional: Rename your build directory structure according to the names you gave in 2., you might want to also correct the package deceleration in your source code files.)
5. Compile your service with "ant", this will also build the service jar.  
6. Generate documentation, run your JUnit tests and generate service and user agent with "ant all" (If this did not run check that the policy files are working correctly).  

The jar file with your service will be in "export/" and "service/" and the generated agent XML files in "etc/startup/".
You can find the JUnit reports in the folder "test_reports/".  

If you decide to change the dependencies of your project, please make sure to run "ant clean_all" to remove all previously
added libraries first.  


3. Next Steps
-------------------------------------
Please visit the [Wiki](https://github.com/rwth-acis/LAS2peer-Template-Project/wiki/) of this project.
There you will find guides and tutorials, information on LAS2peer concepts and further interesting LAS2peer knowledge.  