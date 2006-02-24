package net.jsunit.plugin.intellij.configuration;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import net.jsunit.configuration.ConfigurationSource;
import org.jdom.Element;

import javax.swing.*;

public class ConfigurationComponent implements ApplicationComponent, Configurable, JDOMExternalizable {
    public String installationDirectory = "";
    public String logsDirectory = "";
    public String[] browserFileNames = new String[]{};
    public String testPageExtensions = "html,htm";
    public boolean closeBrowserAfterTestRuns = true;
    public boolean logStatusToConsole = false;
    public Integer timeoutSeconds = 60;
    private ConfigurationForm form;

    public String getComponentName() {
        return "JsUnitConfigurationComponent";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public String getInstallationDirectory() {
        return installationDirectory;
    }

    public void setInstallationDirectory(final String installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

    public String getLogsDirectory() {
        return logsDirectory;
    }

    public void setLogsDirectory(final String logsDirectory) {
        this.logsDirectory = logsDirectory;
    }

    public String[] getBrowserFileNames() {
        return browserFileNames;
    }

    public void setBrowserFileNames(final Object[] names) {
        this.browserFileNames = new String[names.length];
        for (int i = 0; i < names.length; i++)
            this.browserFileNames[i] = names[i].toString();
    }

    public String getTestPageExtensions() {
        return testPageExtensions;
    }

    public void setTestPageExtensions(final String testPageExtensions) {
        this.testPageExtensions = testPageExtensions;
    }

    public boolean isCloseBrowserAfterTestRuns() {
        return closeBrowserAfterTestRuns;
    }

    public void setCloseBrowserAfterTestRuns(final boolean closeBrowserAfterTestRuns) {
        this.closeBrowserAfterTestRuns = closeBrowserAfterTestRuns;
    }

    public boolean isLogStatusToConsole() {
        return logStatusToConsole;
    }

    public void setLogStatusToConsole(final boolean logStatusToConsole) {
        this.logStatusToConsole = logStatusToConsole;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(final int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public ConfigurationSource asConfigurationSource() {
        return new ConfigurationComponentSource(this);
    }

    public JComponent createComponent() {
        if (form == null) {
            form = new ConfigurationForm();
        }
        return form.getRootComponent();
    }

    public boolean isModified() {
        return form != null && form.isModified(this);
    }

    public void apply() throws ConfigurationException {
        if (form != null) {
            form.getData(this);
        }
    }

    public void reset() {
        if (form != null) {
            form.setData(this);
        }
    }

    public void disposeUIResources() {
        form = null;
    }

    public String getDisplayName() {
        return "JsUnit";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }
}