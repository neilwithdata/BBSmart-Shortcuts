package com.bbsmart.mobile.bb.gogo.components;

import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.adapters.IModelAdapter;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.IAutoCompleteListener;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.IAutoCompleteService;

import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;

public class AutoCompleteComponent extends Component implements FieldChangeListener, FocusChangeListener, IAutoCompleteListener {

    private static final int OFFSET                 = 3;
    private static final int PADDING                = 1;
    private static final int MAX_DISPLAYABLE_ITEMS  = 3;

    private int x;
    private int y;
    private int rectWidth;
    private int rectHeight;
    private int firstDisplayedItemIndex = 0;
    private int curSelectionIndex;
    private boolean isLayoutValid;
    private boolean isActive = false;
    private boolean fieldChangedViaNavClick = false;
    
    private ActiveEditField editField;
    private Vector response;
    private Observable observedField;
    private IAutoCompleteService autoCompleteService;
    private IModelAdapter adapter;
    
    private Font font = Font.getDefault().derive(Font.PLAIN, 14, Ui.UNITS_px);
    
    public void setObservedField(Observable observable) {
        // Doing it like this only until the component hierarchy gets straightened out.
        this.observedField = observable;
        this.editField = (ActiveEditField) observable;
        
        editField.setChangeListener(this);
        editField.setFocusListener(this);
    }

    public void fieldChanged(Field field, int context) {
        if (fieldChangedViaNavClick) {
            fieldChangedViaNavClick = false;
            return;
        }

        if (isActive) {
            requestAutoComplete();
        }
    }
    
    public void focusChanged(Field field, int eventType) {
        if (eventType == FocusChangeListener.FOCUS_GAINED) {
            isActive = true;
            requestAutoComplete();
        }
        else if (eventType == FocusChangeListener.FOCUS_LOST) {
            isActive = false;
            notifyComponentChangeListeners(ComponentChangeListener.COMPONENT_DISMISS);
        }
    }
    
    public IAutoCompleteService getAutoCompleteService() {
        return autoCompleteService;
    }
    
    public boolean keyDown(int keycode, int time) {
        if (Keypad.key(keycode) == Keypad.KEY_ESCAPE) {
            notifyComponentChangeListeners(ComponentChangeListener.COMPONENT_DISMISS);
            return true;
        }
        
        return super.keyDown(keycode, time);
    }

    public boolean navigationClick(int status, int time) {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        store.remove(Config.RUNTIME_STORE_UID_ID);

        int numEntries = response.size();

        for (int i = 0; i < numEntries; i++) {
            if (i == curSelectionIndex) {
                fieldChangedViaNavClick = true;

                Object item = response.elementAt(i);
                editField.setText(adapter.toString(item));

                //TODO: Move business logic out of here.
                if (item instanceof AddressBookContact) {
                    AddressBookContact contact = (AddressBookContact) item;
                    store.put(Config.RUNTIME_STORE_UID_ID, contact.uid);
                }
            }
        }
        
        // No matter what, we're going to dismiss the component here.
        notifyComponentChangeListeners(ComponentChangeListener.COMPONENT_DISMISS);
        
        return true;
    }

    public boolean navigationMovement(int dx, int dy, int status, int time) {
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        store.remove(Config.RUNTIME_STORE_UID_ID);

        int numEntries = response.size();
        
        if (dy > 0) {
            if (curSelectionIndex < numEntries - 1) {
                curSelectionIndex++;
                
                if (curSelectionIndex > MAX_DISPLAYABLE_ITEMS - 1) {
                    firstDisplayedItemIndex++;
                }

                return true;
            }
        }
        else if (dy < 0) {
            if (curSelectionIndex >= 0) {
                curSelectionIndex--;
                
                if (curSelectionIndex < firstDisplayedItemIndex) {
                    if (firstDisplayedItemIndex > 0) {
                        firstDisplayedItemIndex--;
                    }
                }

                return true;
            }
        }
        
        return false;
    }

    public void onAutoCompleteResponse(Vector response, IModelAdapter adapter) {
        this.response = response;
        this.adapter = adapter;

        if (response.size() == 0) {
            notifyComponentChangeListeners(ComponentChangeListener.COMPONENT_DISMISS);
        }
        else {
            isLayoutValid = false;
            notifyComponentChangeListeners(ComponentChangeListener.COMPONENT_DISPLAY);
            return;
        }
    }

    public void paint(Graphics graphics) {
        if (shouldLayout()) {
            layout();
        }
        
        int backgroundColor;
        int foregroundColor;

        int origColor = graphics.getColor();
        Font origFont = graphics.getFont();

        graphics.setFont(font);
        
        graphics.setColor(Color.LIGHTYELLOW);
        graphics.fillRect(x, y, rectWidth, rectHeight);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, rectWidth, rectHeight);
        
        int yOffset = y;
        int numEntries = response.size();
        int numDisplayedItems = 0;
        for (int i = 0; i < numEntries; i++) {
            if (i < firstDisplayedItemIndex) {
                continue;
            }

            Object item = response.elementAt(i);
            if (adapter != null) {
                if (i == curSelectionIndex) {
                    backgroundColor = Config.COLOR_TEXT_ACTIVE_BACKGROUND;
                    foregroundColor = Config.COLOR_TEXT_ACTIVE_FOREGROUND;
                }
                else {
                    backgroundColor = Config.COLOR_TEXT_INACTIVE_BACKGROUND;
                    foregroundColor = Config.COLOR_TEXT_INACTIVE_FOREGROUND;
                }
                
                XYRect region = new XYRect(x + PADDING, yOffset + PADDING, 
                        rectWidth - (PADDING * 2), font.getHeight());
                graphics.pushRegion(region);
                
                graphics.setColor(backgroundColor);
                paintItemBackground(graphics);

                graphics.setColor(foregroundColor);
                adapter.paint(item, graphics);

                graphics.popContext();
            }
            yOffset += font.getHeight() + PADDING;
            
            numDisplayedItems++;
            
            if (numDisplayedItems == MAX_DISPLAYABLE_ITEMS) {
                break;
            }
        }
        
        graphics.setColor(origColor);
        graphics.setFont(origFont);
    }
    
    public void setAdapter(IModelAdapter adapter) {
        this.adapter = adapter;
    }
    
    public void setAutoCompleteService(IAutoCompleteService autoCompleteService) {
        this.autoCompleteService = autoCompleteService;
        
        autoCompleteService.addAutoCompleteListener(this);
    }

    public boolean shouldLayout() {
        if (response == null) {
            return false;
        }

        if (isLayoutValid == false) {
            return true;
        }
        
        return false;
    }
    
    protected void layout() {
        XYRect fieldRect = observedField.getAbsoluteClippingRect();
        
        int xOffset = fieldRect.x + OFFSET;
        int yOffset = fieldRect.y + fieldRect.height;
        
        int numEntries = response.size();
        if (numEntries > MAX_DISPLAYABLE_ITEMS) {
            numEntries = MAX_DISPLAYABLE_ITEMS;
        }

        rectWidth = fieldRect.width - (OFFSET * 2);
        rectHeight = (numEntries * font.getHeight()) + ((numEntries + 1) * PADDING);
        
        setPosition(xOffset, yOffset);
        
        curSelectionIndex = -1;

        isLayoutValid = true;
    }
    
    protected void paintItemBackground(Graphics graphics) {
        XYRect clip = graphics.getClippingRect();
        
        graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    private void requestAutoComplete() {
        String text = editField.getEditField().getText();

        if (autoCompleteService != null) {
            autoCompleteService.requestAutoComplete(text);
        }
    }

}
