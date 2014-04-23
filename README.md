LAS2peer-Sample-Service
=======================

This project can be used as a starting point for your LAS2peer service. It contains everything needed to start LAS2peer service development,
you do not need to add any dependencies manually.
For documentation on the LAS2peer service API, please refer to the [LAS2peer Project](https://github.com/rwth-acis/las2peer/).

Please follow the instructions of this ReadMe to setup your service development environment.


Enable Strong Encryption
-----------------------

If you use an Oracle Java version, you have to enable strong encryption for this software.

Please put the files to [...]/lib/security/ of your java runtime installation (replacing the existing files).

The policy files can be downloaded via Oracle:

[JCE for Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html "JCE-7")


Develop a Service for LAS2peer
-------------------------------------

1. If you use Eclipse, import the project or just create a new project in the same folder.  
2. Change "etc/ant_configuration/service.properties" and "etc/ant_configuration/user.properties" according to the service you want to build.  
3. Run "ant get_deps" once to pull all dependencies (You can skip this but Eclipse will complain about missing libraries until you build the first time).  
4. The service source code can be found at "i5.las2peer.services.servicePackage.ServiceClass".  
(Optional: Rename your build directory structure according to the names you gave in 2., you might want to also correct the package deceleration in your source code files.)
5. Compile your service with "ant", this will also build the service jar.  
6. Generate documentation, run your JUnit tests and generate service and user agent with "ant all" (If this did not run check that the policy files are working correctly).  

The jar file with your service will be in "export/" and "service/" and the generated agent XML files in "etc/startup/".


Using a Different Build-System / Directory-Structure
-------------------------------------------------

If you really want to use LAS2peer in a different project structure / with a different build system you can point your build-system to this
Maven repository: http://role.dbis.rwth-aachen.de:9911/archiva/repository/internal/ and add the LAS2peer as dependency for your project:

```
<dependency>
    <groupId>i5</groupId>
    <artifactId>las2peer</artifactId>
    <version>0.0.3</version>
</dependency>
```

For information about the build process look at the "jar" and "generate_configs" tasks in the build.xml file.


Run Your Service
----------------------------------------

The class "i5.las2peer.tools.L2pNodeLaucher" provides a simple way to start a node and launch some testing methods.

All you have to know is a port, which you can open at your local machine.
If you want to join an existing network, you will need to know address and port of at least one participant.

You can just use the helper script located at **bin/start_network.sh(/bat)**.

Please add parameters to the last line of this file according to the following examples:  

1) start a new network with  

    [..] i5.las2peer.tools.L2pNodeLauncher -s 9001 - interactive

2) add an additional node with 

    [..] i5.las2peer.tools.L2pNodeLauncher -s 9002 IP_OF_THE_FIRST_MACHINE:9001 interactive

3) If you want to execute test methods at the nodes just put their names as additional parameters to the start_network.sh(/bat) script like  

    [..] i5.las2peer.tools.L2pNodeLauncher -s 9003 - uploadStartupDirectory startService('i5.las2peer.services.servicePackage.ServiceClass','SampleServicePass') - interactive

More informations about existing test methods can be found in the JavaDoc of the i5.las2peer.tools.L2pNodeLauncher class ([LAS2peer Project](https://github.com/rwth-acis/las2peer/)).
Basically you can use all public (non-static) methods of the class.

Each started node will continue to run if the last command executed via command line does not give a "shutdown" command.

You can stop the complete run using Strg-C at any point.

You can find detailed log files for each node in the directory "log/" afterwards.

After starting your service you can finally execute service methods on an open node using the following commands:


a) First you have to register a user agent

    [..] registerUserAgent('UserA','userAPass')

b) Then you can invoke a service method 

    [..] invoke('i5.las2peer.services.servicePackage.ServiceClass','testMethod2','xyz')

Additional Scripts
----------------

The bin folder contains additional scripts that can be used to generate additional user and service agent XML files.
Please edit them according to your needs. All XML files have to be added to the startup folder and each file has to start with "agent-".
Please don't forget to add filename and passphrase to the "startup/passphrases.txt" file.  

Tutorials
----------------

All LAS2peer related tutorials and additional information on how to develop services currently can be found in the [LAS2peer Tutorial Project](https://github.com/rwth-acis/LAS2peer-Tutorial-Project/).
