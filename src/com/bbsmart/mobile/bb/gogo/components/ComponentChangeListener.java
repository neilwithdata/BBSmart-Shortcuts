package com.bbsmart.mobile.bb.gogo.components;

public interface ComponentChangeListener {

    public static final int COMPONENT_DISPLAY = 1;
    public static final int COMPONENT_DISMISS = 2;

    void onComponentChange(Component component, int eventType);

}
