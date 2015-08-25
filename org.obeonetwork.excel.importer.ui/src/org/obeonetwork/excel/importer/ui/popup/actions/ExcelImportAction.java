package org.obeonetwork.excel.importer.ui.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.obeonetwork.excel.importer.ExcelImportException;
import org.obeonetwork.excel.importer.ExcelImporterEngine;
import org.obeonetwork.excel.importer.ExcelImporterManager;
import org.obeonetwork.excel.importer.IExcelImporter;
import org.obeonetwork.excel.importer.ui.ImporterSelectionDialog;

public class ExcelImportAction implements IObjectActionDelegate {

	private Shell shell;
	
	private IFile excelFile;
	private EObject destinationObject;
	
	/**
	 * Constructor for Action1.
	 */
	public ExcelImportAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		List<IExcelImporter> importers = ExcelImporterManager.eINSTANCE.getAllImporters ();
		
		ImporterSelectionDialog dialog = new ImporterSelectionDialog(shell, importers);
		
		if (dialog.open() == Dialog.OK){
			IExcelImporter importer = dialog.getSelectedImporter ();
			final ExcelImporterEngine engine = new ExcelImporterEngine(excelFile, destinationObject);
			engine.setImporter(importer);
			
			
				TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(destinationObject);
				RecordingCommand myCommand = new RecordingCommand(ted) {
					
					@Override
					protected void doExecute() {
						try {
							engine.run();
						} catch (ExcelImportException e) {
							e.printStackTrace();
						}
					}
				}; 			
			ted.getCommandStack().execute(myCommand);
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			for (Object obj : ss.toList()) {
				if (obj instanceof IFile) {
					IFile file = (IFile) obj;
					if (file.getFileExtension().equals("xlsx")) {
						excelFile = (IFile) obj;
					}
										
				}
				if (obj instanceof EObject) {
					destinationObject = (EObject) obj;					
				}
			}
			
		}
	}

}