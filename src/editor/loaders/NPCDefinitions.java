package editor.loaders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import editor.application.CacheEditor;
import editor.io.InputStream;
import editor.io.OutputStream;
import editor.store.Store;
import editor.utils.Constants;

@SuppressWarnings("unused")
public final class NPCDefinitions implements Cloneable {

	// private static final ConcurrentHashMap<Integer, NPCDefinitions>
	// npcDefinitions = new ConcurrentHashMap<Integer, NPCDefinitions>();

	public int id;
	public HashMap<Integer, Object> clientScriptData;
	public int anInt833;
	public int anInt836;
	public int anInt837;
	public byte respawnDirection;
	public int size = 1;
	public int[][] anIntArrayArray840;
	public boolean aBoolean841;
	public int anInt842;
	public int anInt844;
	public int[] anIntArray845;
	public int anInt846;
	public int renderEmote;
	public boolean aBoolean849 = false;
	public int anInt850;
	public byte aByte851;
	public boolean aBoolean852;
	public int anInt853;
	public byte aByte854;
	public boolean aBoolean856;
	public boolean aBoolean857;
	public short[] originalModelColors;
	public short[] modifiedModelColors;
	public int combatLevel;
	public byte[] aByteArray861;
	public short aShort862;
	public boolean aBoolean863;
	public int height;
	public String name;
	public short[] modifiedTextureColors;
	public byte walkMask;
	public int[] models;
	public int anInt869;
	public int anInt870;
	public int anInt871;
	public int anInt872;
	public int anInt874;
	public int anInt875;
	public int anInt876;
	public int headIcons;
	public int anInt879;
	public short[] originalTextureColors;
	public int[][] anIntArrayArray882;
	public int anInt884;
	public int[] anIntArray885;
	public int anInt888;
	public int anInt889;
	public boolean isVisibleOnMap;
	public int[] dialogueModels;
	public short aShort894;
	public String[] options;
	public int anInt897;
	public int width;
	public int npcId;
	public int anInt901;

	public static NPCDefinitions getNPCDefinition(Store store, int itemId) {
		return getNPCDefinition(store, itemId, true);
	}

	public static NPCDefinitions getNPCDefinition(Store store, int itemId, boolean load) {
		return new NPCDefinitions(store, itemId, load);
	}

	public NPCDefinitions(Store store, int id) {
		this(store, id, true);
	}

