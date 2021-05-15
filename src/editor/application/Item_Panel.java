package editor.application;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.loaders.ItemDefinitions;
import editor.store.Store;
import editor.utils.Constants;
import editor.utils.ListItem;
import editor.utils.Utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class Item_Panel extends JPanel {

	/**
	 * Create the panel.
	 */
	public Item_Panel() {
		setPreferredSize(new Dimension(476, 540));
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 460, 365);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPaneItem = new JScrollPane();
		scrollPaneItem.setBounds(10, 23, 195, 331);
		panel.add(scrollPaneItem);

		scrollPaneItem.setViewportView(listItem);
		listItem.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting() || stopListeners)
					return;
				int index = listItemInfo.getSelectedIndex();
				if (index < 0)
					index = 0;
				if (listModelD.size() > 0) {
					updateData(listItem, listModelD, listItemInfo, listModelItems, txtItemID, CacheEditor.STORE);
					listItemInfo.setSelectedIndex(index);
				}
			}
		});

		txtItemID = new JTextField();
		txtItemID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					int id = 0;
					try {
						id = Integer.parseInt(txtItemID.getText());
					} catch (Exception error) {
						id = 0;
					}
					if (id < 0)
						id = 0;
					if (listModelD.size() < itemList.size())
						populateList();
					if (id >= listItem.getModel().getSize())
						id = listItem.getModel().getSize() - 1;
					int index = listItemInfo.getSelectedIndex();
					listItem.setSelectedIndex(id);
					listItem.ensureIndexIsVisible(id);
					listItemInfo.setSelectedIndex(index);
				}
			}
		});

		JScrollPane scrollPaneInfo = new JScrollPane();
		scrollPaneInfo.setBounds(215, 90, 235, 264);
		panel.add(scrollPaneInfo);

		scrollPaneInfo.setViewportView(listItemInfo);

		JLabel lblItems = new JLabel("ID:");
		lblItems.setBounds(10, 5, 20, 14);
		panel.add(lblItems);

		txtItemID.setText("0");
		txtItemID.setBounds(30, 2, 56, 20);
		panel.add(txtItemID);
		txtItemID.setColumns(10);
		
		JLabel lblItemName = new JLabel("Name:");
		lblItemName.setBounds(96, 5, 38, 14);
		panel.add(lblItemName);
		
		txtItemName = new JTextField();
		txtItemName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					String name = txtItemName.getText().toLowerCase();
					
					stopListeners = true;
					listModelD.removeAllElements();
					Thread thread = new Thread() {
						
						public void run() {
							int foundItems = 0;
							for (ItemDefinitions item : itemList) {
								if (item.getName().toLowerCase().contains(name)) {
									listModelD.addElement(item.toString());
									foundItems++;
								}
							}
							stopListeners = false;
							if (foundItems > 0) {
								listItem.setSelectedIndex(0);
						updateData(listItem, listModelD, listItemInfo, listModelItems, txtItemID, CacheEditor.STORE);
							}
						}
					};
					thread.start();
					/*List<Integer> wordsForGivenAlphabet = itemList.entrySet().stream()
							.filter(x-> x.getKey().equalsIgnoreCase(name))
						    .map(Map.Entry::getValue)
						    .flatMap(List::stream)
						    .collect(Collectors.toList());*/
					//int id = keyStream.findFirst().get();
					//System.out.println(id);
					/*int item = 0;
					try {
						item = Integer.parseInt(txtItemID.getText());
					} catch (Exception error) {
						item = 0;
					}
					int index = listItemInfo.getSelectedIndex();
					listItem.setSelectedIndex(item);
					listItem.ensureIndexIsVisible(item);
					listItemInfo.setSelectedIndex(index);*/
				}
			}
		});
		txtItemName.setColumns(10);
		txtItemName.setBounds(130, 2, 70, 20);
		panel.add(txtItemName);

		JButton btnClone = new JButton("Clone");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cloneItem = new ItemDefinitions(CacheEditor.STORE, listItem.getSelectedIndex(), true);
				lblClone.setText("Clone Item: " + cloneItem.id + " - " + cloneItem.getName() + ".");
			}
		});
		btnClone.setBounds(210, 12, 70, 23);
		panel.add(btnClone);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String element = listModelItems.getElementAt(listItemInfo.getSelectedIndex()).toString();
				String title = element.substring(0, element.indexOf(':'));
				String data = element.substring(element.indexOf(':') + 2);
				String input = (String) JOptionPane.showInputDialog(CacheEditor.frmCacheEditor, "Enter new value:",
						"Editing " + title, JOptionPane.PLAIN_MESSAGE, null, null, data);
				if (input != null)
					changeValue(listItem.getSelectedIndex(), title, input);
			}
		});
		btnEdit.setBounds(377, 58, 73, 23);
		panel.add(btnEdit);

		JButton btnScripts = new JButton("Scripts");
		btnScripts.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				String element = listModelD.getElementAt(listItem.getSelectedIndex());
				int id = Integer.parseInt(element.replaceAll(" ", "").split("-")[0]);
				ItemDefinitions item = itemList.get(id);
				System.out.println(item.getName());

				if (item.clientScriptData != null) {
					for (int key : item.clientScriptData.keySet()) {
						item.printClientScriptData();
						//Object value = listModelD.getElementAt(listItem.getSelectedIndex()).clientScriptData.get(key);
						//System.out.println("KEY: " + key + ", VALUE: " + value);
					}
				}

			}
		});
		btnScripts.setBounds(281, 12, 95, 23);
		panel.add(btnScripts);

		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < needsUpdate.size(); i++) {
					needsUpdate.get(i).write(CacheEditor.STORE);
				}
				needsUpdate.clear();
				btnApply.setEnabled(false);
			}
		});
		btnApply.setEnabled(false);
		btnApply.setBounds(281, 58, 95, 23);
		panel.add(btnApply);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemDefinitions def = new ItemDefinitions(CacheEditor.STORE, listItem.getSelectedIndex(), false);
				int view = listItem.getSelectedIndex() - 1;
				listItem.setSelectedIndex(view > -1 ? view : 0);
				listItem.ensureIndexIsVisible(view > -1 ? view : 0);
				//if (listItems.getSelectedIndex() + 1 == listItems.getModel().getSize() - 1) {
					listModelD.remove(listItem.getSelectedIndex() + 1);
					CacheEditor.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].removeFile(def.getArchiveId(), def.getFileId());
				/*} else {
					ItemDefinitions nothing = new ItemDefinitions(Application.store, listItems.getSelectedIndex() + 1, false);
					listModelD.set(listItems.getSelectedIndex() + 1, nothing);
					nothing.write(Application.store);
				}*/

			}
		});
		btnDelete.setBounds(210, 58, 70, 23);
		panel.add(btnDelete);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemDefinitions d = new ItemDefinitions(CacheEditor.STORE, Utils.getItemDefinitionsSize(CacheEditor.STORE), false);
				d.write(CacheEditor.STORE);
				listModelD.addElement(d.toString());
				listItem.setSelectedIndex(listItem.getModel().getSize() - 1);
				listItem.ensureIndexIsVisible(listItem.getModel().getSize() - 1);
			}
		});
		btnAdd.setBounds(210, 35, 70, 23);
		panel.add(btnAdd);

		JButton btnAddClone = new JButton("Add Clone");
		btnAddClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemDefinitions d = cloneItem;
				if (d != null) {
					d.id = listItem.getModel().getSize();
					d.write(CacheEditor.STORE);
					listModelD.addElement(d.toString());
					listItem.setSelectedIndex(listItem.getModel().getSize() - 1);
					listItem.ensureIndexIsVisible(listItem.getModel().getSize() - 1);
				}
			}
		});
		btnAddClone.setBounds(281, 35, 95, 23);
		panel.add(btnAddClone);

		lblClone.setBounds(10, 449, 444, 14);
		add(lblClone);
		lblColor.setOpaque(true);
		lblColor.setEditable(false);
		lblColor.setHorizontalAlignment(SwingConstants.CENTER);
		lblColor.setBounds(10, 388, 460, 23);
		add(lblColor);
		
		JButton btnPalette = new JButton("Color Palette");
		btnPalette.setBounds(128, 421, 99, 23);
		add(btnPalette);
		
		JButton buttonRS2 = new JButton("RS2 to Hex");
		buttonRS2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog("Enter RS2 color:");
				int number = 70000;
				try {
				    number = Integer.parseInt(input);
				    if (number < Short.MIN_VALUE || number > Short.MAX_VALUE)
				    	number = 70000;
				} catch (NumberFormatException ex) {
					number = 70000;
				}
				if (number != 70000) {
                    Color color = new Color(Utils.RS2HSB_to_RGB(Integer.parseInt(input)));
					if (color != null) {
						String rs2Color = String.valueOf(Utils.RGB_to_RS2HSB(color.getRed(), color.getGreen(), color.getBlue()));
						String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
						String displayText = "RGB: (r:" + color.getRed() + " g: " + color.getGreen() + " b: "+ color.getBlue() + ") Hex: " + hex + " RS2: " + rs2Color;
						lblColor.setText(displayText);
						lblColor.setBackground(color);
						lblColor.setForeground(Utils.getContrastColor(color));
						//Utils.copyToClipboard(rs2Color);
					}
                }
			}
		});
		buttonRS2.setBounds(238, 422, 90, 23);
		add(buttonRS2);
		btnPalette.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
		        if (color != null) {
					String rs2Color = String.valueOf(Utils.RGB_to_RS2HSB(color.getRed(), color.getGreen(), color.getBlue()));
					String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
					String displayText = "RGB: (r:" + color.getRed() + " g: " + color.getGreen() + " b: " + color.getBlue() + ") Hex: " + hex + " RS2: " + rs2Color;
					lblColor.setText(displayText);
					lblColor.setBackground(color);
					lblColor.setForeground(Utils.getContrastColor(color));
					//Utils.copyToClipboard(rs2Color);
		        }
			}
		});

	}

	private void updateData(JList<String> list, DefaultListModel<String> listModel, JList<String> listItemsata,
			DefaultListModel<String> listItemsModel, JTextField txtItem, Store store) {
		listItemsModel.removeAllElements();
		int index = list.getSelectedIndex();
		if (index >= 0) {
			ArrayList<ListItem> itemList = getItemList(listModel.getElementAt(index));
			for (int i = 0; i < itemList.size(); i++)
				listItemsModel.addElement(itemList.get(i).getTitle() + ": " + itemList.get(i).getData());
			if (listItemsata.getSelectedIndex() == -1)
				listItemsata.setSelectedIndex(0);
		}
	}

	private void changeValue(int itemID, String option, String input) {
		String element = listModelD.getElementAt(listItem.getSelectedIndex());
		int id = Integer.parseInt(element.replaceAll(" ", "").split("-")[0]);
		ItemDefinitions item = itemList.get(id);
		
		if (needsUpdate.contains(item))
			needsUpdate.remove(item);
		int value = -1234567890;
		try {
			value = Integer.parseInt(input);
			if (value == -1234567890)
				return;
			switch (option) {
			case "Model ID":
				item.setInvModelId(value);
				break;
			case "Price":
				item.value = value;
				break;
			case "Equip Type":
				item.setEquipType(value);
				break;
			case "Male Model 1":
				item.maleEquipModelId1 = value;
				break;
			case "Male Model 2":
				item.maleEquipModelId2 = value;
				break;
			case "Male Model 3":
				item.maleEquipModelId3 = value;
				break;
			case "Male Dialogue Model":
				item.maleDialogueModel = value;
				break;
			case "Female Model 1":
				item.femaleEquipModelId1 = value;
				break;
			case "Female Model 2":
				item.femaleEquipModelId2 = value;
				break;
			case "Female Model 3":
				item.femaleEquipModelId3 = value;
				break;
			case "Female Dialogue Model":
				item.femaleDialogueModel = value;
				break;
			case "Team ID":
				item.teamId = value;
				break;
			case "Note ID":
				item.notedItemId = value;
				break;
			case "Noted Item ID":
				item.switchNoteItemId = value;
				break;
			case "Lended ID":
				item.lendedItemId = value;
				break;
			case "Lended Item ID":
				item.switchLendItemId = value;
				break;
			case "Stackable":
				item.stackable = value > 0 ? 1 : 0;
				break;
			case "Model Zoom":
				item.invModelZoom = value;
				break;
			case "Model Rotation 1":
				item.modelRotation1 = value;
				break;
			case "Model Rotation 2":
				item.modelRotation2 = value;
				break;
			case "Model Offset 1":
				item.modelOffset1 = value;
				break;
			case "Model Offset 2":
				item.modelOffset2 = value;
				break;

			}
		} catch (Exception e) {
			String list[];
			if (input.equals(""))
				list = null;
			else
				list = input.split(";");
			switch (option) {
			case "Name":
				item.setName(input);
				break;
			case "Ground Options":
				for (int i = 0; i < item.getGroundOptions().length; i++)
					if (list != null && i < list.length)
						item.getGroundOptions()[i] = list[i];
				break;
			case "Inventory Options":
				for (int i = 0; i < item.inventoryOptions.length; i++)
					if (list != null && i < list.length)
						item.inventoryOptions[i] = list[i];
				break;
			case "Model Colors":
				if (list != null) {
					short list2[] = new short[list.length];
					short list3[] = new short[list.length];
					for (int i = 0; i < list.length; i++) {
						list2[i] = Short.parseShort(list[i].split("=")[0]);
						list3[i] = Short.parseShort(list[i].split("=")[1]);
					}
					item.originalModelColors = list2;
					item.modifiedModelColors = list3;
				} else {
					item.originalModelColors = null;
					item.modifiedModelColors = null;
				}
				break;
			case "Model Textures":
				if (list != null) {
					short list2[] = new short[list.length];
					short list3[] = new short[list.length];
					for (int i = 0; i < list.length; i++) {
						list2[i] = Short.parseShort(list[i].split("=")[0]);
						list3[i] = Short.parseShort(list[i].split("=")[1]);
					}
					item.originalTextureColors = list2;
					item.modifiedTextureColors = list3;
				} else {
					item.originalTextureColors = null;
					item.modifiedTextureColors = null;
				}
				break;
			case "Member":
				if (input.equalsIgnoreCase("true"))
					item.setMembersOnly(true);
				else
					item.setMembersOnly(false);
				break;
			case "Stack ID=Amount":
				if (list != null) {
					int list2[] = new int[list.length];
					int list3[] = new int[list.length];
					for (int i = 0; i < list.length; i++) {
						list2[i] = Integer.parseInt(list[i].split("=")[0]);
						list3[i] = Integer.parseInt(list[i].split("=")[1]);
					}
					item.stackIds = list2;
					item.stackAmounts = list3;
				} else {
					item.stackIds = null;
					item.stackAmounts = null;
				}
				break;
			case "UnknownA":
				CacheEditor.setStatus("Unknown options not editable.");
				break;
			case "Unknown1":
				CacheEditor.setStatus("Unknown options not editable.");
				break;
			case "Unknown2":
				CacheEditor.setStatus("Unknown options not editable.");
				break;
			case "Unknown3":
				CacheEditor.setStatus("Unknown options not editable.");
				break;
			case "Unknown4":
				CacheEditor.setStatus("Unknown options not editable.");
				break;
			}
		}
		needsUpdate.add(item);
		btnApply.setEnabled(true);
		int index = listItemInfo.getSelectedIndex();
		if (listItem.getSelectedIndex() == listItem.getModel().getSize() - 1) {
			listItem.setSelectedIndex(listItem.getSelectedIndex() - 1);
			listItem.setSelectedIndex(listItem.getSelectedIndex() + 1);
		} else {
			listItem.setSelectedIndex(listItem.getSelectedIndex() + 1);
			listItem.setSelectedIndex(listItem.getSelectedIndex() - 1);
		}
		listItemInfo.setSelectedIndex(index);

	}

	private ArrayList<ListItem> getItemList(String element) {
		int id = Integer.parseInt(element.replaceAll(" ", "").split("-")[0]);
		ItemDefinitions item = itemList.get(id);
		ArrayList<ListItem> itemInfoList = new ArrayList<ListItem>();
		itemInfoList.add(new ListItem("Name", item.getName()));
		itemInfoList.add(new ListItem("Model ID", item.getInvModelId() + ""));
		itemInfoList.add(new ListItem("Price", item.value + ""));
		itemInfoList.add(new ListItem("Equip Type", item.getEquipType() + ""));
		String groundOptions = "";
		for (int i = 0; i < item.getGroundOptions().length; i++)
			groundOptions += item.getGroundOptions()[i] + ";";
		itemInfoList.add(new ListItem("Ground Options", groundOptions));
		String inventoryOptions = "";
		for (int i = 0; i < item.inventoryOptions.length; i++)
			inventoryOptions += item.inventoryOptions[i] + ";";
		itemInfoList.add(new ListItem("Inventory Options", inventoryOptions));
		String colorOptions = "";
		if (item.originalModelColors != null) {
			for (int i = 0; i < item.originalModelColors.length; i++)
				colorOptions += item.originalModelColors[i] + "=" + item.modifiedModelColors[i] + ";";
		}
		itemInfoList.add(new ListItem("Model Colors", colorOptions));
		String textureOptions = "";
		if (item.originalTextureColors != null) {
			for (int i = 0; i < item.originalTextureColors.length; i++)
				textureOptions += item.originalTextureColors[i] + "=" + item.modifiedTextureColors[i] + ";";
		}
		itemInfoList.add(new ListItem("Model Textures", textureOptions));
		itemInfoList.add(new ListItem("Male Model 1", item.maleEquipModelId1 + ""));
		itemInfoList.add(new ListItem("Male Model 2", item.maleEquipModelId2 + ""));
		itemInfoList.add(new ListItem("Male Model 3", item.maleEquipModelId3 + ""));
		itemInfoList.add(new ListItem("Male Dialogue Model", item.maleDialogueModel + ""));
		itemInfoList.add(new ListItem("Female Model 1", item.femaleEquipModelId1 + ""));
		itemInfoList.add(new ListItem("Female Model 2", item.femaleEquipModelId2 + ""));
		itemInfoList.add(new ListItem("Female Model 3", item.femaleEquipModelId3 + ""));
		itemInfoList.add(new ListItem("Female Dialogue Model", item.femaleDialogueModel + ""));
		itemInfoList.add(new ListItem("Team ID", item.teamId + ""));
		itemInfoList.add(new ListItem("Note ID", item.notedItemId + ""));
		itemInfoList.add(new ListItem("Noted Item ID", item.switchNoteItemId + ""));
		itemInfoList.add(new ListItem("Lended ID", item.lendedItemId + ""));
		itemInfoList.add(new ListItem("Lended Item ID", item.switchLendItemId + ""));
		itemInfoList.add(new ListItem("Member", item.isMembersOnly() + ""));
		itemInfoList.add(new ListItem("Stackable", item.stackable + ""));
		itemInfoList.add(new ListItem("Model Zoom", item.invModelZoom + ""));
		itemInfoList.add(new ListItem("Model Rotation 1", item.modelRotation1 + ""));
		itemInfoList.add(new ListItem("Model Rotation 2", item.modelRotation2 + ""));
		itemInfoList.add(new ListItem("Model Offset 1", item.modelOffset1 + ""));
		itemInfoList.add(new ListItem("Model Offset 2", item.modelOffset2 + ""));
		String stackIDOptions = "";
		if (item.stackIds != null) {
			for (int i = 0; i < item.stackIds.length; i++)
				stackIDOptions += item.stackIds[i] + "=" + item.stackAmounts[i] + ";";
		}
		itemInfoList.add(new ListItem("Stack ID=Amount", stackIDOptions));
		String unknownOptions = "";
		if (item.unknownArray1 != null) {
			for (int i = 0; i < item.unknownArray1.length; i++)
				unknownOptions += item.unknownArray1[i] + "=" + item.unknownArray2[i] + ";";
		}
		itemInfoList.add(new ListItem("UnknownA", unknownOptions));
		itemInfoList.add(new ListItem("Unknown1", item.getUnknownStuff1()));
		itemInfoList.add(new ListItem("Unknown2", item.getUnknownStuff2()));
		itemInfoList.add(new ListItem("Unknown3", item.getUnknownStuff3()));
		itemInfoList.add(new ListItem("Unknown4", item.getUnknownStuff4()));
		return itemInfoList;
	}

	public void populateList() {
		stopListeners = true;
		listModelD.removeAllElements();
		for (int i = 0; i < itemList.size(); i++)
			listModelD.addElement(itemList.get(i).toString());
		stopListeners = false;
	}
	
	public void loadDefs() {
		stopListeners = true;
		listModelD.removeAllElements();
		double progress = 0;
		int size = Utils.getItemDefinitionsSize(CacheEditor.STORE);
		for (int i = 0; i < size; i++) {
			ItemDefinitions def = new ItemDefinitions(CacheEditor.STORE, i, true);
			itemList.add(def);
			listModelD.addElement(def.toString());
			progress = 100.0 / ((double) size) * ((double) i);
			CacheEditor.setProgress(((int) progress) + 100);
		}
		listItem.setSelectedIndex(0);
		stopListeners = false;
	}

	private ArrayList<ItemDefinitions> itemList = new ArrayList<ItemDefinitions>();
	private ArrayList<ItemDefinitions> needsUpdate = new ArrayList<ItemDefinitions>();
	private DefaultListModel<String> listModelD = new DefaultListModel<String>();
	private JList<String> listItem = new JList<String>(listModelD);
	private ItemDefinitions cloneItem = null;
	private JTextField txtItemID;
	private JLabel lblClone = new JLabel("Clone Item:");
	private DefaultListModel<String> listModelItems = new DefaultListModel<String>();
	private JList<String> listItemInfo = new JList<String>(listModelItems);
	private JButton btnApply = new JButton("Apply");
	private JTextField lblColor = new JTextField("RGB: ------- Hex:------- RS2: -------");
	private boolean stopListeners = false;
	private JTextField txtItemName;
}
