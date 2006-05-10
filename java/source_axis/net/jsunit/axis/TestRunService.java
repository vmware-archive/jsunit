package net.jsunit.axis;

import net.jsunit.client.TestPage;
import net.jsunit.model.ReferencedJsFile;
import net.jsunit.model.Result;

public interface TestRunService {

    public Result runTests(TestPage testPage, ReferencedJsFile[] referencedJsFiles);

}
