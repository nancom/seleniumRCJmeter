set SELENIUM_VERSION=2.35.0
﻿echo Shutdown all selenium
start wmic path win32_process Where "CommandLine Like '%%selenium-server%%'"  Call Terminate