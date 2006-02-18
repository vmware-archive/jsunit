package net.jsunit;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;

import org.jdom.Document;

public class FarmTestRunManager {

	private final RemoteRunnerHitter hitter;
	private final List<URL> remoteMachineURLs;
	private TestRunResult result = new TestRunResult();

	public FarmTestRunManager(List<URL> remoteMachineURLs) {
		this(new RemoteMachineRunnerHitter(), remoteMachineURLs);
	}
	
	public FarmTestRunManager(RemoteRunnerHitter hitter, List<URL> remoteMachineURLs) {
		this.hitter = hitter;
		this.remoteMachineURLs = remoteMachineURLs;
	}

	public void runTests() {
		TestRunResultBuilder builder = new TestRunResultBuilder();
		for (URL url : remoteMachineURLs) {
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
