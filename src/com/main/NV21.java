package com.main;

public class NV21 {
	/*
	 * Refer to https://code.google.com/p/android/issues/detail?id=823
	 */
	public static void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
	    final int frameSize = width * height;

	    for (int j = 0, yp = 0; j < height; j++) {
	        int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
	        for (int i = 0; i < width; i++, yp++) {
	            int y = (0xff & ((int) yuv420sp[yp])) - 16;
	            if (y < 0) y = 0;
	            if ((i & 1) == 0) {
	                v = (0xff & yuv420sp[uvp++]) - 128;
	                u = (0xff & yuv420sp[uvp++]) - 128;
	            }
	            int y1192 = 1192 * y;
	            int r = (y1192 + 1634 * v);
	            int g = (y1192 - 833 * v - 400 * u);
	            int b = (y1192 + 2066 * u);

	            if (r < 0) r = 0; else if (r > 262143) r = 262143;
	            if (g < 0) g = 0; else if (g > 262143) g = 262143;
	            if (b < 0) b = 0; else if (b > 262143) b = 262143;

	            rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
	        }
	    }
	}
	
	/*
	 * Refer to http://stackoverflow.com/questions/1893072/getting-frames-from-video-image-in-android
	 */
	public static void decodeYUV(int[] out, byte[] fg, int width, int height)
	        throws NullPointerException, IllegalArgumentException {
	    int sz = width * height;
	    if (out == null)
	        throw new NullPointerException("buffer out is null");
	    if (out.length < sz)
	        throw new IllegalArgumentException("buffer out size " + out.length
	                + " < minimum " + sz);
	    if (fg == null)
	        throw new NullPointerException("buffer 'fg' is null");
	    if (fg.length < sz)
	        throw new IllegalArgumentException("buffer fg size " + fg.length
	                + " < minimum " + sz * 3 / 2);
	    int i, j;
	    int Y, Cr = 0, Cb = 0;
	    for (j = 0; j < height; j++) {
	        int pixPtr = j * width;
	        final int jDiv2 = j >> 1;
	        for (i = 0; i < width; i++) {
	            Y = fg[pixPtr];
	            if (Y < 0)
	                Y += 255;
	            if ((i & 0x1) != 1) {
	                final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
	                Cb = fg[cOff];
	                if (Cb < 0)
	                    Cb += 127;
	                else
	                    Cb -= 128;
	                Cr = fg[cOff + 1];
	                if (Cr < 0)
	                    Cr += 127;
	                else
	                    Cr -= 128;
	            }
	            int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
	            if (R < 0)
	                R = 0;
	            else if (R > 255)
	                R = 255;
	            int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
	                    + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
	            if (G < 0)
	                G = 0;
	            else if (G > 255)
	                G = 255;
	            int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
	            if (B < 0)
	                B = 0;
	            else if (B > 255)
	                B = 255;
	            out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
	        }
	    }

	}

	public static int[] yuv2rgb(byte[] yuv, int width, int height) {
		int total = width * height;
		int[] rgb = new int[total];
		int Y, Cb = 0, Cr = 0, index = 0;
		int R, G, B;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Y = yuv[y * width + x];
				if (Y < 0) Y += 255;
				
				if ((x & 1) == 0) {
					Cr = yuv[(y >> 1) * (width) + x + total];
					Cb = yuv[(y >> 1) * (width) + x + total + 1];
					
					if (Cb < 0) Cb += 127; else Cb -= 128;
					if (Cr < 0) Cr += 127; else Cr -= 128;
				}
				
				R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
				G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
				B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
				
				// Approximation
//				R = (int) (Y + 1.40200 * Cr);
//			    G = (int) (Y - 0.34414 * Cb - 0.71414 * Cr);
//				B = (int) (Y + 1.77200 * Cb);
				
				if (R < 0) R = 0; else if (R > 255) R = 255;
				if (G < 0) G = 0; else if (G > 255) G = 255;
				if (B < 0) B = 0; else if (B > 255) B = 255;
				
				rgb[index++] = 0xff000000 + (R << 16) + (G << 8) + B;
			}
		}
		
		return rgb;
	}
	
}
