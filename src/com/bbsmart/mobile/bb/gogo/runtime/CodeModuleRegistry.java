package com.bbsmart.mobile.bb.gogo.runtime;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.invokers.*;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.util.ImageUtils;
import com.bbsmart.mobile.bb.gogo.util.PNGEncoder;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class CodeModuleRegistry implements ListFieldCallback {

	public static final CodeModuleRegistry instance = new CodeModuleRegistry();

	private Vector overrides = new Vector();
	private Vector registry = new Vector();
	private Vector listeners = new Vector();
	private boolean isLoaded = false;

	private CodeModuleRegistry() {
		load();
	}
	
	public void add(CodeModule codeModule) {
		if (! registry.contains(codeModule)) {
			registry.addElement(codeModule);
			notifyAdd(codeModule);
		}
	}

	public void addListener(CodeModuleRegistryListener listener) {
		if (! listeners.contains(listener)) {
			listeners.addElement(listener);
		}
	}

	public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
		// TODO: Move this binding to CodeModule to an Adapter.

		CodeModule codeModule = (CodeModule) registry.elementAt(index);
    	Bitmap icon = codeModule.getIcon();

		int xOffset = 2;
		int yOffsetIcon = y + (listField.getRowHeight() - icon.getHeight()) / 2;
		int yOffsetText = y + (listField.getRowHeight() - Font.getDefault().getHeight()) / 2;

    	graphics.drawBitmap(xOffset, yOffsetIcon, icon.getWidth(), icon.getHeight(), icon, 0, 0);
    	
    	xOffset += icon.getWidth() + 4;
    	
    	String name = codeModule.getAppName();
    	graphics.drawText(name, xOffset, yOffsetText, 0, width);
	}

	public Enumeration elements() {
		synchronized(registry) {
			return registry.elements();
		}
	}

	public Object get(ListField listField, int index) {
		CodeModule codeModule = (CodeModule) registry.elementAt(index);

		return codeModule;
	}

	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return listField.getSelectedIndex();
	}
	
	public void load() {
		if (isLoaded) {
			return;
		}
		
		synchronized(registry) {
//			loadNativeInvokeOverrides();
//			loadThirdPartyInvokeOverrides();
//			loadOtherInvokeOverrides();
		    loadOverrides();
			loadThirdPartyApps();
		}

		isLoaded = true;
	}
	
	public void notifyAdd(CodeModule codeModule) {
		for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
			CodeModuleRegistryListener listener = (CodeModuleRegistryListener) en.nextElement();
			listener.onCodeModuleRegistryAdd(codeModule);
		}
	}
	
	public void notifyRemove(CodeModule codeModule) {
		for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
			CodeModuleRegistryListener listener = (CodeModuleRegistryListener) en.nextElement();
			listener.onCodeModuleRegistryRemove(codeModule);
		}
	}

	public void removeListener(CodeModuleRegistryListener listener) {
		if (listeners.contains(listener)) {
			listeners.removeElement(listener);
		}
	}
	
	public void remove(CodeModule codeModule) {
		if (registry.contains(codeModule)) {
			registry.removeElement(codeModule);
			notifyRemove(codeModule);
		}
	}
	
	public int size() {
		synchronized(registry) {
			return registry.size();
		}
	}
	
    // We left-shift the ID here because we need to differentiate our overrides
    // from the third-party apps we discover on the device.  For those apps we're
    // going to use their module handle, which is an int and will go into the
    // lower 32 bits of the ID.
    protected void loadOverrides() {
//        NativeModuleConfigLoader loader = NativeModuleConfigLoader.instance;
//        
//        overrides = loader.getCodeModules();
        
        addCodeModule(1,    "Address Book",                 "net_rim_bb_addressbook_app",   "addressbook-24x24.png",    new AddressBookInvoker());
        addCodeModule(2,    "Calendar",                     "net_rim_bb_calendar_app",      "calendar-24x24.png",       new CalendarInvoker());
        addCodeModule(3,    "Calendar : New Appointment",   "net_rim_bb_calendar_app",      "new-calendar-24x24.png",   new NewCalendarAppointmentInvoker());
        addCodeModule(4,    "Camera",                       "net_rim_bb_camera",            "camera-24x24.png",         new CameraInvoker());
        addCodeModule(5,    "Maps",                         "net_rim_bb_lbs",               "maps-24x24.png",           new MapsInvoker());
        
        addCodeModule(6,    "Memo Pad",                     "net_rim_bb_memo_app",          "memo-24x24.png",           new MemoInvoker());
        addCodeModule(7,    "Memo Pad : New Memo",          "net_rim_bb_memo_app",          "new-memo-24x24.png",       new NewMemoInvoker());
        addCodeModule(8,    "Messages",                     "net_rim_bb_messaging_app",     "messaging-24x24.png",      new MessengerInvoker());
        addCodeModule(9,    "Phone",                        "net_rim_bb_phone_app",         "phone-24x24.png",          new PhoneInvoker());
        addCodeModule(10,   "Tasks",                        "net_rim_bb_task_app",          "tasks-24x24.png",          new TaskInvoker());
        
        addCodeModule(11,   "Tasks : New Task",             "net_rim_bb_task_app",                      "new-tasks-24x24.png",      new NewTaskInvoker());
        addCodeModule(12,   "Alarm",                        "net_rim_bb_alarm_app",                     "alarm-24x24.png",          new ThirdPartyInvoker("net_rim_bb_alarm_app"));
        addCodeModule(13,   "BrickBreaker",                 "net_rim_device_apps_games_brickbreaker",   "brickbreaker-24x24.png",   new ThirdPartyInvoker("net_rim_device_apps_games_brickbreaker"));
        addCodeModule(14,   "Calculator",                   "net_rim_bb_standardcalculator_app",        "calculator-24x24.png",     new ThirdPartyInvoker("net_rim_bb_standardcalculator_app"));
        addCodeModule(15,   "Help",                         "net_rim_bb_help",                          "help-24x24.png",           new ThirdPartyInvoker("net_rim_bb_help"));
        
        addCodeModule(16,   "Manage Connections",           "net_rim_bb_manage_connections",            "manage-connections-24x24.png", new ThirdPartyInvoker("net_rim_bb_manage_connections"));
        addCodeModule(17,   "Media",                        "net_rim_bb_file_explorer",                 "media-24x24.png",              new ThirdPartyInvoker("net_rim_bb_file_explorer"));
        addCodeModule(18,   "Options",                      "net_rim_bb_options_app",                   "options-24x24.png",            new ThirdPartyInvoker("net_rim_bb_options_app"));
        addCodeModule(19,   "Password Keeper",              "net_rim_bb_passwordkeeper",                "password-keeper-24x24.png",     new ThirdPartyInvoker("net_rim_bb_passwordkeeper"));
        addCodeModule(20,   "BlackBerry Messenger",         "net_rim_bb_qm_peer",                       "pin-messenger-24x24.png",      new ThirdPartyInvoker("net_rim_bb_qm_peer"));
        
        addCodeModule(21,   "Profiles", "net_rim_bb_profiles_app", "profiles-24x24.png", new ThirdPartyInvoker("net_rim_bb_profiles_app"));
        addCodeModule(22,   "Search", "net_rim_bb_globalsearch_app", "search-24x24.png", new ThirdPartyInvoker("net_rim_bb_globalsearch_app"));
        addCodeModule(23,   "Setup Wizard", "net_rim_bb_setupwizard_app", "setup-wizard-24x24.png", new ThirdPartyInvoker("net_rim_bb_setupwizard_app"));
        addCodeModule(24,   "Video Camera", "net_rim_bb_videorecorder", "video-camera-24x24.png", new ThirdPartyInvoker("net_rim_bb_videorecorder"));
        addCodeModule(25,   "Voice Dialing", "net_rim_vad", "vad-24x24.png", new ThirdPartyInvoker("net_rim_vad"));
        
        addCodeModule(26,   "Voice Notes Recorder", "net_rim_bb_voicenotesrecorder", "voice-notes-24x24.png", new ThirdPartyInvoker("net_rim_bb_voicenotesrecorder"));
        addCodeModule(27,   "Browser", "net_rim_bb_browser_daemon", "browser-24x24.png", new BrowserInvoker());
    }

	protected void loadThirdPartyApps() {
		// TODO: Clean this up.

	    int myModuleHandle = CodeModuleManager.getModuleHandleForClass(this.getClass());
		int[] handles = CodeModuleManager.getModuleHandles();
		
		for (int i = 0; i < handles.length; i++) {
			int handle = handles[i];

			if (CodeModuleManager.isLibrary(handle)) {
				continue;
			}
			
			String moduleName = CodeModuleManager.getModuleName(handle);
			
			Vector moduleOverrides = getModuleOverrides(moduleName);
			
			if (moduleOverrides.size() > 0) {
			    for (Enumeration enumeration = moduleOverrides.elements(); enumeration.hasMoreElements();) {
			        //if (overrides.containsKey(moduleName)) {
			        ApplicationDescriptor[] descriptors = CodeModuleManager.getApplicationDescriptors(handle);

			        if (descriptors != null) {
			            ApplicationDescriptor descriptor = descriptors[0];

			            CodeModule codeModule = (CodeModule) enumeration.nextElement();
			            codeModule.setHandle(descriptor.getModuleHandle());

			            add(codeModule);
			        }
			    }

			    continue;
			}

			// We don't want to list any RIM apps here.  Those will be handled
			// separately.
			// Well, we have those apps written by RIM's ISV team, such as Facebook and Flickr which start with
			// net_rim.  Bad idea, but that's the way it is, so we have to let them through here.
//			if (moduleName.startsWith("net_rim")) {
//				continue;
//			}
			
			//if (moduleName.equals("Shortcuts")) {
			if (handle == myModuleHandle) {
			    continue;
			}
			
			ApplicationDescriptor[] descriptors = CodeModuleManager.getApplicationDescriptors(handle);

			if (descriptors != null) {
			    int numDescriptors = descriptors.length;

			    for (int j = 0; j < numDescriptors; j++) {
			        ApplicationDescriptor descriptor = descriptors[j];
			        
			        // Skip system modules.
			        if ((descriptor.getFlags() & ApplicationDescriptor.FLAG_SYSTEM) != 0) {
			            continue;
			        }

			        String descriptorName = descriptor.getName();

			        if (descriptorName == null) {
			            continue;
			        }

			        // Weed out wacky RIM app names
			        if (descriptorName.startsWith("net_rim")) {
			            continue;
			        }

			        Bitmap bitmap = descriptor.getIcon();

			        if (bitmap != null) {
			            if (bitmap.getBitsPerPixel() == 1) {
			                continue;
			            }

			            Bitmap displayableImage = getDisplayableImage(bitmap);

			            int moduleHandle = descriptor.getModuleHandle();

			            CodeModule codeModule = new CodeModule();
			            codeModule.setIcon(displayableImage);
			            codeModule.setInvoker(new ThirdPartyInvoker(moduleHandle));
			            codeModule.setHandle(moduleHandle);
			            codeModule.setID(moduleHandle);
			            codeModule.setModuleName(descriptorName);
			            codeModule.setAppName(descriptor.getName());

			            add(codeModule);
			        }
			    }
			}
		}
	}
	
	private void addCodeModule(int id, String appName, String moduleName, String iconName, AbstractInvoker invoker) {
        CodeModule codeModule = new CodeModule();

        codeModule.setID(id << 32);
        codeModule.setAppName(appName);
        codeModule.setModuleName(moduleName);
        codeModule.setIcon(Bitmap.getBitmapResource(iconName));
        codeModule.setInvoker(invoker);

        overrides.addElement(codeModule);
	}

	private Bitmap getDisplayableImage(Bitmap bitmap) {
		Bitmap displayableImage = bitmap;

		if (needsResizing(bitmap)) {
			try {
				PNGEncoder encoder = new PNGEncoder(bitmap, true);
				byte[] imageBytes = encoder.encode(true);
				EncodedImage fullImage = EncodedImage.createEncodedImage(imageBytes, 0, imageBytes.length);
				EncodedImage scaledImage = ImageUtils.scaleToSize(fullImage, 24);
				displayableImage = scaledImage.getBitmap();
			}
			catch (IOException ignore) {
				// Didn't work.  Use the default icon instead.
				// TODO: Add a "default" icon to the app.
			}
		}
		
		return displayableImage;
	}
	
	private Vector getModuleOverrides(String moduleName) {
	    Vector moduleOverrides = new Vector();
	    
	    for (Enumeration enumeration = overrides.elements(); enumeration.hasMoreElements();) {
	        CodeModule codeModule = (CodeModule) enumeration.nextElement();
	        
	        if (codeModule.getModuleName().equals(moduleName)) {
	            moduleOverrides.addElement(codeModule);
	        }
	    }
	    
	    return moduleOverrides;
	}
	
	private boolean needsResizing(Bitmap bitmap) {
		boolean needsResizing = false;

		if (bitmap.getWidth() > Config.APPLICATION_ICON_WIDTH) {
			needsResizing = true;
		}
		else if (bitmap.getHeight() > Config.APPLICATION_ICON_HEIGHT) {
			needsResizing = true;
		}
		
		return needsResizing;
	}

}
