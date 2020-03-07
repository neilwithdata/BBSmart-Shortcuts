package com.bbsmart.mobile.bb.gogo.components;

import java.util.Enumeration;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.menu.Menu;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.MacroRegistry;
import com.bbsmart.mobile.bb.gogo.runtime.PersistableRegistryListener;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class MacroManagerList extends VerticalFieldManager implements PersistableRegistryListener, IMenuContributor {

    protected Field movingField;
    protected MacroManager parent;
    protected MacroRegistry registry;

    private int lineHeight = 0;

    private boolean isMoving;
    private int moveDirection;
    private int moveStartingIndex;
    private int buttonWithFocusIndex = 0;
    private int firstDisplayableButtonIndex = 0;
    private int lastSelectedFocusFieldIndex = 0;
    private int numDisplayableButtons = 0;

    public MacroManagerList(MacroManager parent) {
        super(NO_VERTICAL_SCROLL);
        this.parent = parent;
    }

    public void deleteButton(TextButton button) {
        delete(button);

        if (buttonWithFocusIndex == 0) {
            if (getFieldCount() > 0) {
                getField(buttonWithFocusIndex).setFocus();
            }
        }
        else {
            // "Scroll" up when we delete unless we're already at the start.
            if (firstDisplayableButtonIndex != 0) {
                firstDisplayableButtonIndex--;
            }

            if (buttonWithFocusIndex > getFieldCount() - 1) {
                buttonWithFocusIndex--;
            }

            lastSelectedFocusFieldIndex = buttonWithFocusIndex;

            getField(buttonWithFocusIndex).setFocus();
        }

        invalidate();
    }
    
    public MacroRegistry getRegistry() {
        return registry;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void makeContextMenu(Menu menu) {
        if (contextActionsEnabled()) {
            menu.add("Delete", Config.deleteItemAccelerator).setRunnable(new Runnable() {
                public void run() {
                    deleteMacro();
                }
            });
            
            menu.add("Edit", Config.editItemAccelerator).setRunnable(new Runnable() {
                public void run() {
                    editMacro();
                }
            });

            if (getFieldCount() > 1) {
                menu.add("Move", Config.moveItemAccelerator).setRunnable(new Runnable() {
                    public void run() {
                        startMoving();
                    }
                });
            }
        }
    }

    public void onPersistableRegistryAdd(Object object) {
        Shortcut shortcut = (Shortcut) object;
        
        TextButton button = new TextButton();
        button.setAlignment(ActiveField.ALIGN_LEFT | ActiveField.ALIGN_MIDDLE);
        button.setPadding(2);
        button.setRunnable(shortcut);
        button.setText((String) shortcut.getParams().get("name"));
        button.setToolTip(shortcut.getTooltip());
        button.setBackgroundColor(TextButton.STATE_FOCUSED, 0x006B86B5);
        button.setBackgroundColor(TextButton.STATE_UNFOCUSED, Config.COLOR_TEXT_INACTIVE_BACKGROUND);
        button.setForegroundColor(TextButton.STATE_FOCUSED, Config.COLOR_TEXT_ACTIVE_FOREGROUND);
        button.setForegroundColor(TextButton.STATE_UNFOCUSED, 0x00505050);
        
        add(button);
        
        if (isVisible()) {
            invalidate();
        }
    }
    
    public void onPersistableRegistryRemove(Object object) {
    }
    
    public void onPersistableRegistryReplace(Object object) {
        TextButton focusField = (TextButton) getFieldWithFocus();
        Shortcut shortcut = (Shortcut) object;
        focusField.setRunnable(shortcut);
        focusField.setText((String) shortcut.getParams().get("name"));
        focusField.setToolTip(shortcut.getTooltip());
        invalidate();
    }

    public void setLineHeight(int height) {
        this.lineHeight = height;
    }
    
    public void setRegistry(MacroRegistry registry) {
        this.registry = registry;
        registry.addListener(this);
    }
    
    protected void deleteMacro() {
        TextButton focusField = (TextButton) getFieldWithFocus();
        
        Runnable focusFieldRunnable = focusField.getRunnable();

        for (Enumeration en = registry.elements(); en.hasMoreElements();) {
            Shortcut shortcut = (Shortcut) en.nextElement();
            if (shortcut == focusFieldRunnable) {
                // Remove shortcut from registry first.  When we remove from the manager
                // we'll relayout and the registry needs to have the right number of
                // shortcuts.
                registry.remove(shortcut);
                registry.store();
                deleteButton(focusField);
                break;
            }
        }
    }
    
    protected void editMacro() {
        TextButton focusField = (TextButton) getFieldWithFocus();
        
        Runnable focusFieldRunnable = focusField.getRunnable();

        for (Enumeration en = registry.elements(); en.hasMoreElements();) {
            Shortcut shortcut = (Shortcut) en.nextElement();
            if (shortcut == focusFieldRunnable) {
                // Remove shortcut from registry first.  When we remove from the manager
                // we'll relayout and the registry needs to have the right number of
                // shortcuts.
                parent.doEditMacroAction(shortcut);
                break;
            }
        }
    }

    protected boolean keyChar(char c, int status, int time) {
        if (isMoving) {
            if (c == Characters.ESCAPE) {
                undoMove();
            }
            else if (c == Characters.SPACE) {
                stopMoving();
            }
            else if (c == Characters.ENTER) {
                navigationClick(status, time);
            }
            return true;
        }

        if (c == Config.deleteItemAccelerator) {
            deleteMacro();
            return true;
        }
        
        if (c == Config.editItemAccelerator) {
            editMacro();
            return true;
        }

        if (getFieldCount() > 1) {
            if (c == Config.moveItemAccelerator) {
                startMoving();
                return true;
            }

            if (c == Characters.SPACE) {
                startMoving();
                return true;
            }
        }

        return super.keyChar(c, status, time);
    }
    
    protected boolean keyDown(int keycode, int time) {
        // On "del" key (BACKSPACE for some reason), delete selected toolbar item.
        if (Keypad.key(keycode) == Keypad.KEY_BACKSPACE) {
            deleteMacro();
            return true;
        }

        if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
            return navigationClick(KeypadListener.STATUS_TRACKWHEEL, time);
        }
        
        return super.keyDown(keycode, time);
    }

    protected boolean handleMovementNavigation(int dy) {
        if (dy > 0) {
            return moveDown();
        }
        if (dy < 0) {
            return moveUp();
        }
        
        return false;
    }
    
    protected boolean navigationClick(int status, int time) {
        if (isMoving) {
            stopMoving();
            return true;
        }

        return super.navigationClick(status, time);
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        if (handleMovementNavigation(dy)) {
            return true;
        }
        
        if (! isMoving) {
            return super.navigationMovement(dx, dy, status, time);
        }
        
        return true;
    }
    
//    protected int nextFocus(int direction, int axis) {
//        if (isMoving) {
//            return getFieldWithFocusIndex();
//        }
//        else {
//            return super.nextFocus(direction, axis);
//        }
//    }
    
    protected void onFocus(int direction) {
        if (direction != 0) {
            if (getFieldCount() > 0) {
                getField(lastSelectedFocusFieldIndex).setFocus();
            }
        }
        else {
            super.onFocus(direction);
        }
    }

    protected void sublayout(int width, int height) {
        int numFields = getFieldCount();
        int totalHeight = 0;

        numDisplayableButtons = 0;
        
        for (int i = 0; i < numFields; i++) {
            Field field = getField(i);
            
            layoutChild(field, width, height);
            
            totalHeight += field.getHeight();

            if (totalHeight < height) {
                numDisplayableButtons++;
            }
        }
        
        int multiple = height / lineHeight;
        int extentHeight = lineHeight * multiple;

        setExtent(width, extentHeight);
    }
    
    protected void subpaint(Graphics graphics) {
        int yOffset = 0;
        int numButtonsDisplayed = 0;

        int fieldCount = getFieldCount();
        
        int i;
        for (i = 0; i < fieldCount; i++) {
            if (i < firstDisplayableButtonIndex) {
                continue;
            }
            
            Field field = getField(i);
            
            setPositionChild(field, 0, yOffset);
            paintChild(graphics, field);
            
            // This should be moved to the upcoming animation/effects framework
            if (movingField != null) {
                if (movingField == field) {
                    XYRect r = field.getContentRect();
                    int origAlpha = graphics.getGlobalAlpha();
                    int origColor = graphics.getColor();
                    graphics.setColor(Color.WHITE);
                    graphics.setGlobalAlpha(0xC0);
                    graphics.fillRect(r.x, r.y, r.width, r.height);
                    graphics.setColor(origColor);
                    graphics.setGlobalAlpha(origAlpha);
                }
            }
            
            yOffset += field.getHeight();
            
            numButtonsDisplayed++;
            
            if (numButtonsDisplayed == numDisplayableButtons) {
                break;
            }
        }
    }

    private boolean moveUp() {
        if (buttonWithFocusIndex == 0) {
            return false;
        }

        int nextIndex = buttonWithFocusIndex - 1;

        lastSelectedFocusFieldIndex = nextIndex;

        if (isMoving) {
            swapIndices(buttonWithFocusIndex, nextIndex);
        }

        getField(nextIndex).setFocus();
        
        moveDirection--;
        buttonWithFocusIndex--;

        if (buttonWithFocusIndex < firstDisplayableButtonIndex) {
            firstDisplayableButtonIndex--;
        }

        invalidate();
        
        return true;
    }

    private boolean moveDown() {
        if (buttonWithFocusIndex == getFieldCount() - 1) {
            return false;
        }

        int nextIndex = buttonWithFocusIndex + 1;

        lastSelectedFocusFieldIndex = nextIndex;

        if (isMoving) {
            swapIndices(buttonWithFocusIndex, nextIndex);
        }

        getField(nextIndex).setFocus();

        moveDirection++;
        buttonWithFocusIndex++;

        if (buttonWithFocusIndex > (firstDisplayableButtonIndex + numDisplayableButtons - 1)) {
            firstDisplayableButtonIndex++;
        }

        invalidate();
        
        return true;
    }
    
    protected void startMoving() {
        if (isMoving) {
            return;
        }
        
        if (getFieldCount() < 2) {
            return;
        }

        isMoving = true;
        moveDirection = 0;
        moveStartingIndex = getFieldWithFocusIndex();
        movingField = (TextButton) getFieldWithFocus();
        invalidate();
    }
    
    protected void stopMoving() {
        if (! isMoving) {
            return;
        }

        // Ok, listen up.  If we're moving right then when we remove the element all the
        // elements will slide to down one which will throw off the new index unless we
        // correct it, which we do.  Moving left will not throw it off.
        if (moveDirection != 0) {
            int newIndex = moveStartingIndex + moveDirection;
            
            registry.moveElement(moveStartingIndex, newIndex);
            registry.store();

            moveDirection = 0;
        }

        isMoving = false;
        moveDirection = 0;
        movingField = null;
        invalidate();
    }
    
    protected void undoMove() {
        if (! isMoving) {
            return;
        }

        if (moveDirection != 0) {
            int currentIndex = moveStartingIndex + moveDirection;

            swapIndices(moveStartingIndex, currentIndex);

            moveDirection = 0;
        }

        isMoving = false;
        buttonWithFocusIndex = moveStartingIndex;

        invalidate();
    }

    private boolean contextActionsEnabled() {
        return getFieldCount() > 0;
    }

    private void swapButtons(TextButton p, TextButton q) {
        NullField pN = new NullField();
        NullField qN = new NullField();
        
        replace(p, pN);
        replace(q, qN);
        
        replace(pN, q);
        replace(qN, p);
    }

    private void swapIndices(int p, int q) {
        TextButton f1 = (TextButton) getField(p);
        TextButton f2 = (TextButton) getField(q);
        
        swapButtons(f1, f2);
    }

}
