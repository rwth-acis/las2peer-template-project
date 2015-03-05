#! /bin/bash

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${SCRIPTDIR}/../
BASE=${PWD}
export CLASSPATH="${PWD}/lib/*"

if [[ "$#" -ne 2 ]]; then
	echo "Syntax error!"
	echo ""
    echo "Usage: start_ServiceAgentGenerator <service.canonical.class.name> <service.password>";
else
	java -cp "${CLASSPATH}" i5.las2peer.tools.ServiceAgentGenerator $1 $2
fi

