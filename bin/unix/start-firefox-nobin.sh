#!/bin/sh
killall -9 -w firefox
firefox $1 &
