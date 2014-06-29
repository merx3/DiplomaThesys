package tu.mmarinov.agileassist.ui.model;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import tu.mmarinov.agileassist.internal.AgileTemplate;

public class ListViewerHelpers {
	static class TemplateNameLabelProvider implements ILabelProvider{
		@Override
		public void addListener(ILabelProviderListener arg0) {}
		@Override
		public void dispose() {}
		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}
		@Override
		public void removeListener(ILabelProviderListener arg0) {}
		@Override
		public Image getImage(Object arg0) {
			return null;
		}
		@Override
		public String getText(Object arg0) {
			return ((AgileTemplate)arg0).getName();
		}
	}
	
	static class TemplateNameContentProvider implements IStructuredContentProvider{		
		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}			
		@Override
		public void dispose() {}
		@Override
		public Object[] getElements(Object arg0) {
			return ((ArrayList<AgileTemplate>)arg0).toArray();
		}
	}
	
	public static ILabelProvider getLabelProvider(){
		return new TemplateNameLabelProvider();
	}
	
	public static IStructuredContentProvider getContentProvider(){
		return new TemplateNameContentProvider();
	}
}
