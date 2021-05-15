package editor.loaders;

import java.util.Arrays;
import java.util.HashMap;

import editor.application.CacheEditor;
import editor.io.InputStream;
import editor.io.OutputStream;
import editor.store.Store;
import editor.utils.Constants;

@SuppressWarnings("unused")
public class ItemDefinitions implements Cloneable {
	
	public int id;
	private boolean loaded;
	
	private int invModelId;
	private String name;
	
	//model size information
	public int invModelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int modelOffset1;
	public int modelOffset2;
	
	//extra information
	public int stackable;
	public int value;
	public boolean membersOnly;
	
	//wearing model information
	public int maleEquipModelId1;
	public int femaleEquipModelId1;
	public int maleEquipModelId2;
	public int femaleEquipModelId2;

	public int maleEquipModelId3;
	public int femaleEquipModelId3;
	//options
	private String[] groundOptions;
	public String[] inventoryOptions;
	
	//model information
	public short[] originalModelColors;
	public short[] modifiedModelColors;
	public short[] originalTextureColors;
	public short[] modifiedTextureColors;
	public byte[] unknownArray1;
	public int[] unknownArray2;
	//extra information, not used for newer items
	private boolean unnoted;
	
	public int maleDialogueModel;
	public int femaleDialogueModel;
	private int unknownInt3;
	private int unknownInt4;
	private int unknownInt5;
	private int unknownInt6;
	public int switchNoteItemId;
	public int notedItemId;
	public int[] stackIds;
	public int[] stackAmounts;
	private int unknownInt7;
	private int unknownInt8;
	private int unknownInt9;
	private int unknownInt10;
	private int unknownInt11;
	public int teamId;
	public int switchLendItemId;
	public int lendedItemId;
	private int unknownInt12;
	private int unknownInt13;
	private int unknownInt14;
	private int unknownInt15;
	private int unknownInt16;
	private int unknownInt17;
	private int unknownInt18;
	private int unknownInt19;
	private int unknownInt20;
	private int unknownInt21;
	private int unknownInt22;
	private int unknownInt23;
	private int equipType;
	public HashMap<Integer, Object> clientScriptData;
	
	public static ItemDefinitions getItemDefinition(Store store, int itemId) {
		return getItemDefinition(store, itemId, true);
	}
	
	public static ItemDefinitions getItemDefinition(Store store, int itemId, boolean load) {
		return new ItemDefinitions(store, itemId, load);
	}

	public ItemDefinitions(Store cache, int id) {
		this(cache, id, true);
	}

