package net.jsunit.client;

import net.jsunit.model.BrowserSpecification;

class NoSuchPlatformBrowserException extends Exception {
    public NoSuchPlatformBrowserException(BrowserSpecification browserSpec) {
        super("No such platform/browser available: " + browserSpec.displayString());
    }
}
