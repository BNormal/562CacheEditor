package editor.utils;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import editor.io.OutputStream;
import editor.store.Store;


public final class Utils {
	
	public static int[] hsl2rgb;
	
	public static void setColors() {
		int[] out1 = hsl2rgb = new int[65536];
        double d = 0.7D;
        int i = 0;

        for (int i1 = 0; i1 != 512; ++i1)
        {
            float f = ((float)(i1 >> 3) / 64.0F + 0.0078125F) * 360.0F;
            float f1 = 0.0625F + (float)(7 & i1) / 8.0F;

            for (int i2 = 0; i2 != 128; ++i2)
            {
                float f2 = (float)i2 / 128.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = f / 60.0F;
                int i3 = (int)f6;
                int i4 = i3 % 6;
                float f7 = f6 - (float)i3;
                float f8 = f2 * (-f1 + 1.0F);
                float f9 = f2 * (-(f7 * f1) + 1.0F);
                float f10 = (1.0F - f1 * (-f7 + 1.0F)) * f2;
                if (i4 == 0) {
                    f3 = f2;
                    f5 = f8;
                    f4 = f10;
                } else if (i4 == 1) {
                    f5 = f8;
                    f3 = f9;
                    f4 = f2;
                } else if (i4 == 2) {
                    f3 = f8;
                    f4 = f2;
                    f5 = f10;
                } else if (i4 == 3) {
                    f4 = f9;
                    f3 = f8;
                    f5 = f2;
                } else if (i4 == 4) {
                    f5 = f2;
                    f3 = f10;
                    f4 = f8;
                } else {
                    f4 = f8;
                    f5 = f9;
                    f3 = f2;
                }

                out1[i++] = (int)((float)Math.pow((double)f3, d) * 256.0F) << 16
                        | (int)((float)Math.pow((double)f4, d) * 256.0F) << 8
                        | (int)((float)Math.pow((double)f5, d) * 256.0F);
            }
        }
	}
	
	public static byte[] getArchivePacketData(int indexId, int archiveId, byte[] archive) {
		OutputStream stream = new OutputStream(archive.length+4);
		stream.writeByte(indexId);
		stream.writeShort(archiveId);
		stream.writeByte(0); //priority, no compression
		stream.writeInt(archive.length);
		int offset = 8;
		for(int index = 0; index < archive.length; index++) {
			 if(offset == 512) {
				 stream.writeByte(-1);
                 offset = 1;
             }
			 stream.writeByte(archive[index]);
			 offset++;
		}
		byte[] packet = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(packet, 0, packet.length);
		return packet;
	}
	
	
	public static int getNameHash(String name) {
		name = name.toLowerCase();
		int hash = 0;
		for (int index = 0; index < name.length(); index++)
			hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
		return hash;
	}
	
	public static final int getItemDefinitionsSize(Store store) {
		int lastArchiveId = store.getIndexes()[19].getLastArchiveId();
		return lastArchiveId * 256 + store.getIndexes()[19].getValidFilesCount(lastArchiveId);
	}
	
	public static final int getNPCDefinitionsSize(Store store) {
		int lastArchiveId = store.getIndexes()[18].getLastArchiveId();
		return lastArchiveId * 128 + store.getIndexes()[18].getValidFilesCount(lastArchiveId);
	}
	
	public static final int getObjectDefinitionsSize(Store store) {
		int lastArchiveId = store.getIndexes()[16].getLastArchiveId();
		return lastArchiveId * 256 + store.getIndexes()[16].getValidFilesCount(lastArchiveId);
	}
	
	public static final int getInterfaceDefinitionsSize(Store store) {
		return store.getIndexes()[3].getLastArchiveId();
	}
	
	public static final int getInterfaceDefinitionsComponentsSize(Store store, int interfaceId) {
		return store.getIndexes()[3].getValidFilesCount(interfaceId);
	}
	
