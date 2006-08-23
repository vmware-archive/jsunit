package net.jsunit.model;

import java.io.File;

public class DefaultReferencedTestPageResolver implements ReferencedTestPageResolver {
    public File resolve(File jsUnitDirectory, String declaredTestPage) {
        return new File(jsUnitDirectory, declaredTestPage);
    }
}
