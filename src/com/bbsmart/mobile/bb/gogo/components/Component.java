package com.bbsmart.mobile.bb.gogo.components;

import java.util.Vector;

import net.rim.device.api.ui.Graphics;

public abstract class Component implements IComponent {

    Vector listeners = new Vector();
    
    public void addChangeListener(ComponentChangeListener listener) {
        if (! listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }
    
    public boolean keyChar(char c, int status, int time) {
        return false;
    }
    
    public boolean keyDown(int keycode, int time) {
        return false;
    }

    public boolean navigationClick(int status, int time) {
        return false;
    }

    public boolean navigationMovement(int dx, int dy, int status, int time) {
        return false;
    }

    public abstract void paint(Graphics graphics);

    public void removeChangeListener(ComponentChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }
    
    protected void notifyComponentChangeListeners(int eventType) {
        int numListeners = listeners.size();
        
        for (int i = 0; i < numListeners; i++) {
            ComponentChangeListener listener = (ComponentChangeListener) listeners.elementAt(i);
            
            listener.onComponentChange(this, eventType);
        }
    }

}
