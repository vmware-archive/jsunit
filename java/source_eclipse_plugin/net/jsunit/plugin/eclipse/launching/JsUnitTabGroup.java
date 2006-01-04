package net.jsunit.plugin.eclipse.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class JsUnitTabGroup extends AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {		
		ILaunchConfigurationTab[] tabs= new ILaunchConfigurationTab[] {
			new JsUnitTab(),
			new CommonTab()
		};
		setTabs(tabs);
	}
}
