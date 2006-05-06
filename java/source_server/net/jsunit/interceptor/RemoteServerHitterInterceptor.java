package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.RemoteMachineServerHitter;
import net.jsunit.RemoteServerHitter;
import net.jsunit.action.RemoteRunnerHitterAware;

public class RemoteServerHitterInterceptor extends JsUnitInterceptor {

    public interface RemoteServerHitterFactory {
        public RemoteServerHitter create();
    }

    private static class DefaultRemoteServerHitterFactory implements RemoteServerHitterFactory {

        public RemoteServerHitter create() {
            return new RemoteMachineServerHitter();
        }
    }

    public static RemoteServerHitterFactory factory = new DefaultRemoteServerHitterFactory();

    public static void resetFactory() {
        factory = new DefaultRemoteServerHitterFactory();
    }

    protected void execute(Action targetAction) throws Exception {
        RemoteRunnerHitterAware aware = ((RemoteRunnerHitterAware) targetAction);
        aware.setRemoteServerHitter(factory.create());
    }

}
