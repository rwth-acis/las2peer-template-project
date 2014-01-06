cd %~dp0
cd ..
set BASE=%CD%
set CLASSPATH="%BASE%/lib/*;"

java -cp %CLASSPATH% i5.las2peer.testing.L2pNodeLauncher -s 9002 192.168.0.105:9001 - interactive
pause
