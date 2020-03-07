package com.bbsmart.mobile.bb.gogo.dialogs;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.AutoCompleteComponent;
import com.bbsmart.mobile.bb.gogo.components.Component;
import com.bbsmart.mobile.bb.gogo.components.DropDown;
import com.bbsmart.mobile.bb.gogo.components.HrefField;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.IAutoCompleteService;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class NewMacroDialog extends Screen {

    private static final int DEFAULT_MARGIN				= 10;
	
	private static final int SCREEN_WIDTH_PERCENTAGE	= 80;
	
	public static final int DIALOG_RESULT_OK			= 1;
	public static final int DIALOG_RESULT_CANCEL		= 2;
	
	protected DropDown[] dropdown = new DropDown[2];
	protected HrefField okButton = new HrefField("Ok");
	
	protected int resultCode = DIALOG_RESULT_CANCEL;
	
	private Bitmap background;
	private Titlebar titlebar;
	private Component component;

	public NewMacroDialog() {
		super(new VerticalFieldManager(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL) {
			protected void sublayout(int width, int height) {
				int extentWidth = width - (DEFAULT_MARGIN * 2);
				int extentHeight = height - (DEFAULT_MARGIN * 2);

				setExtent(extentWidth, extentHeight);
				setPosition(DEFAULT_MARGIN, DEFAULT_MARGIN);

				super.sublayout(extentWidth, extentHeight);
			}
		});
		
		titlebar = new Titlebar();
		titlebar.setTitle(Config.APP_TITLE);
		add(titlebar);

		for (int i = 0; i < 2; i++) {
			dropdown[i] = new DropDown();
			dropdown[i].setLabel("Label:");
			dropdown[i].setPadding(5, 5, 15, 5);
			add(dropdown[i]);
		}
		
		Font okButtonFont = Font.getDefault().derive(Font.PLAIN, 16, Ui.UNITS_px);

		okButton.setBackgroundColour(Config.COLOR_WINDOW_BACKGROUND);
		okButton.setTextColour(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
		okButton.setFont(okButtonFont);
		okButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
			    if (isValidated()) {
			        dialogAccepted();
			    }
			}
		});
		add(okButton);
	}

	public void displayComponent(Component component) {
	    this.component = component;
	    invalidate();
	}
	
	public void dismissComponent() {
	    component = null;
	    invalidate();
	}

	public String getValue(int index) {
		return dropdown[index].getText();
	}
	
	public int open() {
		synchronized(Application.getEventLock()) {
			UiApplication.getUiApplication().pushModalScreen(this);
		}

		return resultCode;
	}

	public void setAutoCompleteService(int index, IAutoCompleteService service) {
	    dropdown[index].getAutoCompleteComponent().setAutoCompleteService(service);
	}

	public void setEditField(int index, EditField field) {
	    dropdown[index].setEditField(field);
	}
	
	public void setLabel(int index, String label) {
		dropdown[index].setLabel(label);
	}
	
	public void setTitle(String title) {
	    titlebar.setTitle(title);
	}
	
	public void setValue(int index, String value) {
		dropdown[index].setText(value);
	}
	
	protected void createBackground() {
		background = new Bitmap(getWidth(), getHeight());

		Graphics graphics = new Graphics(background);
		XYRect clip = graphics.getClippingRect();
		
		int color = graphics.getColor();
		
		graphics.setColor(0x00EFEFEF);
		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
		
		graphics.setColor(0x006B86B5);
		graphics.drawRect(clip.x, clip.y, clip.width, clip.height);
		
		graphics.setColor(Config.COLOR_WINDOW_BACKGROUND);
		graphics.fillRect(clip.x + 9, clip.y + 9, clip.width - 18, clip.height - 18);

		graphics.setColor(Color.DARKGRAY);
		graphics.drawRect(clip.x + 9, clip.y + 9, clip.width - 18, clip.height - 18);
		
		graphics.setColor(color);
	}

	protected void dialogAccepted() {
		resultCode = DIALOG_RESULT_OK;
		UiApplication.getUiApplication().popScreen(this);
	}

	protected boolean keyChar(char c, int status, int time) {
		if (c == Characters.ESCAPE) {
			resultCode = DIALOG_RESULT_CANCEL;
			UiApplication.getUiApplication().popScreen(this);
			return true;
		}
		
		return super.keyChar(c, status, time);
	}
	
	protected boolean keyDown(int keycode, int time) {
	    if (component != null) {
	        boolean handled = component.keyDown(keycode, time);

	        if (handled) {
	            invalidate();
	            return handled;
	        }
	    }

	    if (Keypad.key(keycode) == Keypad.KEY_ENTER) {
	        return navigationClick(KeypadListener.STATUS_TRACKWHEEL, time);
	    }
	    
	    return super.keyDown(keycode, time);
	}

	protected boolean navigationClick(int status, int time) {
        if (component != null) {
            if (component.navigationClick(status, time)) {
                return true;
            }
        }

		return super.navigationClick(status, time);
	}
	
    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        if (component != null) {
            boolean handled = component.navigationMovement(dx, dy, status, time);
            
            if (handled) {
                invalidate();
                return handled;
            }
        }

        return super.navigationMovement(dx, dy, status, time);
    }
    
    // Need this hackery until we figure out how to turn Components into real Fields.
    protected void onUndisplay() {
        for (int i = 0; i < 2; i++) {
            DropDown d = dropdown[i];
            
            if (d != null) {
                AutoCompleteComponent c = d.getAutoCompleteComponent();
                
                if (c != null) {
                    IAutoCompleteService s = c.getAutoCompleteService();
                    
                    if (s != null) {
                        s.shutdown();
                    }
                }
            }
        }
    }
	
	protected void paint(Graphics graphics) {
	    super.paint(graphics);
	    
	    if (component != null) {
	        XYRect clip = graphics.getClippingRect();
	        XYRect extent = getExtent();
	        
	        int xTranslation = clip.x - extent.x;
	        int yTranslation = clip.y - extent.y;
	        
	        graphics.translate(xTranslation, yTranslation);

	        component.paint(graphics);
	    }
	}

	protected void paintBackground(Graphics graphics) {
		if (background == null) {
			createBackground();
		}

		XYRect extent = getExtent();
		
		graphics.rop(Graphics.ROP_SRC_COPY, 0, 0, extent.width, extent.height, background, 0, 0);
	}
	
	protected void sublayout(int width, int height) {
		int screenWidth = Display.getWidth();
		int screenHeight = Display.getHeight();
		
		int extentWidth = screenWidth * SCREEN_WIDTH_PERCENTAGE / 100;
		
		layoutDelegate(extentWidth, height);

		int extentHeight = getDelegate().getHeight() + (DEFAULT_MARGIN * 2);

		int xOffset = (screenWidth - extentWidth) / 2;
		int yOffset = (screenHeight - extentHeight) / 2;
		
		setExtent(extentWidth, extentHeight);
		setPosition(xOffset, yOffset);
	}

	private boolean isValidated() {
	    for (int i = 0; i < 2; i++) {
	        String text = dropdown[i].getEditField().getText();

	        if (text == null || text.equals("")) {
	            Status status = new Status("Please provide a value for both fields");
	            status.inform();
	            return false;
	        }
	    }
	    
	    return true;
	}
}
