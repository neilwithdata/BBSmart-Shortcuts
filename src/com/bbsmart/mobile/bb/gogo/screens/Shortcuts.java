package com.bbsmart.mobile.bb.gogo.screens;

import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.ShortcutsApp;
import com.bbsmart.mobile.bb.gogo.components.ActiveField;
import com.bbsmart.mobile.bb.gogo.components.IMenuContributor;
import com.bbsmart.mobile.bb.gogo.components.MacroFlipper;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;
import com.bbsmart.mobile.bb.gogo.components.Toolbar;
import com.bbsmart.mobile.bb.gogo.components.Tooltip;
import com.bbsmart.mobile.bb.gogo.components.menu.AbstractMenuItem;
import com.bbsmart.mobile.bb.gogo.components.menu.Menu;
import com.bbsmart.mobile.bb.gogo.components.menu.MenuScreen;
import com.bbsmart.mobile.bb.gogo.components.menu.Separator;
import com.bbsmart.mobile.bb.gogo.components.menu.SubMenu;
import com.bbsmart.mobile.bb.gogo.dialogs.AboutDialog;
import com.bbsmart.mobile.bb.gogo.dialogs.AppInfoDialog;
import com.bbsmart.mobile.bb.gogo.dialogs.NewShortcutDialog;
import com.bbsmart.mobile.bb.gogo.dialogs.Status;
import com.bbsmart.mobile.bb.gogo.macros.EmailMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.MessagingMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.PhoneMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.WebMacroManager;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.preferences.IPreferenceStore;
import com.bbsmart.mobile.bb.gogo.runtime.CodeModuleRegistry;
import com.bbsmart.mobile.bb.gogo.runtime.ToolbarRegistry;

import com.bbsmart.mobile.bb.gogo.persistence.TrialManager;

public class Shortcuts extends MainScreen {

    private MacroFlipper macroFlipper;
	private Menu menu;
	private Timer timer;
	private Toolbar toolbar;
	private Tooltip tooltip;
//	private VerticalFieldManager stationaryManager;
//	private VerticalFieldManager scrollableManager;
	
	private boolean isDisplayingTooltip = false;
	private boolean shouldDisplayTooltip = false;
	private boolean shouldFadeOut = false;
	
	public Shortcuts() {
		super(MainScreen.NO_VERTICAL_SCROLL);
		
		tooltip = new Tooltip();

//		stationaryManager = new VerticalFieldManager(
//				VerticalFieldManager.NO_HORIZONTAL_SCROLL 
//				| VerticalFieldManager.NO_VERTICAL_SCROLL);
//
//		add(stationaryManager);
//		add(scrollableManager);
		
		//add(scrollableManager);

		Font font = Font.getDefault().derive(Font.BOLD, 16, Ui.UNITS_px);
		Font.setDefaultFont(font);

		Titlebar titlebar = new Titlebar();
		titlebar.setPadding(2);
		titlebar.setTitle(Config.APP_TITLE);
		add(titlebar);

		toolbar = new Toolbar();
		add(toolbar);

		macroFlipper = new MacroFlipper();
		add(macroFlipper);
	}

	public void fadeIn() {
		invalidate();
	}

	public void fadeOut() {
	    // Commenting this out until we make sure it works across the board.
	    // shouldFadeOut = true;
	    dismissTooltip();
	    invalidate();
	}

	public void doFadeOut(Graphics graphics) {
	    shouldFadeOut = false;

	    int alpha = graphics.getGlobalAlpha();
	    int color = graphics.getColor();
	    graphics.setGlobalAlpha(0x80);
	    graphics.setColor(Color.BLACK);
	    graphics.fillRect(0, 0, getWidth(), getHeight());
	    graphics.setColor(color);
	    graphics.setGlobalAlpha(alpha);
	}
	
	public Menu getMenu() {
	    return menu;
	}

	public void close() {
	    Application.getApplication().requestBackground();
	}

	public void onTooltipDismissRequest() {
	    dismissTooltip();
	}

	public void onTooltipDisplayRequest(ActiveField field) {
	    if (timer != null) {
	        timer.cancel();
	    }

	    tooltip.setOwner(field);

	    TimerTask displayTooltipTask = new TimerTask() {
	        public void run() {
	            displayTooltip();
	        }
	    };

	    timer = new Timer();

	    timer.schedule(displayTooltipTask, Config.TOOLTIP_DISPLAY_DELAY);
	}
	
	public void showAboutDialogScreen() {
        fadeOut();
        AboutDialog about = new AboutDialog();
        about.open();
        fadeIn();
	}
	
