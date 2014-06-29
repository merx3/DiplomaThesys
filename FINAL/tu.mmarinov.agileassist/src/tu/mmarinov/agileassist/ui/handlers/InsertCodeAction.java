package tu.mmarinov.agileassist.ui.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import tu.mmarinov.agileassist.ui.InsertCodeInTemplate;

public class InsertCodeAction implements IObjectActionDelegate {
	private Shell shell;
	private AbstractTextEditor editor;

	@Override
	public void run(IAction arg0) {
		InsertCodeInTemplate insertDialog = new InsertCodeInTemplate(shell, editor);
		insertDialog.open();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		shell = arg1.getSite().getShell();
		editor = (AbstractTextEditor)arg1.getSite().getPage().getActiveEditor();
	}

}
