package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CounterPanel extends Composite {
	protected Text errorCount;
	protected Text failureCount;
	protected Text browserRunCount;
	protected Text testRunCount;
	protected int totalBrowserCount;
	private int browserRunCountValue;
	
	private final Image testErrorIcon = JsUnitPlugin.soleInstance().createImage("testerr.gif");
	private final Image testFailureIcon = JsUnitPlugin.soleInstance().createImage("testfail.gif");
	private final Image testRunsIcon = JsUnitPlugin.soleInstance().createImage("test.gif");
	private final Image browserRunsIcon = JsUnitPlugin.soleInstance().createImage("tsuite.gif");
			
	public CounterPanel(Composite parent) {
		super(parent, SWT.WRAP);
		GridLayout gridLayout= new GridLayout();
		gridLayout.numColumns= 12;
		gridLayout.makeColumnsEqualWidth= false;
		gridLayout.marginWidth= 0;
		setLayout(gridLayout);
		
		browserRunCount= createLabel("Browser runs:", browserRunsIcon, "0");
		testRunCount= createLabel("Test runs:", testRunsIcon, "0");
		errorCount= createLabel("Errors:", testErrorIcon, "0");
		failureCount= createLabel("Failures:", testFailureIcon, "0");

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}
		});
	}
 
	private void disposeIcons() {
		testErrorIcon.dispose();
		testFailureIcon.dispose();
	}

	private Text createLabel(String name, Image image, String init) {
		Label label= new Label(this, SWT.NONE);
		if (image != null) {
			image.setBackground(label.getBackground());
			label.setImage(image);
		}
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		label= new Label(this, SWT.NONE);
		label.setText(name);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		Text value= new Text(this, SWT.READ_ONLY);
		value.setText(init); 
		value.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
		return value;
	}

	public void reset() {
		setTestErrorCount(0);
		setTestFailureCount(0);
		setBrowserRunCount(0);
		setTestRunCount(0);
		totalBrowserCount = 0;
	}
	
	public void setTotalBrowserCount(int value) {
		totalBrowserCount = value;
		redrawBrowserCount();
	}
	
	public void setBrowserRunCount(int value) {
		this.browserRunCountValue = value;
		redrawBrowserCount();
	}

	private void redrawBrowserCount() {
		String runString= Integer.toString(browserRunCountValue) + "/" + Integer.toString(totalBrowserCount); 
		browserRunCount.setText(runString);

		browserRunCount.redraw();
		redraw();
	}
	
	public void setTestErrorCount(int value) {
		errorCount.setText(Integer.toString(value));
		redraw();
	}
	
	public void setTestFailureCount(int value) {
		failureCount.setText(Integer.toString(value));
		redraw();
	}
	
	public void setTestRunCount(int value) {
		testRunCount.setText(Integer.toString(value));
		redraw();
	}
	
}