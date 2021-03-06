//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
 //-----END DISCLAIMER----- 

package org.jcryptool.visual.aup.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.aup.AndroidUnlockPatternPlugin;

/**
 * 
 * @author Michael Schäfer
 * @author Stefan Kraus
 * 
 */
public class AupView extends ViewPart {

	public enum ApuState {
		ERROR, WARNING, INFO, OK
	}

	// public static final String ID =
	// "org.jcryptool.visual.aup.views.AndroidUnlockPattern";
	// private Action action1;
	// private Action action2;
	private Composite headingBox;
	private Group centerbox;
	private Composite controlBox;
	private Group helpBox;
	private Group optionbox;
	// private Label stDescription;
	private Label[] cntrBtn = new Label[9];
	private Region regionCircle;
	private Button setPattern;
	private Button changePattern;
	private Button checkPattern;
	private Button btnSave;
	private Button btnCancel;
	private ScrolledComposite descTextScroller;
	private StyledText descText;
	private Backend logic;
	private StyledText instrText1;
	private StyledText instrText2;
	private StyledText instrText3;
	private CLabel statusText;
	private Label instrTextHeading;
	private Label descTextHeading;
	// private Canvas canv;
	private Composite parent;
	private Boolean patternInput = false;
	private Boolean inputFinished = false;
	private Boolean advancedGraphic = false;
	private Font nFont;
	private Font bFont;
	
	//precomputed values for APU permutations depending on the APU's length
	private static int[] apuPerm = {
//		0,	//lenght 0
//		0,	//lenght 1
//		0,	//lenght 2
//		0,	//lenght 3
		1624,	//lenght 4
		8776,	//lenght 5 (4+5)
		34792,	//lenght 6 (4+5+6)
		107704,	//lenght 7 (4+5+6+7)
		248408,	//lenght 8 (4+5+6+7+8)
		389112	//lenght 9 (4+5+6+7+8+9)
		};

