package editor.loaders;

import editor.io.InputStream;
import editor.store.Store;
import editor.utils.Constants;

public class Something {

    public char[] aCharArray6195;
    public String aString6198;
	public static int[][][] anIntArrayArrayArray6200 = new int[2][][];
    public char[] aCharArray6205;
    public int[] anIntArray6209;
    public int[] anIntArray6210;
	
	public void loadScript(Store cache, int id) {
		try {
		    byte[] data = cache.getIndexes()[Constants.SOMETHING_INDEX].getFile(0, id);
		    if (data != null)
		    	readOpCodes(new InputStream(data), -32);
		    	
		} catch (RuntimeException runtimeexception) {
		}
	}

	public void readOpCodes(InputStream arg0, int arg1) {
		for (;;) {
			int i = arg0.readUnsignedByte();
			if ((i ^ 0xffffffff) == -1)
				break;
			method1940((byte) 62, i, arg0);
		}
		/* if (arg1 > -15) method1942(-72, null, 75, null, -63, false, 70, 113, 7, null, 93, true); */
	}

	@SuppressWarnings("unused")
	public void method1940(byte arg0, int arg1, InputStream arg2) {
		try {
			if (arg0 <= 46)
				anIntArrayArrayArray6200 = null;
			if (arg1 == 1)
				aString6198 = arg2.readString();
			else if ((arg1 ^ 0xffffffff) == -3) {
				int i = arg2.readUnsignedByte();
				aCharArray6205 = new char[i];
				anIntArray6209 = new int[i];
				for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > (i ^ 0xffffffff); i_1_++) {
					anIntArray6209[i_1_] = arg2.readUnsignedShort();
					byte i_2_ = (byte) arg2.readUnsignedByte();//readSignedByte();
					//aCharArray6205[i_1_] = ((i_2_ ^ 0xffffffff) != -1 ? HashMap.method2782(49, i_2_) : '\0');
				}
			} else if ((arg1 ^ 0xffffffff) == -4) {
				int i = arg2.readUnsignedByte();
				anIntArray6210 = new int[i];
				aCharArray6195 = new char[i];
				for (int i_3_ = 0; i_3_ < i; i_3_++) {
					anIntArray6210[i_3_] = arg2.readUnsignedShort();
					byte i_4_ = (byte) arg2.readUnsignedByte();//readSignedByte()
					//aCharArray6195[i_3_] = ((i_4_ ^ 0xffffffff) == -1 ? '\0' : HashMap.method2782(66, i_4_));
				}
			}
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}

	/*public static void method1942(int arg0, Toolkit arg1, int arg2, Class19[] arg3, int arg4, boolean arg5, int arg6, int arg7, int arg8, byte[] arg9, int arg10, boolean arg11) {
		do {
			try {
				//anInt6202++;
				InputStream stream = new InputStream(arg9);
				int i = -1;
				for (;;) {
					int i_15_ = stream.method1758(32767);
					if ((i_15_ ^ 0xffffffff) == -1)
						break;
					i += i_15_;
					int i_16_ = 0;
					for (;;) {
						int i_17_ = stream.readUnsignedSmart();
						if (i_17_ == 0)
							break;
						i_16_ += i_17_ + -1;
						int i_18_ = i_16_ & 0x3f;
						int i_19_ = (0xfe5 & i_16_) >> -1821055898;
						int i_20_ = i_16_ >> -268354868;
						int i_21_ = stream.readUnsignedByte();
						int i_22_ = i_21_ >> 1669226082;
						int i_23_ = 0x3 & i_21_;
						if ((i_20_ ^ 0xffffffff) == (arg7 ^ 0xffffffff) && (i_19_ ^ 0xffffffff) <= (arg8 ^ 0xffffffff)
								&& (i_19_ ^ 0xffffffff) > (arg8 + 8 ^ 0xffffffff) && arg0 <= i_18_
								&& arg0 - -8 > i_18_) {
							ObjectDefinitions class187 = Class55.createObjectDefinitions(i, -31);
							int i_24_ = (Class35.method468(0x7 & i_18_, class187.anInt2668, class187.anInt2702, 65536,
									i_23_, i_19_ & 0x7, arg6) + arg2);
							int i_25_ = Class131_Sub19.method1811(-6459, i_18_ & 0x7, class187.anInt2668,
									class187.anInt2702, i_19_ & 0x7, i_23_, arg6) + arg4;
							if (i_24_ > 0 && (i_25_ ^ 0xffffffff) < -1
									&& (i_24_ < Class131_Sub41_Sub11_Sub1.map_sizeX + -1)
									&& (Class131_Sub2_Sub26.mapSizeY + -1 > i_25_)) {
								Class19 class19 = null;
								if (!arg5) {
									int i_26_ = arg10;
									if (((Class23_Sub2_Sub1.aByteArrayArrayArray4992[1][i_24_][i_25_]) & 0x2
											^ 0xffffffff) == -3)
										i_26_--;
									if ((i_26_ ^ 0xffffffff) <= -1)
										class19 = arg3[i_26_];
								}
								//Class145.method2168(true, i, arg1, (byte) -91, arg10, class19, i_22_, arg10, i_24_, 0x3 & i_23_ - -arg6, i_25_, -1, arg5);
							}
						}
					}
				}
				if (arg11 == false)
					break;
			} catch (RuntimeException runtimeexception) {
				runtimeexception.printStackTrace();
			}
			break;
		} while (false);
	}*/
}
