package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;

/**
 * The Class ModifyPicture.
 */
public class ModifyPicture {

	/**
	 * Resizes the given image.
	 *
	 * @param originalImage the original image
	 * @param resizedImage the resized image
	 * @param width the width
	 * @param height the height
	 * @param format the format
	 */
	public void resizeImage(File originalImage, File resizedImage, int width, int height, String format) {

		BufferedImage original;
		try {

			original = ImageIO.read(originalImage);

			BufferedImage resized = new BufferedImage(width, height, original.getType());

			Graphics2D g2 = resized.createGraphics();

			g2.drawImage(original, 0, 0, width, height, null);

			g2.dispose();

			ImageIO.write(resized, format, resizedImage);

		} catch (IOException e) {
			System.out.println("Could't resize");
		}
	}

	/**
	 * Byte to image.
	 *
	 * @param imageByteArray the image byte array
	 * @param Location the location
	 */
	public void byteToImage(byte[] imageByteArray, File Location) {
		InputStream in = new ByteArrayInputStream(imageByteArray);
		BufferedImage bImageFromConvert;
		try {
			bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "jpg", Location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Image to byte.
	 *
	 * @param image the image
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] ImageToByte(File image) throws IOException {
		return Files.readAllBytes(image.toPath());
	}

}
