package tu.mmarinov.agileassist.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
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
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TemplatesEditor extends Dialog {
	private Text txtTemplateDesc;

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
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new StackLayout());
		
		Composite composite = new Composite(container, SWT.NONE);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(349, 248, 90, 30);
		btnNewButton.setText("Edit");
		//composite.setVisible(false);
		
		List list = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String templateName = e.text;
				AgileTemplate t = TemplateLoader.getTemplate(templateName);
				txtTemplateDesc.setText(t.getDescription());
			}
		});
		String[] templateNames = getTemplateNames();
		list.setItems(templateNames);	
		list.setBounds(41, 10, 148, 221);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setBounds(469, 248, 90, 30);
		btnNewButton_1.setText("Delete");
		
		Button btnCreateNewTemplate = new Button(composite, SWT.NONE);
		btnCreateNewTemplate.setBounds(41, 248, 109, 30);
		btnCreateNewTemplate.setText("New Template");
		
		Label lblTemplatePreview = new Label(composite, SWT.NONE);
		lblTemplatePreview.setBounds(210, 14, 138, 20);
		lblTemplatePreview.setText("Template Preview:");
		
		txtTemplateDesc = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtTemplateDesc.setText("");
		txtTemplateDesc.setBounds(210, 40, 349, 191);
		
		Composite composite_1 = new Composite(container, SWT.NONE);
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
		return new Point(604, 400);
	}
}
