/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.bbsmart.mobile.bb.gogo.effects;

import net.rim.device.api.system.Bitmap;

/**
 * An abstract superclass for point filters. The interface is the same as the old RGBImageFilter.
 */
public abstract class PointFilter extends AbstractBitmapOp {

	protected boolean canFilterIndexColorModel = false;

    public Bitmap filter( Bitmap src, Bitmap dst ) {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = new Bitmap(width, height);

		int[] inPixels = new int[width];
		for ( int y = 0; y < height; y++ ) {
			getRGB( src, 0, y, width, 1, inPixels );
			for ( int x = 0; x < width; x++ )
				inPixels[x] = filterRGB( x, y, inPixels[x] );
			setRGB( dst, 0, y, width, 1, inPixels );
		}

        return dst;
    }

	public abstract int filterRGB(int x, int y, int rgb);
}
