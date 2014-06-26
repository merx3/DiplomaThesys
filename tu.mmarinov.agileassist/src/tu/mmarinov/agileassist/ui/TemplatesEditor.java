package tu.mmarinov.agileassist.ui;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.prefs.TemplatePreferencesEditor;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class TemplatesEditor extends Dialog {
	private Text txtTemplateDesc;
	private Composite cmpMain;
	private Composite cmpEdit;
	private StackLayout layout;
	private Text txtEditTemplate;
	private Combo cbInsertTemplSegment; 
	private Composite container;
	
	private AgileTemplate selectedTemplate;
	private Text txtPropName;
	private Text txtPropDesc;
	private Text txtEditName;
	private Text txtEditPropName;
	private Text txtEditInfo;
	private Table table;
	private TableViewer tableViewer;
	private Text txtEditDefaultValue;
	private Combo cbEditDefaultsCount;
	private String[] updateDefaultValues;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TemplatesEditor(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		layout = new StackLayout(); 
		container.setLayout(layout);
		
		cmpMain = new Composite(container, SWT.NONE);
		//cmpMain.setLayout(new RowLayout());
		
		Button btnNewButton = new Button(cmpMain, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedTemplate != null) {
					layout.topControl = cmpEdit;
					txtEditTemplate.setText(selectedTemplate.getDescription());
					txtEditName.setText(selectedTemplate.getName());
					txtEditPropName.setText(selectedTemplate.getProposalName());
					txtEditInfo.setText(selectedTemplate.getProposalDescription());
					int defaultValuesCount = selectedTemplate.getDefaultValues().length;
					if (defaultValuesCount > 0) {
						String[] defaultsNumber = new String[defaultValuesCount];
						for (int i = 0; i < defaultValuesCount; i++) {
							defaultsNumber[i] = i + "";
						}
						cbEditDefaultsCount.setItems(defaultsNumber);
						cbEditDefaultsCount.select(0);	
						txtEditDefaultValue.setText(selectedTemplate.getDefaultValues()[0]);
					}
					cbInsertTemplSegment.setItems(getTemplateNames());
					String[] def = selectedTemplate.getDefaultValues();
					updateDefaultValues = new String[def.length];
					for (int i = 0; i < updateDefaultValues.length; i++) {
						updateDefaultValues[i] = def[i];
					}
					container.layout();	
				}
			}
		});
		btnNewButton.setBounds(357, 352, 90, 30);
		btnNewButton.setText("Edit");
		//composite.setVisible(false);
				
		Button btnNewButton_1 = new Button(cmpMain, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedTemplate != null) {
					String templateName = selectedTemplate.getName();
					int dialogButton = JOptionPane.YES_NO_OPTION;
	                JOptionPane.showConfirmDialog (null, "Are you sure you want to delete template '" + templateName + "'?","Warning",dialogButton);
	                
					TemplatePreferencesEditor.deleteTemplateFromPreferences(templateName);
					TemplatePreferencesEditor.savePreferences();
					ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.loadTemplatesFromPreferences();
					TemplateLoader.setTemplates(templates);					
				}
			}
		});
		btnNewButton_1.setBounds(469, 352, 90, 30);
		btnNewButton_1.setText("Delete");
		
		Button btnCreateNewTemplate = new Button(cmpMain, SWT.NONE);
		btnCreateNewTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = cmpEdit;
				cbInsertTemplSegment.setItems(getTemplateNames());
				container.layout();	
			}
		});
		btnCreateNewTemplate.setBounds(29, 352, 109, 30);
		btnCreateNewTemplate.setText("New Template");
		
		Label lblTemplatePreview = new Label(cmpMain, SWT.NONE);
		lblTemplatePreview.setBounds(210, 14, 138, 20);
		lblTemplatePreview.setText("Template Preview:");
		
		txtTemplateDesc = new Text(cmpMain, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtTemplateDesc.setText("");
		txtTemplateDesc.setBounds(210, 40, 237, 230);
		
		Label lblTemplateProposal = new Label(cmpMain, SWT.NONE);
		lblTemplateProposal.setBounds(210, 286, 129, 20);
		lblTemplateProposal.setText("Template proposal:");
		
		txtPropName = new Text(cmpMain, SWT.BORDER | SWT.READ_ONLY);
		txtPropName.setBounds(210, 312, 129, 26);
		txtPropName.setText("");
		
		Label lblTemplateDescription = new Label(cmpMain, SWT.NONE);
		lblTemplateDescription.setBounds(357, 286, 144, 20);
		lblTemplateDescription.setText("Template info:");
		
		txtPropDesc = new Text(cmpMain, SWT.BORDER | SWT.READ_ONLY);
		txtPropDesc.setBounds(357, 312, 280, 26);
		txtPropDesc.setText("");
		
		ListViewer listViewer = new ListViewer(cmpMain, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		listViewer.setLabelProvider(new ILabelProvider () {
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
		});
		listViewer.setContentProvider(new IStructuredContentProvider () {			
			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}			
			@Override
			public void dispose() {}
			@Override
			public Object[] getElements(Object arg0) {
				return ((ArrayList<AgileTemplate>)arg0).toArray();
			}
		});
		listViewer.setInput(TemplateLoader.getTemplates());
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection)arg0.getSelection();
				if (selection.size()==1) {
					selectedTemplate = (AgileTemplate)selection.getFirstElement();
					//String templateName = selectedTemplate.getName();
					//System.out.println("SELECTED TEMPLATE:" + templateName);					
					//AgileTemplate t = TemplateLoader.getTemplate(templateName);					
					tableViewer.setInput(selectedTemplate.getDefaultValues());	
					txtTemplateDesc.setText(selectedTemplate.getDescription());
					txtPropName.setText(selectedTemplate.getProposalName());
					txtPropDesc.setText(selectedTemplate.getProposalDescription());
				}
				
			}
		});
		List list_1 = listViewer.getList();
		list_1.setBounds(29, 40, 138, 292);
	
		
		Label lblTemplates = new Label(cmpMain, SWT.NONE);
		lblTemplates.setText("Templates:");
		lblTemplates.setBounds(29, 14, 138, 20);
		
		cmpEdit = new Composite(container, SWT.NONE);
		cmpEdit.setVisible(false);
		
		txtEditTemplate = new Text(cmpEdit, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		txtEditTemplate.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String str = txtEditTemplate.getText();
				String findStr = "$[#";
				String findStr2 = "$[]";
				int lastIndex = 0;
				int count = 0;

				int li1, li2;
				while(lastIndex != -1){						
					li1 = str.indexOf(findStr,lastIndex);
					li2 = str.indexOf(findStr2,lastIndex);
					if (li1 >= 0 && li1 < li2) {
						lastIndex = li1;
					}
					else {
						lastIndex = li2;
					}
			        if( lastIndex != -1){
		                count ++;
		                lastIndex+=findStr.length();
			        }				      
				}
				if (count != updateDefaultValues.length) {
					String[] newudv = new String[count];
					int oldCount = updateDefaultValues.length;
					for (int i = 0; i < count; i++) {
						newudv[i] = i < oldCount? updateDefaultValues[i] : "";
					}
					updateDefaultValues = newudv;
				}
			}
		});
		txtEditTemplate.setBounds(10, 13, 317, 388);
		
		Button btnNewButton_2 = new Button(cmpEdit, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String oldName = selectedTemplate.getName();
				selectedTemplate.setDescription(txtEditTemplate.getText());
				selectedTemplate.setName(txtEditName.getText());
				selectedTemplate.setProposalName(txtPropName.getText());
				selectedTemplate.setProposalDescription(txtPropDesc.getText());
				selectedTemplate.setDefaultValues(updateDefaultValues);
				TemplatePreferencesEditor.deleteTemplateFromPreferences(oldName);
				TemplatePreferencesEditor.addNewTemplateToPreferences(selectedTemplate);
				TemplatePreferencesEditor.savePreferences();
				ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.loadTemplatesFromPreferences();
				TemplateLoader.setTemplates(templates);	
				layout.topControl = cmpMain;
				container.layout();
			}
		});
		btnNewButton_2.setBounds(486, 371, 90, 30);
		btnNewButton_2.setText("Update");
		
		Button btnCancel = new Button(cmpEdit, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = cmpMain;
				container.layout();
			}
		});
		btnCancel.setBounds(601, 371, 90, 30);
		btnCancel.setText("Cancel");
		
		cbInsertTemplSegment = new Combo(cmpEdit, SWT.NONE);
		cbInsertTemplSegment.setBounds(486, 312, 122, 28);
		
		Button btnInsert = new Button(cmpEdit, SWT.NONE);
		btnInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tName = cbInsertTemplSegment.getText();
				if (!tName.isEmpty()) {
					txtEditTemplate.insert("$[#" + tName + "]");
				}
			}
		});
		btnInsert.setBounds(617, 310, 48, 30);
		btnInsert.setText("Insert");
		
		Label lblInsertTemplateTo = new Label(cmpEdit, SWT.NONE);
		lblInsertTemplateTo.setBounds(347, 300, 133, 40);
		lblInsertTemplateTo.setText("Insert template to\r\niterate:");
		
		Label lblTemplateName = new Label(cmpEdit, SWT.NONE);
		lblTemplateName.setBounds(358, 13, 107, 20);
		lblTemplateName.setText("Template name:");
		
		Label lblTemplateProposal_1 = new Label(cmpEdit, SWT.NONE);
		lblTemplateProposal_1.setBounds(336, 48, 129, 20);
		lblTemplateProposal_1.setText("Template proposal:");
		
		Label lblTemplateDescription_1 = new Label(cmpEdit, SWT.NONE);
		lblTemplateDescription_1.setBounds(369, 83, 96, 20);
		lblTemplateDescription_1.setText("Template info:");
		
		txtEditName = new Text(cmpEdit, SWT.BORDER);
		txtEditName.setBounds(472, 10, 138, 26);
		
		txtEditPropName = new Text(cmpEdit, SWT.BORDER);
		txtEditPropName.setBounds(472, 45, 138, 26);
		
		txtEditInfo = new Text(cmpEdit, SWT.BORDER);
		txtEditInfo.setBounds(471, 80, 220, 138);
		
		Label lblDefaultValue = new Label(cmpEdit, SWT.NONE);
		lblDefaultValue.setBounds(368, 231, 97, 20);
		lblDefaultValue.setText("Default values:");
		
		txtEditDefaultValue = new Text(cmpEdit, SWT.BORDER);
		txtEditDefaultValue.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int defValIndex = Integer.parseInt(cbEditDefaultsCount.getText());
				selectedTemplate.getDefaultValues()[defValIndex] = txtEditDefaultValue.getText();
			}
		});
		txtEditDefaultValue.setBounds(472, 262, 219, 26);
		
		cbEditDefaultsCount = new Combo(cmpEdit, SWT.READ_ONLY);
		cbEditDefaultsCount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int defValueIndex = cbEditDefaultsCount.getSelectionIndex();
				txtEditDefaultValue.setText(updateDefaultValues[defValueIndex]);
			}
		});
		cbEditDefaultsCount.setBounds(472, 228, 63, 28);
		
		Button btnAdd = new Button(cmpEdit, SWT.NONE);
		btnAdd.setBounds(549, 226, 44, 30);
		btnAdd.setText("Add ");
		
		Button btnRemove = new Button(cmpEdit, SWT.NONE);
		btnRemove.setBounds(599, 226, 66, 30);
		btnRemove.setText("Remove");

		layout.topControl = cmpMain;
		
		tableViewer = new TableViewer(cmpMain, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLocation(521, 14);
		table.setSize(144, 256);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(39);
		tblclmnNewColumn_1.setText("No");
		//tableViewer.setInput(TemplateLoader.getTemplates());
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Value");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());
		return container;
	}
	
	private String[] getTemplateNames() {
		ArrayList<AgileTemplate> templates = TemplateLoader.getTemplates();
		ArrayList<String> templateNames = new ArrayList<String>();
		for (int i = 0; i < templates.size(); i++) {
			templateNames.add(templates.get(i).getName());
		}
		String[] names = templateNames.toArray(new String[templateNames.size()]);
		Arrays.sort(names);
		return names;
	}

	@Override
	protected void configureShell(Shell shell) {
	    super.configureShell(shell);
	    shell.setText("Edit Agile Templates");
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(707, 510);
	}
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
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
	
	private class ContentProvider implements IStructuredContentProvider {        
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
}

