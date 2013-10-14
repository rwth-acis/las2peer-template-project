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
2. Change conf/service.properties according to the service you want to build.
3. Run "ant get_deps" once to pull all dependencies. (You can skip this but Eclipse will complain about missing libraries until you build the first time)
4. Implement a service by inheriting from i5.las2peer.api.Service (Or refactor from the existing i5.las2peer.ServicePackage.ServiceClass).
5. Run "ant" to just build the service jar or "ant run" to directly run your service.

A jar file with your service will be in export/ and a generated service agent XML file in startup/.


Using a Different Build-System / Directory-Structure
-------------------------------------------------

If you really want to use LAS2peer in a different project structure / with a different build system you can point your build-system to this
Maven repository: http://role.dbis.rwth-aachen.de:9911/archiva/repository/internal/ and add the LAS2peer as dependency for your project:

```
<dependency>
    <groupId>i5</groupId>
    <artifactId>las2peer</artifactId>
    <version>0.0.2</version>
</dependency>
```

For information about the build process look at the "jar" and "generate_configs" tasks in the build.xml file.


Run Your Service
----------------------------------------

The class i5.las2peer.testing.L2pNodeLaucher provides a simple way to start a node and launch some testing methods.

All you have to know is a port, which you can open at your local machine.
If you want to join an existing network, you will need to know address and port of at least one participant.

You can just use the helper script located at bin/start_network.sh(/bat).

Please add parameters to the last line of this file according to the following examples:  

1. start a new network with  
```[..] i5.las2peer.testing.L2pNodeLauncher -s 9001 -```

2. add an additional node hosted at another machine with  
```[..] i5.las2peer.testing.L2pNodeLauncher -s 9001 IP_OF_THE_FIRST_MACHINE:9001```

3. If you want to execute test methods at the nodes just put their names as additional parameters to the start_network.sh(/bat) script like  
```[..] i5.las2peer.testing.L2pNodeLauncher -s 9001 - uploadAgents waitALittle searchEve```  

More informations about existing test methods can be found in the JavaDoc of the i5.las2peer.testing.L2pNodeLauncher class ([LAS2peer Project](https://github.com/rwth-acis/las2peer/)).
Basically you can use all public (non-static) methods of the class.

You can find detailed log files for each node in the directory log/ afterwards.


The End of a Run
----------------

Each started node will be kept running if the last command executed via command line does not give a "shutdown" command.

You can stop the complete run using Strg-C at any point, of course.