	public NPCDefinitions(Store store, int id, boolean load) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		if (load)
			loadNPCDefinitions(store);
	}

	public void loadNPCDefinitions(Store cache) {
		byte[] data = cache.getIndexes()[Constants.NPC_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			CacheEditor.addMessage("FAILED LOADING NPC " + id);
			return;
		}
		readValueLoop(new InputStream(data));
	}

	public int getArchiveId() {
		return id >>> 134238215;
	}

	public int getFileId() {
		return id & 0x7f;
	}

	public void method694() {
		if (models == null)
			models = new int[0];
	}

	private void readValueLoop(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public void write(Store store) {
		store.getIndexes()[Constants.NPC_DEFINITIONS_INDEX].putFile(getArchiveId(), getFileId(), encode());
	}

	public byte[] encode() {
		OutputStream stream = new OutputStream();

		if (models != null) {
			stream.writeByte(1);
			stream.writeByte(models.length);// modelIds
			for (int i = 0; i < models.length; i++)
				stream.writeBigSmart(models[i]);
		}
		if (!this.name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(name);
		}

		stream.writeByte(12);
		stream.writeByte(size);

		for (int index = 0; index < this.options.length; index++) {
			stream.writeByte(30 + index);
			if (this.options[index] == null || this.options[index].equals("null"))
				stream.writeString("Hidden");
			else
				stream.writeString(this.options[index]);
		}

		if ((this.originalModelColors != null) && (this.modifiedModelColors != null)) {
			stream.writeByte(40);
			stream.writeByte(this.originalModelColors.length);
			for (int index = 0; index < this.originalModelColors.length; index++) {
				stream.writeShort(this.originalModelColors[index]);// originalTextureColors
				stream.writeShort(this.modifiedModelColors[index]);// modifiedTextureColors
			}
		}
		
		if ((this.originalTextureColors != null) && (this.modifiedTextureColors != null)) {
			stream.writeByte(41);
			stream.writeByte(this.originalTextureColors.length);
			for (int index = 0; index < this.originalTextureColors.length; index++) {
				stream.writeShort(this.originalTextureColors[index]);// originalModelColors
				stream.writeShort(this.modifiedTextureColors[index]);// modifiedModelColors
			}
		}

		if (this.dialogueModels != null) {// chatHeadArray
			stream.writeByte(60);
			stream.writeByte(this.dialogueModels.length);
			for (int index = 0; index < this.dialogueModels.length; index++) {
				stream.writeBigSmart(this.dialogueModels[index]);
			}
		}

		if (!isVisibleOnMap)
			stream.writeByte(93);

		if (this.combatLevel > -1) {
			stream.writeByte(95);
			stream.writeShort(combatLevel);
		}

		/*if (walkMask > -1) {
			stream.writeByte(191);
			stream.writeBigSmart(walkMask);
		}*/
		
		if (height != 0) {// npcHeight
			stream.writeByte(97);
			stream.writeShort(height);
		}

		if (width != 0) {// npcWidth
			stream.writeByte(98);
			stream.writeShort(width);
		}

		if (headIcons > -1) {
			stream.writeByte(102);
			stream.writeShort(headIcons);
		}

		/*if (this.respawnDirection != 7) {
			stream.writeByte(125);
			stream.writeBigSmart(this.respawnDirection);
		}*/

		if (this.renderEmote != -1) {
			stream.writeByte(127);
			stream.writeShort(renderEmote);
		}
		
		for (int index = 0; index < this.options.length; index++) {
			stream.writeByte(150 + index);
			if (this.options[index] == null || this.options[index].equals("null"))
				stream.writeString("Hidden");
			else
				stream.writeString(this.options[index]);
		}

		@SuppressWarnings("rawtypes")
		Iterator i$;
		if (this.clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(this.clientScriptData.size());
			for (i$ = this.clientScriptData.keySet().iterator(); i$.hasNext();) {
				int key = ((Integer) i$.next()).intValue();
				Object value = this.clientScriptData.get(Integer.valueOf(key));
				stream.writeByte((value instanceof String) ? 1 : 0);
				stream.write24BitInt(key);
				if ((value instanceof String))
					stream.writeString((String) value);
				else {
					stream.writeInt(((Integer) value).intValue());
				}
			}
		}

		/*
		 * if(aClass180_832 != null) { stream.writeByte(249);
		 * stream.writeByte(aClass180_832.size()); for(int key :
		 * aClass180_832.keySet()) { Object value = aClass180_832.get(key);
		 * stream.writeByte(value instanceof String ? 1 : 0);
		 * stream.write24BitInt(key); if(value instanceof String) {
		 * stream.writeString((String) value); }else{ stream.writeInt((Integer)
		 * value); } } }
		 */

		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode != 1) {
			if (opcode == 2)
				name = stream.readString();
			else if ((opcode ^ 0xffffffff) != -13) {
				if (opcode >= 30 && opcode <= 34) {
					options[opcode - 30] = stream.readString();
					if (options[-30 + opcode].equalsIgnoreCase("Hidden")) {
						options[-30 + opcode] = null;
					}
				} else if ((opcode ^ 0xffffffff) != -41) {
					if (opcode == 41) {
						int i = stream.readUnsignedByte();
						originalTextureColors = new short[i];
						modifiedTextureColors = new short[i];
						for (int i_54_ = 0; (i_54_ ^ 0xffffffff) > (i ^ 0xffffffff); i_54_++) {
							originalTextureColors[i_54_] = (short) stream.readUnsignedShort();
							modifiedTextureColors[i_54_] = (short) stream.readUnsignedShort();
						}
					} else if ((opcode ^ 0xffffffff) == -43) {
						int i = stream.readUnsignedByte();
						aByteArray861 = new byte[i];
						for (int i_55_ = 0; i > i_55_; i_55_++)
							aByteArray861[i_55_] = (byte) stream.readByte();
					} else if ((opcode ^ 0xffffffff) != -61) {
						if (opcode == 93)
							isVisibleOnMap = false;
						else if ((opcode ^ 0xffffffff) == -96)
							combatLevel = stream.readUnsignedShort();
						else if (opcode != 97) {
							if ((opcode ^ 0xffffffff) == -99)
								width = stream.readUnsignedShort();
							else if ((opcode ^ 0xffffffff) == -100)
								aBoolean863 = true;
							else if (opcode == 100)
								anInt869 = stream.readByte();
							else if ((opcode ^ 0xffffffff) == -102)
								anInt897 = stream.readByte() * 5;
							else if ((opcode ^ 0xffffffff) == -103)
								headIcons = stream.readUnsignedShort();
							else if (opcode != 103) {
								if (opcode == 106 || opcode == 118) {
									anInt844 = stream.readUnsignedShort();
									if (anInt844 == 65535)
										anInt844 = -1;
									anInt888 = stream.readUnsignedShort();
									if (anInt888 == 65535)
										anInt888 = -1;
									int i = -1;
									if ((opcode ^ 0xffffffff) == -119) {
										i = stream.readUnsignedShort();
										if ((i ^ 0xffffffff) == -65536)
											i = -1;
									}
									int i_56_ = stream.readUnsignedByte();
									anIntArray845 = new int[2 + i_56_];
									for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
										anIntArray845[i_57_] = stream.readUnsignedShort();
										if (anIntArray845[i_57_] == 65535)
											anIntArray845[i_57_] = -1;
									}
									anIntArray845[i_56_ - -1] = i;
								} else if ((opcode ^ 0xffffffff) != -108) {
									if ((opcode ^ 0xffffffff) == -110)
										aBoolean852 = false;
									else if ((opcode ^ 0xffffffff) != -112) {
										if (opcode != 113) {
											if (opcode == 114) {
												aByte851 = (byte) (stream.readByte());
												aByte854 = (byte) (stream.readByte());
											} else if (opcode == 115) {
												stream.readUnsignedByte();
												stream.readUnsignedByte();
											} else if ((opcode ^ 0xffffffff) != -120) {
												if (opcode != 121) {
													if ((opcode ^ 0xffffffff) != -123) {
														if (opcode == 123)
															anInt846 = (stream.readUnsignedShort());
														else if (opcode != 125) {
															if (opcode == 127)
																renderEmote = (stream.readUnsignedShort());
															else if ((opcode ^ 0xffffffff) == -129)
																stream.readUnsignedByte();
															else if (opcode != 134) {
																if (opcode == 135) {
																	anInt833 = stream.readUnsignedByte();
																	anInt874 = stream.readUnsignedShort();
																} else if (opcode != 136) {
																	if (opcode != 137) {
																		if (opcode != 138) {
																			if ((opcode ^ 0xffffffff) != -140) {
																				if (opcode == 140)
																					anInt850 = stream
																							.readUnsignedByte();
																				else if (opcode == 141)
																					aBoolean849 = true;
																				else if ((opcode
																						^ 0xffffffff) != -143) {
																					if (opcode == 143)
																						aBoolean856 = true;
																					else if (opcode >= 150 && opcode < 155) {
																						options[opcode - 150] = stream.readString();
																						if (options[opcode - 150].equalsIgnoreCase("Hidden"))
																							options[opcode - 150] = null;
																					} else if ((opcode
																							^ 0xffffffff) == -161) {
																						int i = stream
																								.readUnsignedByte();
																						anIntArray885 = new int[i];
																						for (int i_58_ = 0; i > i_58_; i_58_++)
																							anIntArray885[i_58_] = stream
																									.readUnsignedShort();

																						// all
																						// added
																						// after
																						// here
																					} else if (opcode == 155) {
																						int aByte821 = stream
																								.readByte();
																						int aByte824 = stream
																								.readByte();
																						int aByte843 = stream
																								.readByte();
																						int aByte855 = stream
																								.readByte();
																					} else if (opcode == 158) {
																						byte aByte833 = (byte) 1;
																					} else if (opcode == 159) {
																						byte aByte833 = (byte) 0;
																					} else if (opcode == 162) { // added
																												// opcode
																						boolean aBoolean3190 = true;
																					} else if (opcode == 163) { // added
																												// opcode
																						int anInt864 = stream
																								.readUnsignedByte();
																					} else if (opcode == 164) {
																						int anInt848 = stream
																								.readUnsignedShort();
																						int anInt837 = stream
																								.readUnsignedShort();
																					} else if (opcode == 165) {
																						int anInt847 = stream
																								.readUnsignedByte();
																					} else if (opcode == 168) {
																						int anInt828 = stream
																								.readUnsignedByte();
																					} else if (opcode == 249) {
																						int i = stream.readUnsignedByte();
																						if (clientScriptData == null) {
																							/* int i_59_ = Class101 .method887(
																							 * 1388313616, i ) ; aClass180_832 =
																							 * new HashTable( i_59_ ) ; */
																							clientScriptData = new HashMap<Integer, Object>(i);
																						}
																						for (int i_60_ = 0; i > i_60_; i_60_++) {
																							boolean bool = stream.readUnsignedByte() == 1;
																							int key = stream.read24BitInt();
																							Object value;
																							if (bool)
																								value = stream.readString();
																							else
																								value = stream.readInt();
																							clientScriptData.put(key, value);
																						}
																					}
																				} else
																					anInt870 = stream
																							.readUnsignedShort();
																			} else
																				anInt879 = stream.readUnsignedShort();
																		} else
																			anInt901 = stream.readUnsignedShort();
																	} else
																		anInt872 = stream.readUnsignedShort();
																} else {
																	anInt837 = stream.readUnsignedByte();
																	anInt889 = stream.readUnsignedShort();
																}
															} else {
																anInt876 = (stream.readUnsignedShort());
																if (anInt876 == 65535)
																	anInt876 = -1;
																anInt842 = (stream.readUnsignedShort());
																if (anInt842 == 65535)
																	anInt842 = -1;
																anInt884 = (stream.readUnsignedShort());
																if ((anInt884 ^ 0xffffffff) == -65536)
																	anInt884 = -1;
																anInt871 = (stream.readUnsignedShort());
																if ((anInt871 ^ 0xffffffff) == -65536)
																	anInt871 = -1;
																anInt875 = (stream.readUnsignedByte());
															}
														} else
															respawnDirection = (byte) (stream.readByte());
													} else
														anInt836 = (stream.readUnsignedShort());
												} else {
													anIntArrayArray840 = (new int[models.length][]);
													int i = (stream.readUnsignedByte());
													for (int i_62_ = 0; ((i_62_ ^ 0xffffffff) > (i
															^ 0xffffffff)); i_62_++) {
														int i_63_ = (stream.readUnsignedByte());
														int[] is = (anIntArrayArray840[i_63_] = (new int[3]));
														is[0] = (stream.readByte());
														is[1] = (stream.readByte());
														is[2] = (stream.readByte());
													}
												}
											} else
												walkMask = (byte) (stream.readByte());
										} else {
											aShort862 = (short) (stream.readUnsignedShort());
											aShort894 = (short) (stream.readUnsignedShort());
										}
									} else
										aBoolean857 = false;
								} else
									aBoolean841 = false;
							} else
								anInt853 = stream.readUnsignedShort();
						} else
							height = stream.readUnsignedShort();
					} else {
						int i = stream.readUnsignedByte();
						dialogueModels = new int[i];
						for (int i_64_ = 0; (i_64_ ^ 0xffffffff) > (i ^ 0xffffffff); i_64_++)
							dialogueModels[i_64_] = stream.readUnsignedShort();
					}
				} else {
					int i = stream.readUnsignedByte();
					modifiedModelColors = new short[i];
					originalModelColors = new short[i];
					for (int i_65_ = 0; (i ^ 0xffffffff) < (i_65_ ^ 0xffffffff); i_65_++) {
						originalModelColors[i_65_] = (short) stream.readUnsignedShort();
						modifiedModelColors[i_65_] = (short) stream.readUnsignedShort();
					}
				}
			} else
				size = stream.readUnsignedByte();
		} else {
			int i = stream.readUnsignedByte();
			models = new int[i];
			for (int i_66_ = 0; i_66_ < i; i_66_++) {
				models[i_66_] = stream.readUnsignedShort();
				if ((models[i_66_] ^ 0xffffffff) == -65536)
					models[i_66_] = -1;
			}
		}
	}

	/*
	 * public static final void clearNPCDefinitions() { npcDefinitions.clear();
	 * }
	 */

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDefaultOptions() {
		options = new String[5];
	}

	public void setDefaultsVariableValues() {
		anInt842 = -1;
		anInt844 = -1;
		anInt837 = -1;
		anInt846 = -1;
		anInt853 = 32;
		combatLevel = -1;
		anInt836 = -1;
		name = "null";
		anInt869 = 0;
		walkMask = (byte) 0;
		anInt850 = 255;
		anInt871 = -1;
		aBoolean852 = true;
		aShort862 = (short) 0;
		anInt876 = -1;
		aByte851 = (byte) -96;
		anInt875 = 0;
		anInt872 = -1;
		renderEmote = -1;
		respawnDirection = (byte) 7;
		aBoolean857 = true;
		anInt870 = -1;
		anInt874 = -1;
		anInt833 = -1;
		height = 128;
		headIcons = -1;
		aBoolean856 = false;
		anInt888 = -1;
		aByte854 = (byte) -16;
		aBoolean863 = false;
		isVisibleOnMap = true;
		anInt889 = -1;
		anInt884 = -1;
		aBoolean841 = true;
		anInt879 = -1;
		width = 128;
		aShort894 = (short) 0;
		anInt897 = 0;
		anInt901 = -1;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return id + " - " + name;
	}

	public boolean hasMarkOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("mark"))
				return true;
		}
		return false;
	}

	public boolean hasAttackOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("attack"))
				return true;
		}
		return false;
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op))
				return true;
		}
		return false;
	}

	public String[] options() {
		return options;
	}
}
