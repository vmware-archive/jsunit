#!/bin/sh

# Starts Firefox3 when it is installed in Applications/Firefox3 (to coexist with Firefox 2 in Applications/Firefox). Use this instead of calling the AppleScripts directly.

#osascript bin/mac/stop-firefox.scpt
open -a Firefox3 $*