	public ItemDefinitions(Store cache, int id, boolean load) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		if (load)
			loadItemDefinition(cache);
	}

	public boolean isLoaded() {
		return loaded;
	}
	
	public void write(Store store) {
		store.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].putFile(getArchiveId(), getFileId(), encode());
	}

	private void loadItemDefinition(Store cache) {
		byte[] data = cache.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			CacheEditor.addMessage("FAILED LOADING ITEM " + id);
			return;
		}
		readOpcodeValues(new InputStream(data));
		if(notedItemId != -1)
			toNote(cache);
		if(lendedItemId != -1)
			toLend(cache);
		loaded = true;
	}

	private void toNote(Store store) {
		//ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinition(store, switchNoteItemId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
	}
	
	private void toLend(Store store) {
		//ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinition(store, switchLendItemId);
		originalModelColors = realItem.originalModelColors;
		modifiedModelColors = realItem.modifiedModelColors;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquipModelId1 = realItem.maleEquipModelId1;
		maleEquipModelId2 = realItem.maleEquipModelId2;
		femaleEquipModelId1 = realItem.femaleEquipModelId1;
		femaleEquipModelId2 = realItem.femaleEquipModelId2;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
	}
	public int getArchiveId() {
		return id >>> 8;
	}
	
	public int getFileId() {
		return 0xff & id;
	}
	
	public boolean hasSpecialBar() {
		if(clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(686);
		if(specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}
	public int getRenderAnimId() {
		if(clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if(animId != null){
			System.out.println("1");
			if(animId instanceof Integer){
				System.out.println("2");
				return (Integer) animId;
			}
		}
		return 1426;
	}
	
	public int getQuestId() {
		if(clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if(questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}
	
	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		if(clientScriptData == null)
			return null;
		HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
		int nextLevel = -1;
		int nextSkill = -1;
		for(int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if(value instanceof String)
				continue;
			if(key == 23) {
				skills.put(4, (Integer) value);
				skills.put(11, 61);
			}else if (key >= 749 && key < 797) {
				if(key % 2 == 0)
					nextLevel = (Integer) value;
				else
					nextSkill = (Integer) value;
				if(nextLevel != -1 && nextSkill != -1) {
					skills.put(nextSkill, nextLevel);
					nextLevel = -1;
					nextSkill = -1;
				}
			}
				
		}
		return skills;
	}
	
	//test :P
	public void printClientScriptData() {
		for(int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			System.out.println("KEY: "+key+", VALUE: "+value);
		}
		HashMap<Integer, Integer> requiriments = getWearingSkillRequiriments();
		if(requiriments == null) {
			System.out.println("null.");
			return;
		}
		System.out.println(requiriments.keySet().size());
		for(int key : requiriments.keySet()) {
			Object value = requiriments.get(key);
			System.out.println("SKILL: "+key+", LEVEL: "+value);
		}
	}
	
	
	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}
	
	private void setDefaultsVariableValues() {
		name = "null";
		maleEquipModelId1 = -1;
		maleEquipModelId2 = -1;
		femaleEquipModelId1 = -1;
		femaleEquipModelId2 = -1;
		invModelZoom = 2000;
		switchLendItemId = -1;
		lendedItemId = -1;
		switchNoteItemId = -1;
		notedItemId = -1;
		unknownInt9 = 128;
		value = 1;
		maleEquipModelId3 = -1;
		femaleEquipModelId3 = -1;
		setEquipType(-1);
	}
	
	public byte[] encode() {
		OutputStream stream = new OutputStream();
		
		stream.writeByte(1);
		stream.writeShort(invModelId);
		
		if(!name.equals("null") && notedItemId == -1) {
			stream.writeByte(2);
			stream.writeString(name);
		}
		
		if(invModelZoom != 2000) {
			stream.writeByte(4);
			stream.writeShort(invModelZoom);
		}
		
		if(modelRotation1 != 0) {
			stream.writeByte(5);
			stream.writeShort(modelRotation1);
		}
		
		if(modelRotation2 != 0) {
			stream.writeByte(6);
			stream.writeShort(modelRotation2);
		}
		
		if(modelOffset1 != 0) {
			stream.writeByte(7);
			int value = modelOffset1 >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}
		
		if(modelOffset2 != 0) {
			stream.writeByte(8);
			int value = modelOffset2 >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}
		
		if(stackable >= 1  && notedItemId == -1) {
			stream.writeByte(11);
		}
		
		if(value != 1  && lendedItemId == -1) {
			stream.writeByte(12);
			stream.writeInt(value);
		}
		
		if(getEquipType() != 1) {
			stream.writeByte(14);
			stream.writeByte(getEquipType());
		}
		
		if(membersOnly && notedItemId == -1) {
			stream.writeByte(16);
		}
		
		if(maleEquipModelId1 != -1) {
			stream.writeByte(23);
			stream.writeShort(maleEquipModelId1);
		}
		
		if(maleEquipModelId2 != -1) {
			stream.writeByte(24);
			stream.writeShort(maleEquipModelId2);
		}
		
		if(femaleEquipModelId1 != -1) {
			stream.writeByte(25);
			stream.writeShort(femaleEquipModelId1);
		}
		
		if(femaleEquipModelId2 != -1) {
			stream.writeByte(26);
			stream.writeShort(femaleEquipModelId2);
		}
		
		for(int index = 0; index < groundOptions.length; index++) {
			if(groundOptions[index] == null || inventoryOptions[index].equals("null") || (index == 2 && groundOptions[index].equals("take")))
				continue;
			stream.writeByte(30+index);
			stream.writeString(groundOptions[index]);
		}
		
		for(int index = 0; index < inventoryOptions.length; index++) {
			if(inventoryOptions[index] == null || inventoryOptions[index].equals("null") || (index == 4 && inventoryOptions[index].equals("drop")))
				continue;
			stream.writeByte(35+index);
			stream.writeString(inventoryOptions[index]);
		}
		
		if(originalModelColors != null && modifiedModelColors != null) {
			stream.writeByte(40);
			stream.writeByte(originalModelColors.length);
			for(int index = 0; index < originalModelColors.length; index++) {
				stream.writeShort(originalModelColors[index]);
				stream.writeShort(modifiedModelColors[index]);
			}
		}
		
		if(originalTextureColors != null && modifiedTextureColors != null) {
			stream.writeByte(41);
			stream.writeByte(originalTextureColors.length);
			for(int index = 0; index < originalTextureColors.length; index++) {
				stream.writeShort(originalTextureColors[index]);
				stream.writeShort(modifiedTextureColors[index]);
			}
		}
		
		if(unknownArray1 != null) {
			stream.writeByte(42);
			stream.writeByte(unknownArray1.length);
			for(int index = 0; index < unknownArray1.length; index++)
				stream.writeByte(unknownArray1[index]);
		}
		if(unnoted) {
			stream.writeByte(65);
		}
		
		if(maleEquipModelId3 != -1) {
			stream.writeByte(78);
			stream.writeShort(maleEquipModelId3);
		}
		
		if(femaleEquipModelId3 != -1) {
			stream.writeByte(79);
			stream.writeShort(femaleEquipModelId3);
		}
		if (maleDialogueModel >= -1) {
			stream.writeByte(90);
			stream.writeShort(maleDialogueModel);
		}
		
		if (femaleDialogueModel >= -1) {
			stream.writeByte(91);
			stream.writeShort(femaleDialogueModel);
		}
		
		//TODO FEW OPCODES HERE
		
		if(switchNoteItemId != -1) {
			stream.writeByte(97);
			stream.writeShort(switchNoteItemId);
		}
		
		if(notedItemId != -1) {
			stream.writeByte(98);
			stream.writeShort(notedItemId);
		}
		
		if(stackIds != null && stackAmounts != null) {
			for(int index = 0; index < stackIds.length; index++) {
				if(stackIds[index] == 0 && stackAmounts[index] == 0)
					continue;
				stream.writeByte(100+index);
				stream.writeShort(stackIds[index]);
				stream.writeShort(stackAmounts[index]);
			}
		}
		
		//TODO FEW OPCODES HERE
		
		if(teamId != 0) {
			stream.writeByte(115);
			stream.writeByte(teamId);
		}
		
		if(switchLendItemId != -1) {
			stream.writeByte(121);
			stream.writeShort(switchLendItemId);
		}
		
		if(lendedItemId != -1) {
			stream.writeByte(122);
			stream.writeShort(lendedItemId);
		}
		
		//TODO FEW OPCODES HERE
		
		if(unknownArray2 != null) {
			stream.writeByte(132);
			stream.writeByte(unknownArray2.length);
			for(int index = 0; index < unknownArray2.length; index++)
				stream.writeShort(unknownArray2[index]);
		}
		
		if(clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(clientScriptData.size());
			for(int key : clientScriptData.keySet()) {
				Object value = clientScriptData.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInt(key);
				if(value instanceof String) {
					stream.writeString((String) value);
				}else{
					stream.writeInt((Integer) value);
				}
			}
		}
		//end
		stream.writeByte(0);
		
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}
	
	public int getInvModelId() {
		return invModelId;
	}
	
	public void setInvModelId(int modelId) {
		this.invModelId = modelId;
	}
	
	public int getInvModelZoom() {
		return invModelZoom;
	}
	
	public void setInvModelZoom(int modelZoom) {
		this.invModelZoom = modelZoom;
	}
	
	private void readValues(InputStream stream, int opcode) {
		if(opcode == 1)
			invModelId = stream.readUnsignedShort();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			invModelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotation1 = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotation2 = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767)
				modelOffset1 -= 65536;
			modelOffset1 <<= 0;
		}else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767)
				modelOffset2 -= 65536;
			modelOffset2 <<= 0;
		}else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = stream.readInt();
		else if (opcode == 14)
			setEquipType(stream.readUnsignedByte());
		else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 23)
			maleEquipModelId1 = stream.readUnsignedShort();
		else if (opcode == 24)
			maleEquipModelId2 = stream.readUnsignedShort();
		else if (opcode == 25)
			femaleEquipModelId1 = stream.readUnsignedShort();
		else if (opcode == 26)
			femaleEquipModelId2 = stream.readUnsignedShort();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode-30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode-35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new short[length];
			modifiedModelColors = new short[length];
			for(int index = 0; index < length; index++) {
				originalModelColors[index] = (short) stream.readUnsignedShort();
				modifiedModelColors[index] = (short) stream.readUnsignedShort();
			}
		}else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureColors = new short[length];
			modifiedTextureColors = new short[length];
			for(int index = 0; index < length; index++) {
				originalTextureColors[index] = (short) stream.readUnsignedShort();
				modifiedTextureColors[index] = (short) stream.readUnsignedShort();
			}
		}else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			unknownArray1 = new byte[length];
			for(int index = 0; index < length; index++)
				unknownArray1[index] = (byte) stream.readByte();
		}else if (opcode == 65)
			unnoted = true;
		else if (opcode == 78)
			maleEquipModelId3 = stream.readUnsignedShort();
		else if (opcode == 79)
			femaleEquipModelId3 = stream.readUnsignedShort();
		else if (opcode == 90)
			maleDialogueModel = stream.readUnsignedShort();
		else if (opcode == 91)
			femaleDialogueModel = stream.readUnsignedShort();
		else if (opcode == 92)
			unknownInt3 = stream.readUnsignedShort();
		else if (opcode == 93)
			unknownInt4 = stream.readUnsignedShort();
		else if (opcode == 95)
			unknownInt5 = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			switchNoteItemId = stream.readUnsignedShort();
		else if (opcode == 98)
			notedItemId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode-100] = stream.readUnsignedShort();
			stackAmounts[opcode-100] = stream.readUnsignedShort();
		}else if (opcode == 110)
			unknownInt7 = stream.readUnsignedShort();
		else if (opcode == 111)
			unknownInt8 = stream.readUnsignedShort();
		else if (opcode == 112)
			unknownInt9 = stream.readUnsignedShort();
		else if (opcode == 113)
			unknownInt10 = stream.readByte();
		else if (opcode == 114)
			unknownInt11 = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			switchLendItemId = stream.readUnsignedShort();
		else if (opcode == 122)
			lendedItemId = stream.readUnsignedShort();
		else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 0;
			unknownInt13 = stream.readByte() << 0;
			unknownInt14 = stream.readByte() << 0;
		}else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 0;
			unknownInt16 = stream.readByte() << 0;
			unknownInt17 = stream.readByte() << 0;
		}else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		}else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		}else if (opcode == 129) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		}else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		}else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for(int index = 0; index < length; index++)
				unknownArray2[index] = stream.readUnsignedShort();
		}else if (opcode == 139) {
			int unknownValue = stream.readUnsignedShort();
		}else if (opcode == 140) {
			int unknownValue = stream.readUnsignedShort();
		}else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if(clientScriptData == null)
				clientScriptData = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				clientScriptData.put(key, value);
			}
		}
		else
			throw new RuntimeException("MISSING OPCODE "+opcode+" FOR ITEM "+id);
	}

	private void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void resetTextureColors() {
		originalTextureColors = null;
		modifiedTextureColors = null;
	}
	
	public void changeTextureColor(int originalModelColor, int modifiedModelColor) {
		if(originalTextureColors != null) {
			for(int i = 0; i < originalTextureColors.length; i++) {
				if(originalTextureColors[i] == originalModelColor) {
					modifiedTextureColors[i] = (short) modifiedModelColor;
					return;
				}
			}
			short[] newOriginalModelColors = Arrays.copyOf(originalTextureColors, originalTextureColors.length+1);
			short[] newModifiedModelColors = Arrays.copyOf(modifiedTextureColors, modifiedTextureColors.length+1);
			newOriginalModelColors[newOriginalModelColors.length-1] = (short) originalModelColor;
			newModifiedModelColors[newModifiedModelColors.length-1] = (short) modifiedModelColor;
			originalTextureColors = newOriginalModelColors;
			modifiedTextureColors = newModifiedModelColors;
		}else{
			originalTextureColors = new short[] {(short) originalModelColor};
			modifiedTextureColors = new short[] {(short) modifiedModelColor};
		}
	}
	
	public void resetModelColors() {
		originalModelColors = null;
		modifiedModelColors = null;
	}
	
	public void changeModelColor(int originalModelColor, int modifiedModelColor) {
		if(originalModelColors != null) {
			for(int i = 0; i < originalModelColors.length; i++) {
				if(originalModelColors[i] == originalModelColor) {
					modifiedModelColors[i] = (short) modifiedModelColor;
					return;
				}
			}
			short[] newOriginalModelColors = Arrays.copyOf(originalModelColors, originalModelColors.length+1);
			short[] newModifiedModelColors = Arrays.copyOf(modifiedModelColors, modifiedModelColors.length+1);
			newOriginalModelColors[newOriginalModelColors.length-1] = (short) originalModelColor;
			newModifiedModelColors[newModifiedModelColors.length-1] = (short) modifiedModelColor;
			originalModelColors = newOriginalModelColors;
			modifiedModelColors = newModifiedModelColors;
		}else{
			originalModelColors = new short[] {(short) originalModelColor};
			modifiedModelColors = new short[] {(short) modifiedModelColor};
		}
	}
	
	public String[] getGroundOptions() {
		return groundOptions;
	}

	public String[] getInventoryOptions() {
		return inventoryOptions;
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

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public void setMembersOnly(boolean b) {
		membersOnly = b;
	}

	public boolean isMembersOnly() {
		return membersOnly;
	}
	
	public String getUnknownStuff1() {
		return "3: " + unknownInt3 + ", 4: " + unknownInt4 + ", 5: " + unknownInt5 + ", 6: " + unknownInt6;
	}
	
	public String getUnknownStuff2() {
		return "1: " + unknownInt7 + ", 2: " + unknownInt8 + ", 3: " + unknownInt9 + ", 4: " + unknownInt10 + ", 5: " + unknownInt11 + ", 6: " + unknownInt12;
	}
	
	public String getUnknownStuff3() {
		return "1: " + unknownInt13 + ", 2: " + unknownInt14 + ", 3: " + unknownInt15 + ", 4: " + unknownInt16 + ", 5: " + unknownInt17 + ", 6: " + unknownInt18;
	}
	
	public String getUnknownStuff4() {
		return "1: " + unknownInt19 + ", 2: " + unknownInt20 + ", 3: " + unknownInt21 + ", 4: " + unknownInt22 + ", 5: " + unknownInt23;
	}
}