	/**
	 * The constructor.
	 */
	public AupView() {
		logic = new Backend(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 * 
	 * @param parent
	 *            a swt Composite, which is the parent..
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;

		// set context help
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(
						parent,
						AndroidUnlockPatternPlugin.PLUGIN_ID
								+ ".ContextHelpView"); //$NON-NLS-1$

		// Create the ScrolledComposite to scroll horizontally and vertically
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setMinHeight(500);
		sc.setMinWidth(500);
		// Create a child composite to hold the controls
		final Composite child = new Composite(sc, SWT.NONE);
		child.setLayout(new FormLayout());

		sc.setContent(child);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		headingBox = new Composite(child, SWT.NONE);
		controlBox = new Composite(child, SWT.None);
		centerbox = new Group(child, SWT.NONE);
		centerbox.setText(Messages.AndroidUnlockPattern_centerbox_text);
		optionbox = new Group(controlBox, SWT.NONE);
		optionbox.setToolTipText(Messages.AndroidUnlockPattern_optionbox_toolTipText);
		helpBox = new Group(child, SWT.NONE);
		helpBox.setToolTipText(Messages.AndroidUnlockPattern_helpBox_toolTipText);
		helpBox.setText(Messages.AndroidUnlockPattern_GroupHeadingHelp);
		setPattern = new Button(optionbox, SWT.RADIO);
		GridData gd_setPattern = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_setPattern.widthHint = 56;
		gd_setPattern.minimumHeight = 30;
		gd_setPattern.minimumWidth = 30;
		setPattern.setLayoutData(gd_setPattern);
		changePattern = new Button(optionbox, SWT.RADIO);
		checkPattern = new Button(optionbox, SWT.RADIO);

		for (int i = 0; i < cntrBtn.length; i++) {
			cntrBtn[i] = new Label(centerbox, SWT.NONE);
			cntrBtn[i].setData("nummer", i); //$NON-NLS-1$
			cntrBtn[i].setSize(40, 40); // set initial size; will be updated during initiation
		}

		statusText = new CLabel(centerbox, SWT.LEFT);
		statusText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		setPattern.setText(Messages.AndroidUnlockPattern_ModeSetText);
		changePattern.setText(Messages.AndroidUnlockPattern_ModeChangeText);
		checkPattern.setText(Messages.AndroidUnlockPattern_ModeCheckText);
		optionbox.setText(Messages.AndroidUnlockPattern_GroupHeadingModes);

		//get standard font
		FontData fd = setPattern.getFont().getFontData()[0];
		nFont = new Font(child.getDisplay(), fd);
		fd.setStyle(SWT.BOLD);
		bFont = new Font(child.getDisplay(), fd);
		
		initLayout();
		addActions();
		centerResize();
		
		logic.init();
		child.pack();	//update the size of the visuals child's
//		child.layout(true);
		
		//dispose allocated resources on shutdown
		parent.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				for(Label l:cntrBtn)
				{
					if(l.getImage() != null) l.getImage().dispose(); //dispose image
				}
				if(statusText.getImage() != null) statusText.getImage().dispose();
				headingBox.getChildren()[0].getFont().dispose();
				nFont.dispose();
				bFont.dispose();
			}
		});
		
		//test if advanced graphic processing is available
		Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/view.gif").createImage(child.getDisplay());
		GC gc = new GC(img);
		gc.setAdvanced(true);	// will do nothing if advanced graphic processing is not available
		if (gc.getAdvanced()){
			advancedGraphic = true;
		}
		gc.dispose();
		img.dispose();
	}

	/**
	 * sets the initial Layout
	 */
	private void initLayout() {

		headingBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		headingBox.setLayout(new FormLayout());
		final FormData fd_headingBox = new FormData(-1, -1);
		fd_headingBox.left = new FormAttachment(0);
		fd_headingBox.right = new FormAttachment(100);
		fd_headingBox.top = new FormAttachment(0);
		headingBox.setLayoutData(fd_headingBox);

		// top
		controlBox.setLayout(new FormLayout());
		final FormData fd_controlBox = new FormData(-1, -1);
		fd_controlBox.bottom = new FormAttachment(centerbox, 0, SWT.BOTTOM);
		fd_controlBox.top = new FormAttachment(headingBox, 6);
		fd_controlBox.right = new FormAttachment(centerbox, -6);
		fd_controlBox.left = new FormAttachment(0);
		controlBox.setLayoutData(fd_controlBox);

		// optionbox
		GridLayout gl_optionbox = new GridLayout();
		gl_optionbox.marginWidth = 10;
		gl_optionbox.marginHeight = 10;
		gl_optionbox.makeColumnsEqualWidth = true;
		optionbox.setLayout(gl_optionbox);
		final FormData fdOb = new FormData(-1, -1);
		fdOb.left = new FormAttachment(0, 10);
		fdOb.top = new FormAttachment(0);
		optionbox.setLayoutData(fdOb);
		btnSave = new Button(controlBox, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.top = new FormAttachment(optionbox, 6);
		fd_btnSave.right = new FormAttachment(optionbox, 0, SWT.RIGHT);
		fd_btnSave.left = new FormAttachment(optionbox, 0, SWT.LEFT);
		fd_btnSave.height = 30;
		fd_btnSave.width = 80;
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText(Messages.AndroidUnlockPattern_ButtonSaveText);
		btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		// btnCheck = new Button(bottombox, SWT.None);
		btnCancel = new Button(controlBox, SWT.NONE);
		btnCancel.setEnabled(false);
		btnCancel.setToolTipText(Messages.AndroidUnlockPattern_btnCancel_toolTipText);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(btnSave, 6);
		fd_btnCancel.right = new FormAttachment(optionbox, 0, SWT.RIGHT);
		fd_btnCancel.left = new FormAttachment(optionbox, 0, SWT.LEFT);
		fd_btnCancel.width = 80;
		fd_btnCancel.height = 30;
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText(Messages.AndroidUnlockPattern_ButtonCancelText);

		// center
		final GridLayout clayout = new GridLayout(3, false);
		clayout.marginLeft = 10;
		clayout.marginRight = 10;
		clayout.marginTop = 10;
		clayout.horizontalSpacing = 15;
		clayout.verticalSpacing = 15;
		centerbox.setLayout(clayout);
		final FormData fdCb = new FormData(0, 0);
		fdCb.bottom = new FormAttachment(100, -170);
		fdCb.top = new FormAttachment(headingBox, 6);
		fdCb.left = new FormAttachment(0, 156);
		fdCb.right = new FormAttachment(100, -10);
		centerbox.setLayoutData(fdCb);

		Label heading = new Label(headingBox, SWT.NONE);
		heading.setFont(FontService.getHeaderFont());
		FormData fd_heading = new FormData();
		fd_heading.top = new FormAttachment(0, 10);
		fd_heading.left = new FormAttachment(0, 10);
		heading.setLayoutData(fd_heading);
		heading.setText(Messages.AndroidUnlockPattern_Heading);
		
		GridLayout gl_helpBox = new GridLayout(2, true);
		gl_helpBox.horizontalSpacing = 25;
		helpBox.setLayout(gl_helpBox);
		final FormData fd_helpBox = new FormData(180, -1);
		fd_helpBox.top = new FormAttachment(centerbox, 6);
		fd_helpBox.bottom = new FormAttachment(100, -10);
		fd_helpBox.left = new FormAttachment(0, 10);
		fd_helpBox.right = new FormAttachment(100, -10);
		helpBox.setLayoutData(fd_helpBox);
		
		instrTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrTextHeading.setText(Messages.AndroidUnlockPattern_helpBox_instrText_Heading);
		
		descTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		descTextHeading.setText(Messages.AndroidUnlockPattern_helpBox_descText_Heading);

		instrText1 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText1.setDoubleClickEnabled(false);
		instrText1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText1.setAlignment(SWT.LEFT);
		instrText1.setText(Messages.Mode_Set_1);
		
		descTextScroller = new ScrolledComposite(helpBox, SWT.V_SCROLL);
		descTextScroller.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 3));
		descTextScroller.setExpandHorizontal(true);
		descTextScroller.setExpandVertical(true);
		
		descText = new StyledText(descTextScroller, SWT.READ_ONLY | SWT.WRAP);
		descText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		descText.setDoubleClickEnabled(false);
		descText.setText(Messages.AndroidUnlockPattern_helpBox_descText);
		descTextScroller.setContent(descText);
		
		instrText2 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText2.setDoubleClickEnabled(false);
		instrText2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText2.setAlignment(SWT.LEFT);
		instrText2.setText(Messages.Mode_Set_1);
		
		instrText3 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText3.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText3.setDoubleClickEnabled(false);
		instrText3.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText3.setAlignment(SWT.LEFT);
		instrText3.setText(Messages.Mode_Set_1);

	}

	// Code from dev.eclipse.org Licence: Eclipse Public License
	// http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet294.java?view=co
	private int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

	/**
	 * add listener to buttons etc
	 * 
	 */
	private void addActions() {
		centerbox.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				centerResize();
				centerbox.redraw();
			}
		});

		// centrcalBtns
		for (int i = 0; i < cntrBtn.length; i++) {
//			cntrBtn[i].addSelectionListener(new SelectionListener() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					if (e.widget
//							.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
//						// to get here the button needs to be unclicked
//						// (in this case e.widget.getData("icon").toString() is
//						// "icons/black.png")
//						// for performance reasons only the 7. char of the
//						// string is checked
//						final int btnNummer = (Integer) e.widget
//								.getData("nummer"); //$NON-NLS-1$
//						logic.btnMainClick(btnNummer);
//					}
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//
//				}
//			});
			cntrBtn[i].addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {					
					patternInput = true; //enable touch input
					((Label)e.widget).setCapture(false); //release mouse to fire subsequent mouse events

					if (!inputFinished && e.widget.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
						// to get here the button needs to be unclicked
						// (in this case e.widget.getData("icon").toString() is "icons/black.png")
						// for performance reasons only the 7. char of the string is checked
						int btnNummer = (Integer) e.widget.getData("nummer"); //$NON-NLS-1$
						logic.btnMainClick(btnNummer);
					}
				}

				@Override
				public void mouseUp(MouseEvent e) {
					patternInput = false; //disable touch input
					inputFinished = true; //disable subsequent pattern input
				}
				
			});
			cntrBtn[i].addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseEnter(MouseEvent e) {	
					if (patternInput && !inputFinished &&  e.widget.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
						final int btnNummer = (Integer) e.widget.getData("nummer"); //$NON-NLS-1$
						logic.btnMainClick(btnNummer);
					}
				}

				@Override
				public void mouseExit(MouseEvent e) {
				}

				@Override
				public void mouseHover(MouseEvent e) {
//					System.out.println(e.stateMask);
//					if((e.stateMask & SWT.BUTTON1) != 0)
//						System.out.println("left down");
//					if((e.stateMask & SWT.BUTTON2) != 0)	//1048576
//						System.out.println("right down");
				}
			});
		}
		
		btnSave.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnSave.setEnabled(false);
				btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				btnCancel.setEnabled(false);
				patternInput = inputFinished = false;
				if(logic.isFirst()) {
					int length = 0;
					for(Label a : cntrBtn) {
						if(a.getData("icon").toString().regionMatches(false, 6, "g", 0, 1))
							length++;
					}
					descText.setText(String.format(Messages.AndroidUnlockPattern_helpBox_descText_Security, Messages.AndroidUnlockPattern_helpBox_descText, length, apuPerm[length-4]));
					recalcDescTextScrolling();
					helpBox.layout(true);
				}
				logic.btnSaveClick();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnCancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCancel.setEnabled(false);
				btnSave.setEnabled(false);
				btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				patternInput = inputFinished = false;
				logic.btnCancelClick();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		centerbox.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				drawLines(e);
			}

		});

		setPattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				logic.setModus(1);
				patternInput = inputFinished = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		changePattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(logic.isChangeable())
