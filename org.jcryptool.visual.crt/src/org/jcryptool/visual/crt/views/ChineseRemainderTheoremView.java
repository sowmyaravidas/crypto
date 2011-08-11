package org.jcryptool.visual.crt.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.crt.export.FileExporter;


/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class ChineseRemainderTheoremView extends ViewPart implements Constants{

	private Action exportToLatexAction;
	private Action exportToCSVAction;
	private Action exportToPdfAction;

	private StackLayout layout;
	private CRTGroup CRTGroup;

	/**
	 * Create contents of the view part
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "1");

		layout = new StackLayout();
		parent.setLayout(layout);

		CRTGroup = new CRTGroup(parent, SWT.NONE, this);
		layout.topControl = CRTGroup;

		createActions();
		initializeMenu();
	}

	/**
	 * Create the actions
	 */
	private void createActions() {

		exportToPdfAction = new Action(MESSAGE_EXPORT_PDF) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.pdf" });
		        dialog.setFilterNames(new String[] {"PDF Files (*.pdf)"});
		        dialog.setFilterPath(DirectoryService.getUserHomeDir());
		        dialog.setOverwrite(true);
				String filename = dialog.open();

				if (filename != null) {
					FileExporter pdfExport = new FileExporter(CRTGroup.getCrt(), filename);
					pdfExport.exportToPDF();
				}

			}
		};
		exportToPdfAction.setEnabled(false);

		exportToCSVAction = new Action(MESSAGE_EXPORT_CSV) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.csv" });
                dialog.setFilterNames(new String[] {"CSV Files (*.csv)"});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);
				String filename = dialog.open();

				if (filename != null) {
					FileExporter csvExport = new FileExporter(CRTGroup.getCrt(), filename);
					csvExport.exportToCSV();
				}
			}
		};
		exportToCSVAction.setEnabled(false);

		exportToLatexAction = new Action(MESSAGE_EXPORT_LATEX) {
			@Override
			public void run() {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterNames(new String[] {"Latex Files (*.tex)"});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
				dialog.setFilterExtensions(new String[] { "*.tex" });
		        dialog.setOverwrite(true);
				String filename = dialog.open();

				if (filename != null) {
					FileExporter latexExport = new FileExporter(CRTGroup.getCrt(), filename);
					latexExport.exportToLatex();
				}

			}
		};
		exportToLatexAction.setEnabled(false);
		// Create the actions
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();

		menuManager.add(exportToPdfAction);
		menuManager.add(exportToCSVAction);
		menuManager.add(exportToLatexAction);
	}

	public void enableMenu(boolean enable){
		exportToPdfAction.setEnabled(enable);
		exportToLatexAction.setEnabled(enable);
		exportToCSVAction.setEnabled(enable);
	}

	@Override
	public void setFocus() {
	}

}