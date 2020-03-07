package com.bbsmart.mobile.bb.gogo.components;

import java.util.Enumeration;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.menu.Menu;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.runtime.PersistableRegistryListener;
import com.bbsmart.mobile.bb.gogo.runtime.ToolbarRegistry;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class Toolbar extends HorizontalFieldManager implements PersistableRegistryListener, IMenuContributor {
	
    private static final int SCROLLBAR_INDICATOR_WIDTH          = 8;
	private static final int SEPARATOR_BAR_HEIGHT		        = 2;
	private static final int VERTICAL_PADDING                   = 2;

//	private Field lastSelectedField;
	private ToolbarRegistry registry = ToolbarRegistry.instance;
	private boolean isMoving;
    // Cheating for now.  This will fix the height of an empty toolbar.
    private int maxFieldHeight = 34;
	private int moveDirection;
	private int moveStartingIndex;
	private int lastSelectedFocusFieldIndex;
	
	private int firstDisplayableButtonIndex = 0;
	private int buttonSpacing = 2;
	private int buttonWithFocusIndex = 0;
	private int numButtonsDisplayed = 0;
	private int numButtonsOnScreen;
	private int numDisplayableButtons;
	private int scrollIndicatorWidth = 2 + SCROLLBAR_INDICATOR_WIDTH + 4;
	
	public Toolbar() {
		super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);
		
		for (Enumeration en = registry.elements(); en.hasMoreElements();) {
			Shortcut shortcut = (Shortcut) en.nextElement();
			addToolbarButton(shortcut, false);
		}
	}
	
	public void makeContextMenu(Menu menu) {
	    if (isMoving) {
	        return;
	    }

	    if (contextActionsEnabled()) {
	        menu.add("Delete", Config.deleteItemAccelerator).setRunnable(new Runnable() {
	            public void run() {
	                deleteToolbarAction();
	            }
	        });
	        
	        if (numButtonsDisplayed > 1) {
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
		addToolbarButton(shortcut, true);
	}

	public void onPersistableRegistryRemove(Object object) {
		//System.out.println("Toolbar.onShortcutRegistryRemove");
	}
	
	public void onPersistableRegistryReplace(Object object) {
	    //System.out.println("Toolbar.onShortcutRegistryRemove");
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

		if (c == Config.deleteItemAccelerator && contextActionsEnabled()) {
			deleteToolbarAction();
			return true;
		}

		if (c == Config.moveItemAccelerator && contextActionsEnabled()) {
			startMoving();
			return true;
		}
		
		if (c == Characters.SPACE) {
			startMoving();
			return true;
		}

		return super.keyChar(c, status, time);
	}
	
	protected boolean keyDown(int keycode, int time) {
	    // On "del" key (BACKSPACE for some reason), delete selected toolbar item.
		if (Keypad.key(keycode) == Keypad.KEY_BACKSPACE) {
			deleteToolbarAction();
			return true;
		}
		
        if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
            return navigationClick(KeypadListener.STATUS_TRACKWHEEL, time);
        }
		
		return super.keyDown(keycode, time);
	}

	protected void handleMovementNavigation(int dx) {
		if (dx > 0) {
			moveRight();
		}
		if (dx < 0) {
			moveLeft();
		}
	}
	
	protected boolean navigationClick(int status, int time) {
		if (isMoving) {
			stopMoving();
			return true;
		}

		return super.navigationClick(status, time);
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
	    handleMovementNavigation(dx);

	    if (! isMoving) {
	        if (dy != 0) {
	            lastSelectedFocusFieldIndex = getFieldWithFocusIndex();
	            return super.navigationMovement(dx, dy, status, time);
	            //invalidate();
	        }
	    }
	    
	    return true;
	}
	
	// This is NOT being called.
//	protected int nextFocus(int direction, boolean alt) {
//		System.out.println("Toolbar.nextFocus int alt");
//		int ret = super.nextFocus(direction, alt);
//		return ret;
//	}
	
	// This is NOT documented but it IS being called. W T F?
	protected int nextFocus(int direction, int wtf) {
		if (wtf == Field.AXIS_SEQUENTIAL) {
			return getFieldWithFocusIndex();
		}
		else {
			return super.nextFocus(direction, wtf);
		}
	}
	
	protected void onDisplay() {
	    registry.addListener(this);

		super.onDisplay();
	}
	
	protected void onExposed() {
	    invalidate();
	}

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
	
	protected void restoreButtonBitmap() {
		ImageButton button = (ImageButton) getFieldWithFocus();
		button.useOriginalBitmap();
		button.setFocus();
	}

	protected void sublayout(int width, int height) {
		int fieldCount = getFieldCount();
		
		for (int i = 0; i < fieldCount; i++) {
			Field field = getField(i);
			layoutChild(field, width, height);
			maxFieldHeight = Math.max(maxFieldHeight, field.getHeight());
		}
		
		int extentHeight = maxFieldHeight + VERTICAL_PADDING * 2 + SEPARATOR_BAR_HEIGHT;
		
		setExtent(width, extentHeight); 
		
		layoutButtonSpacing();
	}
	
	protected void subpaint(Graphics graphics) {
		XYRect content = getContentRect();
		
		int color = graphics.getColor();

		graphics.setColor(0xEFEFEF);
		graphics.fillRect(0, 0, content.width, content.height - SEPARATOR_BAR_HEIGHT);

		graphics.setColor(0x6B86B5);
		graphics.fillRect(0, content.height - SEPARATOR_BAR_HEIGHT, content.width, SEPARATOR_BAR_HEIGHT);

		graphics.setColor(color);
		
		int xOffset = 2;
		
		if (firstDisplayableButtonIndex > 0) {
		    paintLeftScrollIndicator(graphics, xOffset);
		}
		else {
		    paintScrollEndIndicator(graphics, xOffset);
		}
		
		xOffset += SCROLLBAR_INDICATOR_WIDTH + 4 + buttonSpacing;
		numButtonsDisplayed = 0;

		int fieldCount = getFieldCount();
		
		int i;
		for (i = 0; i < fieldCount; i++) {
		    if (i < firstDisplayableButtonIndex) {
		        continue;
		    }
		    
			Field field = getField(i);
			
			setPositionChild(field, xOffset, VERTICAL_PADDING);
			paintChild(graphics, field);
			
			xOffset += field.getWidth() + buttonSpacing;
			
			numButtonsDisplayed++;
			
			if (numButtonsDisplayed == numDisplayableButtons) {
			    break;
			}
		}
		
		if (i < fieldCount - 1) {
		    paintRightScrollIndicator(graphics, getWidth() - (SCROLLBAR_INDICATOR_WIDTH + 2));
		}
		else {
		    paintScrollEndIndicator(graphics, getWidth() - (SCROLLBAR_INDICATOR_WIDTH + 2));
		}
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

        ImageButton button = (ImageButton) getField(moveStartingIndex);
        button.useOriginalBitmap();
        button.setFocus();

        invalidate();
    }
	
//	protected void toggleMove() {
//		if (isMoving) {
//			stopMoving();
//		}
//		else {
//			startMoving();
//		}
//	}

	private void addToolbarButton(Shortcut shortcut, boolean hasFocus) {
        ImageButton ab = new ImageButton(
                Config.APPLICATION_ICON_WIDTH, Config.APPLICATION_ICON_HEIGHT);
        ab.setPadding(4);
        ab.setBitmap(shortcut.getCodeModule().getIcon());
        ab.setRunnable(shortcut);
        ab.setToolTip(shortcut.getCodeModule().getAppName());
        
        add(ab);
        
        if (hasFocus) {
            ab.setFocus();
            buttonWithFocusIndex = getFieldWithFocusIndex();
            lastSelectedFocusFieldIndex = buttonWithFocusIndex;
            int lastDisplayableButtonIndex = firstDisplayableButtonIndex + numDisplayableButtons - 1;
            if (buttonWithFocusIndex > lastDisplayableButtonIndex) {
                int overflow = buttonWithFocusIndex - lastDisplayableButtonIndex;
                firstDisplayableButtonIndex += overflow;
            }
        }
	}
	
    private void deleteToolbarAction() {
        ImageButton focusField = (ImageButton) getFieldWithFocus();
        Runnable focusFieldRunnable = focusField.getRunnable();

        for (Enumeration en = registry.elements(); en.hasMoreElements();) {
            Shortcut shortcut = (Shortcut) en.nextElement();
            if (shortcut == focusFieldRunnable) {
                // Remove shortcut from registry first.  When we remove from the manager
                // we'll relayout and the registry needs to have the right number of
                // shortcuts.
                registry.remove(shortcut);
                registry.store();
                delete(focusField);
                break;
            }
        }

        if (buttonWithFocusIndex == 0) {
            if (getFieldCount() > 0) {
                getField(buttonWithFocusIndex).setFocus();
            }
        }
        else {
            // "Scroll" left when we delete unless we're already at the start.
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

	private boolean contextActionsEnabled() {
		return getFieldCount() > 0;
	}

	private void layoutButtonSpacing() {
	    int width = getWidth();

	    numDisplayableButtons = (width - (scrollIndicatorWidth * 2)) / 34;
	    numButtonsOnScreen = Math.min(registry.size(), numDisplayableButtons);

	    //int totalUnoccupiedWidth = getWidth() - ((numButtonsOnScreen * 34) + (numSpaces * buttonSpacing) + (scrollIndicatorWidth * 2));
	    int totalUnoccupiedWidth = width - ((numButtonsOnScreen * 34) + (scrollIndicatorWidth * 2));

	    buttonSpacing = totalUnoccupiedWidth / (numButtonsOnScreen + 1);
	}

	private void moveLeft() {
	    if (buttonWithFocusIndex == 0) {
	        return;
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
	}

	private void moveRight() {
	    if (buttonWithFocusIndex == getFieldCount() - 1) {
	        return;
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
	}

    private void paintLeftScrollIndicator(Graphics graphics, int xOffset) {
        int indicatorHeight = maxFieldHeight / 3;
        int contentHeight = 38;

        int yOffset = (contentHeight - indicatorHeight) / 2;

        int xPts[] = new int[] { xOffset, xOffset + SCROLLBAR_INDICATOR_WIDTH, xOffset + SCROLLBAR_INDICATOR_WIDTH };
        int yPts[] = new int[] { contentHeight / 2, yOffset, contentHeight - yOffset };

        byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT, 
                Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT };

        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

        int origColor = graphics.getColor();
        graphics.setColor(Color.DARKGRAY);
        graphics.drawFilledPath(xPts, yPts, pointTypes, null);
        graphics.setColor(origColor);
    }

	private void paintRightScrollIndicator(Graphics graphics, int xOffset) {
	    int indicatorHeight = maxFieldHeight / 3;
	    int contentHeight = 38;

	    int yOffset = (contentHeight - indicatorHeight) / 2;

	    int xPts[] = new int[] { xOffset, xOffset + SCROLLBAR_INDICATOR_WIDTH, xOffset };
	    int yPts[] = new int[] { yOffset, contentHeight / 2, contentHeight - yOffset };

	    byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT, 
	            Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT };

	    graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
	    graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

        int origColor = graphics.getColor();
        graphics.setColor(Color.DARKGRAY);
        graphics.drawFilledPath(xPts, yPts, pointTypes, null);
        graphics.setColor(origColor);
	}
	
	private void paintScrollEndIndicator(Graphics graphics, int xOffset) {
	    int indicatorHeight = SCROLLBAR_INDICATOR_WIDTH;
	    int contentWidth = SCROLLBAR_INDICATOR_WIDTH;
	    int contentHeight = 38;
	    
	    int yOffset = (contentHeight - indicatorHeight) / 2;

	    int cx = xOffset + (contentWidth / 2);
	    int cy = contentHeight / 2;
	    int px = xOffset + contentWidth;
	    int py = contentHeight / 2;
	    int qx = xOffset + (contentWidth / 2);
	    int qy = contentHeight - yOffset;

	    graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
	    graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

	    int origColor = graphics.getColor();
	    graphics.setColor(Color.DARKGRAY);
	    graphics.fillEllipse(cx, cy, px, py, qx, qy, 0, 360);
	    graphics.setColor(origColor);
	}

	private void startMoving() {
	    if (isMoving) {
	        return;
	    }

	    isMoving = true;
	    moveDirection = 0;
	    moveStartingIndex = getFieldWithFocusIndex();
	    ImageButton button = (ImageButton) getFieldWithFocus();
	    button.setOpacity(Config.BUTTON_OPACITY_WHILE_MOVING);
	    invalidate();
	}

	private void stopMoving() {
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
	    restoreButtonBitmap();
	    invalidate();
	}
	    
	private void swapButtons(ImageButton p, ImageButton q) {
	    NullField pN = new NullField();
	    NullField qN = new NullField();
	    
	    replace(p, pN);
	    replace(q, qN);
	    
	    replace(pN, q);
	    replace(qN, p);
	}

	private void swapIndices(int p, int q) {
	    ImageButton f1 = (ImageButton) getField(p);
		ImageButton f2 = (ImageButton) getField(q);
		
		swapButtons(f1, f2);
	}

}