package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.ui.Graphics;

public interface IComponent {

    void addChangeListener(ComponentChangeListener listener);
    boolean keyChar(char c, int status, int time);
    boolean keyDown(int keycode, int time);
    boolean navigationClick(int status, int time);
    boolean navigationMovement(int dx, int dy, int status, int time);
    void paint(Graphics graphics);
    void removeChangeListener(ComponentChangeListener listener);

}
