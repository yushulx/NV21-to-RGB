package com.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		File file = new File("2048x1536.yuv"); // The input NV21 file
		if (!file.exists())
			return;

		// BMP file info
		int width = 2048, height = 1536;
		short pixelBits = 32;

		try {
			// Read all bytes
			byte[] bytes = Files.readAllBytes(file.toPath());

			int[] data = NV21.yuv2rgb(bytes, width, height);
			BMP bmp = new BMP(width, height, pixelBits, data);
			bmp.saveBMP("nv21.bmp"); // The output BMP file

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Conversion is done.");
	}

}
