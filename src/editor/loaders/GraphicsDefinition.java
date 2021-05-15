package editor.loaders;

import java.util.HashMap;

import editor.io.InputStream;
import editor.store.Store;
import editor.utils.Constants;

public class GraphicsDefinition {
	
	public static int anInt6332;
	public HashMap<Integer, Object> clientScriptData;
	//public HashTable aClass180_6333;
	public static int anInt6334;
	public static int anInt6335;
	public static int anInt6336;
	public char aChar6337;
	public String aString6338 = "null";
	public static int anInt6339;
	public static int anInt6340;
	public static int anInt6341;
	public static int anInt6342;
	public static int anInt6343;
	public static int anInt6344;
	public char aChar6345;
	public static int anInt6346;
	public static int anInt6347;
	//public HashTable aClass180_6348;
	public int anInt6349;
	
	public void loadScript(Store cache, int id) {
		try {
			byte[] is = cache.getIndexes()[Constants.GRAPHICS_DEFINITION_INDEX].getFile(0, id);
			if (is != null)
				readOpCodes(new InputStream(is), -11607);
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}
	/*public static Class131_Sub41_Sub14 method2396(int arg0, int arg1) {
		try {
			anInt2160++;
			Class131_Sub41_Sub14 class131_sub41_sub14 = ((Class131_Sub41_Sub14) Class131_Sub2_Sub21.aClass137_5853.method2111(arg0 ^ ~0x10b, (long) arg1));
			if (class131_sub41_sub14 != null)
				return class131_sub41_sub14;
			byte[] is = (GraphicsDefinitions.aClass158_1437.method2364(Class131_Sub18.method1803((byte) 41, arg1), Class131_Sub8.method1674(arg1, 0), 0));
			class131_sub41_sub14 = new Class131_Sub41_Sub14();
			if (arg0 != 256)
				return null;
			if (is != null)
				class131_sub41_sub14.method2030(new Stream(is), -11607);
			Class131_Sub2_Sub21.aClass137_5853.method2110(class131_sub41_sub14, false, (long) arg1);
			return class131_sub41_sub14;
		} catch (RuntimeException runtimeexception) {
			throw Class131_Sub2_Sub6.method1495(runtimeexception, ("qr.G(" + arg0 + ',' + arg1 + ')'));
		}
	}*/
	
	public void readOpCodes(InputStream stream, int arg1) {
		try {
			for (;;) {
				int i = stream.readUnsignedByte();
				if (i == 0)
					break;
				method2021(i, stream, 0);
			}
			/*if (arg1 != -11607)
				method2027(-112, -14);*/
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public void method2021(int arg0, InputStream stream, int arg2) {
		try {
			if (arg2 == 0) {
				if (arg0 == 1) {
					stream.readUnsignedByte();
					//aChar6337 = HashMap.method2782(52, stream.readUnsignedByte();
				} else if ((arg0 ^ 0xffffffff) != -3) {
					if ((arg0 ^ 0xffffffff) != -4) {
						if (arg0 == 4)
							anInt6349 = stream.readInt();
						else if ((arg0 ^ 0xffffffff) == -6 || (arg0 ^ 0xffffffff) == -7) {
							int i = stream.readUnsignedShort();
							//aClass180_6348 = new HashTable(Class101.method887(1388313616, i));
							for (int i_0_ = 0; (i_0_ ^ 0xffffffff) > (i ^ 0xffffffff); i_0_++) {
								int i_1_ = stream.readInt();
								//Class131 class131;
								if (arg0 == 5) {
									System.out.println("1: " + stream.readString());
									//class131 = (new Class131_Sub29(stream.readString()));
								} else {
									int u = stream.readInt();
									//class131 = (new IntegerNode(stream.readInt()));
								//aClass180_6348.method2523(false, (long) i_1_, class131);
								}
							}
						}
					} else
						aString6338 = stream.readString();
					System.out.println("2: " + aString6338);
						if(aString6338.equals("this skill"))
							aString6338 = "Dungeoneering";
				} else {
					int j = stream.readUnsignedByte();
				//aChar6345 = HashMap.method2782(44, stream.readUnsignedByte());
				}
				anInt6346++;
			}
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}
	
	/*public static void method2027(int arg0, int arg1) {
		try {
			anInt6339++;
			synchronized (IComponent.aClass214_2425) {
				IComponent.aClass214_2425.method2786(0, arg0);
			}
			synchronized (Class153.aClass214_2040) {
				Class153.aClass214_2040.method2786(0, arg0);
			}
			synchronized (Class131_Sub2_Sub30.aClass214_5988) {
				Class131_Sub2_Sub30.aClass214_5988.method2786(0, arg0);
			}
			synchronized (Class192.aClass214_2824) {
				Class192.aClass214_2824.method2786(0, arg0);
				int i = -92 % ((arg1 - 64) / 55);
			}
		} catch (RuntimeException runtimeexception) {
			throw Class131_Sub2_Sub6.method1495(runtimeexception, ("of.E(" + arg0 + ',' + arg1 + ')'));
		}
	}*/
}
