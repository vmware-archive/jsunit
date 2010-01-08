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
  sauce_api_url = "https://#{@selenium_config['username']}:#{@selenium_config['access-key']}@saucelabs.com/rest/#{@selenium_config['username']}/"
  # puts "[saucelabs-adapter] Connecting to Sauce API at #{sauce_api_url}"
  @sauce_api_endpoint = SauceREST::Client.new sauce_api_url
end

@selenium_config = SeleniumConfig.new('saucelabs', '../selenium.yml', 8080)
connect_to_rest_api
@sauce_api_endpoint.delete :tunnel, @tunnel_id

exit 0


