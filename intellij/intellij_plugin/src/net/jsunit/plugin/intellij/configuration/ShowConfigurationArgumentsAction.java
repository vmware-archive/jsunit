package net.jsunit.plugin.intellij.configuration;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;

public class ShowConfigurationArgumentsAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Application application = ApplicationManager.getApplication();
        ConfigurationComponent component = application.getComponent(ConfigurationComponent.class);
        ConfigurationSource source = component.asConfigurationSource();
        Configuration configuration = new Configuration(source);
        String[] arguments = configuration.asArgumentsArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <arguments.length; i++) {
            buffer.append(arguments[i++]);
            buffer.append("=");
            buffer.append(arguments[i]);
            buffer.append("\n");
        }
        showDialog(buffer.toString());
    }

    public void showDialog(String message) {
        Messages.showMessageDialog(
                message,
                "JsUnitConfiguration",
                Messages.getInformationIcon()
        );
    }

}
