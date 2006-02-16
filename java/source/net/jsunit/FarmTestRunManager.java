package net.jsunit;

import java.net.URL;

import org.jdom.Document;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;

public class FarmTestRunManager {

	private final RemoteRunnerHitter hitter;
	private final Configuration configuration;
	private TestRunResult result = new TestRunResult();

	public FarmTestRunManager(Configuration configuration) {
		this(new RemoteMachineRunnerHitter(), configuration);
	}
	
	public FarmTestRunManager(RemoteRunnerHitter hitter, Configuration configuration) {
		this.hitter = hitter;
		this.configuration = configuration;
	}

	public void runTests() {
		TestRunResultBuilder builder = new TestRunResultBuilder();
		for (URL url : configuration.getRemoteMachineURLs()) {
			Document documentFromRemoteMachine = hitter.hitRemoteRunner(url);
			TestRunResult resultFromRemoteMachine = builder.build(documentFromRemoteMachine);
			result.mergeWith(resultFromRemoteMachine);
		}
	}

	public TestRunResult getTestRunResult() {
		return result;
	}

}