	protected void addNewToolbarAction() {
		fadeOut();

		boolean canAddNewToolbarAction = false;
		Enumeration en = CodeModuleRegistry.instance.elements();

		while (en.hasMoreElements()) {
		    CodeModule codeModule = (CodeModule) en.nextElement();
		    if (!ToolbarRegistry.instance.exists(codeModule)) {
		        canAddNewToolbarAction = true;
		        break;
		    }
		}
	        
		if (canAddNewToolbarAction) {
		    NewShortcutDialog dialog = new NewShortcutDialog();
		    CodeModule codeModule = dialog.open();

		    if (codeModule != null) {
		        Shortcut newShortcut = new Shortcut();
		        newShortcut.setCodeModule(codeModule);

		        ToolbarRegistry.instance.add(newShortcut);
		        ToolbarRegistry.instance.store();
		    }
		}
		else {
		    Status status = new Status("No applications available");
		    status.inform();
		}
		
		fadeIn();
	}

	protected void displayTooltip() {
	    shouldDisplayTooltip = true;
	    invalidate();

	    TimerTask displayTooltipTask = new TimerTask() {
	        public void run() {
	            dismissTooltip();
	        }
	    };

	    timer = new Timer();

	    timer.schedule(displayTooltipTask, 2000);
	}

	protected void dismissTooltip() {
	    if (timer != null) {
	        timer.cancel();
	    }

	    shouldDisplayTooltip = false;

	    if (isDisplayingTooltip) {
	        invalidate();
	    }
	}

	//
	// Faking menu shortcuts here until it gets implemented correctly.
	// This is all pretty bad (ignores the Runnables we set when we created the menu),
	// but we'll get real functionality implemented shortly.
	//
	protected boolean keyChar(char c, int status, int time) {
		if (c == Config.closeAppAccelerator) {
			Application.getApplication().requestBackground();
			return true;
		}
		
		if (c == Config.emailShortcutAccelerator) {
		    Runnable runnable = EmailMacroManager.instance.getNewShortcutRunnable();
		    if (runnable != null) {
		        runnable.run();
		    }
		    return true;
		}
		
		if (c == Config.messagingShortcutAccelerator) {
		    Runnable runnable = MessagingMacroManager.instance.getNewShortcutRunnable();
		    if (runnable != null) {
		        runnable.run();
		    }
		    return true;
		}

		if (c == Config.phoneShortcutAccelerator) {
		    Runnable runnable = PhoneMacroManager.instance.getNewShortcutRunnable();
		    if (runnable != null) {
		        runnable.run();
		    }
		    return true;
		}
		
		if (c == Config.toolbarShortcutAccelerator) {
		    addNewToolbarAction();
		    return true;
		}
		
		if (c == Config.appInfoAccelerator) {
			showAppInfoDialog();
			return true;
		}
		
		if (c == Config.buyAppAccelerator) {
		    UiApplication.getUiApplication().pushScreen(
		            new RegisterScreen(false));
		}

		if (c == Config.webShortcutAccelerator) {
		    Runnable runnable = WebMacroManager.instance.getNewShortcutRunnable();
		    if (runnable != null) {
		        runnable.run();
		    }
		    return true;
		}
		
		return super.keyChar(c, status, time);
	}

	protected boolean keyDown(int keycode, int time) {
		if (Keypad.key(keycode) == Keypad.KEY_MENU) {
		    dismissTooltip();
			displayMenu();
			return true;
		}
			
		return super.keyDown(keycode, time);
	}
	
	protected void onDisplay() {
	    Config.toolbarShortcutAccelerator         = Characters.LATIN_SMALL_LETTER_T;
	    Config.emailShortcutAccelerator           = Characters.LATIN_SMALL_LETTER_E;
	    Config.messagingShortcutAccelerator       = Characters.LATIN_SMALL_LETTER_M;
	    Config.phoneShortcutAccelerator           = Characters.LATIN_SMALL_LETTER_P; // !ST
	    Config.webShortcutAccelerator             = Characters.LATIN_SMALL_LETTER_W; // !ST
	    Config.deleteItemAccelerator              = Characters.LATIN_SMALL_LETTER_D;
	    Config.editItemAccelerator                = Characters.LATIN_SMALL_LETTER_I; // !ST
	    Config.moveItemAccelerator                = Characters.LATIN_SMALL_LETTER_V; // !ST
	    Config.closeAppAccelerator                = Characters.LATIN_SMALL_LETTER_Z;
	    Config.appInfoAccelerator				  = Characters.LATIN_SMALL_LETTER_A;
	    Config.buyAppAccelerator                  = Characters.LATIN_SMALL_LETTER_B;

	    // Overrides for SureType
	    if (Keypad.getHardwareLayout() == Keypad.HW_LAYOUT_REDUCED_24) {
	        Config.editItemAccelerator            = Characters.LATIN_SMALL_LETTER_U;
	        Config.phoneShortcutAccelerator       = Characters.LATIN_SMALL_LETTER_O;
	        Config.webShortcutAccelerator         = Characters.LATIN_SMALL_LETTER_Q;
	        Config.moveItemAccelerator            = Characters.LATIN_SMALL_LETTER_C;
	    }
	    
	    super.onDisplay();
	    
	    ShortcutsApp app = ShortcutsApp.getInstance();
	    IPreferenceStore prefs = app.getPreferenceStore();
	    
	    if (prefs.getBoolean(Config.PREF_ON_FIRST_LAUNCH, true)) {
	        prefs.putBoolean(Config.PREF_ON_FIRST_LAUNCH, false);
	        prefs.save();
	        UiApplication.getUiApplication().invokeLater(new Runnable() {
	            public void run() {
	                showAboutDialogScreen();
	            }
	        });
	    }
	}
	