//					setStatusText(Messages.Backend_PopupCancelMessage, ApuState.WARNING); //$NON-NLS-1$
//				else
//					setStatusText("", null); //$NON-NLS-1$
				logic.setModus(2);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		checkPattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(logic.isChangeable())
//					setStatusText(Messages.Backend_PopupCancelMessage, ApuState.WARNING); //$NON-NLS-1$
//				else
//					setStatusText("", null); //$NON-NLS-1$
				logic.setModus(3);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		descTextScroller.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				recalcDescTextScrolling();
			}
		});
	}

	protected void drawLines(PaintEvent e) {
		centerResize();
		e.gc.setForeground(logic.getLineColor());
		e.gc.setLineWidth(10);
		for (int[] point : logic.getPoints()) {
			e.gc.drawLine(point[0], point[1], point[2], point[3]);
		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		parent.setFocus();
	}

	public int MsgBox(String header, String msg, int options) {
		final MessageBox mb = new MessageBox(Display.getDefault()
				.getActiveShell(), options);
		mb.setText(header);
		mb.setMessage(msg);
		return mb.open();
	}

	public void setBtnSaveText(String text) {
		btnSave.setText(text);
	}

	/**
	 * @return the setPattern
	 */
	public Button getSetPattern() {
		return setPattern;
	}

//	/**
//	 * @param setPattern
//	 *            the setPattern to set
//	 */
//	public void setSetPattern(Button setPattern) {
//		this.setPattern = setPattern;
//	}

	/**
	 * @return the changePattern
	 */
	public Button getChangePattern() {
		return changePattern;
	}

//	/**
//	 * @param changePattern
//	 *            the changePattern to set
//	 */
//	public void setChangePattern(Button changePattern) {
//		this.changePattern = changePattern;
//	}

	/**
	 * @return the checkPattern
	 */
	public Button getCheckPattern() {
		return checkPattern;
	}

//	/**
//	 * @param checkPattern
//	 *            the checkPattern to set
//	 */
//	public void setCheckPattern(Button checkPattern) {
//		this.checkPattern = checkPattern;
//	}

	/**
	 * @return the cntrBtn
	 */
	public Label[] getCntrBtn() {
		return cntrBtn;
	}

	/**
	 * @return the btnSave
	 */
	public Button getBtnSave() {
		return btnSave;
	}

	/**
	 * @return the btnCancel
	 */
	public Button getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @return the centerbox
	 */
	public Group getCenterbox() {
		return centerbox;
	}

	public void centerResize() {
		// centerButtons
		GridLayout layout = (GridLayout) centerbox.getLayout();
		int size = Math.min(centerbox.getClientArea().height
				- layout.marginHeight * 2 - layout.horizontalSpacing * 3 - layout.marginTop
				- statusText.getClientArea().height,
				centerbox.getClientArea().width - layout.marginWidth * 2
						- layout.verticalSpacing * 2 - layout.marginLeft - layout.marginRight);
		if (size < 0)
			return; // Layout not yet initialized
		size = size / 3; // 3x3 centrcalBtns
		logic.recalculateLines();
		for (int i = 0; i < cntrBtn.length; i++) {
			if (cntrBtn[i].getData("icon") != null) { //$NON-NLS-1$
				cntrBtn[i].getImage().dispose(); //dispose the old image
				String tmpStr = cntrBtn[i].getData("icon").toString(); //$NON-NLS-1$
				ImageData tmp = AndroidUnlockPatternPlugin
						.getImageDescriptor(tmpStr).getImageData()
						.scaledTo(size, size);
				Image img = new Image(cntrBtn[i].getDisplay(), tmp);
				GC gc = new GC(img);
				
				if(cntrBtn[i].getData("arc") != null && advancedGraphic) {
					Image arrow = null;
					if(tmpStr.regionMatches(false, 6, "g", 0, 1))
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowGreen.png").createImage(cntrBtn[i].getDisplay());
					else if(tmpStr.regionMatches(false, 6, "y", 0, 1))
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowYellow.png").createImage(cntrBtn[i].getDisplay());
					
					if(arrow != null) {
						Transform oldTransform = new Transform(gc.getDevice());
						gc.getTransform(oldTransform);
						
						Transform transform = new Transform(gc.getDevice());
						transform.translate(size/2, size/2);
						transform.rotate((Float)cntrBtn[i].getData("arc"));
						transform.translate(-size/2, -size/2);
						
						gc.setTransform(transform);
						gc.drawImage(arrow, 0, 0, arrow.getImageData().width, arrow.getImageData().height, 0, 0, size, size);
						gc.setTransform(oldTransform);
						oldTransform.dispose();
						transform.dispose();
						arrow.dispose();
					}
				}
				
				cntrBtn[i].setImage(img);
			}
			regionCircle = new Region();
			regionCircle.add(circle(size / 2, cntrBtn[i].getBounds().width / 2,
					cntrBtn[i].getBounds().height / 2));

			cntrBtn[i].setRegion(regionCircle);
//			cntrBtn[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			cntrBtn[i].setLayoutData(new GridData(size, size));
			regionCircle.dispose();
		}
	}

	/**
	 * Resets the state information of the plug-in. Asks the user first if he is really sure.
	 */
	public void resetClick() {
		int tmp = MsgBox(Messages.Backend_PopupResetHeading,
				Messages.Backend_PopupResetMessage, SWT.YES | SWT.NO
						| SWT.ICON_WARNING);
		if (tmp == SWT.YES) {
			setStatusText("", null); //$NON-NLS-1$
			patternInput = inputFinished = false;
			btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			descText.setText(Messages.AndroidUnlockPattern_helpBox_descText);
			recalcDescTextScrolling();
			logic.reset();
		}
	}

	protected void setStatusText(String message, ApuState state) {
		if(statusText.getImage() != null)
			statusText.getImage().dispose();
		if(state == null) {
			statusText.setImage(null);
			statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		} else {
		switch (state) {
			case ERROR:
				statusText.setImage(AndroidUnlockPatternPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "/icons/full/obj16/error_tsk.gif").createImage());
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
				break;
			case WARNING:
				statusText.setImage(AndroidUnlockPatternPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "/icons/full/obj16/warn_tsk.gif").createImage());
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW));
				break;
			case INFO:
				statusText.setImage(AndroidUnlockPatternPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "/icons/full/obj16/info_tsk.gif").createImage());
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				break;
			case OK:
				statusText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("/icons/ok_st_obj.gif").createImage());
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				break;
			default:
				statusText.setImage(null);
			}
		}
		statusText.setText(message);
		//to avoid control from growing and to automatically lay out do not call pack(true) here
