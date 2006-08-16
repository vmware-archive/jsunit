package net.jsunit.action;

import net.jsunit.BrowserTestRunnerStub;
import net.jsunit.model.Browser;

import java.util.Arrays;
import java.util.List;

public class BlockingTestRunner extends BrowserTestRunnerStub {
    public boolean blocked;

    public void startTestRun() {
        blocked = true;
        while (blocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    public List<Browser> getBrowsers() {
        return Arrays.asList(new Browser[]{new Browser("browser.exe", 0)});
    }

}
