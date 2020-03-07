//
// Thanks to http://www.northcubed.com for the foundation of this class.
//
package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class HrefField extends Field {

	private String content;
	private Font fieldFont;
	private int fieldWidth;
	private int fieldHeight;
	private boolean active = false;
	private int backgroundColour = 0xffffff;
	private int textColour = 0x333333;
	private int maskColour = 0xBBBBBB;
	private int xOffset = 0;
	private long style;

	public HrefField(String content) {
	    this(content, Field.FIELD_RIGHT);
	}

	public HrefField(String content, long style) {
		super(Field.FOCUSABLE);
		this.content = content;
		this.style = style;
		fieldFont = defaultFont();
		fieldWidth = fieldFont.getAdvance(content)+2;
		fieldHeight = fieldFont.getHeight() + 3;
	}

	public void setColours(int backgroundColour, int textColour, int maskColour) {
		this.backgroundColour = backgroundColour;
		this.textColour = textColour;
		this.maskColour = maskColour;
		invalidate();
	}

	public void setBackgroundColour(int backgroundColour){
		this.backgroundColour = backgroundColour;
		invalidate();
	}

	public void setTextColour(int textColour){
		this.textColour = textColour;
		invalidate();
	}

	public void setMaskColour(int maskColour){
		this.maskColour = maskColour;
		invalidate();
	}

	public void setFont(Font fieldFont){
		this.fieldFont = fieldFont;
		this.fieldWidth = fieldFont.getAdvance(content)+2;
	}

	public int getPreferredWidth() {
		return fieldWidth;
	}

	public int getPreferredHeight() {
		return fieldHeight + 5;
	}

	protected void layout(int arg0, int arg1) {
		setExtent(arg0, getPreferredHeight());
	}

	protected void paint(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();

		graphics.setColor(backgroundColour);
		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);

		if (style == Field.FIELD_LEFT) {
		    xOffset = 0;
		}
		else if (style == Field.FIELD_HCENTER) {
		    xOffset = getWidth() / 2 - 5;
		}
		else if (style == Field.FIELD_RIGHT) {
		    xOffset = getWidth() - fieldWidth - 5;
		}
		
		if (active) {
			graphics.setColor(maskColour);
			graphics.fillRect(xOffset, 0, fieldWidth, fieldHeight);
		} else {
			graphics.setColor(backgroundColour);
			graphics.fillRect(xOffset, 0, fieldWidth, fieldHeight);
		}

		graphics.setColor(textColour);
		graphics.setFont(fieldFont);
		graphics.drawText(content, xOffset+1, 1);
		graphics.drawLine(xOffset+1, fieldHeight-2, xOffset+fieldWidth-2, fieldHeight-2);
	}

	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(1);
		return true;
	}

	protected void onFocus(int direction) {
		active = true;
		invalidate();
	}

	protected void onUnfocus() {
		active = false;
		invalidate();
	}

	public static Font defaultFont() {
		try {
			FontFamily theFam = FontFamily.forName("SYSTEM");
			return theFam.getFont(net.rim.device.api.ui.Font.PLAIN, 14);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
