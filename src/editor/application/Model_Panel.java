package editor.application;

import javax.swing.JPanel;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;

import editor.loaders.ClientScripts;
import editor.loaders.Model;
import editor.store.Store;
import editor.util.rsmv.ModelRSMV;
import editor.util.rsmv.ModelPanel;
import editor.utils.Constants;
import editor.utils.Utils;

@SuppressWarnings("serial")
public class Model_Panel extends JPanel {
	private JTextField txtModel;
	private DefaultListModel<Integer> listModelM = new DefaultListModel<Integer>();
	private JList<Integer> listModel = new JList<Integer>(listModelM);
	private JFileChooser chooser = new JFileChooser();
	private JButton btnDelete = new JButton("Delete");
	//private ModelViewer modelViewer = new ModelViewer();
	private ModelPanel modelPanel;
	private Thread modelViewThread;
	@SuppressWarnings("unused")
	private Model model = null;

	/**
	 * Create the panel.
	 */
	public Model_Panel() {
		setPreferredSize(new Dimension(476, 540));
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 11, 460, 365);
		add(panel);
		
		JScrollPane scrollPaneModel = new JScrollPane();
		scrollPaneModel.setBounds(10, 23, 195, 331);
		panel.add(scrollPaneModel);
		
		scrollPaneModel.setViewportView(listModel);
		listModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				//System.out.println(listModel.getSelectedIndex());
				//modelViewer.setId(listModel.getSelectedIndex());
				//modelViewer.loadModel();
				if (!modelPanel.loaded) {
					modelPanel.loaded = true;
					modelPanel.render();
					modelViewThread = new Thread(modelPanel);
					modelViewThread.start();
					ModelRSMV.load();
				}
				int id = listModel.getSelectedIndex();
				byte[] data = CacheEditor.STORE.getIndexes()[7].getFile(id, 0, null);
				if (data != null) {
					model = new Model(id, data);
					modelPanel.loadModelFromBytes(data);
				}
			}
		});
		
		txtModel = new JTextField();
		txtModel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					int id = 0;
					try {
						id = Integer.parseInt(txtModel.getText());
					} catch (Exception error) {
						id = 0;
					}
					//int index = listModel.getSelectedIndex();
					listModel.setSelectedIndex(id);
					listModel.ensureIndexIsVisible(id);
					//listModel.setSelectedIndex(index);
				}
			}
		});
		txtModel.setText("0");
		txtModel.setColumns(10);
		txtModel.setBounds(60, 2, 86, 20);
		panel.add(txtModel);
		
		JLabel lblModels = new JLabel("Models");
		lblModels.setBounds(10, 5, 195, 14);
		panel.add(lblModels);
		
		JButton btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = getFile();
				if (file != null){
					//int index = replaceCustomModel(CacheEditor.STORE, file, listModel.getSelectedIndex());
					replaceCustomModel(CacheEditor.STORE, file, 64);
				}
			}
		});
		btnReplace.setBounds(210, 12, 86, 23);
		panel.add(btnReplace);
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int view = listModel.getSelectedIndex() - 1;
				CacheEditor.STORE.getIndexes()[Constants.MODELS_INDEX].removeArchive(listModel.getSelectedIndex() + 1);
				listModelM.remove(listModel.getSelectedIndex());
				listModel.setSelectedIndex(view > -1 ? view : 0);
				listModel.ensureIndexIsVisible(view > -1 ? view : 0);
			}
		});
		btnDelete.setBounds(210, 58, 86, 23);
		panel.add(btnDelete);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JList list, DefaultListModel model, Store store
				File file = getFile();
				if (file != null){
					int index = addCustomModel(CacheEditor.STORE, file);
					if (index != -1)
						listModelM.addElement(index);
					listModel.setSelectedIndex(index);
					listModel.ensureIndexIsVisible(index);
				}
			}
		});
		btnAdd.setBounds(210, 35, 86, 23);
		panel.add(btnAdd);
		
		JButton btnDump = new JButton("Dump");
		btnDump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//dumpModel(CacheEditor.STORE.getIndexes()[Constants.MODELS_INDEX].getFile((listModel.getSelectedIndex()), 0, null), (listModel.getSelectedIndex()) + "");
					dumpModel(CacheEditor.STORE.getIndexes()[Constants.CLIENT_SCRIPTS_INDEX].getFile(64, 0, null), 64 + "");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnDump.setBounds(210, 81, 86, 23);
		panel.add(btnDump);
		

		//modelViewer.setBounds(215, 125, 235, 229);
		//panel.add(modelViewer);
		
		modelPanel = new ModelPanel();
		modelPanel.setBounds(215, 125, 235, 229);
		panel.add(modelPanel);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < Utils.getClientScriptsSize(CacheEditor.STORE); i++) {
					ClientScripts cs = new ClientScripts();
					cs.loadScripts(i);
				}
				/*ClientScripts cs = new ClientScripts();
				cs.loadScripts(19);*/
				//GraphicsDefinition def = new GraphicsDefinition();
				//def.loadScript(CacheEditor.STORE, ji);
				//ji++;
			}
		});
		btnLoad.setBounds(306, 12, 89, 23);
		panel.add(btnLoad);
		
	}

	private File getFile() {
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				return chooser.getSelectedFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String getDirectory() {
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file.isDirectory()) {
				try {
					return file.getAbsolutePath() + "/";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public int replaceCustomModel(Store store, File file, int index){
		try {
			return packCustomModel(store, getBytesFromFile(file), index);
		} catch (IOException ex) {
            System.out.println("Error Packing Custom Model.");
        }
		return -1;
	}
	
	public int addCustomModel(Store store, File file){
		try {
			return packCustomModel(store, getBytesFromFile(file), store.getIndexes()[Constants.MODELS_INDEX].getLastArchiveId() + 1);
		} catch (IOException ex) {
            System.out.println("Error Packing Custom Model.");
        }
		return -1;
	}
	
	public int packCustomModel(Store store, byte[] data, int archiveId) {
		//if (store.getIndexes()[Constants.MODELS_INDEX].putFile(archiveId, 0, data))
			//return archiveId;
		if (store.getIndexes()[Constants.CLIENT_SCRIPTS_INDEX].putFile(archiveId, 0, data))
			return archiveId;
		return -1;
	}
	
	@SuppressWarnings("resource")
	public byte[] getBytesFromFile(File file) throws IOException {
		if (file == null)
			return null;
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
			offset += numRead;
		if (offset < bytes.length) {
			System.out.println("Error");
			throw new IOException("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}
	
	public void dumpModel(byte[] bytes, String name) throws IOException {
		if (bytes == null)
			return;
		FileOutputStream fileOuputStream = null;
		try {
			String dir = getDirectory() + name + ".dat";
			File outputFile = new File(dir);
			outputFile.createNewFile();
			fileOuputStream = new FileOutputStream(outputFile);
			fileOuputStream.write(bytes);
			fileOuputStream.close();
		} finally {
		}
	}
	
	public void loadDefs() {
		listModelM.removeAllElements();
		double progress;
		int size = CacheEditor.STORE.getIndexes()[Constants.MODELS_INDEX].getLastArchiveId();
		//Model m = null;
		for (int i = 0; i <= size; i++) {
			//byte[] data = CacheEditor.STORE.getIndexes()[7].getFile(i, 0, null);
			//m = new Model(i, data);
			listModelM.addElement(i);
			progress = 100.0 / ((double) size) * ((double) i);
			CacheEditor.setProgress(((int) progress) + 400);
		}
		//CacheEditor.setProgress(500);
		listModel.setSelectedIndex(0);
	}
	public int ji = 0;
}