//		if(state != null)
//			System.out.println(state.toString() + ": " + message);
//		else
//			System.out.println("NoState: " + message);
	}

	
	/**
	 * Updates the user progress information in the description box.
	 */
	protected void updateProgress() {
		switch(logic.getModus()) {
			case 1: {	// set				
				if (logic.isFirst()) { // 1. step
					//set texts
					instrTextHeading.setText(String.format(Messages.AndroidUnlockPattern_helpBox_instrText_Heading, Messages.AndroidUnlockPattern_ModeSetText));
					instrText1.setText(String.format(Messages.AndroidUnlockPattern_Step, 1, Messages.Mode_Set_1));
					instrText2.setText(String.format(Messages.AndroidUnlockPattern_Step, 2, Messages.Mode_Set_2));
					instrText3.setText("");	
					
					//set highlight
					instrText1.setFont(bFont);
					instrText2.setFont(nFont);
//					instrText1.setEnabled(true); 
//					instrText2.setEnabled(false);
				} else { // 2. step
					instrText1.setFont(nFont);
					instrText2.setFont(bFont);
//					instrText1.setEnabled(false); 
//					instrText2.setEnabled(true);
				}
					
				break;
			}
			case 2: {	// change
				if (!logic.isChangeable()) { // 1. step
					//set texts
					instrTextHeading.setText(String.format(Messages.AndroidUnlockPattern_helpBox_instrText_Heading, Messages.AndroidUnlockPattern_ModeChangeText));
					instrText1.setText(String.format(Messages.AndroidUnlockPattern_Step, 1, Messages.Mode_Change_1));
					instrText2.setText(String.format(Messages.AndroidUnlockPattern_Step, 2, Messages.Mode_Change_2));
					instrText3.setText(String.format(Messages.AndroidUnlockPattern_Step, 3, Messages.Mode_Set_2));
					
					//set highlight
					instrText1.setFont(bFont);
					instrText2.setFont(nFont);
					instrText3.setFont(nFont);
//					instrText1.setEnabled(true);
//					instrText2.setEnabled(false);
//					instrText3.setEnabled(false);
				} else if (logic.isFirst()) { //2. step
					instrText1.setFont(nFont);
					instrText2.setFont(bFont);
					instrText3.setFont(nFont);
//					instrText1.setEnabled(false); 
//					instrText2.setEnabled(true);
//					instrText3.setEnabled(false);
				} else { // 3. step
					instrText1.setFont(nFont);
					instrText2.setFont(nFont);
					instrText3.setFont(bFont);
//					instrText1.setEnabled(false); 
//					instrText2.setEnabled(false);
//					instrText3.setEnabled(true);
				}
				
				break;
			}
			case 3: {	// check
				instrTextHeading.setText(String.format(Messages.AndroidUnlockPattern_helpBox_instrText_Heading, Messages.AndroidUnlockPattern_ModeCheckText));
				instrText1.setText(Messages.Mode_Check_1);
				instrText2.setText("");
				instrText3.setText("");
				
				instrText1.setFont(bFont);
//				instrText1.setEnabled(true);
				break;
			}
		}
		helpBox.layout(true);
	}
	
	/**
	 * Recalculate the scrolling area size for the description text.
	 * <br>
	 * Has to be called after every description text update.
	 */
	private void recalcDescTextScrolling() {		
		Point size = descText.computeSize(descTextScroller.getClientArea().width, SWT.DEFAULT);	// compute required height for fixed width
		descTextScroller.setMinHeight(size.y); // enable scrolling
	}

}