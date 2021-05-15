package editor.loaders;

import java.util.ArrayList;

import editor.application.CacheEditor;
import editor.utils.Constants;

public class Interface {
	public ArrayList<IOComponent> components = new ArrayList<IOComponent>();
	public int id;
	
	public Interface(int id) {
		this.id = id;
		int size = CacheEditor.STORE.getIndexes()[Constants.INTERFACE_DEFINITIONS_INDEX].getLastFileId(id);
		try {
			for (int index = 0; index < size; index++)
				components.add(new IOComponent(CacheEditor.STORE, id, index, true));
		} catch(Exception e) {
			//e.printStackTrace();
		}
	}
	
	public IOComponent getComponent(int id) {
		if (id > -1 && id < components.size())
			return components.get(id);
		return null;
	}
	
	public int getCompSize() {
		return components.size();
	}
	
	@Override
	public String toString() {
		return id + "";
	}
}
