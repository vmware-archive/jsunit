package net.jsunit.model;

import java.io.File;

public interface ReferencedTestPageResolver {
    File resolve(File suiteDirectory, String declaredTestPage);
}
