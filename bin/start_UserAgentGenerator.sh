#! /bin/bash

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${SCRIPTDIR}/../
BASE=${PWD}
export CLASSPATH="${PWD}/lib/*"

if [[ "$#" -ne 3 ]]; then
	echo "Syntax error!"
	echo ""
    echo "Usage: start_UserAgentGenerator <user.name> <user.pass> <user.mail>";
else
	java -cp "${CLASSPATH}" i5.las2peer.tools.UserAgentGenerator $2 $1 $3
fi