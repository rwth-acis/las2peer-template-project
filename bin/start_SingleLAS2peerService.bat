cd %~dp0
cd ..
set BASE=%CD%
set CLASSPATH="%BASE%/lib/*;"

java -cp %CLASSPATH% i5.las2peer.tools.ServiceStarter -x startup/agent-service-i5.las2peer.ServicePackage.xml SampleServicePass -b ""
pause
