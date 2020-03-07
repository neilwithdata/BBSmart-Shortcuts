package com.bbsmart.mobile.bb.gogo.macros;

import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.text.NumericTextFilter;

import com.bbsmart.mobile.bb.gogo.components.IMacroManager;
import com.bbsmart.mobile.bb.gogo.components.MacroManager;
import com.bbsmart.mobile.bb.gogo.dialogs.NewMacroDialog;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services.FullNameAutoCompleteService;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services.PhoneNumberAutoCompleteService;
import com.bbsmart.mobile.bb.gogo.util.CodeModuleUtils;

public class MessagingMacroManager extends MacroManager implements IMacroManager {
	
    public static MessagingMacroManager instance = new MessagingMacroManager();

    private final int FIELD_NAME    = 0;
    private final int FIELD_PHONE   = 1;

    private String name;
    private String phone;

	protected MessagingMacroManager() {
		super();
	}
	
	public void doEditMacroAction(Shortcut shortcut) {
        NewMacroDialog dialog = new NewMacroDialog();
        dialog.setTitle("New Messaging Shortcut");

        dialog.setLabel(                  FIELD_NAME, "Name:");
        dialog.setEditField(              FIELD_NAME, new EditField("", shortcut.getParam("name")));
        dialog.setAutoCompleteService(    FIELD_NAME, new FullNameAutoCompleteService());
        
        dialog.setLabel(                  FIELD_PHONE, "Phone:");
        dialog.setEditField(              FIELD_PHONE, new EditField(
                "", 
                shortcut.getParam("phone"),
                BasicEditField.DEFAULT_MAXCHARS,
                EditField.FILTER_DEFAULT));
        dialog.setAutoCompleteService(    FIELD_PHONE, new PhoneNumberAutoCompleteService());
        
        if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
            name = dialog.getValue(FIELD_NAME);
            phone = sanitize(dialog.getValue(FIELD_PHONE));
            
            CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

            Shortcut newShortcut = new Shortcut();
            newShortcut.setCodeModule(codeModule);
            newShortcut.setParam("name", name);
            newShortcut.setParam("phone", phone);
            newShortcut.setTooltip(phone);

            getRegistry().replace(shortcut, newShortcut);
            getRegistry().store();
        }
	}

	public void doNewMacroAction() {
		NewMacroDialog dialog = new NewMacroDialog();
		dialog.setTitle("New Messaging Shortcut");

        dialog.setLabel(                  FIELD_NAME, "Name:");
        dialog.setEditField(              FIELD_NAME, new EditField());
        dialog.setAutoCompleteService(    FIELD_NAME, new FullNameAutoCompleteService());
        
        dialog.setLabel(                  FIELD_PHONE, "Phone:");
        dialog.setEditField(              FIELD_PHONE, new EditField(EditField.FILTER_DEFAULT));
        dialog.setAutoCompleteService(    FIELD_PHONE, new PhoneNumberAutoCompleteService());
		
		if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
			name = dialog.getValue(FIELD_NAME);
			phone = sanitize(dialog.getValue(FIELD_PHONE));
			
			CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

			Shortcut newShortcut = new Shortcut();
			newShortcut.setCodeModule(codeModule);
			newShortcut.setParam("name", name);
			newShortcut.setParam("phone", phone);
			newShortcut.setTooltip(phone);

			getRegistry().add(newShortcut);
			getRegistry().store();
		}
	}
	
	public String getTooltip(Shortcut shortcut) {
	    return (String) shortcut.getParams().get("phone");
	}
	
	private String sanitize(String original) {
	    StringBuffer buf = new StringBuffer();
	    NumericTextFilter filter = new NumericTextFilter();
	    
	    char c;
	    int originalLen = original.length();
	    
	    for (int i = 0; i < originalLen; i++) {
	        c = original.charAt(i);

	        // Let int'l numbers through.
	        if (i == 0) {
	            if (c == '+') {
	                buf.append(c);
	                continue;
	            }
	        }
	        if (filter.validate(c)) {
	            buf.append(filter.convert(c, 0));
	        }
	    }
	    
	    return buf.toString();
	}
	
}
