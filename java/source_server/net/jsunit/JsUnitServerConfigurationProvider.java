package net.jsunit;

import com.opensymphony.webwork.dispatcher.HttpHeaderResult;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.entities.*;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork.interceptor.ParametersInterceptor;
import net.jsunit.action.*;
import net.jsunit.interceptor.*;

import java.util.HashMap;
import java.util.Map;

public class JsUnitServerConfigurationProvider extends XmlConfigurationProvider {

    public JsUnitServerConfigurationProvider() {
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
        packageConfig.addInterceptorConfig(new InterceptorConfig("server", ServerInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("requestSource", RequestSourceInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("browserTestRunner", BrowserTestRunnerInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("browserResult", BrowserResultInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("localhostOnly", LocalhostOnlyInterceptor.class, new HashMap()));
        packageConfig.addInterceptorConfig(new InterceptorConfig("browserSelection", BrowserSelectionInterceptor.class, new HashMap()));
    }

    private void addActionConfigsTo(PackageConfig packageConfig) {
        packageConfig.addActionConfig("acceptor", acceptorActionConfig());
        packageConfig.addActionConfig("config", configActionConfig());
        packageConfig.addActionConfig("displayer", displayerActionConfig());
        packageConfig.addActionConfig("runner", runnerActionConfig());
    }

    private ActionConfig acceptorActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(ResultAcceptorAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class.getName()));
        Map<String, String> headerParamsMap = new HashMap<String, String>();
        headerParamsMap.put("status", "403");
        result.addResultConfig(new ResultConfig(LocalhostOnlyInterceptor.DENIED_NOT_LOCALHOST, HttpHeaderResult.class.getName(), headerParamsMap));
        result.addInterceptor(new InterceptorMapping("localhostOnly", new LocalhostOnlyInterceptor()));
        result.addInterceptor(new InterceptorMapping("browserTestRunner", new BrowserTestRunnerInterceptor()));
        result.addInterceptor(new InterceptorMapping("browserResult", new BrowserResultInterceptor()));
        return result;
    }

    private ActionConfig runnerActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(TestRunnerAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class.getName()));
        result.addResultConfig(new ResultConfig("error", XmlResult.class.getName()));
        result.addInterceptor(new InterceptorMapping("browserTestRunner", new BrowserTestRunnerInterceptor()));
        result.addInterceptor(new InterceptorMapping("params", new ParametersInterceptor()));
        result.addInterceptor(new InterceptorMapping("requestSource", new RequestSourceInterceptor()));
        result.addInterceptor(new InterceptorMapping("browserSelection", new BrowserSelectionInterceptor()));
        return result;
    }

    private ActionConfig displayerActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(ResultDisplayerAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class.getName()));
        result.addResultConfig(new ResultConfig("error", XmlResult.class.getName()));
        result.addInterceptor(new InterceptorMapping("browserTestRunner", new BrowserTestRunnerInterceptor()));
        result.addInterceptor(new InterceptorMapping("params", new ParametersInterceptor()));
        return result;
    }

    private ActionConfig configActionConfig() {
        ActionConfig result = newActionConfig();
        result.setClassName(ConfigurationAction.class.getName());
        result.addResultConfig(new ResultConfig(Action.SUCCESS, XmlResult.class.getName()));
        result.addInterceptor(new InterceptorMapping("server", new ServerInterceptor()));
        return result;
    }

    public boolean needsReload() {
        return true;
    }

    private ActionConfig newActionConfig() {
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setPackageName("net.jsunit.action");
        return actionConfig;
    }

    public void destroy() {
    }

}
