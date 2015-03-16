#!/bin/sh
exec java -jar selenium-server-standalone-2.35.0.jar -role hub
exec java -jar selenium-server-standalone-2.35.0.jar -role node -hub http://localhost:4444/wd/hub -port 5555 -nodeConfig config.txt
