package tu.mmarinov.agileassist.ui.model;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import tu.mmarinov.agileassist.ui.model.ListViewerHelpers.TemplateNameContentProvider;
import tu.mmarinov.agileassist.ui.model.ListViewerHelpers.TemplateNameLabelProvider;

public class TableViewerHelpers {
	static class TableViewerLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			SimpleEntry<String, Integer> e = (SimpleEntry<String, Integer>)element;
			switch (columnIndex) {
			case 0:
				return e.getValue().toString();
			case 1:
				return e.getKey();
			default:
				return "";
			}
			
		}
	}
	
	static class TableViewerContentProvider implements IStructuredContentProvider {        
        public Object[] getElements(Object inputElement) {
        	String[] defaults = (String[]) inputElement;
			ArrayList<SimpleEntry<String, Integer>> models =new ArrayList<SimpleEntry<String, Integer>>();
			for (int i = 0; i < defaults.length; i++) {
				String string = defaults[i];
				models.add(new SimpleEntry<String, Integer>(string, i));				
			}
            return models.toArray(new SimpleEntry[0]);
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	public static ILabelProvider getLabelProvider(){
		return new TableViewerLabelProvider();
	}
	
	public static IStructuredContentProvider getContentProvider(){
		return new TableViewerContentProvider();
	}	
}
