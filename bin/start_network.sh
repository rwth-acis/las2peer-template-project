#!/bin/bash

# this script starts a las2peer node
# pls run the script form the root folder of your deployment, e. g. ./bin/start_network.sh

java -cp "lib/*" i5.las2peer.tools.L2pNodeLauncher -s 9011 - interactive
