package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.MathUtilities;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.runtime.ShortcutManagerFactory;
import com.bbsmart.mobile.bb.gogo.util.Utils;

public class MacroFlipper extends VerticalFieldManager implements MacroManagerStatusListener {

    private IMacroManager[] macroManagers;
    private IMacroManager currentMacroManager;

    private int currentMacroManagerIndex = -1;

    public MacroFlipper() {
        super(VerticalFieldManager.NO_VERTICAL_SCROLL);
        
        macroManagers = new IMacroManager[4];
        
        addShortcutManager(0, Config.SHORTCUT_MANAGER_TYPE_EMAIL);
        addShortcutManager(1, Config.SHORTCUT_MANAGER_TYPE_MESSAGING);
        addShortcutManager(2, Config.SHORTCUT_MANAGER_TYPE_PHONE);
        addShortcutManager(3, Config.SHORTCUT_MANAGER_TYPE_WEB);
    }

    public void sublayout(int width, int height) {
        int numFields = getFieldCount();
        int xOffset = 5;
        int yOffset = 5;
        int heightAvailable = height - 10;

        for (int i = 0; i < numFields; i++) {
            Field field = getField(i);

            layoutChild(field, width - 10, heightAvailable);
            setPositionChild(field, xOffset, yOffset);
            int fieldHeight = field.getHeight() + 5;
            heightAvailable -= fieldHeight;
            yOffset += fieldHeight;
        }

        setExtent(width, height);
    }

    public void onMacroManagerEmpty(MacroManager manager) {
        if (manager == currentMacroManager) {
            flipToNext();
        }
        else {
            resetFlipState();
        }
    }
    
    public void onMacroAdded(MacroManager manager) {
        if (currentMacroManagerIndex == -1) {
            flipToFirst();
        }
        
        if (manager != currentMacroManager) {
            currentMacroManager.setFlippable(true);
        }
    }
    
    public void onMacroRemoved(MacroManager manager) {
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        if (dx > 0) {
            flipToNext();
            return true;
        }

        if (dx < 0) {
            flipToPrevious();
            return true;
        }

        return super.navigationMovement(dx, dy, status, time);
	}

	protected void onDisplay() {
		flipToFirst();
	}

    protected void subpaint(Graphics graphics) {
        graphics.setColor(0xEFEFEF);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        
        if (currentMacroManagerIndex  != -1) {
            super.subpaint(graphics);
        }
    }

    private void addShortcutManager(int index, int type) {
        MacroManager macroManager = (MacroManager) ShortcutManagerFactory.instance.getManager(type);
        macroManager.addStatusListener(this);
        macroManagers[index] = macroManager;
    }
    
    private boolean canFlip() {
        int availableMacroManagers = 0;

        for (int i = 0; i < 4; i++) {
            IMacroManager nextManager = macroManagers[i];

            MacroManager manager = (MacroManager) Utils.getField(nextManager);

            if (manager.hasMacros()) {
                availableMacroManagers++;
            }
        }
        
        return availableMacroManagers > 1;
    }

    private void flip() {
        currentMacroManager = macroManagers[currentMacroManagerIndex];

        MacroManager macroManagerField = (MacroManager) Utils.getField(currentMacroManager);

        replaceManager(macroManagerField);
        
        macroManagerField.setFocus();
        
        resetFlipState();
    }
    
    private void flipToFirst() {
        for (int i = 0; i < 4; i++) {
            IMacroManager nextManager = macroManagers[i];

            MacroManager manager = (MacroManager) Utils.getField(nextManager);

            if (manager.hasMacros()) {
                currentMacroManagerIndex = i;
                flip();
                return;
            }
        }
        
        // Didn't find any valid ones; don't show anything then.
        currentMacroManagerIndex = -1;
    }

    // precondition: currentMacroManagerIndex != -1
    private void flipToNext() {
        int nextMacroManagerIndex = currentMacroManagerIndex + 1;
        
        nextMacroManagerIndex = MathUtilities.wrap(0, nextMacroManagerIndex, 3);

        while (nextMacroManagerIndex != currentMacroManagerIndex) {
            IMacroManager nextManager = macroManagers[nextMacroManagerIndex];

            MacroManager manager = (MacroManager) Utils.getField(nextManager);

            if (manager.hasMacros()) {
                currentMacroManagerIndex = nextMacroManagerIndex;
                flip();
                return;
            }
            else {
                nextMacroManagerIndex++;
                nextMacroManagerIndex = MathUtilities.wrap(0, nextMacroManagerIndex, 3);
            }
        }
        
        // if we get to here, we couldn't find the next one to flip to...stay
		// put if we've got some macros to display
		MacroManager current = (MacroManager) macroManagers[currentMacroManagerIndex];
		if (!current.hasMacros()) {
			// Didn't find any valid ones; don't show anything then.
			currentMacroManagerIndex = -1;
		}
    }

    private void flipToPrevious() {
        int nextMacroManagerIndex = currentMacroManagerIndex - 1;
        
        nextMacroManagerIndex = MathUtilities.wrap(0, nextMacroManagerIndex, 3);

        while (nextMacroManagerIndex != currentMacroManagerIndex) {
            IMacroManager nextManager = macroManagers[nextMacroManagerIndex];

            MacroManager manager = (MacroManager) Utils.getField(nextManager);

            if (manager.hasMacros()) {
                currentMacroManagerIndex = nextMacroManagerIndex;
                flip();
            }
            else {
                nextMacroManagerIndex--;
                nextMacroManagerIndex = MathUtilities.wrap(0, nextMacroManagerIndex, 3);
            }
        }
    }
    
    private void resetFlipState() {
        if (currentMacroManager != null) {
            if (canFlip()) {
                currentMacroManager.setFlippable(true);
            }
            else {
                currentMacroManager.setFlippable(false);
            }
        }
    }

    private void replaceManager(MacroManager macroManager) {
        deleteAll();
        add(macroManager);
    }

}
