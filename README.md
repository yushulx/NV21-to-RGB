# NV21 to RGB in Java

## What's included?
* A custom BMP Class.
* Methods of converting NV21 to RGB.

## Sample

```Java
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
```

## Reference
* [BITMAPINFOHEADER structure][1]
* [YUV][2]
* [BMP file format][3]

## Blog
[How to Convert NV21 Data to BMP File in Java][4]

[1]:https://msdn.microsoft.com/en-us/library/windows/desktop/dd183376(v=vs.85).aspx
[2]:https://en.wikipedia.org/wiki/YUV
[3]:https://en.wikipedia.org/wiki/BMP_file_format
[4]:https://www.dynamsoft.com/codepool/nv21-bmp-java.html
