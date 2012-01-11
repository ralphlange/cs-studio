/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.ui.util.helpers;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

/**
 * Decorates a Combo box to maintain the history.
 * <p>
 * Newly entered items are added to the top of the combo list, dropping last
 * items off the list when reaching a configurable maximum list size. If an item
 * is selected/entered again, it will pop at the beginning of the list (so
 * that continuously used items are not lost).
 * <p>
 * You must
 * <ul>
 * <li>implement newSelection() to handle entered/selected values
 * <li>decide if you want to call loadSettings() to restore the saved values
 * <li>save values via saveSettings, or use the save_on_dispose option of the
 * constructor.
 * </ul>
 * 
 * @see #newSelection(String)
 * @author Kay Kasemir
 * @author Gabriele Carcassi
 */
public abstract class ComboHistoryHelper {
	
	private static final String TAG = "values"; //$NON-NLS-1$
	private static final int DEFAULT_HISTORY_SIZE = 10;
	private final IDialogSettings settings;
	private final String tag;
	private final Combo combo;
	private final int max;

	/**
	 * Attach helper to given combo box, using max list length.
	 * 
	 * @param settings where to persist the combo box list
	 * @param tag tag used for persistence
	 * @param combo the combo box
	 */
	public ComboHistoryHelper(IDialogSettings settings, String tag, Combo combo) {
		this(settings, tag, combo, DEFAULT_HISTORY_SIZE, true);
	}

	/**
	 * Attach helper to given combo box, using max list length.
	 * 
	 * @param settings where to persist the combo box list
	 * @param tag tag used for persistence
	 * @param combo the combo box
	 * @param max number of elements to keep in history
	 * @param saveOnDispose whether current values should be saved at widget disposal
	 */
	public ComboHistoryHelper(IDialogSettings settings, String tag,
			Combo combo, int max, boolean saveOnDispose) {
		this.settings = settings;
		this.tag = tag;
		this.combo = combo;
		this.max = max;

		// React whenever an existing entry is selected,
		// or a new name is entered.
		// New names are also added to the list.
		combo.addSelectionListener(new SelectionListener() {
			// Called after <Return> was pressed
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				String new_entry = ComboHistoryHelper.this.combo.getText();
				addEntry(new_entry);
				ComboHistoryHelper.this.combo.select(0);
				newSelection(new_entry);
			}

			// Called after existing entry was picked from list
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = ComboHistoryHelper.this.combo.getText();
				addEntry(name);
				ComboHistoryHelper.this.combo.select(0);
				newSelection(name);
			}
		});

		// Register saving on dispose
		if (saveOnDispose) {
			combo.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					saveSettings();
				}
			});
		}
	}

	/**
	 * Adds a new entry to the list. If entry already there, do nothing.
	 * 
	 * @param newEntry the new value
	 */
	public void addEntry(String newEntry) {
		// Avoid empty entries
		if (newEntry.trim().isEmpty())
			return;
		
		// Remove if present, so that is
		// re-added on top
		int entry = -1;
		while ((entry = combo.indexOf(newEntry)) != -1) {
			combo.remove(entry);
		}
		
		// Maybe remove oldest, i.e. bottom-most, entry
		if (combo.getItemCount() >= max)
			combo.remove(combo.getItemCount() - 1);
		
		// Add at the top
		combo.add(newEntry, 0);
	}

	/** Invoked whenever a new entry was entered or selected. */
	public abstract void newSelection(String entry);

	/** Load persisted list values. */
	public void loadSettings() {
		if (settings != null) {
			IDialogSettings pvs = settings.getSection(tag);
			if (pvs == null)
				return;
			String values[] = pvs.getArray(TAG);
			if (values != null)
				for (int i = values.length-1; i >= 0; i--)
					// Load as if they were entered, i.e. skip duplicates
					addEntry(values[i]);
		}
	}

	/** Save list values to persistent storage. */
	public void saveSettings() {
		if (settings != null) {
			IDialogSettings values = settings.addNewSection(tag);
			values.put(TAG, combo.getItems());
		}
	}
}
