set SELENIUM_VERSION=2.35.0
set HUB_URL=http://localhost:4444/wd/hub
set PORT=5555
start java -jar selenium-server-standalone-%SELENIUM_VERSION%.jar -role hub 
start java -jar selenium-server-standalone-%SELENIUM_VERSION%.jar -role node -hub %HUB_URL% -port %PORT% -nodeConfig config.txt