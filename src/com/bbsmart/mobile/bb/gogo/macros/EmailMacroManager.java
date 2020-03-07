package com.bbsmart.mobile.bb.gogo.macros;

import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.EmailAddressEditField;

import com.bbsmart.mobile.bb.gogo.components.IMacroManager;
import com.bbsmart.mobile.bb.gogo.components.MacroManager;
import com.bbsmart.mobile.bb.gogo.dialogs.NewMacroDialog;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services.EmailAutoCompleteService;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services.FullNameAutoCompleteService;
import com.bbsmart.mobile.bb.gogo.util.CodeModuleUtils;

public class EmailMacroManager extends MacroManager implements IMacroManager {
	
    public static EmailMacroManager instance = new EmailMacroManager();
    
    final int FIELD_NAME    = 0;
    final int FIELD_EMAIL   = 1;

    private String name;
    private String email;

	protected EmailMacroManager() {
		super();
	}
	
    public void doEditMacroAction(Shortcut shortcut) {
        NewMacroDialog dialog = new NewMacroDialog();
        dialog.setTitle("New Email Shortcut");
        
        dialog.setLabel(                  FIELD_NAME, "Name:");
        dialog.setEditField(              FIELD_NAME, new EditField("", shortcut.getParam("name")));
        dialog.setAutoCompleteService(    FIELD_NAME, new FullNameAutoCompleteService());
        
        dialog.setLabel(                  FIELD_EMAIL, "Email:");
        dialog.setEditField(              FIELD_EMAIL, new EmailAddressEditField("", shortcut.getParam("email")));
        dialog.setAutoCompleteService(    FIELD_EMAIL, new EmailAutoCompleteService());

        if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
            name = dialog.getValue(FIELD_NAME);
            email = dialog.getValue(FIELD_EMAIL);
            
            CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

            Shortcut newShortcut = new Shortcut();
            newShortcut.setCodeModule(codeModule);
            newShortcut.setParam("name", name);
            newShortcut.setParam("email", email);
            newShortcut.setTooltip(email);

            getRegistry().replace(shortcut, newShortcut);
            getRegistry().store();
        }
    }

	public void doNewMacroAction() {
		NewMacroDialog dialog = new NewMacroDialog();
		dialog.setTitle("New Email Shortcut");
		
		dialog.setLabel(                  FIELD_NAME, "Name:");
		dialog.setEditField(              FIELD_NAME, new EditField());
		dialog.setAutoCompleteService(    FIELD_NAME, new FullNameAutoCompleteService());
		
		dialog.setLabel(                  FIELD_EMAIL, "Email:");
		dialog.setEditField(              FIELD_EMAIL, new EmailAddressEditField("", ""));
		dialog.setAutoCompleteService(    FIELD_EMAIL, new EmailAutoCompleteService());

		if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
			name = dialog.getValue(FIELD_NAME);
			email = dialog.getValue(FIELD_EMAIL);
			
			CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

			Shortcut newShortcut = new Shortcut();
			newShortcut.setCodeModule(codeModule);
			newShortcut.setParam("name", name);
			newShortcut.setParam("email", email);
			newShortcut.setTooltip(email);

			getRegistry().add(newShortcut);
			getRegistry().store();
		}
	}
	
	public String getTooltip(Shortcut shortcut) {
	    return (String) shortcut.getParams().get("email");
	}
	
}
