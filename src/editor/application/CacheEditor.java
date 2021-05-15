package editor.application;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import editor.store.Store;
import editor.utils.Utils;

import java.awt.Dimension;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;

public class CacheEditor {

	public static Store STORE;
	
	public static JFrame frmCacheEditor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}*/
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new CacheEditor();
					frmCacheEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CacheEditor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCacheEditor = new JFrame();
		frmCacheEditor.setResizable(false);
		frmCacheEditor.setTitle("562 Cache Editor v1.0");
		frmCacheEditor.setIconImage(Toolkit.getDefaultToolkit().getImage("./images/appIcon.png"));
		frmCacheEditor.setSize(500, 650);
		frmCacheEditor.setBounds(0, 0, frmCacheEditor.getWidth(), frmCacheEditor.getHeight());
		frmCacheEditor.setMinimumSize(new Dimension(frmCacheEditor.getWidth(), frmCacheEditor.getHeight()));
		frmCacheEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCacheEditor.setLocationRelativeTo(null);
		lblStatus.setForeground(Color.BLACK);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		cache_Panel = new Cache_Panel();		
		image_Panel = new Image_Panel();
		item_Panel = new Item_Panel();
		npc_Panel = new NPC_Panel();
		object_Panel = new Object_Panel();		
		model_Panel = new Model_Panel();	
		interface_Panel = new Interface_Panel();

		/******************** CACHE ********************/
		JPanel cache_panel = new JPanel();
		tabbedPane.addTab("Cache", null, cache_panel, null);
		cache_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneCache = new JScrollPane();
		cache_panel.add(scrollPaneCache, BorderLayout.CENTER);
		cache_Panel.txtStatus.setBounds(11, 150, 216, 279);
		
		scrollPaneCache.setViewportView(cache_Panel);
		/************************************************/

		/******************** IMAGES ********************/
		JPanel image_panel = new JPanel();
		tabbedPane.addTab("Image", null, image_panel, null);
		tabbedPane.setEnabledAt(1, false);
		image_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneImage = new JScrollPane();
		scrollPaneImage.setViewportBorder(null);
		image_panel.add(scrollPaneImage, BorderLayout.CENTER);
		
		scrollPaneImage.setViewportView(image_Panel);
		/************************************************/

		/******************** ITEMS ********************/
		JPanel item_panel = new JPanel();
		tabbedPane.addTab("Item", null, item_panel, null);
		tabbedPane.setEnabledAt(2, false);
		item_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneItem = new JScrollPane();
		item_panel.add(scrollPaneItem, BorderLayout.CENTER);

		scrollPaneItem.setViewportView(item_Panel);
		/************************************************/

		/******************** NPCS ********************/
		JPanel npc_panel = new JPanel();
		tabbedPane.addTab("NPC", null, npc_panel, null);
		tabbedPane.setEnabledAt(3, false);
		npc_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneNPC = new JScrollPane();
		npc_panel.add(scrollPaneNPC, BorderLayout.CENTER);
		
		scrollPaneNPC.setViewportView(npc_Panel);
		/************************************************/
		
		/******************** OBJECTS ********************/
		JPanel object_panel = new JPanel();
		tabbedPane.addTab("Object", null, object_panel, null);
		tabbedPane.setEnabledAt(4, false);
		object_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneObject = new JScrollPane();
		object_panel.add(scrollPaneObject, BorderLayout.CENTER);
		
		scrollPaneObject.setViewportView(object_Panel);
		/************************************************/
		
		/******************** MODELS ********************/
		JPanel model_panel = new JPanel();
		tabbedPane.addTab("Model", null, model_panel, null);
		tabbedPane.setEnabledAt(5, false);
		model_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneModel = new JScrollPane();
		model_panel.add(scrollPaneModel, BorderLayout.CENTER);

		scrollPaneModel.setViewportView(model_Panel);
		/************************************************/

		/******************** INTERFACES ********************/
		JPanel interface_panel = new JPanel();
		tabbedPane.addTab("Interface", null, interface_panel, null);
		tabbedPane.setEnabledAt(6, false);
		interface_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneInterface = new JScrollPane();
		interface_panel.add(scrollPaneInterface, BorderLayout.CENTER);
		
		scrollPaneInterface.setViewportView(interface_Panel);
		/************************************************/

		/******************** PACK ********************/
		JPanel pack_panel = new JPanel();
		tabbedPane.addTab("Pack", null, pack_panel, null);
		tabbedPane.setEnabledAt(7, false);
		pack_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPanePack = new JScrollPane();
		pack_panel.add(scrollPanePack, BorderLayout.CENTER);
		frmCacheEditor.getContentPane().setLayout(new BoxLayout(frmCacheEditor.getContentPane(), BoxLayout.Y_AXIS));
		frmCacheEditor.getContentPane().add(tabbedPane);
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 1, 0));
		statusPanel.add(lblStatus);
		frmCacheEditor.getContentPane().add(statusPanel);
		
		JMenuBar menuBar = new JMenuBar();
		frmCacheEditor.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setMargin(new Insets(2, -25, 2, 2));
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmClose);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmDescription = new JMenuItem("Description");
		mntmDescription.setMargin(new Insets(2, -25, 2, 2));
		mnHelp.add(mntmDescription);
		
		JMenuItem mntmGettingStarted = new JMenuItem("Getting Started");
		mntmGettingStarted.setMargin(new Insets(2, -25, 2, 2));
		mnHelp.add(mntmGettingStarted);
		
		JMenuItem mntmCredits = new JMenuItem("Credits");
		mntmCredits.setMargin(new Insets(2, -25, 2, 2));
		mnHelp.add(mntmCredits);
		Utils.setColors();
	}
	
	public static void enableTabs(boolean b) {
		for (int i = 1; i < tabbedPane.getTabCount(); i++)
			tabbedPane.setEnabledAt(i, b);
	}
	
	public static int getSelectedTab() {
		return tabbedPane.getSelectedIndex();
	}
	
	public static void loadCacheTabData() {
		Thread thread = new Thread() {
			public void run() {
				image_Panel.loadPanes();
				//setProgress(84);
				item_Panel.loadDefs();
				//setProgress(168);
				npc_Panel.loadDefs();
				//setProgress(252);
				object_Panel.loadDefs();
				//setProgress(336);
				model_Panel.loadDefs();
				//setProgress(420);
				interface_Panel.loadDefs();
				//addMessage(STORE.getIndexes()[3].getValidArchivesCount() + " : " + STORE.getIndexes()[3].getLastArchiveId());
				addMessage("Succussfully loaded caches.");
				setStatus("Succussfully loaded caches.");
				//setProgress(500);
				enableTabs(true);
			}
		};
		thread.start();
	}
	
	public static void addMessage(String status) {
		cache_Panel.txtStatus.append(status + "\n");
	}
	
	public static void setStatus(String status) {
		lblStatus.setText("Status: " + status);
	}
	
	public static void setProgress(int progress) {
		cache_Panel.progressBar.setValue(progress);
	}
	
	private static JLabel lblStatus = new JLabel("Status:");
	private static JTabbedPane tabbedPane;
	private static Cache_Panel cache_Panel;	
	private static Image_Panel image_Panel;
	private static Item_Panel item_Panel;
	private static NPC_Panel npc_Panel;
	private static Object_Panel object_Panel;		
	private static Model_Panel model_Panel;		
	private static Interface_Panel interface_Panel;
}
