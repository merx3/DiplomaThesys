package tu.mmarinov.agileassist.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import tu.mmarinov.agileassist.ui.InsertCodeInTemplate;

public class InsertCodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked (event).getShell();
		AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) HandlerUtil.getActiveEditor(event);
		InsertCodeInTemplate insertDialog = new InsertCodeInTemplate(shell, activeEditor);
		insertDialog.open();
		return null;
	}

}
