#!/usr/bin/env ruby

require 'rubygems'
require 'saucelabs-adapter'

STDOUT.sync = true

case ARGV.size
when 1:
  @tunnel_id = ARGV[0]
else
  puts "Usage: sauce-tunnel-delete.rb <tunnel_id>"
  exit 1
end

def connect_to_rest_api
  sauce_api_url = "https://#{@selenium_config.saucelabs_username}:#{@selenium_config.saucelabs_access_key}@saucelabs.com/rest/#{@selenium_config.saucelabs_username}/"
  @sauce_api_endpoint = SauceREST::Client.new sauce_api_url
end

puts "[sauce-tunnel] Stopping tunnel #{@tunnel_id}"
@selenium_config = SaucelabsAdapter::SeleniumConfig.new('saucelabs', '../selenium.yml')
connect_to_rest_api
@sauce_api_endpoint.delete :tunnel, @tunnel_id

exit 0


