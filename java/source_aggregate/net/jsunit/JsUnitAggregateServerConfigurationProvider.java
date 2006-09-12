package net.jsunit;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationProvider;
import com.opensymphony.xwork.config.entities.*;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork.interceptor.ParametersInterceptor;
import net.jsunit.action.AggregateConfigurationAction;
import net.jsunit.action.DistributedTestRunnerAction;
import net.jsunit.action.XmlResult;
import net.jsunit.interceptor.AggregateServerInterceptor;
import net.jsunit.interceptor.BrowserTestRunnerInterceptor;
import net.jsunit.interceptor.RemoteServerHitterInterceptor;
import net.jsunit.interceptor.RequestSourceInterceptor;

import java.util.HashMap;

public class JsUnitAggregateServerConfigurationProvider implements ConfigurationProvider {

    public JsUnitAggregateServerConfigurationProvider() {
        super();
    }

    public void init(Configuration configuration) {
        new XmlConfigurationProvider("webwork-default.xml").init(configuration);
        PackageConfig packageConfig = new PackageConfig();
        addInterceptorConfigsTo(packageConfig);
        addActionConfigsTo(packageConfig);
        configuration.addPackageConfig("default", packageConfig);
    }

    private void addInterceptorConfigsTo(PackageConfig packageConfig) {
        packageConfig.addInterceptorConfig(new InterceptorConfig("aggregateServer", AggregateServerInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("requestSource", RequestSourceInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("remoteRunnerHitter", BrowserTestRunnerInterceptor.class, new HashMap()));
    }

    private void addActionConfigsTo(PackageConfig packageConfig) {
        packageConfig.addActionConfig("config", configActionConfig());
        packageConfig.addActionConfig("runner", runnerActionConfig());
    }

    private ActionConfig runnerActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(DistributedTestRunnerAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class));
        result.addResultConfig(new ResultConfig("error", XmlResult.class));
        result.addInterceptor(new InterceptorMapping("aggregateServer", new AggregateServerInterceptor()));
        result.addInterceptor(new InterceptorMapping("params", new ParametersInterceptor()));
        result.addInterceptor(new InterceptorMapping("requestSource", new RequestSourceInterceptor()));
        result.addInterceptor(new InterceptorMapping("remoteServerHitter", new RemoteServerHitterInterceptor()));
        return result;
    }

    private ActionConfig newActionConfig() {
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setPackageName("net.jsunit.action");
        return actionConfig;
    }

    private ActionConfig configActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(AggregateConfigurationAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class));
        result.addInterceptor(new InterceptorMapping("aggregateServer", new AggregateServerInterceptor()));
        return result;
    }

    public boolean needsReload() {
        return true;
    }

    public void destroy() {
    }
}
