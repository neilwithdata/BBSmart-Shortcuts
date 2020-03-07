package com.bbsmart.mobile.bb.gogo.dialogs;

import java.util.Timer;
import java.util.TimerTask;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class Status extends Screen {

    private static final int DEFAULT_MARGIN             = 10;
    private static final int SCREEN_WIDTH_PERCENTAGE    = 80;
    
    private Bitmap background;
    private Bitmap icon;
    private Titlebar titlebar;
    
    public Status(String message) {
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
        titlebar.setTitle("Shortcuts Info");
        add(titlebar);
        
        HorizontalFieldManager hfm = new HorizontalFieldManager() {
            protected void sublayout(int width, int height) {
                int xOffset = 0;
                int yOffset = 0;
                int extentHeight = 0;
                int contentWidth = width;
                
                Field icon = getField(0);
                layoutChild(icon, contentWidth, height);
                setPositionChild(icon, xOffset, yOffset);
                xOffset += icon.getWidth();
                yOffset += icon.getHeight();
                contentWidth -= icon.getWidth();
                extentHeight = Math.max(extentHeight, icon.getHeight());
                
                Field message = getField(1);
                layoutChild(message, contentWidth, height);
                int messageHeight = message.getHeight();
                yOffset = (icon.getHeight() - messageHeight) / 2;
                setPositionChild(message, xOffset, yOffset);
                extentHeight = Math.max(extentHeight, message.getHeight());
                xOffset += message.getWidth();
                
                setExtent(xOffset, extentHeight);
            }
        };
        add(hfm);
        
        icon = Bitmap.getBitmapResource("info.png");
        BitmapField iconField = new BitmapField(icon);
        iconField.setPadding(10, 10, 10, 10);
        hfm.add(iconField);

        RichTextField textField = new RichTextField(message, 
                Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER);
        textField.setPadding(0, 10, 0, 0);
        hfm.add(textField);
    }

    public void inform() {
        synchronized(Application.getEventLock()) {
            UiApplication.getUiApplication().pushModalScreen(this);
        }
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

    protected void onVisibilityChange(boolean visible) {
        if (visible) {
            TimerTask task = new TimerTask() {
                public void run() {
                    dismiss();
                }
            };

            Timer timer = new Timer();

            timer.schedule(task, 2000);
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
    
    private void dismiss() {
        synchronized (Application.getEventLock()) {
            UiApplication.getUiApplication().popScreen(this);
        }
    }

}