	protected void onObscured() {
	    dismissTooltip();
	    super.onObscured();
	}
	
	// Alt-pppp to really exit the app.
	protected boolean openDevelopmentBackdoor(int backdoorCode) {
	    switch( backdoorCode ) {
	    case ( 'P' << 24 ) | ( 'P' << 16 ) | ( 'P' << 8 ) | 'P':
	        UiApplication.getUiApplication().invokeLater (new Runnable() {
	            public void run() {
	                System.out.println("exiting");
	                System.exit(0);
	            }
	        });
	    return true;
	    }

	    return super.openDevelopmentBackdoor(backdoorCode);
	}

	protected void paint(Graphics graphics) {
	    super.paint(graphics);

	    if (shouldDisplayTooltip) {
	        tooltip.paint(graphics);
	        isDisplayingTooltip = true;
	        shouldDisplayTooltip = false;
	    }
	    else {
	        isDisplayingTooltip = false;
	    }
	    
	    if (shouldFadeOut) {
	        doFadeOut(graphics);
	    }
	}
	
    private void displayMenu() {
        menu = makeMenu();

        MenuScreen menuScreen = new MenuScreen(menu);

        UiApplication.getUiApplication().pushScreen(menuScreen);
    }

	private Menu makeMenu() {
	    menu = new Menu();

	    SubMenu subFoo = menu.addSubMenu("New");

	    subFoo.add("Toolbar Shortcut", Config.toolbarShortcutAccelerator).setRunnable(new Runnable() {
	        public void run() {
	            addNewToolbarAction();
	        }
	    });
	    subFoo.add("Email Macro", Config.emailShortcutAccelerator).setRunnable(
	            EmailMacroManager.instance.getNewShortcutRunnable());
	    subFoo.add("Messaging Macro", Config.messagingShortcutAccelerator).setRunnable(
	            MessagingMacroManager.instance.getNewShortcutRunnable());
	    subFoo.add("Phone Macro", Config.phoneShortcutAccelerator).setRunnable(
	            PhoneMacroManager.instance.getNewShortcutRunnable());
	    subFoo.add("Web Macro", Config.webShortcutAccelerator).setRunnable(
	            WebMacroManager.instance.getNewShortcutRunnable());
	    
	    menu.addSeparator();
	    
	    // Eh...  This is crazy, but it seems to work...
	    Manager manager = this;
	    int menuSize = menu.size();

	    while(true) {
	        Field field = manager.getFieldWithFocus();

	        if (field instanceof IMenuContributor) {
	            IMenuContributor menuContributor = (IMenuContributor) field;
	            menuContributor.makeContextMenu(menu);
	            
	            // If the contributor added something we want to make sure there's a separator
	            // after their addition.
	            int newMenuSize = menu.size();
	            if (newMenuSize != menuSize) {
	                menuSize = newMenuSize;
	                AbstractMenuItem lastMenuItem = (AbstractMenuItem) menu.getLast();
	                if (! (lastMenuItem instanceof Separator)) {
	                    menu.addSeparator();
	                }
	            }
	            
	            // We only want to call the most-overridden implementation.  After that,
	            // it's up to the component to call superclass impls if it wants to.
	            break;
	        }
	        
	        if (field instanceof Manager) {
	            manager = (Manager) field;
	        }
	        else {
	            break;
	        }
	    }
        
        if (TrialManager.getInstance().state != TrialManager.STATE_REG) {
            menu.add("Buy/Register", Config.buyAppAccelerator).setRunnable(new Runnable() {
                public void run() {
                    UiApplication.getUiApplication().pushScreen(
                            new RegisterScreen(false));
                }
            });
        }

	    menu.add("About", Config.appInfoAccelerator).setRunnable(new Runnable() {
			public void run() {
				showAppInfoDialog();
			}
		});
	    
	    menu.add("Close", Config.closeAppAccelerator).setRunnable(new Runnable() {
	        public void run() {
	            // We're going to try and live in the background in order to:
	            //   o launch faster;
	            //   o keep the addressbook in memory
	            Application.getApplication().requestBackground();
	            //System.exit(0);
	        }
	    });

	    return menu;
	}

	private void showAppInfoDialog() {
	    fadeOut();
	    AppInfoDialog appInfo = new AppInfoDialog();
	    appInfo.open();
	    fadeIn();
	}

}
