package com.main;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

/*
 * BMP structure: https://en.wikipedia.org/wiki/BMP_file_format#File_structure
 * DIB format: https://msdn.microsoft.com/en-us/library/windows/desktop/dd183376(v=vs.85).aspx
 */
public class BMP {

	// BMP Header
	private byte[] id = { 0x42, 0x4D };
	private int fileSize = 0;
	private short spec1 = 0, spec2 = 0;
	private int offset = 54;

	// DIB Header
	private int biSize = 40;
	private int biWidth, biHeight;
	private short biPlanes = 0, biBitCount = 32;
	private int biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant;

	// bitmap data
	private int[] data;

	public BMP(int width, int height, short pixelBits, int[] pixels) {
		biWidth = width;
		biHeight = height;
		biBitCount = pixelBits;
		data = pixels;
		fileSize = width * height * pixelBits / 8 + offset;
	}

	public int getFileSize() {
		return fileSize;
	}

	private byte[] getFile() {
		ByteBuffer buffer = ByteBuffer.allocate(fileSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(id);
		buffer.putInt(fileSize);
		buffer.putShort(spec1);
		buffer.putShort(spec2);
		buffer.putInt(offset);

		buffer.putInt(biSize);
		buffer.putInt(biWidth);
		buffer.putInt(biHeight);
		buffer.putShort(biPlanes);
		buffer.putShort(biBitCount);
		buffer.putInt(biCompression);
		buffer.putInt(biSizeImage);
		buffer.putInt(biXPelsPerMeter);
		buffer.putInt(biYPelsPerMeter);
		buffer.putInt(biClrUsed);
		buffer.putInt(biClrImportant);

		for (int i = 0; i < data.length; i++) {
			buffer.putInt(data[i]);
		}

		return buffer.array();
	}

	private byte[] getHeader() {
		ByteBuffer buffer = ByteBuffer.allocate(54);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(id);
		buffer.putInt(fileSize);
		buffer.putShort(spec1);
		buffer.putShort(spec2);
		buffer.putInt(offset);

		buffer.putInt(biSize);
		buffer.putInt(biWidth);
		buffer.putInt(biHeight);
		buffer.putShort(biPlanes);
		buffer.putShort(biBitCount);
		buffer.putInt(biCompression);
		buffer.putInt(biSizeImage);
		buffer.putInt(biXPelsPerMeter);
		buffer.putInt(biYPelsPerMeter);
		buffer.putInt(biClrUsed);
		buffer.putInt(biClrImportant);

		return buffer.array();
	}

	public void saveBMP(String fileName) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(fileName);
			out.write(getFile());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveBMPFileWithDataOutPutStream(int[] data) {
		DataOutputStream output;
		try {
			output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("big-endian.bmp")));
			output.write(getHeader());
			for (int i = 0; i < data.length; i++) {
				output.writeInt(data[i]);
			}
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveBMPFile(int[] data) {
		byte[] result = new byte[data.length * 4];
		;
		int j = 0;

		for (int i = 0; i < data.length; i++) {
			result[j + 3] = (byte) (data[i] >> 24);
			result[j + 2] = (byte) (data[i] >> 16 & 0x00FF);
			result[j + 1] = (byte) (data[i] >> 8 & 0x0000FF);
			result[j] = (byte) (data[i] & 0x000000FF);

			j += 4;
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream("little-endian.bmp");
			out.write(getHeader());
			out.write(result);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Use BufferedImage Class
	public static void saveBMPBufferedImage(int[] data, int width, int height, String fileName) {
		System.out.println("data length: " + data.length + ", width = " + width + ", height: " + height);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		bufferedImage.setRGB(0, 0, width, height, data, 0, width);
		try {
			ImageIO.write(bufferedImage, "JPG", new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
