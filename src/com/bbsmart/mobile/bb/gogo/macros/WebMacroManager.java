package com.bbsmart.mobile.bb.gogo.macros;

import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.EditField;

import com.bbsmart.mobile.bb.gogo.components.IMacroManager;
import com.bbsmart.mobile.bb.gogo.components.MacroManager;
import com.bbsmart.mobile.bb.gogo.dialogs.NewMacroDialog;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services.WebAutoCompleteService;
import com.bbsmart.mobile.bb.gogo.util.CodeModuleUtils;

public class WebMacroManager extends MacroManager implements IMacroManager {
	
    public static WebMacroManager instance = new WebMacroManager();

    private final int FIELD_NAME    = 0;
    private final int FIELD_URL     = 1;

    private String name;
    private String url;

	protected WebMacroManager() {
		super();
	}
	
	public void doEditMacroAction(Shortcut shortcut) {
        NewMacroDialog dialog = new NewMacroDialog();
        dialog.setTitle("New Web Shortcut");
        
        dialog.setLabel(                  FIELD_NAME, "Title:");
        dialog.setEditField(              FIELD_NAME, new EditField("", shortcut.getParam("name")));
        
        dialog.setLabel(                  FIELD_URL, "URL:");
        dialog.setEditField(              FIELD_URL, new EditField(
                "", 
                shortcut.getParam("url"),
                BasicEditField.DEFAULT_MAXCHARS,
                EditField.FILTER_URL));
        dialog.setAutoCompleteService(    FIELD_URL, new WebAutoCompleteService());
        
        if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
            name = dialog.getValue(FIELD_NAME);
            url = dialog.getValue(FIELD_URL);
            
            CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

            Shortcut newShortcut = new Shortcut();
            newShortcut.setCodeModule(codeModule);
            newShortcut.setParam("name", name);
            newShortcut.setParam("url", url);
            newShortcut.setTooltip(url);
            
            getRegistry().replace(shortcut, newShortcut);
            getRegistry().store();
        }
	}

	public void doNewMacroAction() {
		NewMacroDialog dialog = new NewMacroDialog();
		dialog.setTitle("New Web Shortcut");
		
		dialog.setLabel(                  FIELD_NAME, "Title:");
		dialog.setEditField(              FIELD_NAME, new EditField());
		
		dialog.setLabel(                  FIELD_URL, "URL:");
		dialog.setEditField(              FIELD_URL, new EditField(EditField.FILTER_URL));
		dialog.setAutoCompleteService(    FIELD_URL, new WebAutoCompleteService());
		
		if (dialog.open() == NewMacroDialog.DIALOG_RESULT_OK) {
			name = dialog.getValue(FIELD_NAME);
			url = dialog.getValue(FIELD_URL);
			
			CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(this.moduleName);

			Shortcut newShortcut = new Shortcut();
			newShortcut.setCodeModule(codeModule);
			newShortcut.setParam("name", name);
			newShortcut.setParam("url", url);
			newShortcut.setTooltip(url);

			getRegistry().add(newShortcut);
			getRegistry().store();
		}
	}
	
	public String getTooltip(Shortcut shortcut) {
	    return (String) shortcut.getParams().get("url");
	}
	
}
