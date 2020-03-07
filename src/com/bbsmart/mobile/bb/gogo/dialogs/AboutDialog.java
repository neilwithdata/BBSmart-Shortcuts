package com.bbsmart.mobile.bb.gogo.dialogs;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.HrefField;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;

public class AboutDialog extends Screen {

    private static final int DEFAULT_MARGIN             = 10;
    private static final int SCREEN_WIDTH_PERCENTAGE    = 80;

    private Bitmap background;
    private HrefField okButton = new HrefField("Ok", Field.FIELD_HCENTER);
    private Titlebar titlebar;

    public AboutDialog() {
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
        titlebar.setTitle("Instructions");
        add(titlebar);
        
        String message =
            "This application will allow you to consolidate your most commonly used " +
            "actions in one place. For best results please assign this application to " +
            "your phone's convenience key.";

        RichTextField textField = new RichTextField(message, 
                Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER);
        textField.setPadding(5, 5, 10, 5);
        add(textField);
        
        Font okButtonFont = Font.getDefault().derive(Font.PLAIN, 16, Ui.UNITS_px);

        okButton.setBackgroundColour(Config.COLOR_WINDOW_BACKGROUND);
        okButton.setTextColour(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
        okButton.setFont(okButtonFont);
        okButton.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                dialogAccepted();
            }
        });
        add(okButton);
    }
    
    public void open() {
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
    
    protected void dialogAccepted() {
        UiApplication.getUiApplication().popScreen(this);
    }

    protected boolean keyChar(char c, int status, int time) {
      if (c == Characters.ESCAPE || c == Characters.ENTER) {
          UiApplication.getUiApplication().popScreen(this);
          return true;
      }
      
      return super.keyChar(c, status, time);
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
    
}
