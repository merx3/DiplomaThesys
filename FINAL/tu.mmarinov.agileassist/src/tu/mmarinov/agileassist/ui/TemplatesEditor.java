package tu.mmarinov.agileassist.ui;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
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
import tu.mmarinov.agileassist.prefs.DefaultTemplatesSupplier;
import tu.mmarinov.agileassist.prefs.TemplatePreferencesEditor;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;
import tu.mmarinov.agileassist.ui.model.ListViewerHelpers;
import tu.mmarinov.agileassist.ui.model.TableViewerHelpers;
import tu.mmarinov.agileassist.ui.model.TemplateEditorModel;

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
import org.eclipse.ui.IEditorPart;

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
	private TableViewer tableDefaultValues;
	private Text txtEditDefaultValue;
	private Combo cbEditDefaultsCount;
	private String[] newDefaultValues;
	private Button btnUpdateTemplate;
	private ListViewer lvTemplateNames;

	private IEditorPart activeEditor;
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
					newDefaultValues = selectedTemplate.getDefaultValues().clone();
					int defaultValuesCount = newDefaultValues.length;
					if (defaultValuesCount > 0) {
						String[] defaultsNumber = new String[defaultValuesCount];
						for (int i = 0; i < defaultValuesCount; i++) {
							defaultsNumber[i] = i + "";
						}
						cbEditDefaultsCount.setItems(defaultsNumber);
						cbEditDefaultsCount.select(0);	
						txtEditDefaultValue.setText(newDefaultValues[0]);
					}
					cbInsertTemplSegment.setItems(getTemplateNames());
					btnUpdateTemplate.setText("Update");
					container.layout();
				}
			}
		});
		btnNewButton.setBounds(479, 352, 90, 30);
		btnNewButton.setText("Edit");
		//composite.setVisible(false);
				
		Button btnDeleteTemplate = new Button(cmpMain, SWT.NONE);
		btnDeleteTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedTemplate != null) {
					String templateName = selectedTemplate.getName();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int reply = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete template '" + templateName + "'?","Warning",dialogButton);
					if (reply == JOptionPane.YES_OPTION)
				    {
						try{
							TemplatePreferencesEditor.deleteTemplateFromPreferences(templateName);
							TemplatePreferencesEditor.savePreferences();
							ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.getTemplatesFromPreferences();
							TemplateLoader.setTemplates(templates);		
							lvTemplateNames.setInput(templates);
							selectedTemplate = null;
							tableDefaultValues.setInput(null);	
							txtTemplateDesc.setText("");						
							txtPropName.setText("");
							txtPropDesc.setText("");
						}
						catch(IllegalArgumentException ex){
							JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
							return;
						}
				    }			
				}
			}
		});
		btnDeleteTemplate.setBounds(575, 352, 90, 30);
		btnDeleteTemplate.setText("Delete");
		
		Button btnCreateNewTemplate = new Button(cmpMain, SWT.NONE);
		btnCreateNewTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = cmpEdit;
				txtEditTemplate.setText("");
				txtEditName.setText("");
				txtEditInfo.setText("");
				txtEditDefaultValue.setText("");
				txtEditPropName.setText("");
				cbEditDefaultsCount.removeAll();
				newDefaultValues = new String[0];
				cbInsertTemplSegment.setItems(getTemplateNames());
				btnUpdateTemplate.setText("Create");
				selectedTemplate = null;
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
		
		lvTemplateNames = new ListViewer(cmpMain, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		lvTemplateNames.setLabelProvider(ListViewerHelpers.getLabelProvider());
		lvTemplateNames.setContentProvider(ListViewerHelpers.getContentProvider());
		lvTemplateNames.setInput(TemplateLoader.getTemplates());
		lvTemplateNames.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection)arg0.getSelection();
				if (selection.size()==1) {
					selectedTemplate = (AgileTemplate)selection.getFirstElement();	
					newDefaultValues = selectedTemplate.getDefaultValues().clone();
					tableDefaultValues.setInput(newDefaultValues);	
					txtTemplateDesc.setText(selectedTemplate.getDescription());
					txtPropName.setText(selectedTemplate.getProposalName());
					txtPropDesc.setText(selectedTemplate.getProposalDescription());
				}
				
			}
		});
		List list_1 = lvTemplateNames.getList();
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
				newDefaultValues = TemplateEditorModel.updateDefaultValues(newDefaultValues, str);
				int defaultsCount = newDefaultValues.length;				
				if (defaultsCount > 0) {
					String[] defaultsNumber = new String[defaultsCount];
					for (int i = 0; i < defaultsCount; i++) {
						defaultsNumber[i] = i + "";
					}
					cbEditDefaultsCount.setItems(defaultsNumber);
					cbEditDefaultsCount.select(0);	
					txtEditDefaultValue.setText(newDefaultValues[0]);
				}
				else{
					cbEditDefaultsCount.removeAll();
					txtEditDefaultValue.setText("");
				}
			}			
		});
		txtEditTemplate.setBounds(10, 13, 317, 388);
		
		btnUpdateTemplate = new Button(cmpEdit, SWT.NONE);
		btnUpdateTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String oldName = selectedTemplate == null ? "" : selectedTemplate.getName();
				String newName = txtEditName.getText();
				String newDescription = txtEditTemplate.getText();
				String newProposalName = txtEditPropName.getText();
				String newProposalDescription = txtEditInfo.getText();
				try{
					selectedTemplate = new AgileTemplate(newName, newDescription, newDefaultValues, newProposalName, newProposalDescription);
					TemplatePreferencesEditor.deleteTemplateFromPreferences(oldName);
					TemplatePreferencesEditor.addNewTemplateToPreferences(selectedTemplate);
					TemplatePreferencesEditor.savePreferences();
					ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.getTemplatesFromPreferences();
					TemplateLoader.setTemplates(templates);
					lvTemplateNames.setInput(templates);
					layout.topControl = cmpMain;
					container.layout();
				}
				catch(IllegalArgumentException ex){
					JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		btnUpdateTemplate.setBounds(486, 371, 90, 30);
		btnUpdateTemplate.setText("Update/Create");
		
		Button btnCancel = new Button(cmpEdit, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lvTemplateNames.getList().deselectAll();
				txtTemplateDesc.setText("");
				txtPropName.setText("");
				txtPropDesc.setText("");
				tableDefaultValues.setInput(null);
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
				if (cbEditDefaultsCount.getItemCount() != 0) {
					int defValIndex = Integer.parseInt(cbEditDefaultsCount.getText());
					newDefaultValues[defValIndex] = txtEditDefaultValue.getText();
				}
			}
		});
		txtEditDefaultValue.setBounds(472, 262, 219, 26);
		
		cbEditDefaultsCount = new Combo(cmpEdit, SWT.READ_ONLY);
		cbEditDefaultsCount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int defValueIndex = cbEditDefaultsCount.getSelectionIndex();
				txtEditDefaultValue.setText(newDefaultValues[defValueIndex]);
			}
		});
		cbEditDefaultsCount.setBounds(472, 228, 63, 28);

		layout.topControl = cmpMain;
		
		tableDefaultValues = new TableViewer(cmpMain, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableDefaultValues.getTable();
		table.setLocation(521, 14);
		table.setSize(144, 256);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(39);
		tblclmnNewColumn_1.setText("No");
		//tableViewer.setInput(TemplateLoader.getTemplates());
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableDefaultValues, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Value");
		
		Button btnLoadDefaultTemplates = new Button(cmpMain, SWT.NONE);
		btnLoadDefaultTemplates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int reply = JOptionPane.showConfirmDialog (null, "Are you sure you want to load the default templates? The current templates will be deleted PERMANENTLY!","Warning",dialogButton);
				if (reply == JOptionPane.YES_OPTION)
			    {
					ArrayList<AgileTemplate> templates = DefaultTemplatesSupplier.getDefaultTemplates();
					TemplateLoader.setTemplates(templates);
					TemplatePreferencesEditor.setPreferencesFromTemplates(templates);	
					TemplatePreferencesEditor.savePreferences();
					//System.out.println("LOADED DEFAULTS");
					lvTemplateNames.setInput(templates);
					selectedTemplate = null;
					tableDefaultValues.setInput(null);	
					txtTemplateDesc.setText("");						
					txtPropName.setText("");
					txtPropDesc.setText("");
			    }	
			}
		});
		btnLoadDefaultTemplates.setBounds(155, 352, 171, 30);
		btnLoadDefaultTemplates.setText("Load Default Templates");
		tableDefaultValues.setLabelProvider(TableViewerHelpers.getLabelProvider());
		tableDefaultValues.setContentProvider(TableViewerHelpers.getContentProvider());
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
}

