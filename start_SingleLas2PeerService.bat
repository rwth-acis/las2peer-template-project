set BASE=%CD%
set CLASSPATH="%BASE%/lib/*;"

java -cp %CLASSPATH% i5.las2peer.tools.ServiceStarter -x startup/agent-SampleService.xml SampleServicePass -b ""
pause