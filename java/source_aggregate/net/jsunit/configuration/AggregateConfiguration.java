package net.jsunit.configuration;

public class AggregateConfiguration extends ServerConfiguration {

    public AggregateConfiguration(ConfigurationSource source) {
        super(source);
    }

    public ServerType getServerType() {
        return ServerType.AGGREGATE;
    }
}
