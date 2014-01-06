#! /bin/bash

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${SCRIPTDIR}/../
BASE=${PWD}
export CLASSPATH="${PWD}/lib/*"

java -cp "${CLASSPATH}" i5.las2peer.tools.UserAgentGenerator userAPass "User A" usera@mail.com
