#! /bin/bash

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${SCRIPTDIR}/../
BASE=${PWD}
export CLASSPATH="${PWD}/lib/*"

java -cp "${CLASSPATH}" i5.las2peer.tools.ServiceStarter -x startup/agent-service-i5.las2peer.ServicePackage.xml SampleServicePass -b ""