	public static final int getSomethingSize(Store store) {
		int lastArchiveId = store.getIndexes()[Constants.SOMETHING_INDEX].getLastArchiveId();
		return store.getIndexes()[Constants.SOMETHING_INDEX].getValidFilesCount(lastArchiveId);
	}
	
	public static final int getClientScriptsSize(Store store) {
		int lastArchiveId = store.getIndexes()[Constants.CLIENT_SCRIPTS_INDEX].getLastArchiveId();
		return lastArchiveId;
	}
	
	public static final int getGraphicsDefinitionSize(Store store) {
		int lastArchiveId = store.getIndexes()[Constants.GRAPHICS_DEFINITION_INDEX].getLastArchiveId();
		return lastArchiveId * 256 + store.getIndexes()[Constants.GRAPHICS_DEFINITION_INDEX].getValidFilesCount(lastArchiveId);
	}
	// Models = 7, ClientScripts = 25
	
	/*
	 * TODO rename this
	 */
	private static final byte method1258(char c) {
		byte charByte;
		if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
			charByte = (byte) c;
		} else if (c != '\u20AC') {
			if (c != '\u201A') {
				if (c != '\u0192') {
					if (c == '\u201E') {
						charByte = -124;
					} else if (c != '\u2026') {
						if (c != '\u2020') {
							if (c == '\u2021') {
								charByte = -121;
							} else if (c == '\u02C6') {
								charByte = -120;
							} else if (c == '\u2030') {
								charByte = -119;
							} else if (c == '\u0160') {
								charByte = -118;
							} else if (c == '\u2039') {
								charByte = -117;
							} else if (c == '\u0152') {
								charByte = -116;
							} else if (c != '\u017D') {
								if (c == '\u2018') {
									charByte = -111;
								} else if (c != '\u2019') {
									if (c != '\u201C') {
										if (c == '\u201D') {
											charByte = -108;
										} else if (c != '\u2022') {
											if (c == '\u2013') {
												charByte = -106;
											} else if (c == '\u2014') {
												charByte = -105;
											} else if (c == '\u02DC') {
												charByte = -104;
											} else if (c == '\u2122') {
												charByte = -103;
											} else if (c != '\u0161') {
												if (c == '\u203A') {
													charByte = -101;
												} else if (c != '\u0153') {
													if (c == '\u017E') {
														charByte = -98;
													} else if (c != '\u0178') {
														charByte = 63;
													} else {
														charByte = -97;
													}
												} else {
													charByte = -100;
												}
											} else {
												charByte = -102;
											}
										} else {
											charByte = -107;
										}
									} else {
										charByte = -109;
									}
								} else {
									charByte = -110;
								}
							} else {
								charByte = -114;
							}
						} else {
							charByte = -122;
						}
					} else {
						charByte = -123;
					}
				} else {
					charByte = -125;
				}
			} else {
				charByte = -126;
			}
		} else {
			charByte = -128;
		}
		return charByte;
	}
	
	public static void copyToClipboard(String color) {
        StringSelection selection = new StringSelection(color);
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(selection, null);
    }
	
	public static Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
	
	public static int RGB_to_RS2HSB(int red, int green, int blue) {
		float[] HSB = Color.RGBtoHSB(red, green, blue, null);
		float hue = HSB[0];
		float saturation = HSB[1];
		float brightness = HSB[2];
		int encode_hue = (int) (hue * 63.0F);
		int encode_saturation = (int) (saturation * 7.0F);
		int encode_brightness = (int) (brightness * 127.0F);
		return (encode_hue << 10) + (encode_saturation << 7) + encode_brightness;
	}

	public static int RS2HSB_to_RGB(int RS2HSB) {
		int decode_hue = RS2HSB >> 10 & 0x3F;
		int decode_saturation = RS2HSB >> 7 & 0x7;
		int decode_brightness = RS2HSB & 0x7F;
		return Color.HSBtoRGB(decode_hue / 63.0F, decode_saturation / 7.0F, decode_brightness / 127.0F);
	}
	
	private Utils() {
		
	}
	
}
