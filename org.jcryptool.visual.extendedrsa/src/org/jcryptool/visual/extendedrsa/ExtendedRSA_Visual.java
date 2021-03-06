// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.ui.wizards.DeleteIdentityWizard;
import org.jcryptool.visual.extendedrsa.ui.wizards.ManageVisibleIdentitesWizard;
import org.jcryptool.visual.extendedrsa.ui.wizards.NewIdentityWizard;

/**
 * Represents the visual
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class ExtendedRSA_Visual extends ViewPart {

    private final String ALICE = Messages.ExtendedRSA_Visual_1;
    private final String BOB = Messages.ExtendedRSA_Visual_2;
    private final String BLANK = Messages.ExtendedRSA_Visual_3;

    private ScrolledComposite sc;
    private Composite composite;
    private GridLayout gl;
    private Composite headComposite;
    private StyledText head_description;
    private Group grp_id_mgmt;
    private Button btn_newID;
    private Button btn_manageID;
    private Button btn_delID;
    private Composite comp_center;
    private ExtendedTabFolder tabFolder;
    private Label txtExplain;

    public ExtendedRSA_Visual() {
    }

    @Override
    public void createPartControl(Composite parent) {

        // make the composite scrollable
        sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        composite = new Composite(sc, SWT.NONE);
        sc.setContent(composite);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setMinSize(composite.computeSize(1000, 680));

        gl = new GridLayout(1, false);
        gl.verticalSpacing = 20;
        composite.setLayout(gl);

        // Begin - Header
        headComposite = new Composite(composite, SWT.NONE);
        headComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        headComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        headComposite.setLayout(new GridLayout());

        Label label = new Label(headComposite, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setText(Messages.ExtendedRSA_Visual_4);
        head_description = new StyledText(headComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        head_description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        head_description.setText(Messages.ExtendedRSA_Visual_5);
        // End - Header

        grp_id_mgmt = new Group(composite, SWT.NONE);
        grp_id_mgmt.setText(Messages.ExtendedRSA_Visual_6);
        grp_id_mgmt.setLayout(new GridLayout(3, true));

        btn_newID = new Button(grp_id_mgmt, SWT.PUSH);
        btn_manageID = new Button(grp_id_mgmt, SWT.PUSH);
        btn_delID = new Button(grp_id_mgmt, SWT.PUSH);

        btn_newID.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new WizardDialog(getSite().getShell(), new NewIdentityWizard(tabFolder, btn_delID)).open();
                grp_id_mgmt.update();
            }
        });
        btn_newID.setText(Messages.ExtendedRSA_Visual_7);
        btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        btn_manageID.setText(Messages.ExtendedRSA_Visual_8);
        btn_manageID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btn_manageID.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                new WizardDialog(getSite().getShell(), new ManageVisibleIdentitesWizard(tabFolder, txtExplain)).open();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        btn_delID.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new WizardDialog(getSite().getShell(), new DeleteIdentityWizard(tabFolder, btn_delID)).open();
                grp_id_mgmt.update();
            }
        });
        btn_delID.setText(Messages.ExtendedRSA_Visual_9);
        btn_delID.setEnabled(ContactManager.getInstance().getContactSize() > 2);

        btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        comp_center = new Composite(composite, SWT.NONE);
        // 2 columns (tabs and explanation) --> new GridLayout(2, false);
        comp_center.setLayout(new GridLayout(2, false));
        comp_center.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        tabFolder = new ExtendedTabFolder(comp_center, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Group grp_explain = new Group(comp_center, SWT.NONE);
        grp_explain.setLayout(new GridLayout(1, true));
        GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gd_explain.widthHint = 300;

        grp_explain.setText(Messages.ExtendedRSA_Visual_10);

        txtExplain = new Label(grp_explain, SWT.WRAP);
        GridData gd_txtEplain = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_txtEplain.heightHint = 300;
        txtExplain.setLayoutData(gd_txtEplain);

        grp_explain.setLayoutData(gd_explain);

        initKeystore(tabFolder);

    }

    private void initKeystore(TabFolder tabfolder) {
        try {
            if (!KeyStorePlugin.isInitialized()) {
                KeyStorePlugin.initialize();
            }

            IdentityManager iMgr = IdentityManager.getInstance();
            Vector<String> contactNames = iMgr.getContacts();
            if (!contactNames.contains(ALICE)) {
                // create Alice in the keystore
                iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_11, Messages.ExtendedRSA_Visual_12, 1024);
            }
            Vector<String> keyAlgos = iMgr.getAsymmetricKeyAlgorithms(ALICE);

            int count = 0;
            int count2 = 0;
            for (int i = 0; i < keyAlgos.size(); i++) {
                if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_13)) {
                    count++;
                }
                if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_14)) {
                    count2++;
                }
            }
            if (count == 0) {
                iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_15, Messages.ExtendedRSA_Visual_16, 1024);
            }
            if (count2 == 0) {
                iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_17, Messages.ExtendedRSA_Visual_18, 1024);
            }

            String[] alice_split = ALICE.split(BLANK);
            new Identity(tabFolder, SWT.NONE, new Contact(ALICE, alice_split[0], alice_split[1],
                    Messages.ExtendedRSA_Visual_19, Messages.ExtendedRSA_Visual_20), txtExplain);

            if (!contactNames.contains(BOB)) {
                // create Bob in the keystore
                iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_21, Messages.ExtendedRSA_Visual_22, 1024);
            }
            keyAlgos = iMgr.getAsymmetricKeyAlgorithms(BOB);

            count = 0;
            count2 = 0;
            for (int i = 0; i < keyAlgos.size(); i++) {
                if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_23)) {
                    count++;
                }
                if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_24)) {
                    count2++;
                }
            }
            if (count == 0) {
                // create an MpRSA-Key
                iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_25, Messages.ExtendedRSA_Visual_26, 1024);
            }
            if (count2 == 0) {
                // create an RSA-Key
                iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_27, Messages.ExtendedRSA_Visual_28, 1024);
            }

            String[] bob_split = BOB.split(BLANK);
            new Identity(tabFolder, SWT.NONE, new Contact(BOB, bob_split[0], bob_split[1],
                    Messages.ExtendedRSA_Visual_29, Messages.ExtendedRSA_Visual_30), txtExplain);

        } catch (Exception e) {
            LogUtil.logError(e);
        }
    }

    @Override
    public void setFocus() {
    }
}
