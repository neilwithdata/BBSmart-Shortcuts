package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.dialogs.NewMacroDialog;

import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class DropDown extends VerticalFieldManager implements ComponentChangeListener{

	private RichTextField label;
	private ActiveEditField editField;
	private AutoCompleteComponent autoComplete;
	
	public static final int DROPDOWN_TYPE_PLAIN    = 1;
	public static final int DROPDOWN_TYPE_EMAIL    = 2;
	
	public DropDown() {
		label = new RichTextField("", Field.NON_FOCUSABLE);
		add(label);
		
	    autoComplete = new AutoCompleteComponent();
	    autoComplete.addChangeListener(this);
	}
	
	public AutoCompleteComponent getAutoCompleteComponent() {
	    return autoComplete;
	}
	
	public ActiveEditField getEditField() {
	    return editField;
	}
	
	public String getText() {
		return editField.getText();
	}

	public void onComponentChange(Component component, int eventType) {
        //TODO: Obviously, get rid of this binding.  Temp hack.
        NewMacroDialog screen = (NewMacroDialog) getScreen();
        
        if (eventType == ComponentChangeListener.COMPONENT_DISPLAY) {
            screen.displayComponent(autoComplete);
        }
        else {
            screen.dismissComponent();
        }
    }

    public void setEditField(EditField field) {
	    this.editField = new ActiveEditField(field);
	    
	    autoComplete.setObservedField(editField);
	    
	    add(editField);
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public void setText(String text) {
		editField.setText(text);
	}

	protected boolean keyDown(int keycode, int time) {
	    // User shouldn't need to press enter on these fields.
	    // We do this to avoid a UI bug.
	    if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
	        return true;
	    }
	    
	    return super.keyDown(keycode, time);
	}
	
	protected void onDisplay() {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        store.remove(Config.RUNTIME_STORE_UID_ID);
	}

	protected void sublayout(int width, int height) {
		int xOffset = 0;
		int yOffset = getPaddingTop();
		
		layoutChild(label, width, height);
		setPositionChild(label, xOffset, yOffset);
		
		xOffset += 20;
		yOffset += label.getHeight() + 2;
		int editFieldWidth = width - 20;
		
		layoutChild(editField, editFieldWidth, height);
		setPositionChild(editField, xOffset, yOffset);
		
		yOffset += editField.getHeight();
		
		setExtent(width, yOffset);
	}

}
