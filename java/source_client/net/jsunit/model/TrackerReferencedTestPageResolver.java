package net.jsunit.model;

import java.io.File;

public class TrackerReferencedTestPageResolver implements ReferencedTestPageResolver {
    public File resolve(File jsUnitDirectory, String declaredTestPage) {
        return new File(jsUnitDirectory, declaredTestPage.split("\\+")[1].trim());
    }
}
