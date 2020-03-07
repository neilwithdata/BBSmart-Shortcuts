package com.bbsmart.mobile.bb.gogo.effects;

import net.rim.device.api.system.Bitmap;

public abstract class AbstractBitmapOp {

	public int[] getRGB( Bitmap bitmap, int x, int y, int width, int height, int[] pixels ) {
		int[] tmpPixels = null;

		if (pixels == null) {
			tmpPixels = new int[width * height];
		}
		else {
			tmpPixels = pixels;
		}

		bitmap.getARGB(tmpPixels, 0, width, x, y, width, height);

		return tmpPixels;
	}
	
	public void setRGB( Bitmap bitmap, int x, int y, int width, int height, int[] pixels ) {
		bitmap.setARGB(pixels, 0, width, x, y, width, height);
	}
	
}
