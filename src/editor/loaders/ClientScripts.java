package editor.loaders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import editor.application.CacheEditor;
import editor.io.InputStream;
import editor.io.OutputStream;
import editor.store.Store;
import editor.utils.Constants;

public class ClientScripts {
	
	public char[] aCharArray6195;
	public String aString6198;
	public static int[][][] anIntArrayArrayArray6200 = new int[2][][];
	public char[] aCharArray6205;
	public int[] anIntArray6209;
	public int[] anIntArray6210;
	int[] anIntArray6356;
	int[] anIntArray6369;
	String[] interfaceText;
	int anInt6361;
	int anInt6362;
	int anInt6366;
	int anInt6359;
	
	public ArrayList<Object> scripts = new ArrayList<Object>();
	public int id;
	
	public void write(Store store) {
		store.getIndexes()[Constants.CLIENT_SCRIPTS_INDEX].putFile(id, 0, encode());
	}
	
	public byte[] encode() {
		OutputStream stream = new OutputStream();
		
		stream.writeByte(1);//start
		
		
		
		stream.writeByte(0);//end
		
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}
	
	public void loadScripts(int id) {
		try {
			this.id = id;
			byte[] data = CacheEditor.STORE.getIndexes()[Constants.CLIENT_SCRIPTS_INDEX].getFile(id/*ArchiveId*/, 0/*FileID*/);//19
			if (data == null || data.length <= 1)
				return;
			method231(data, -2);
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public void method231(byte[] data, int arg1) {
		try {
			//Class131_Sub41_Sub15 class131_sub41_sub15 = new Class131_Sub41_Sub15();
			InputStream stream = new InputStream(data);
			stream.offset = stream.getBuffer().length + -2;
			int i = stream.readUnsignedShort();
			int i_0_ = -12 + stream.getBuffer().length + arg1 + -i;
			stream.offset = i_0_;
			int size = stream.readInt();
			anInt6361 = stream.readUnsignedShort();
			anInt6362 = stream.readUnsignedShort();
			anInt6366 = stream.readUnsignedShort();
			anInt6359 = stream.readUnsignedShort();
			
			int i_2_ = stream.readUnsignedByte();
			if (i_2_ > 0) {
				//class131_sub41_sub15.aClass180Array6353 = new HashTable[i_2_];
				for (int i_3_ = 0; i_3_ < i_2_; i_3_++) {
					int i_4_ = stream.readUnsignedShort();
					//HashTable class180 = new HashTable(Class101.method887(1388313616, i_4_));
					//class131_sub41_sub15.aClass180Array6353[i_3_] = class180;
					while ((i_4_-- ^ 0xffffffff) < -1) {
						int i_5_ = stream.readInt();
						int i_6_ = stream.readInt();
						//class180.method2523(false, (long) i_5_, new IntegerNode(i_6_));
					}
				}
			}
			stream.offset = 0;
			String aString6352 = stream.readString();
			anIntArray6356 = new int[size];
			anIntArray6369 = new int[size];
			interfaceText = new String[size];
			int i_7_ = 0;
			while ((stream.offset ^ 0xffffffff) > (i_0_ ^ 0xffffffff)) {//ClientScripts
				int i_8_ = stream.readUnsignedShort();
				//System.out.print(i_8_ + ", ");
				if (i_8_ != 3) {
					if (i_8_ < 100 && i_8_ != 21 && i_8_ != 38 && i_8_ != 39) {
						anIntArray6356[i_7_] = stream.readInt();
						//System.out.print(anIntArray6356[i_7_] + " ");
						scripts.add((int) anIntArray6356[i_7_]);
					} else {
						anIntArray6356[i_7_] = (stream.readUnsignedByte());
						scripts.add((byte) anIntArray6356[i_7_]);
					}
				} else {
					interfaceText[i_7_] = stream.readString().intern();
					scripts.add((String) interfaceText[i_7_]);
				}
				anIntArray6369[i_7_++] = i_8_;
			}
			//System.out.println("");
			String dump = "Script ID " + id + ": ";
			for (int j = 0; j < scripts.size(); j++) {
				dump += scripts.get(j) + " ";
			}
			dump += "\n";
			writeData(dump);
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
	}
	public static void writeData(String text){
		
		BufferedWriter bw = null;
        try {
        	File file = new File("ClientScriptDumps.txt");
	    	if(!file.exists())
	    		file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(text);
            bw.newLine();
            bw.flush();
	    	bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}
	
}
