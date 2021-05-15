package editor.application;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.loaders.NPCDefinitions;
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
public class NPC_Panel extends JPanel {
	private JTextField txtNPCID = new JTextField();
	private DefaultListModel<NPCDefinitions> listModelNPC = new DefaultListModel<NPCDefinitions>();
	private JList<NPCDefinitions> listNPC = new JList<NPCDefinitions>(listModelNPC);
	private DefaultListModel<String> listModelInfo = new DefaultListModel<String>();
	private JList<String> listInfo = new JList<String>(listModelInfo);
	private JLabel lblClone = new JLabel("Clone NPC:");
	private JButton btnApply = new JButton("Apply");
	private ArrayList<NPCDefinitions> needsUpdate = new ArrayList<NPCDefinitions>();
	private NPCDefinitions cloneNPC = null;
	private boolean stopListeners = false;

	/**
	 * Create the panel.
	 */
	public NPC_Panel() {
		setPreferredSize(new Dimension(476, 540));
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 11, 460, 365);
		add(panel);
		
		JScrollPane scrollPaneNPC = new JScrollPane();
		scrollPaneNPC.setBounds(10, 23, 195, 331);
		panel.add(scrollPaneNPC);
		
		scrollPaneNPC.setViewportView(listNPC);
		listNPC.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (stopListeners)
					return;
				int index = listInfo.getSelectedIndex();
				updateData(listNPC, listModelNPC, listInfo, listModelInfo, txtNPCID, CacheEditor.STORE);
				listInfo.setSelectedIndex(index);
			}
		});
		
		JScrollPane scrollPaneInfo = new JScrollPane();
		scrollPaneInfo.setBounds(215, 90, 235, 264);
		panel.add(scrollPaneInfo);
		
		scrollPaneInfo.setViewportView(listInfo);
		
		txtNPCID.setText("0");
		txtNPCID.setColumns(10);
		txtNPCID.setBounds(60, 2, 86, 20);
		txtNPCID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					int npc = 0;
					try {
						npc = Integer.parseInt(txtNPCID.getText());
					} catch (Exception error) {
						npc = 0;
					}
					listNPC.setSelectedIndex(npc);
					listNPC.ensureIndexIsVisible(npc);
				}
			}
		});
		panel.add(txtNPCID);
		
		JLabel lblNpcs = new JLabel("NPCs");
		lblNpcs.setBounds(10, 5, 195, 14);
		panel.add(lblNpcs);
		
		JButton btnClone = new JButton("Clone");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cloneNPC = new NPCDefinitions(CacheEditor.STORE, listNPC.getSelectedIndex(), true);
				lblClone.setText(
						"Clone NPC: " + cloneNPC.id + " - " + cloneNPC.getName() + ".");
			}
		});
		btnClone.setBounds(210, 12, 70, 23);
		panel.add(btnClone);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String element = listModelInfo.getElementAt(listInfo.getSelectedIndex()).toString();
				String title = element.substring(0, element.indexOf(':'));
				String data = element.substring(element.indexOf(':') + 2);
				String input = (String) JOptionPane.showInputDialog(CacheEditor.frmCacheEditor, "Enter new value:",
						"Editing " + title, JOptionPane.PLAIN_MESSAGE, null, null, data);
				if (input != null)
					changeValue(listNPC.getSelectedIndex(), title, input);
			}
		});
		btnEdit.setBounds(377, 58, 73, 23);
		panel.add(btnEdit);
		
		JButton btnScripts = new JButton("Scripts");
		btnScripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(listModelNPC.getElementAt(listNPC.getSelectedIndex()).getName());

				if (listModelNPC.getElementAt(listNPC.getSelectedIndex()).clientScriptData != null) {
					for (int key : listModelNPC.getElementAt(listNPC.getSelectedIndex()).clientScriptData.keySet()) {
						Object value = listModelNPC.getElementAt(listNPC.getSelectedIndex()).clientScriptData.get(key);
						System.out.println("KEY: " + key + ", VALUE: " + value);
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
				NPCDefinitions def = new NPCDefinitions(CacheEditor.STORE, listNPC.getSelectedIndex(), false);
				int view = listNPC.getSelectedIndex() - 1;
				listNPC.setSelectedIndex(view > -1 ? view : 0);
				listNPC.ensureIndexIsVisible(view > -1 ? view : 0);
				listModelNPC.remove(listNPC.getSelectedIndex() + 1);
				CacheEditor.STORE.getIndexes()[Constants.NPC_DEFINITIONS_INDEX].removeFile(def.getArchiveId(), def.getFileId());
			}
		});
		btnDelete.setBounds(210, 58, 70, 23);
		panel.add(btnDelete);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NPCDefinitions npc = new NPCDefinitions(CacheEditor.STORE, Utils.getNPCDefinitionsSize(CacheEditor.STORE), false);
				npc.write(CacheEditor.STORE);
				listModelNPC.addElement(npc);
				listNPC.setSelectedIndex(listNPC.getModel().getSize() - 1);
				listNPC.ensureIndexIsVisible(listNPC.getModel().getSize() - 1);
			}
		});
		btnAdd.setBounds(210, 35, 70, 23);
		panel.add(btnAdd);
		
		JButton btnAddClone = new JButton("Add Clone");
		btnAddClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NPCDefinitions npc = cloneNPC;
				if (npc != null) {
					npc.id = listNPC.getModel().getSize();
					npc.write(CacheEditor.STORE);
					listModelNPC.addElement(npc);
					listNPC.setSelectedIndex(listNPC.getModel().getSize() - 1);
					listNPC.ensureIndexIsVisible(listNPC.getModel().getSize() - 1);
				}
			}
		});
		btnAddClone.setBounds(281, 35, 95, 23);
		panel.add(btnAddClone);
		
		lblClone.setBounds(10, 384, 444, 14);
		add(lblClone);

	}
	
	private void changeValue(int npcID, String option, String input) {

		if (needsUpdate.contains(listModelNPC.getElementAt(listNPC.getSelectedIndex())))
			needsUpdate.remove(listModelNPC.getElementAt(listNPC.getSelectedIndex()));
		int value = -1234567890;
		try {
			value = Integer.parseInt(input);
			if (value == -1234567890)
				return;
			switch (option) {
			case "Combat Level":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).combatLevel = value;
				break;
			case "Render Animation":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).renderEmote = value;
				break;
			case "Head Icon":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).headIcons = value;
				break;
			case "Size":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).size = value;
				break;
			case "Height":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).height = value;
				break;
			case "Width":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).width = value;
				break;
			case "Walk Mask":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).walkMask = (byte) value;
				break;
			case "Respawn Direction":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).respawnDirection = (byte) value;
				break;
			}
		} catch (Exception e) {
			switch (option) {
			case "Name":
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).setName(input);
				break;
			case "Options":
				String list[] = input.split(";");
				int listInt[];
				for (int i = 0; i < listModelNPC.getElementAt(listNPC.getSelectedIndex()).options.length; i++)
					if (i < list.length)
						listModelNPC.getElementAt(listNPC.getSelectedIndex()).options[i] = list[i];
				break;
			case "Visible On Map":
				if (input.equalsIgnoreCase("true"))
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).isVisibleOnMap = true;
				else
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).isVisibleOnMap = false;
				break;
			case "Dialogue Models":
				list = input.split(";");
				listInt = new int[list.length];
				for (int i = 0; i < list.length; i++)
						listInt[i] = Integer.parseInt(list[i]);
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).dialogueModels = listInt;
				break;
			case "Models":
				list = input.split(";");
				listInt = new int[list.length];
				for (int i = 0; i < list.length; i++)
						listInt[i] = Integer.parseInt(list[i]);
				listModelNPC.getElementAt(listNPC.getSelectedIndex()).models = listInt;
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
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).originalModelColors = list2;
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).modifiedModelColors = list3;
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
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).originalTextureColors = list2;
					listModelNPC.getElementAt(listNPC.getSelectedIndex()).modifiedTextureColors = list3;
				}
				break;
			}
		}
		needsUpdate.add(listModelNPC.getElementAt(listNPC.getSelectedIndex()));
		btnApply.setEnabled(true);
		int index = listInfo.getSelectedIndex();
		if (listNPC.getSelectedIndex() == listNPC.getModel().getSize() - 1) {
			listNPC.setSelectedIndex(listNPC.getSelectedIndex() - 1);
			listNPC.setSelectedIndex(listNPC.getSelectedIndex() + 1);
		} else {
			listNPC.setSelectedIndex(listNPC.getSelectedIndex() + 1);
			listNPC.setSelectedIndex(listNPC.getSelectedIndex() - 1);
		}
		listInfo.setSelectedIndex(index);
	}
	
	private ArrayList<ListItem> getNPCList(NPCDefinitions npc) {
		ArrayList<ListItem> npcInfoList = new ArrayList<ListItem>();
		npcInfoList.add(new ListItem("Name", npc.getName()));
		npcInfoList.add(new ListItem("Combat Level", npc.combatLevel + ""));
		npcInfoList.add(new ListItem("Render Animation", npc.renderEmote + ""));
		String options = "";
		for (int i = 0; i < npc.options.length; i++)
			options += npc.options[i] + ";";
		npcInfoList.add(new ListItem("Options", options));
		npcInfoList.add(new ListItem("Head Icon", npc.headIcons + ""));
		npcInfoList.add(new ListItem("Size", npc.size + ""));
		npcInfoList.add(new ListItem("Height", npc.height + ""));
		npcInfoList.add(new ListItem("Width", npc.width + ""));
		npcInfoList.add(new ListItem("Visible On Map", npc.isVisibleOnMap + ""));
		npcInfoList.add(new ListItem("Walk Mask", npc.walkMask + ""));
		npcInfoList.add(new ListItem("Respawn Direction", npc.respawnDirection + ""));
		options = "";
		if (npc.dialogueModels != null)
			for (int i = 0; i < npc.dialogueModels.length; i++)
				options += npc.dialogueModels[i] + ";";
		npcInfoList.add(new ListItem("Dialogue Models", options));
		options = "";
		if (npc.models != null)
			for(int i = 0; i < npc.models.length; i++)
				options += npc.models[i] + ";";
		npcInfoList.add(new ListItem("Models", options));
		options = "";
		if (npc.originalModelColors != null)
			for (int i = 0; i < npc.originalModelColors.length; i++)
				options += npc.originalModelColors[i] + "=" + npc.modifiedModelColors[i] + ";";
		npcInfoList.add(new ListItem("Model Colors", options));
		options = "";
		if (npc.originalTextureColors != null)
			for (int i = 0; i < npc.originalTextureColors.length; i++)
				options += npc.originalTextureColors[i] + "=" + npc.modifiedTextureColors[i] + ";";
		npcInfoList.add(new ListItem("Model Textures", options));
		return npcInfoList;
	}
	
	private void updateData(JList<NPCDefinitions> list, DefaultListModel<NPCDefinitions> listModel, JList<String> listNpc,
			DefaultListModel<String> listNPCModel, JTextField txtNPC, Store store) {
		txtNPC.setText("" + list.getSelectedIndex());
		listNPCModel.removeAllElements();
		ArrayList<ListItem> npcList = getNPCList(listModel.getElementAt(list.getSelectedIndex()));
		for (int i = 0; i < npcList.size(); i++)
			listNPCModel.addElement(npcList.get(i).getTitle() + ": " + npcList.get(i).getData());
		if (listNpc.getSelectedIndex() == -1)
			listNpc.setSelectedIndex(0);
	}
	
	public void loadDefs() {
		stopListeners = true;
		listModelNPC.removeAllElements();
		double progress = 0;
		int size = Utils.getNPCDefinitionsSize(CacheEditor.STORE);
		for (int i = 0; i < size; i++) {
			listModelNPC.addElement(new NPCDefinitions(CacheEditor.STORE, i, true));
			progress = 100.0 / ((double) size) * ((double) i);
			CacheEditor.setProgress(((int) progress) + 200);
		}
		listNPC.setSelectedIndex(0);
		stopListeners = false;
	}

}
