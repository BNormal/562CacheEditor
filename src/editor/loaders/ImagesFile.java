package editor.loaders;

import java.awt.image.BufferedImage;

import editor.io.InputStream;
import editor.io.OutputStream;
import editor.store.Store;
import editor.utils.Constants;

public final class ImagesFile {

	private BufferedImage[] images;

	private int pallete[];
	private int pixelsIndexes[][];
	private byte extraPixels[][];
	private boolean[] usesExtraPixels;
	private int biggestWidth;
	private int biggestHeight;

	public ImagesFile(BufferedImage... images) {
		this.images = images;
	}

	public ImagesFile(Store store, int containerId, int fileId) {
		decodeContainer(store, containerId, fileId);
	}

	public void decodeContainer(Store store, int containerId, int fileId) {
		byte[] data = store.getIndexes()[Constants.IMAGES_INDEX].getFile(containerId, fileId);
		if (data == null)
			return;
		InputStream stream = new InputStream(data);
		stream.offset = data.length - 2;
		int count = stream.readUnsignedShort();
		boolean newformat = false;
		if (count == 0) {
			newformat = true;
			stream.offset = data.length - 4;
			count = stream.readUnsignedShort();

		}
		images = new BufferedImage[count];
		pixelsIndexes = new int[images.length][];
		extraPixels = new byte[images.length][];
		usesExtraPixels = new boolean[images.length];
		int[] imagesMinX = new int[images.length];
		int[] imagesMinY = new int[images.length];
		int[] imagesWidth = new int[images.length];
		int[] imagesHeight = new int[images.length];
		stream.offset = data.length - (newformat ? 13 : 7) - images.length * 8;
		setBiggestWidth(stream.readShort()); // biggestWidth
		setBiggestHeight(stream.readShort()); // biggestHeight
		int palleteLength = (newformat ? stream.readInt() : (stream.readUnsignedByte() & 0xff)) + 1;
		for (int index = 0; index < images.length; index++)
			imagesMinX[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesMinY[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesWidth[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesHeight[index] = stream.readUnsignedShort();
		stream.offset = data.length - (newformat ? 13 : 7) - images.length * 8 - (palleteLength - 1) * 3;
		pallete = new int[palleteLength];
		for (int index = 1; index < palleteLength; index++) {
			pallete[index] = stream.read24BitInt();
			if (pallete[index] == 0)
				pallete[index] = 1;
		}
		stream.offset = 0;
		for (int i_20_ = 0; i_20_ < images.length; i_20_++) {
			int pixelsIndexesLength = imagesWidth[i_20_] * imagesHeight[i_20_];
			pixelsIndexes[i_20_] = new int[pixelsIndexesLength];
			extraPixels[i_20_] = new byte[pixelsIndexesLength];
			int maskData = stream.readUnsignedByte();
			if ((maskData & 0x2) == 0) {
				if ((maskData & 0x1) == 0) {
					for (int index = 0; index < pixelsIndexesLength; index++) {
						pixelsIndexes[i_20_][index] = (byte) stream.readByte();
					}
				} else {
					for (int i_24_ = 0; i_24_ < imagesWidth[i_20_]; i_24_++) {
						for (int i_25_ = 0; i_25_ < imagesHeight[i_20_]; i_25_++) {
							pixelsIndexes[i_20_][i_24_ + i_25_ * imagesWidth[i_20_]] = (byte) stream.readByte();
						}
					}
				}
			} else {
				usesExtraPixels[i_20_] = true;
				boolean bool = false;
				if ((maskData & 0x1) == 0) {
					for (int index = 0; index < pixelsIndexesLength; index++) {
						pixelsIndexes[i_20_][index] = (byte) stream.readByte();
					}
					for (int i_27_ = 0; i_27_ < pixelsIndexesLength; i_27_++) {
						byte i_28_ = (extraPixels[i_20_][i_27_] = (byte) stream.readByte());
						bool = bool | i_28_ != -1;
					}
				} else {
					for (int i_29_ = 0; i_29_ < imagesWidth[i_20_]; i_29_++) {
						for (int i_30_ = 0; i_30_ < imagesHeight[i_20_]; i_30_++) {
							pixelsIndexes[i_20_][i_29_ + i_30_ * imagesWidth[i_20_]] = stream.readByte();
						}
					}
					for (int i_31_ = 0; i_31_ < imagesWidth[i_20_]; i_31_++) {
						for (int i_32_ = 0; i_32_ < imagesHeight[i_20_]; i_32_++) {
							byte i_33_ = (extraPixels[i_20_][i_31_ + i_32_ * imagesWidth[i_20_]] = (byte) stream.readByte());
							bool = bool | i_33_ != -1;
						}
					}
				}
				if (!bool)
					extraPixels[i_20_] = null;
			}
			images[i_20_] = getBufferedImage(imagesWidth[i_20_], imagesHeight[i_20_], pixelsIndexes[i_20_],
					extraPixels[i_20_], usesExtraPixels[i_20_]);
		}
	}

	public BufferedImage getBufferedImage(int width, int height, int[] pixelsIndexes, byte[] extraPixels,
			boolean useExtraPixels) {
		if (width <= 0 || height <= 0)
			return null;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] rgbArray = new int[width * height];
		int i = 0;
		int i_43_ = 0;
		if (useExtraPixels && extraPixels != null) {
			for (int i_44_ = 0; i_44_ < height; i_44_++) {
				for (int i_45_ = 0; i_45_ < width; i_45_++) {
					rgbArray[i_43_++] = (extraPixels[i] << 24 | (pallete[pixelsIndexes[i] & 0xff]));
					i++;
				}
			}
		} else {
			for (int i_46_ = 0; i_46_ < height; i_46_++) {
				for (int i_47_ = 0; i_47_ < width; i_47_++) {
					int i_48_ = pallete[pixelsIndexes[i++] & 0xff];
					rgbArray[i_43_++] = i_48_ != 0 ? ~0xffffff | i_48_ : 0;
				}
			}
		}
		image.setRGB(0, 0, width, height, rgbArray, 0, width);
		image.flush();
		return image;
	}

	public byte[] encodeContainer() {
		if (pallete == null) // if not generated yet
			generatePallete();
		OutputStream stream = new OutputStream(100000000);
		// sets pallete indexes and int size bytes
		for (int imageId = 0; imageId < images.length; imageId++) {
			int pixelsMask = 0;
			if (usesExtraPixels[imageId])
				pixelsMask |= 0x2;
			// pixelsMask |= 0x1; //sets read all rgbarray indexes 1by1
			stream.writeByte(pixelsMask);
			for (int index = 0; index < pixelsIndexes[imageId].length; index++)
				if (pallete.length > 256)
					stream.writeInt(pixelsIndexes[imageId][index]);
				else
					stream.writeByte(pixelsIndexes[imageId][index]);
			if (usesExtraPixels[imageId])
				for (int index = 0; index < extraPixels[imageId].length; index++)
					stream.writeByte(extraPixels[imageId][index]);
		}

		// sets up to 256colors pallete
		for (int index = 0; index < pallete.length; index++)
			stream.write24BitInt(pallete[index]);

		// extra inform
		if (biggestWidth == 0 && biggestHeight == 0) {
			for (BufferedImage image : images) {
				if (image.getWidth() > biggestWidth)
					biggestWidth = image.getWidth();
				if (image.getHeight() > biggestHeight)
					biggestHeight = image.getHeight();
			}
		}
		stream.writeShort(biggestWidth); // probably used for textures
		stream.writeShort(biggestHeight);// probably used for textures
		if (pallete.length > 256)
			stream.writeInt(pallete.length - 1);
		else
			stream.writeByte(pallete.length - 1); // sets pallete size
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getMinX());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getMinY());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getWidth());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getHeight());
		stream.writeShort(images.length); // amt of images
		if (pallete.length > 256)
			stream.writeShort(0); // tells new header type
		// generates fixed byte data array
		byte[] container = new byte[stream.offset];
		stream.offset = 0;
		stream.getBytes(container, 0, container.length);

		return container;
	}

	public int convertTo24BitInt(int rgb) {
		OutputStream oStream = new OutputStream(4);
		oStream.writeInt(rgb);
		oStream.offset = 1;
		InputStream iStream = new InputStream(oStream.getBuffer());
		iStream.setOffset(oStream.getOffset());
		iStream.setBitPosition(oStream.getBitPosition());
		int i = iStream.read24BitInt();
		return i;
	}

	public int getPalleteIndex(int rgb) {
		if (pallete == null)
			pallete = new int[] { 0 };
		for (int index = 0; index < pallete.length; index++) {
			if (pallete[index] == rgb)
				return index;
		}
		if (pallete.length == 257)
			throw new RuntimeException("Pallete to big, please reduce images quality.");
		int[] newpallete = new int[pallete.length + 1];
		System.arraycopy(pallete, 0, newpallete, 0, pallete.length);
		newpallete[pallete.length] = rgb;
		pallete = newpallete;
		return pallete.length - 1;
	}

	public void generatePallete() {
		pixelsIndexes = new int[images.length][];
		extraPixels = new byte[images.length][];
		usesExtraPixels = new boolean[images.length];
		for (int index = 0; index < images.length; index++) {
			BufferedImage image = images[index];
			int[] rgbArray = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
			pixelsIndexes[index] = new int[image.getWidth() * image.getHeight()];
			extraPixels[index] = new byte[image.getWidth() * image.getHeight()];
			for (int pixel = 0; pixel < pixelsIndexes[index].length; pixel++) {
				int rgb = rgbArray[pixel];
				int medintrgb = convertTo24BitInt(rgb);
				int i = getPalleteIndex(medintrgb);
				pixelsIndexes[index][pixel] = i;
				if (rgb >> 24 != 0) {
					extraPixels[index][pixel] = (byte) (rgb >> 24);
					usesExtraPixels[index] = true;
				}
			}
		}
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public int getBiggestWidth() {
		return biggestWidth;
	}

	public void setBiggestWidth(int biggestWidth) {
		this.biggestWidth = biggestWidth;
	}

	public int getBiggestHeight() {
		return biggestHeight;
	}

	public void setBiggestHeight(int biggestHeight) {
		this.biggestHeight = biggestHeight;
	}

}
