package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.Result;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerStressTest extends TestCase {

    //average all machines 6.12
    //average maverick: 3.07
    //average iceman: 5.71
    //average slider: 6.17

    public void testStressServer() throws Exception {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++)
            threads.add(new Runner(i));
        for (Thread thread : threads) {
            thread.start();
            Thread.sleep(250);
        }
        for (Thread thread : threads)
            thread.join();
    }

    static class Runner extends Thread {
        private int id;

        public Runner(int id) {
            this.id = id;
        }

        public void run() {
            TestRunServiceClient client = new TestRunServiceClient(
                    "http://services.jsunit.net/services/TestRunService", "admin@jsunit.net", "king olaf the hairy"
            );
            long startTime = System.currentTimeMillis();
            int count = 50;
            int delay = 1000;
            for (int i = 0; i < count; i++) {
                System.out.println("Thread " + id + ": Starting run " + i);
                Result result = null;
                try {
                    result = client.send(FileUtility.jsUnitPath(), new File("tests", "jsUnitUtilityTests.html"));
                    Thread.sleep(delay);
                } catch (Exception e) {
                    fail(e.getMessage());
                }
                System.out.println("Thread " + id + ": Finished run " + i);
                if (!result.wasSuccessful())
                    fail(XmlUtility.asPrettyString(result.asXml()));
            }
            System.out.println("Average time: " + ((System.currentTimeMillis() - startTime - (count * delay)) / count / 1000d));
        }
    }

}
