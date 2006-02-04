package net.jsunit.plugin.eclipse.resultsui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class JsUnitProgressBar extends Canvas {
	
	private static final int DEFAULT_WIDTH = 160;
	private static final int DEFAULT_HEIGHT = 18;

	private int currentTickCount= 0;
	private int maxTickCount= 0;	
	private int fColorBarWidth= 0;
	private Color okColor;
	private Color problemColor;
	private Color stoppedColor;
	private boolean hasProblems;
	private boolean stopped= false;
	
	public JsUnitProgressBar(Composite parent) {
		super(parent, SWT.NONE);
		
		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				fColorBarWidth= scale(currentTickCount);
				redraw();
			}
		});	
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paint(e);
			}
		});
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				problemColor.dispose();
				okColor.dispose();
				stoppedColor.dispose();
			}
		});
		Display display= parent.getDisplay();
		problemColor= new Color(display, 159, 63, 63);
		okColor= new Color(display, 95, 191, 95);
		stoppedColor= new Color(display, 120, 120, 120);
	}

	public void setMaximum(int max) {
		maxTickCount= max;
	}
		
	public void reset() {
		hasProblems= false;
		stopped= false;
		currentTickCount= 0;
		fColorBarWidth= 0;
		maxTickCount= 0;
		redraw();
	}
	
	private void paintStep(int startX, int endX) {
		GC gc = new GC(this);	
		setStatusColor(gc);
		Rectangle rect= getClientArea();
		startX= Math.max(1, startX);
		gc.fillRectangle(startX, 1, endX-startX, rect.height-2);
		gc.dispose();		
	}

	private void setStatusColor(GC gc) {
		if (stopped)
			gc.setBackground(stoppedColor);
		else if (hasProblems)
			gc.setBackground(problemColor);
		else if (stopped)
			gc.setBackground(stoppedColor);
		else
			gc.setBackground(okColor);
	}

	public void stopped() {
		stopped = true;
		redraw();
	}
	
	private int scale(int value) {
		if (maxTickCount > 0) {
			Rectangle r= getClientArea();
			if (r.width != 0)
				return Math.max(0, value*(r.width-2)/maxTickCount);
		}
		return value; 
	}
	
	private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
		gc.setForeground(topleft);
		gc.drawLine(x, y, x+w-1, y);
		gc.drawLine(x, y, x, y+h-1);
		
		gc.setForeground(bottomright);
		gc.drawLine(x+w, y, x+w, y+h);
		gc.drawLine(x, y+h, x+w, y+h);
	}
	
	private void paint(PaintEvent event) {
		GC gc = event.gc;
		Display disp= getDisplay();
			
		Rectangle rect= getClientArea();
		gc.fillRectangle(rect);
		drawBevelRect(gc, rect.x, rect.y, rect.width-1, rect.height-1,
			disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
			disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		setStatusColor(gc);
		fColorBarWidth= Math.min(rect.width-2, fColorBarWidth);
		gc.fillRectangle(1, 1, fColorBarWidth, rect.height-2);
	}	
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point size= new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		if (wHint != SWT.DEFAULT) size.x= wHint;
		if (hHint != SWT.DEFAULT) size.y= hHint;
		return size;
	}
	
	public void step(int problemCount) {
		currentTickCount++;
		int x = fColorBarWidth;

		fColorBarWidth= scale(currentTickCount);

		if (!hasProblems && problemCount > 0) {
			hasProblems= true;
		}
		if (hasProblems)
			x = 1;
		if (currentTickCount == maxTickCount)
			fColorBarWidth= getClientArea().width-1;
		paintStep(x, fColorBarWidth);
	}
	
	public void setError() {
		hasProblems = true;
	}

}
