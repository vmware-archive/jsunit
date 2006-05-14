package net.jsunit.client;

class NoSuchPlatformBrowserException extends Exception {
    public NoSuchPlatformBrowserException() {
        super("No such platform/browser available");
    }
}
