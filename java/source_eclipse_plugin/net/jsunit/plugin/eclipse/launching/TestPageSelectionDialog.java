package net.jsunit.plugin.eclipse.launching;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

public class TestPageSelectionDialog extends TwoPaneElementSelector {

	private IProject project;
	private Set<IResource> allTestPagesInProject;
	
	public TestPageSelectionDialog(Shell parent, IProject project) {
		super(parent, new TestPageLabelProvider(), new TestPageLabelProvider());
		this.project = project;
	}
	
	static class TestPageLabelProvider extends LabelProvider {
	    public String getText(Object element) {
	    	if (element instanceof IResource) {
	    		return ((IResource)element).getProjectRelativePath().toString();
	    	}
	        return super.getText(element);
	    }
	}
	
	
	public int open() {
		if (allTestPagesInProject== null) {
			try {
				allTestPagesInProject = findTestPages();
			} catch (InvocationTargetException e) {
				return CANCEL;
			} catch (InterruptedException e) {
				return CANCEL;
			}
		}
		setElements(allTestPagesInProject.toArray(new IFile[allTestPagesInProject.size()]));
		return super.open();
	}


	private Set<IResource> findTestPages() throws InvocationTargetException, InterruptedException {
		final Set<IResource> result= new HashSet<IResource>();		
		IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor pm) throws InterruptedException {
				StringBuffer descriptionBuffer = new StringBuffer();
				descriptionBuffer.append("Searching for Test Pages in project ");
				descriptionBuffer.append(project.getName());
				pm.beginTask(descriptionBuffer.toString(), 1);
				try {
					final List<String> validExtensions = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore().testPageExtensions();
					project.accept(new IResourceVisitor() {
						public boolean visit(IResource resource) throws CoreException {
							if (resource.getType() == IResource.FILE) {
								String fileExtension = resource.getFileExtension();
								if (fileExtension!= null) {
									fileExtension = fileExtension.toLowerCase();
									if (validExtensions.contains(fileExtension))
										result.add(resource);
								}
							}
							return true;
						}
					});
				} catch (CoreException e) {
				}
				pm.done();
			}
		};
		PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);			
		return result;
	}

}