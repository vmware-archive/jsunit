#!/bin/sh
# for firefox-2 on newer debian-based distros
killall -9 -w firefox-2-bin
firefox-2 $1 &
