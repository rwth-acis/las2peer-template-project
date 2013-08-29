#! /bin/bash

BASE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../
export CLASSPATH="${BASE}lib/*"

java -cp "${CLASSPATH}" i5.las2peer.testing.L2pNodeLauncher -s 9011 - interactive
