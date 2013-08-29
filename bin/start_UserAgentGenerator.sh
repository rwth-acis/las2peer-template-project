#! /bin/bash

BASE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/
export CLASSPATH="${BASE}lib/*"

java -cp "${CLASSPATH}" i5.las2peer.tools.UserAgentGenerator userAPass "User A" usera@mail.com
