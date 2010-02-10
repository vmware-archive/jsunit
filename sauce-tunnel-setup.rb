#!/usr/bin/env ruby

require 'rubygems'
require 'saucelabs_adapter'

STDOUT.sync = true

def start_tunnel
  puts "[sauce-tunnel] Starting tunnel"
  @tunnel = SaucelabsAdapter::SauceTunnel.new(@selenium_config)
end

def stop_tunnel
  puts "[sauce-tunnel] Shutting down tunnel"
  @tunnel.shutdown
  exit 0
end

Signal.trap('INT') { stop_tunnel }
Signal.trap('TERM') { stop_tunnel }
Signal.trap('HUP') { stop_tunnel }

@selenium_config = SaucelabsAdapter::SeleniumConfig.new('saucelabs', '../selenium.yml')

start_tunnel
sleep 3600
stop_tunnel

exit 1
