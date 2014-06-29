package tu.mmarinov.agileassist.ui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.templatehandler.TemplateCodeInserter;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;
import tu.mmarinov.agileassist.ui.model.ListViewerHelpers;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;

public class InsertCodeInTemplate extends Dialog {
	private Text txtInsertPreview;
	private AgileTemplate selectedTemplate;
	private AgileTemplate templateToInsert;
	private String selectedText;
	private Spinner spinner;
	private AbstractTextEditor activeEditor;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public InsertCodeInTemplate(Shell parentShell, AbstractTextEditor editor) {
		super(parentShell);
		activeEditor = editor;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		
		ListViewer lvTemplateName = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);		
		lvTemplateName.setLabelProvider(ListViewerHelpers.getLabelProvider());
		lvTemplateName.setContentProvider(ListViewerHelpers.getContentProvider());
		lvTemplateName.setInput(TemplateLoader.getTemplates());
		lvTemplateName.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection)arg0.getSelection();
				if (selection.size()==1) {
					selectedTemplate = (AgileTemplate)selection.getFirstElement();	
					if(selectedTemplate != null){
						int iterations = Integer.parseInt(spinner.getText());
						try{
							templateToInsert = TemplateCodeInserter.generateTemplateFromSelection(selectedTemplate, iterations, selectedText);
							txtInsertPreview.setText(templateToInsert.getDescription());
						}
						catch(IllegalArgumentException ex){
							JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}				
			}
		});
		
		List list = lvTemplateName.getList();
		list.setBounds(10, 38, 138, 358);
		
		txtInsertPreview = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtInsertPreview.setText("");
		txtInsertPreview.setBounds(177, 39, 304, 307);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Templates:");
		label.setBounds(10, 10, 138, 20);
		
		Label lblInsertionPreview = new Label(composite, SWT.NONE);
		lblInsertionPreview.setText("Insertion Preview:");
		lblInsertionPreview.setBounds(191, 10, 138, 20);
		
		Label lblIterations = new Label(composite, SWT.NONE);
		lblIterations.setBounds(235, 364, 70, 20);
		lblIterations.setText("Iterations:");
		
		spinner = new Spinner(composite, SWT.BORDER);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedTemplate != null){
					int iterations = Integer.parseInt(spinner.getText());
					try{
						templateToInsert = TemplateCodeInserter.generateTemplateFromSelection(selectedTemplate, iterations, selectedText);
						txtInsertPreview.setText(templateToInsert.getDescription());
					}
					catch(IllegalArgumentException ex){
						JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		});
		spinner.setMinimum(1);
		spinner.setBounds(311, 361, 59, 26);

		ITextSelection selection = TemplateCodeInserter.getCurrentSelection();
		selectedText = selection.getText();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, "Accept",
				true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TemplateLoader.applyTemplate(templateToInsert, activeEditor);
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(529, 517);
	}	
}
