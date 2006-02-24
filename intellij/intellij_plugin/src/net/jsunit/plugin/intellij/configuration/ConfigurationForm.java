package net.jsunit.plugin.intellij.configuration;

import javax.swing.*;

public class ConfigurationForm {
    private JPanel rootComponent;
    private JRadioButton defaultLogsDirectory;
    private JCheckBox logStatusToConsole;
    private JCheckBox closeBrowserAfterTestRuns;
    private JTextField testPageExtensions;
    private JTextField logsDirectory;
    private JRadioButton customLogsDirectory;
    private JFormattedTextField installationDirectory;
    private JButton installationDirectoryButton;
    private JButton customLogsDirectoryButton;
    private JList browserFileNames;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JLabel header;
    private JSpinner timeoutSeconds;
    private JTree tree1;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JTable table1;
    private JRadioButton radioButton1;

    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void setData(ConfigurationComponent data) {
        installationDirectory.setText(data.getInstallationDirectory());
        logsDirectory.setText(data.getLogsDirectory());
        browserFileNames.setListData(data.getBrowserFileNames());
        testPageExtensions.setText(data.getTestPageExtensions());
        closeBrowserAfterTestRuns.setSelected(data.isCloseBrowserAfterTestRuns());
        logStatusToConsole.setSelected(data.isLogStatusToConsole());
        timeoutSeconds.setValue(data.getTimeoutSeconds());
    }

    public void getData(ConfigurationComponent data) {
        data.setInstallationDirectory(installationDirectory.getText());
        data.setLogsDirectory(logsDirectory.getText());
        data.setBrowserFileNames(browserFileNames.getSelectedValues());
        data.setTestPageExtensions(testPageExtensions.getText());
        data.setCloseBrowserAfterTestRuns(closeBrowserAfterTestRuns.isSelected());
        data.setLogStatusToConsole(logStatusToConsole.isSelected());
        data.setTimeoutSeconds((Integer) timeoutSeconds.getValue());
    }

    public boolean isModified(ConfigurationComponent data) {
        if (installationDirectory.getText() != null ? !installationDirectory.getText().equals(data.getInstallationDirectory()) : data.getInstallationDirectory() != null)
            return true;
        if (logsDirectory != null ? !logsDirectory.equals(data.getLogsDirectory()) : data.getLogsDirectory() != null)
            return true;
        if (browserFileNames.getSelectedValues() != null ? !browserFileNames.getSelectedValues().equals(data.getBrowserFileNames()) : data.getBrowserFileNames() != null)
            return true;
        if (testPageExtensions.getText() != null ? !testPageExtensions.getText().equals(data.getTestPageExtensions()) : data.getTestPageExtensions() != null)
            return true;
        if (closeBrowserAfterTestRuns.isSelected() != data.isCloseBrowserAfterTestRuns()) return true;
        if (logStatusToConsole.isSelected() != data.isLogStatusToConsole()) return true;
        return timeoutSeconds.getValue() != null ? !timeoutSeconds.getValue().equals(data.getTimeoutSeconds()) : data.getTimeoutSeconds() != 0;
    }
}
