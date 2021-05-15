package editor.application;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.loaders.ObjectDefinitions;
import editor.store.Store;
import editor.utils.Constants;
import editor.utils.ListItem;
import editor.utils.Utils;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Object_Panel extends JPanel {
	private JTextField txtObjectID = new JTextField();
	private DefaultListModel<ObjectDefinitions> listModelObject = new DefaultListModel<ObjectDefinitions>();
	private JList<ObjectDefinitions> listObject = new JList<ObjectDefinitions>(listModelObject);
	private DefaultListModel<String> listModelInfo = new DefaultListModel<String>();
	private JList<String> listInfo = new JList<String>(listModelInfo);
	private JButton btnApply = new JButton("Apply");
	private JLabel lblClone = new JLabel("Clone Object:");
	private ArrayList<ObjectDefinitions> needsUpdate = new ArrayList<ObjectDefinitions>();
	private ObjectDefinitions cloneObject = null;
	private boolean stopListeners = false;

	/**
	 * Create the panel.
	 */
	public Object_Panel() {
		setPreferredSize(new Dimension(476, 540));
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 11, 460, 365);
		add(panel);
		
		JScrollPane scrollPaneObject = new JScrollPane();
		scrollPaneObject.setBounds(10, 23, 195, 331);
		panel.add(scrollPaneObject);
		
		scrollPaneObject.setViewportView(listObject);
		listObject.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (stopListeners)
					return;
				int index = listInfo.getSelectedIndex();
				updateData(listObject, listModelObject, listInfo, listModelInfo, txtObjectID, CacheEditor.STORE);
				listInfo.setSelectedIndex(index);
			}
		});
		
		JScrollPane scrollPaneInfo = new JScrollPane();
		scrollPaneInfo.setBounds(215, 90, 235, 264);
		panel.add(scrollPaneInfo);
		
		scrollPaneInfo.setViewportView(listInfo);
		
		txtObjectID.setText("0");
		txtObjectID.setColumns(10);
		txtObjectID.setBounds(60, 2, 86, 20);
		txtObjectID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					int object = 0;
					try {
						object = Integer.parseInt(txtObjectID.getText());
					} catch (Exception error) {
						object = 0;
					}
					listObject.setSelectedIndex(object);
					listObject.ensureIndexIsVisible(object);
				}
			}
		});
		panel.add(txtObjectID);
		
		JLabel lblObjects = new JLabel("Objects");
		lblObjects.setBounds(10, 5, 195, 14);
		panel.add(lblObjects);
		
		JButton btnClone = new JButton("Clone");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cloneObject = new ObjectDefinitions(CacheEditor.STORE, listObject.getSelectedIndex(), true);
				lblClone.setText("Clone Object: " + cloneObject.id + " - " + cloneObject.getName() + ".");
			}
		});
		btnClone.setBounds(210, 12, 70, 23);
		panel.add(btnClone);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String element = listModelInfo.getElementAt(listInfo.getSelectedIndex()).toString();
				String title = element.substring(0, element.indexOf(':'));
				String data = element.substring(element.indexOf(':') + 2);
				String input = (String) JOptionPane.showInputDialog(CacheEditor.frmCacheEditor, "Enter new value:",
						"Editing " + title, JOptionPane.PLAIN_MESSAGE, null, null, data);
				if (input != null)
					changeValue(listObject.getSelectedIndex(), title, input);
			}
		});
		btnEdit.setBounds(377, 58, 73, 23);
		panel.add(btnEdit);
		
		JButton btnScripts = new JButton("Scripts");
		btnScripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(listModelObject.getElementAt(listObject.getSelectedIndex()).getName());

				if (listModelObject.getElementAt(listObject.getSelectedIndex()).clientScriptData != null) {
					for (int key : listModelObject.getElementAt(listObject.getSelectedIndex()).clientScriptData.keySet()) {
						Object value = listModelObject.getElementAt(listObject.getSelectedIndex()).clientScriptData.get(key);
						System.out.println("KEY: " + key + ", VALUE: " + value);
					}
				}
			}
		});
		btnScripts.setBounds(281, 12, 95, 23);
		panel.add(btnScripts);

		btnApply.setEnabled(false);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < needsUpdate.size(); i++) {
					needsUpdate.get(i).write(CacheEditor.STORE);
				}
				needsUpdate.clear();
				btnApply.setEnabled(false);
			}
		});
		btnApply.setBounds(281, 58, 95, 23);
		panel.add(btnApply);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObjectDefinitions def = new ObjectDefinitions(CacheEditor.STORE, listObject.getSelectedIndex(), false);
				int view = listObject.getSelectedIndex() - 1;
				listObject.setSelectedIndex(view > -1 ? view : 0);
				listObject.ensureIndexIsVisible(view > -1 ? view : 0);
				listModelObject.remove(listObject.getSelectedIndex() + 1);
				CacheEditor.STORE.getIndexes()[Constants.OBJECT_DEFINITIONS_INDEX].removeFile(def.getArchiveId(), def.getFileId());
			}
		});
		btnDelete.setBounds(210, 58, 70, 23);
		panel.add(btnDelete);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObjectDefinitions object = new ObjectDefinitions(CacheEditor.STORE, Utils.getObjectDefinitionsSize(CacheEditor.STORE), false);
				object.write(CacheEditor.STORE);
				listModelObject.addElement(object);
				listObject.setSelectedIndex(listObject.getModel().getSize() - 1);
				listObject.ensureIndexIsVisible(listObject.getModel().getSize() - 1);
			}
		});
		btnAdd.setBounds(210, 35, 70, 23);
		panel.add(btnAdd);
		
		JButton btnAddClone = new JButton("Add Clone");
		btnAddClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObjectDefinitions object = cloneObject;
				if (object != null) {
					object.id = listObject.getModel().getSize();
					object.write(CacheEditor.STORE);
					listModelObject.addElement(object);
					listObject.setSelectedIndex(listObject.getModel().getSize() - 1);
					listObject.ensureIndexIsVisible(listObject.getModel().getSize() - 1);
				}
			}
		});
		btnAddClone.setBounds(281, 35, 95, 23);
		panel.add(btnAddClone);
		
		lblClone.setBounds(10, 384, 444, 14);
		add(lblClone);

	}
	
	private void changeValue(int objectID, String option, String input) {

		if (needsUpdate.contains(listModelObject.getElementAt(listObject.getSelectedIndex())))
			needsUpdate.remove(listModelObject.getElementAt(listObject.getSelectedIndex()));
		int value = -1234567890;
		try {
			value = Integer.parseInt(input);
			if (value == -1234567890)
				return;
			switch (option) {
			case "Size Y":
				listModelObject.getElementAt(listObject.getSelectedIndex()).sizeY = value;
				break;
			case "Size X":
				listModelObject.getElementAt(listObject.getSelectedIndex()).sizeX = value;
				break;
			}
		} catch (Exception e) {
			switch (option) {
			case "Name":
				listModelObject.getElementAt(listObject.getSelectedIndex()).setName(input);
				break;
			case "Options":
				String list[] = input.split(";");
				int listInt[];
				for (int i = 0; i < listModelObject.getElementAt(listObject.getSelectedIndex()).options.length; i++)
					if (i < list.length)
						listModelObject.getElementAt(listObject.getSelectedIndex()).options[i] = list[i];
				break;
			case "Models":
				list = input.split(";");
				listInt = new int[list.length];
				for (int i = 0; i < list.length; i++)
						listInt[i] = Integer.parseInt(list[i]);
				listModelObject.getElementAt(listObject.getSelectedIndex()).models = listInt;
				break;
			case "Model Colors":
				list = input.split(";");
				if (list != null) {
					short list2[] = new short[list.length];
					short list3[] = new short[list.length];
					for (int i = 0; i < list.length; i++) {
						list2[i] = Short.parseShort(list[i].split("=")[0]);
						list3[i] = Short.parseShort(list[i].split("=")[1]);
					}
					listModelObject.getElementAt(listObject.getSelectedIndex()).originalModelColors = list2;
					listModelObject.getElementAt(listObject.getSelectedIndex()).modifiedModelColors = list3;
				}
				break;
			case "Model Textures":
				list = input.split(";");
				if (list != null) {
					short list2[] = new short[list.length];
					short list3[] = new short[list.length];
					for (int i = 0; i < list.length; i++) {
						list2[i] = Short.parseShort(list[i].split("=")[0]);
						list3[i] = Short.parseShort(list[i].split("=")[1]);
					}
					listModelObject.getElementAt(listObject.getSelectedIndex()).originalTextureColors = list2;
					listModelObject.getElementAt(listObject.getSelectedIndex()).modifiedTextureColors = list3;
				}
				break;
			}
		}
		needsUpdate.add(listModelObject.getElementAt(listObject.getSelectedIndex()));
		btnApply.setEnabled(true);
		int index = listInfo.getSelectedIndex();
		if (listObject.getSelectedIndex() == listObject.getModel().getSize() - 1) {
			listObject.setSelectedIndex(listObject.getSelectedIndex() - 1);
			listObject.setSelectedIndex(listObject.getSelectedIndex() + 1);
		} else {
			listObject.setSelectedIndex(listObject.getSelectedIndex() + 1);
			listObject.setSelectedIndex(listObject.getSelectedIndex() - 1);
		}
		listInfo.setSelectedIndex(index);
	}
	
	private ArrayList<ListItem> getObjectList(ObjectDefinitions object) {
		ArrayList<ListItem> objectInfoList = new ArrayList<ListItem>();
		String options = "";
		objectInfoList.add(new ListItem("Name", object.getName()));
		for (int i = 0; i < object.options.length; i++)
			options += object.options[i] + ";";
		objectInfoList.add(new ListItem("Options", options));
		objectInfoList.add(new ListItem("Size Y", object.sizeY + ""));
		objectInfoList.add(new ListItem("Size X", object.sizeX + ""));
		objectInfoList.add(new ListItem("Config Id", object.configId + ""));
		options = "";
		if (object.originalModelColors != null)
			for (int i = 0; i < object.originalModelColors.length; i++)
				options += object.originalModelColors[i] + "=" + object.modifiedModelColors[i] + ";";
		objectInfoList.add(new ListItem("Model Colors", options));
		options = "";
		if (object.originalTextureColors != null)
			for (int i = 0; i < object.originalTextureColors.length; i++)
				options += object.originalTextureColors[i] + "=" + object.modifiedTextureColors[i] + ";";
		objectInfoList.add(new ListItem("Model Textures", options));
		objectInfoList.add(new ListItem("Clipping Type", object.clipType + ""));
		objectInfoList.add(new ListItem("Projectile Clipped", object.projectileCliped + ""));
		objectInfoList.add(new ListItem("Clipped", !object.notCliped + ""));
		options = "";
		if (object.models != null)
			for (int i = 0; i < object.models.length; i++)
				options += object.models[i] + ";";
		objectInfoList.add(new ListItem("Models", options));
		options = "";
		if (object.childrenIds != null)
			for (int i = 0; i < object.childrenIds.length; i++)
				options += object.childrenIds[i] + ";";
		objectInfoList.add(new ListItem("Child Id", options));
		return objectInfoList;
	}
	
	private void updateData(JList<ObjectDefinitions> list, DefaultListModel<ObjectDefinitions> listModel, JList<String> listObject,
			DefaultListModel<String> listObjectModel, JTextField txtObject, Store store) {
		txtObject.setText("" + list.getSelectedIndex());
		listObjectModel.removeAllElements();
		ArrayList<ListItem> objectList = getObjectList(listModel.getElementAt(list.getSelectedIndex()));
		for (int i = 0; i < objectList.size(); i++)
			listObjectModel.addElement(objectList.get(i).getTitle() + ": " + objectList.get(i).getData());
		if (listObject.getSelectedIndex() == -1)
			listObject.setSelectedIndex(0);
	}
	
	public void loadDefs() {
		stopListeners = true;
		listModelObject.removeAllElements();
		double progress = 0;
		int size = Utils.getObjectDefinitionsSize(CacheEditor.STORE);
		for (int i = 0; i < size; i++) {
			listModelObject.addElement(new ObjectDefinitions(CacheEditor.STORE, i, true));
			progress = 100.0 / ((double) size) * ((double) i);
			CacheEditor.setProgress(((int) progress) + 300);
		}
		listObject.setSelectedIndex(0);
		stopListeners = false;
	}

}
