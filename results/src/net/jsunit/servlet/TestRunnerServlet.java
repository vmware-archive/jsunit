package net.jsunit.servlet;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestResult;
import junit.textui.TestRunner;
import net.jsunit.StandaloneTest;
import net.jsunit.Utility;
public class TestRunnerServlet extends HttpServlet {
	protected synchronized void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utility.log("Received request to run standalone test...");
		StandaloneTest test = new StandaloneTest("testStandaloneRun");
		test.setStartAndStopServer(false);
		TestResult result = TestRunner.run(test);
		response.setContentType("text/xml");
		OutputStream out = response.getOutputStream();
		String resultString = result.wasSuccessful() ? "success" : "failure";
		out.write(("<result>" + resultString + "</result>").getBytes());
		out.close();
		Utility.log("...Done");
	}
}
