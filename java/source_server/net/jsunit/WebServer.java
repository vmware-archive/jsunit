package net.jsunit;

public interface WebServer {
    boolean isAlive();
    void start() throws Exception;
    void dispose();
}
