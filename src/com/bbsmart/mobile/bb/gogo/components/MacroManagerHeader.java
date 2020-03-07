package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public final class MacroManagerHeader extends HorizontalFieldManager {

    private final TextButton titleField;
    private final ScrollIndicator leftScrollIndicator;
    private final ScrollIndicator rightScrollIndicator;

    private Font[] font;
    private int state;
    private int[] backgroundColor;
    private int[] foregroundColor;

    public MacroManagerHeader() {
        super(NO_HORIZONTAL_SCROLL);
        
        state = ActiveField.STATE_UNFOCUSED;

        font = new Font[2];
        font[ActiveField.STATE_FOCUSED] = Font.getDefault();
        font[ActiveField.STATE_UNFOCUSED] = Font.getDefault();

        backgroundColor = new int[2];
        backgroundColor[ActiveField.STATE_FOCUSED] = Color.WHITE;
        backgroundColor[ActiveField.STATE_UNFOCUSED] = Color.WHITE;

        foregroundColor = new int[2];
        foregroundColor[ActiveField.STATE_FOCUSED] = Color.BLACK;
        foregroundColor[ActiveField.STATE_UNFOCUSED] = Color.BLACK;
        
        titleField = new TextButton();
        titleField.setAlignment(ActiveField.ALIGN_CENTER | ActiveField.ALIGN_MIDDLE);
        titleField.setPadding(2);
        
        leftScrollIndicator = new ScrollIndicator(ScrollIndicator.TYPE_LEFTPOINTING);
        leftScrollIndicator.setAlignment(ActiveField.ALIGN_CENTER | ActiveField.ALIGN_MIDDLE);
        leftScrollIndicator.setPadding(2);
        leftScrollIndicator.setStyle(ActiveField.USE_ALL_HEIGHT);

        rightScrollIndicator = new ScrollIndicator(ScrollIndicator.TYPE_RIGHTPOINTING);
        rightScrollIndicator.setAlignment(ActiveField.ALIGN_CENTER | ActiveField.ALIGN_MIDDLE);
        rightScrollIndicator.setPadding(2);
        rightScrollIndicator.setStyle(ActiveField.USE_ALL_HEIGHT);
        
        add(leftScrollIndicator);
        add(titleField);
        add(rightScrollIndicator);
    }
    
    public boolean isFocusable() {
        return false;
    }
    
    public void setBackgroundColor(int state, int color) {
        this.backgroundColor[state] = color;
        
        titleField.setBackgroundColor(state, color);
        leftScrollIndicator.setBackgroundColor(state, color);
        rightScrollIndicator.setBackgroundColor(state, color);
    }
    
    public void setScrollingEnabled(boolean isEnabled) {
        if (isEnabled) {
            leftScrollIndicator.setType(ScrollIndicator.TYPE_LEFTPOINTING);
            rightScrollIndicator.setType(ScrollIndicator.TYPE_RIGHTPOINTING);
        }
        else {
            leftScrollIndicator.setType(ScrollIndicator.TYPE_CIRCLE);
            rightScrollIndicator.setType(ScrollIndicator.TYPE_CIRCLE);
        }
    }
    
    public void setFont(int state, Font font) {
        this.font[state] = font;
    }
    
    public void setForegroundColor(int state, int color) {
        this.foregroundColor[state] = color;

        titleField.setForegroundColor(state, color);
        leftScrollIndicator.setForegroundColor(state, color);
        rightScrollIndicator.setForegroundColor(state, color);
    }
    
    public void setTitle(String title) {
        titleField.setText(title);
    }

    // Layout policy: Use all width; constrain height to tallest component.
    protected void sublayout(int width, int height) {
        int availableWidth = width - leftScrollIndicator.getPreferredWidth() - rightScrollIndicator.getPreferredWidth() - 1;
        int xOffset = 0;
        int yOffset = 0;

        layoutChild(titleField, availableWidth, height);
        int extentHeight = titleField.getHeight();
        
        layoutChild(leftScrollIndicator, width, extentHeight);
        setPositionChild(leftScrollIndicator, xOffset, yOffset);
        xOffset += leftScrollIndicator.getWidth();
        
        setPositionChild(titleField, xOffset, yOffset);
        xOffset += titleField.getWidth();

        layoutChild(rightScrollIndicator, width, extentHeight);
        setPositionChild(rightScrollIndicator, xOffset, yOffset);
        
        setExtent(width, extentHeight);
    }
    
    protected void subpaint(Graphics graphics) {
        int origColor = graphics.getColor();

        graphics.setColor(backgroundColor[state]);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(origColor);
        
        super.subpaint(graphics);
    }
    
}
