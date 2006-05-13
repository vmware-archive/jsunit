package net.jsunit;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Invalid username/password combination");
    }
}
