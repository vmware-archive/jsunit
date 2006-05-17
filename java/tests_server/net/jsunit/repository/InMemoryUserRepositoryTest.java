package net.jsunit.repository;

public class InMemoryUserRepositoryTest extends UserRepositoryTestCase {
    protected UserRepository repository() {
        return new InMemoryUserRepository();
    }
}
