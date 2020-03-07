package com.bbsmart.mobile.bb.gogo.components;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.MacroRegistry;
import com.bbsmart.mobile.bb.gogo.runtime.PersistableRegistryListener;

public abstract class MacroManager extends VerticalFieldManager implements IMacroManager, PersistableRegistryListener {

    protected static final int BORDER_WIDTH = 1;
    protected static final int PADDING      = 2;

    protected MacroManagerHeader header;
    protected MacroManagerList macroManagerList;
    protected MacroRegistry registry;
	protected String moduleName;
	protected Vector statusListeners = new Vector();
	
	protected boolean isFlippable;
	
	protected MacroManager() {
	    super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);
	    
	    header = new MacroManagerHeader();
        header.setBackgroundColor(ActiveField.STATE_FOCUSED, 0x00E7EBF7);
        header.setBackgroundColor(ActiveField.STATE_UNFOCUSED, 0x00E7EBF7);
        header.setForegroundColor(ActiveField.STATE_FOCUSED, Color.DARKGRAY);
        header.setForegroundColor(ActiveField.STATE_UNFOCUSED, Color.DARKGRAY);
		add(header);
		
		macroManagerList = new MacroManagerList(this);
		macroManagerList.setLineHeight(Font.getDefault().getHeight() + (PADDING * 2));
		add(macroManagerList);
	}
	
    public void addStatusListener(MacroManagerStatusListener listener) {
        if (!statusListeners.contains(listener)) {
            statusListeners.addElement(listener);
        }
    }

	public Runnable getNewShortcutRunnable() {
		return new Runnable() {
			public void run() {
			    addNewShortcutAction();
			}
		};
	}

	public abstract void doEditMacroAction(Shortcut shortcut);
	
	public abstract void doNewMacroAction();

	public void addNewShortcutAction() {
		// Will eventually be replaced by animation/transition framework.
		//((Shortcuts) getScreen()).fadeOut();
		
		doNewMacroAction();
		
		//((Shortcuts) getScreen()).fadeIn();
	}
	
    public MacroRegistry getRegistry() {
        return macroManagerList.getRegistry();
    }

	public boolean hasMacros() {
	    return registry.size() > 0;
	}

	public boolean isActive() {
	    return false;
	}
	
    public void onFocus(int direction) {
        header.setBackgroundColor(TextButton.STATE_FOCUSED, 0x00395994);
        header.setBackgroundColor(TextButton.STATE_UNFOCUSED, 0x00395994);
        header.setForegroundColor(TextButton.STATE_FOCUSED, Color.WHITE);
        header.setForegroundColor(TextButton.STATE_UNFOCUSED, Color.WHITE);
        super.onFocus(direction);
        invalidate();
    }
    
    public void onPersistableRegistryAdd(Object object) {
        fireMacroAddedEvent();
    }

    public void onPersistableRegistryRemove(Object object) {
        if (registry.size() == 0) {
            fireMacroManagerEmptyEvent();
        }
    }
    
    public void onPersistableRegistryReplace(Object object) {
        // empty
    }

    public void onUnfocus() {
        header.setBackgroundColor(TextButton.STATE_FOCUSED, 0x00E7EBF7);
        header.setBackgroundColor(TextButton.STATE_UNFOCUSED, 0x00E7EBF7);
        header.setForegroundColor(TextButton.STATE_FOCUSED, Color.DARKGRAY);
        header.setForegroundColor(TextButton.STATE_UNFOCUSED, Color.DARKGRAY);
        super.onUnfocus();
        invalidate();
    }

    public void setFlippable(boolean isFlippable) {
        this.isFlippable = isFlippable;
        
        if (isFlippable) {
            header.setScrollingEnabled(true);
        }
        else {
            header.setScrollingEnabled(false);
        }
    }

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

    public void setRegistry(MacroRegistry registry) {
        this.registry = registry;
        registry.addListener(this);
        macroManagerList.setRegistry(registry);
    }
	
	public void setTitle(String title) {
	    header.setTitle(title);
	}
	
    protected void fireMacroAddedEvent() {
        for (Enumeration enumeration = statusListeners.elements(); enumeration.hasMoreElements();) {
            MacroManagerStatusListener listener = (MacroManagerStatusListener) enumeration.nextElement();
            listener.onMacroAdded(this);
        }
    }

    protected void fireMacroManagerEmptyEvent() {
        for (Enumeration enumeration = statusListeners.elements(); enumeration.hasMoreElements();) {
            MacroManagerStatusListener listener = (MacroManagerStatusListener) enumeration.nextElement();
            listener.onMacroManagerEmpty(this);
        }
    }
    
    protected int nextFocus(int direction, int axis) {
        if (macroManagerList.isMoving()) {
            return 1;
        }
        else {
            return super.nextFocus(direction, axis);
        }
    }
    
	// Layout policy: Use all width; expand vertically to fit as many even list items as possible.
	protected void sublayout(int width, int height) {
		int childWidth = width - (BORDER_WIDTH * 2);
		int childHeight = height - (BORDER_WIDTH * 2);
		int totalHeight = BORDER_WIDTH * 2;

		layoutChild(header, childWidth, childHeight);
		totalHeight += header.getHeight();
		childHeight -= header.getHeight();

		layoutChild(macroManagerList, childWidth, childHeight);
		totalHeight += macroManagerList.getHeight();
		
		int xOffset = BORDER_WIDTH;
		int yOffset = BORDER_WIDTH;
		
		setPositionChild(header, xOffset, yOffset);
		yOffset += header.getHeight();
		
		setPositionChild(macroManagerList, xOffset, yOffset);

		setExtent(width, totalHeight);
	}
	
    protected void subpaint(Graphics graphics) {
        int origColor = graphics.getColor();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(0x006B86B5);
        graphics.drawRect(0, 0, getWidth(), getHeight());
        
        graphics.setColor(origColor);
        
        super.subpaint(graphics);
    }
    
}
