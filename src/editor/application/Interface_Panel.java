package editor.application;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import editor.loaders.IOComponent;
import editor.loaders.ImagesFile;
import editor.loaders.Interface;
import editor.store.Store;
import editor.utils.ListItem;
import editor.utils.Utils;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;

@SuppressWarnings("serial")
public class Interface_Panel extends JPanel {
	/**
	 * Create the panel.
	 */
	public Interface_Panel() {
		setLayout(null);
		setSize(464, 478);
		setPreferredSize(new Dimension(464, 500));

		JScrollPane scrollPaneTreeS = new JScrollPane();
		scrollPaneTreeS.setBounds(15, 11, 139, 133);
		add(scrollPaneTreeS);

		treeI.setShowsRootHandles(true);
		treeI.setModel(treeModelI);
		scrollPaneTreeS.setViewportView(treeI);
		treeI.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				if (stopListeners)
					return;
				frameTreeValueChanged(evt, treeI, treeC, nodeC, treeModelC, CacheEditor.STORE, true);
			}
		});

		JScrollPane scrollPaneTreeF = new JScrollPane();
		scrollPaneTreeF.setBounds(164, 11, 139, 133);
		add(scrollPaneTreeF);

		treeC.setShowsRootHandles(true);
		treeC.setModel(treeModelC);
		scrollPaneTreeF.setViewportView(treeC);
		treeC.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				frameTreeValueChanged(evt, treeI, treeC, nodeC, treeModelC, CacheEditor.STORE, false);
			}
		});

		JButton btnDump = new JButton("Dump");
		btnDump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JScrollPane scrollPaneInfo = new JScrollPane();
		scrollPaneInfo.setBounds(15, 156, 427, 158);
		add(scrollPaneInfo);
		
		scrollPaneInfo.setViewportView(listInfo);
		
		JScrollPane scrollPaneImage = new JScrollPane();
		scrollPaneImage.setBounds(15, 327, 427, 117);
		add(scrollPaneImage);
		
		panelImage.setName("panelD");
		scrollPaneImage.setViewportView(panelImage);
		
		
		btnDump.setBounds(313, 126, 129, 18);
		add(btnDump);

		JButton btnAddSprite = new JButton("Add Sprite");
		btnAddSprite.setEnabled(false);
		btnAddSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnAddSprite.setBounds(313, 80, 129, 18);
		add(btnAddSprite);

		JButton btnDeleteSprite = new JButton("Delete Sprite");
		btnDeleteSprite.setEnabled(false);
		btnDeleteSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnDeleteSprite.setBounds(313, 103, 129, 18);
		add(btnDeleteSprite);

		JButton btnAddFrame = new JButton("Add Frame");
		btnAddFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnAddFrame.setBounds(313, 11, 129, 18);
		add(btnAddFrame);

		JButton btnDeleteFrame = new JButton("Delete Frame");
		btnDeleteFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeleteFrame.setBounds(313, 34, 129, 18);
		add(btnDeleteFrame);

		JButton btnReplaceFrame = new JButton("Replace Frame");
		btnReplaceFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnReplaceFrame.setBounds(313, 57, 129, 18);
		add(btnReplaceFrame);

	}
	
	private ArrayList<ListItem> getList(IOComponent component) {
		ArrayList<ListItem> componentInfoList = new ArrayList<ListItem>();
		componentInfoList.add(new ListItem("X", component.getX() + ""));
		componentInfoList.add(new ListItem("Y", component.getY() + ""));
		componentInfoList.add(new ListItem("Width", component.getWidth() + ""));
		componentInfoList.add(new ListItem("Height", component.getHeight() + ""));
		String options = "";
		if (component.aStringArray2385 != null)
			for (int i = 0; i < component.aStringArray2385.length; i++)
				options += component.aStringArray2385[i] + ";";
		componentInfoList.add(new ListItem("aStringArray2385", options));
		
		options = "";
		if (component.anObjectArray2439 != null)
			for (int i = 0; i < component.anObjectArray2439.length; i++)
				options += component.anObjectArray2439[i] + ";";
		componentInfoList.add(new ListItem("anObjectArray2439", options));
		
		options = "";
		if (component.defaultScript != null)
			for (int i = 0; i < component.defaultScript.length; i++)
				options += component.defaultScript[i] + ";";
		componentInfoList.add(new ListItem("defaultScript", options));
		
		componentInfoList.add(new ListItem("aString2473", component.aString2473));
		componentInfoList.add(new ListItem("aString2357", component.aString2357));
		componentInfoList.add(new ListItem("aString2391", component.aString2391));
		componentInfoList.add(new ListItem("aString2330", component.aString2330));
		componentInfoList.add(new ListItem("aString2463", component.aString2463));
		
		componentInfoList.add(new ListItem("Image ID", component.getImageId() + ""));
		componentInfoList.add(new ListItem("Hidden", component.isHidden() + ""));
		componentInfoList.add(new ListItem("Type", component.getType() + ""));
		/*npcInfoList.add(new ListItem("Name", component.getName()));
		npcInfoList.add(new ListItem("Combat Level", component.combatLevel + ""));
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
		npcInfoList.add(new ListItem("Model Textures", options));*/
		return componentInfoList;
	}

	private void frameTreeValueChanged(TreeSelectionEvent evt, JTree treeInterface, JTree treeIOComponent,
			DefaultMutableTreeNode treeNodeIOComponent, DefaultTreeModel treeModelIOComponent, Store store,
			boolean update) {
		try {
			int id = Integer.parseInt(treeInterface.getLastSelectedPathComponent().toString());
			if (update) {
				treeNodeIOComponent.removeAllChildren();
				for (int i = 0; i < interfaceList.get(id). getCompSize(); i++)
					treeNodeIOComponent.add(new DefaultMutableTreeNode(i));
				treeModelIOComponent.reload(treeNodeIOComponent);
				treeIOComponent.expandRow(0);
				if (treeIOComponent.getLastSelectedPathComponent() == null)
					treeIOComponent.setSelectionRow(1);
			} else {
				/*** handle IOComponent Info ***/
				if (treeIOComponent.getLastSelectedPathComponent() != null) {
					int index = Integer.parseInt(treeIOComponent.getLastSelectedPathComponent().toString());
					updateData(listInfo, listModelInfo, id, index);
				}
				/*******************************/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateData(JList<String> listInfo, DefaultListModel<String> listInfoModel, int interfaceId, int componentId) {
		listInfoModel.removeAllElements();
		ArrayList<ListItem> componentList = getList(interfaceList.get(interfaceId).getComponent(componentId));
		for (int i = 0; i < componentList.size(); i++)
			listInfoModel.addElement(componentList.get(i).getTitle() + ": " + componentList.get(i).getData());
		if (listInfo.getSelectedIndex() == -1)
			listInfo.setSelectedIndex(0);
		int imageId = interfaceList.get(interfaceId).getComponent(componentId).getImageId();
		if (imageId > 0) {
			ImagesFile oldImagesFile = new ImagesFile(CacheEditor.STORE, imageId, 0);
			BufferedImage[] images = oldImagesFile.getImages();
			sprite = images[0];
		} else {
			sprite = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		}
		if (sprite.getWidth() > panelImage.getWidth() || sprite.getHeight() > panelImage.getHeight())
			panelImage.setPreferredSize(new Dimension(sprite.getWidth(), sprite.getHeight()));
		panelImage.revalidate();
		panelImage.repaint();
	}

	public void loadDefs() {
		stopListeners = true;
		storeSize = Utils.getInterfaceDefinitionsSize(CacheEditor.STORE);
		nodeI.removeAllChildren();
		double progress = 0;
		
		for (int i = 0; i < storeSize; i++) {//new Interface(i)
			nodeI.add(new DefaultMutableTreeNode(i));
			interfaceList.add(new Interface(i));
			progress = 100.0 / ((double) storeSize) * ((double) i);
			CacheEditor.setProgress((int) progress + 500);
		}
		treeModelI.reload(nodeI);
		treeI.expandRow(0);
		treeI.setSelectionRow(0);
		stopListeners = false;

	}

	private int storeSize;
	private JTree treeI = new JTree();
	private DefaultListModel<String> listModelInfo = new DefaultListModel<String>();
	private JList<String> listInfo = new JList<String>(listModelInfo);
	private DefaultMutableTreeNode nodeI = new DefaultMutableTreeNode("Interfaces");
	private DefaultTreeModel treeModelI = new DefaultTreeModel(nodeI);
	private JTree treeC = new JTree();
	private DefaultMutableTreeNode nodeC = new DefaultMutableTreeNode("IOComponent");
	private DefaultTreeModel treeModelC = new DefaultTreeModel(nodeC);
	ArrayList<Interface> interfaceList = new ArrayList<Interface>();
	private boolean stopListeners = false;
	private BufferedImage sprite = null;
	private JPanel panelImage = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, panelImage.getWidth(), panelImage.getHeight());
			g.drawImage(sprite, 0, 0, null);
		}
	};
}
