/*******************************************************************************
 *  Copyright (c) 2016 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/package org.obeonetwork.spreadsheet.importer.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.obeonetwork.spreadsheet.importer.IExcelImporter;

public class ImporterSelectionDialog extends org.eclipse.jface.dialogs.Dialog {
	private Combo _combo;
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Label label = new Label(container, SWT.NONE);
		label.setText("Select an excel importer");
		
		_combo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);		
		for (IExcelImporter iExcelImporter : _importers) {
			_combo.add(iExcelImporter.getName ());
		}
		_combo.select(0);


		return container;
	}



	private List<IExcelImporter> _importers;
	private IExcelImporter _selectedImporter;

	public ImporterSelectionDialog(Shell parent, List<IExcelImporter> importers) {
		super(parent);
		_importers = importers;
}

	public IExcelImporter getSelectedImporter() {
		return _selectedImporter;
	}

	@Override
	public boolean close() {
		int selection = _combo.getSelectionIndex();
		_selectedImporter =  _importers.get(selection);
		return super.close();
	}


}
