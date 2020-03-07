package com.bbsmart.mobile.bb.gogo.util;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;

public class ImageUtils {

	public static EncodedImage scaleToDimensions(EncodedImage encoded, int toWidth, int toHeight) {
		int fixedX = Fixed32.toFP(encoded.getWidth());
		int fixedEncodedWidth = Fixed32.toFP(toWidth);
		fixedX = Fixed32.div(fixedX, fixedEncodedWidth);

		int fixedY = Fixed32.toFP(encoded.getHeight());
		int fixedEncodedHeight = Fixed32.toFP(toHeight);
		fixedY = Fixed32.div(fixedY, fixedEncodedHeight);

		EncodedImage scaled = encoded.scaleImage32(fixedX, fixedY);
		
		return scaled;
	}
	
	public static EncodedImage scaleToFactor(EncodedImage encoded, int curSize, int newSize) {
		int numerator = Fixed32.toFP(curSize);
		int denominator = Fixed32.toFP(newSize);
		int scale = Fixed32.div(numerator, denominator);

		return encoded.scaleImage32(scale, scale);
	}

	public static EncodedImage scaleToHeight(EncodedImage encoded, int newHeight) {
		return scaleToFactor(encoded, encoded.getHeight(), newHeight);
	}

	public static EncodedImage scaleToSize(EncodedImage originalImage, int size) {
		int bitmapWidth = originalImage.getWidth();
		int bitmapHeight = originalImage.getHeight();
		
		int widthDiff = bitmapWidth - size;
		int heightDiff = bitmapHeight - size;
		
		int maxDiff = Math.max(widthDiff, heightDiff);
		
		EncodedImage scaledImage;
		
		if (maxDiff == widthDiff) {
			scaledImage = ImageUtils.scaleToWidth(originalImage, size);
		}
		else {
			scaledImage = ImageUtils.scaleToHeight(originalImage, size);
		}
		
		return scaledImage;
	}
	
	public static EncodedImage scaleToWidth(EncodedImage encoded, int newWidth) {
		return scaleToFactor(encoded, encoded.getWidth(), newWidth);
	}
	
}
