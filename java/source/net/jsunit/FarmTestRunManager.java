package net.jsunit;

import java.io.IOException;
import java.net.URL;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;

import org.jdom.Document;

public class FarmTestRunManager {

	private RemoteRunnerHitter hitter;
	private Configuration configuration;
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
			Document documentFromRemoteMachine;
			try {
				documentFromRemoteMachine = hitter.hitRemoteRunner(url);
				TestRunResult resultFromRemoteMachine = builder.build(documentFromRemoteMachine);
				result.mergeWith(resultFromRemoteMachine);
			} catch (IOException e) {
				result.addTimedOutRemoteURL(url);
			}
		}
	}

	public TestRunResult getTestRunResult() {
		return result;
	}

}
